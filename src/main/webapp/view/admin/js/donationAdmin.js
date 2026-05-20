
'use strict';

const CTX = window.CTX || '';

const state = {
  currentPage:  (window.PAGE_INFO && window.PAGE_INFO.currentPage) || 1,
  totalCount:   (window.PAGE_INFO && window.PAGE_INFO.totalCount)  || 0,
  pageSize:     (window.PAGE_INFO && window.PAGE_INFO.pageSize)    || 5,
  donFilter: { keyword:'', paymentType:'', method:'', startDate:'', endDate:'' },
  calcOpen:     false,
  calcExpr:     '',
  calcDisplay:  '0',
  calcHistory:  [],
  activeTab:    'all',
  activeDrawer: null,
};

const fmt = n => Number(n || 0).toLocaleString('ko-KR');

function showToast(msg, type = 'info', duration = 3000) {
  const icons = { success:'check_circle', error:'error', info:'info' };
  const c = document.getElementById('toastContainer');
  const t = document.createElement('div');
  t.className = `toast toast--${type}`;
  t.innerHTML = `<span class="material-symbols-outlined">${icons[type]||'info'}</span><span>${msg}</span>`;
  c.appendChild(t);
  setTimeout(() => { t.classList.add('leaving'); setTimeout(() => t.remove(), 220); }, duration);
}

function closeAllCtx() {
  document.querySelectorAll('.ctx-menu.visible').forEach(m => m.classList.remove('visible'));
}

/* TABS */
function initTabs() {
  document.querySelectorAll('.tab-btn').forEach(btn => {
    btn.addEventListener('click', () => {
      const tab = btn.dataset.tab;
      state.activeTab = tab;
      document.querySelectorAll('.tab-btn').forEach(b => b.classList.toggle('active', b.dataset.tab === tab));
      document.querySelectorAll('.tab-panel').forEach(p => p.classList.toggle('active', p.dataset.panel === tab));
    });
  });
}

/* PAGINATION */
function renderPagination() {
  const wrap = document.getElementById('donPagination');
  if (!wrap) return;

  const total = state.totalCount; // 전체 데이터 건수
  const size  = state.pageSize;   // 페이지당 건수 (5)
  const cur   = state.currentPage; // 현재 페이지
  const pages = Math.max(1, Math.ceil(total / size)); // 전체 페이지 수
  const start = (cur - 1) * size + 1; // 현재 페이지 시작 건 번호
  const end   = Math.min(cur * size, total); // 현재 페이지 끝 건 번호

  // "총 N건의 내역 중 M-K 표시" 텍스트 갱신
  const info = document.getElementById('donPagInfo');
  if (info) info.textContent = `총 ${total}건의 내역 중 ${start}-${end} 표시`;

  // 이전 버튼 (1페이지면 disabled)
  let html = `<button class="page-btn" onclick="changePage(${cur-1})" ${cur<=1?'disabled':''}>
                <span class="material-symbols-outlined">chevron_left</span></button>`;
  
  /*
    * 최대 3개 연속 번호만 표시
    * - blockStart: 현재 페이지 기준으로 3개 블록의 시작 번호 계산
    *   예) cur=1 → blockStart=1 (1,2,3)
    *       cur=2 → blockStart=1 (1,2,3)
    *       cur=3 → blockStart=2 (2,3,4)  ← cur-1
    *       cur=4 → blockStart=2 (2,3,4)  ← 끝에 맞춰 조정
    */
   const SHOW = 3; // 보여줄 페이지 버튼 개수
   // cur 기준 중앙 배치: cur-1 ~ cur+1, 단 범위를 벗어나면 조정
   let blockStart = Math.max(1, cur - 1);                   // cur-1 부터 시작
   let blockEnd   = Math.min(pages, blockStart + SHOW - 1); // 최대 SHOW개
   // 끝이 잘리면 시작을 당겨서 항상 SHOW개 유지
   if (blockEnd - blockStart < SHOW - 1) {
     blockStart = Math.max(1, blockEnd - SHOW + 1);
   }

   for (let p = blockStart; p <= blockEnd; p++) {
     html += `<button class="page-btn${p === cur ? ' active' : ''}" onclick="changePage(${p})">${p}</button>`;
   }


  // 다음 버튼 (마지막 페이지면 disabled)
  html += `<button class="page-btn" onclick="changePage(${cur+1})" ${cur>=pages?'disabled':''}>
             <span class="material-symbols-outlined">chevron_right</span></button>`;

  wrap.innerHTML = html;
}

/* FILTER */
function initDonationFilters() {
    const elSearch = document.getElementById('donSearch');
    const elType = document.getElementById('donTypeFilter');
    const elMethod = document.getElementById('donMethodFilter');
    const elStart = document.getElementById('startDate');
    const elEnd = document.getElementById('endDate');

    // 필터/검색어 변경 시 즉시 실행
    [elType, elMethod, elStart, elEnd].forEach(el => {
        el?.addEventListener('change', () => submitFilter(1));
    });

    document.getElementById('searchBtn')?.addEventListener('click', () => submitFilter(1));
    elSearch?.addEventListener('keypress', (e) => { if (e.key === 'Enter') submitFilter(1); });

    
}

function submitFilter(page = 1) {
    
    const params = {
        keyword: document.getElementById('donSearch')?.value || '',
        paymentType: document.getElementById('donTypeFilter')?.value || '',
        method: document.getElementById('donMethodFilter')?.value || '',
        startDate: document.getElementById('startDate')?.value || '',
        endDate: document.getElementById('endDate')?.value || '',
        page: page
    };
    
   
    fetchDonationData(params);
}

function initHeaderSearch() {
  const hs = document.getElementById('headerSearch');
  const ds = document.getElementById('donSearch');
  hs?.addEventListener('input', e => { if(ds) ds.value = e.target.value; });
  hs?.addEventListener('keydown', e => { if(e.key==='Enter') submitFilter(); });
}

/* CONTEXT MENU */
function bindCtxMenus() {
  document.querySelectorAll('[data-ctx]').forEach(btn => {
    btn.addEventListener('click', e => {
      e.stopPropagation();
      const menu = document.getElementById(`ctx-${btn.dataset.ctx}`);
      if(!menu) return;
      const isVisible = menu.classList.contains('visible');
      closeAllCtx();
      if(!isVisible) menu.classList.add('visible');
    });
  });
}

function bindCtxActions() {
  document.querySelectorAll('[data-action]').forEach(el => {
    el.addEventListener('click', e => {
      e.stopPropagation();
      const {action,id} = el.dataset;
      closeAllCtx();
      if(action==='detail') openDrawer(+id);
      if(action==='edit')   openEditModal(+id);
      if(action==='delete') openDeleteConfirm(+id);
    });
  });
}

/* DETAIL DRAWER */
function openDrawer(id) {
  const row = document.querySelector(`tr[data-id="${id}"]`);
  if(!row) return;
  state.activeDrawer = id;
  const cells = row.querySelectorAll('td');
  document.getElementById('drawerDonorName').textContent = cells[1]?.textContent.trim()||'—';
  document.getElementById('drawerAmount').textContent    = cells[2]?.textContent.trim()||'—';
  document.getElementById('drawerDate').textContent      = cells[0]?.textContent.trim()||'—';
  document.getElementById('drawerType').innerHTML        = cells[3]?.innerHTML||'—';
  document.getElementById('drawerMethod').innerHTML      = cells[4]?.innerHTML||'—';
  document.getElementById('drawerId').textContent        = `#${id}`;
  document.getElementById('drawerOverlay').classList.add('show');
  document.getElementById('drawer').classList.add('show');
}

function closeDrawer() {
  document.getElementById('drawerOverlay').classList.remove('show');
  document.getElementById('drawer').classList.remove('show');
  state.activeDrawer = null;
}

function initDrawer() {
  document.getElementById('drawerClose')?.addEventListener('click', closeDrawer);
  document.getElementById('drawerOverlay')?.addEventListener('click', closeDrawer);
  document.getElementById('drawerEditBtn')?.addEventListener('click', () => {
    const id = state.activeDrawer; closeDrawer(); openEditModal(id);
  });
}

/* EDIT MODAL */
function openEditModal(id) {
  const row = document.querySelector(`tr[data-id="${id}"]`);
  if(!row) return;
  const cells = row.querySelectorAll('td');
  document.getElementById('editId').value     = id;
  document.getElementById('editName').value   = cells[1]?.textContent.trim()||'';
  document.getElementById('editAmount').value = (cells[2]?.textContent||'').replace(/[₩,원\s]/g,'');
  const typeText = cells[3]?.textContent.trim();
  const em = document.getElementById('editType');
  if(em) em.value = typeText.includes('정기') ? '정기' : '일시불';
  const mm = document.getElementById('editMethod');
  if(mm) mm.value = cells[4]?.textContent.trim().includes('카드') ? '카드' : '계좌이체';
  document.getElementById('editModal').classList.add('show');
}

function initEditModal() {
  document.getElementById('editModalClose')?.addEventListener('click', ()=>document.getElementById('editModal').classList.remove('show'));
  document.getElementById('editModalCancel')?.addEventListener('click', ()=>document.getElementById('editModal').classList.remove('show'));
  document.getElementById('editModalSave')?.addEventListener('click', ()=>{
    const id=document.getElementById('editId').value;
    const name=document.getElementById('editName').value.trim();
    const amount=document.getElementById('editAmount').value;
    const type=document.getElementById('editType').value;
    const method=document.getElementById('editMethod')?.value||'';
    if(!name||!amount){showToast('이름과 금액을 입력해주세요.','error');return;}
    fetch(`${CTX}/admin/donation/update.do`,{
      method:'POST',
      headers:{'Content-Type':'application/x-www-form-urlencoded'},
      body:new URLSearchParams({donationId:id,donatorName:name,donationAmount:amount,donationType:type,donationMethod:method}),
    }).then(r=>r.json()).then(data=>{
      document.getElementById('editModal').classList.remove('show');
      if(data.success){showToast('후원 내역이 수정되었습니다.','success');setTimeout(()=>location.reload(),900);}
      else showToast(data.message||'수정 중 오류가 발생했습니다.','error');
    }).catch(()=>showToast('서버와 통신 중 문제가 발생했습니다.','error'));
  });
}

/* DELETE MODAL */
let deleteTargetId = null;
function openDeleteConfirm(id) {
  deleteTargetId = id;
  const name = document.querySelector(`tr[data-id="${id}"]`)?.querySelectorAll('td')[1]?.textContent.trim()||'이 후원자';
  document.getElementById('deleteTargetName').textContent = name;
  document.getElementById('deleteModal').classList.add('show');
}

function initDeleteModal() {
  document.getElementById('deleteModalClose')?.addEventListener('click', ()=>document.getElementById('deleteModal').classList.remove('show'));
  document.getElementById('deleteModalCancel')?.addEventListener('click', ()=>document.getElementById('deleteModal').classList.remove('show'));
  document.getElementById('deleteModalConfirm')?.addEventListener('click', ()=>{
    if(deleteTargetId===null) return;
    fetch(`${CTX}/admin/donation/delete.do`,{
      method:'POST',
      headers:{'Content-Type':'application/x-www-form-urlencoded'},
      body:new URLSearchParams({donationId:deleteTargetId}),
    }).then(r=>r.json()).then(data=>{
      document.getElementById('deleteModal').classList.remove('show');
      deleteTargetId=null;
      if(data.success){showToast('후원 내역이 삭제되었습니다.','error');setTimeout(()=>location.reload(),900);}
      else showToast(data.message||'삭제 중 오류가 발생했습니다.','error');
    }).catch(()=>showToast('서버와 통신 중 문제가 발생했습니다.','error'));
  });
}

/* RECEIPT FILTER */
function initReceiptFilters(){
  // 탭 안 pills (id="receiptTabPills")
  document.getElementById('receiptTabPills')?.querySelectorAll('.pill-btn').forEach(btn=>{
    btn.addEventListener('click',()=>{
      document.getElementById('receiptTabPills').querySelectorAll('.pill-btn').forEach(b=>b.classList.remove('active'));
      btn.classList.add('active');
      filterReceiptRows('receiptTableBody', btn.dataset.filter);
    });
  });

  document.getElementById('receiptTableBody')?.addEventListener('change', e => {
    if(!e.target.matches('[data-receipt-id]')) return;
    const donationId = e.target.dataset.receiptId;
    const isChecked = e.target.checked;
    const newStatus = isChecked ? 'I' : 'Y';
    const tr = e.target.closest('tr');
    const badge = tr?.querySelector('.badge');
    const name = tr?.querySelector('.td-name')?.textContent || '';

    fetch(`${CTX}/admin/updateReceiptStatus.do`,{
      method:'POST',
      headers:{'Content-Type':'application/x-www-form-urlencoded'},
      body:`donationId=${donationId}&receiptYn=${newStatus}`
    })
    .then(res=>res.json())
    .then(data=>{
      if(data.result==='success'){
        if(badge){
          badge.className = isChecked ? 'badge badge--issued' : 'badge badge--waiting';
          badge.textContent = isChecked ? '발급완료' : '대기중';
        }
        showToast(isChecked ? `${name}님 영수증 발급 완료!` : `${name}님 발급 대기 전환`, isChecked ? 'success' : 'info');
      } else {
        alert('DB 저장 중 오류가 발생했습니다.');
        e.target.checked = !isChecked; 
      }
    })
    .catch(err=>{console.error(err); e.target.checked = !isChecked;});
  });
}

function filterReceiptRows(tbodyId, filter) {
  document.querySelectorAll(`#${tbodyId} tr`).forEach(row=>{
    const badge=row.querySelector('.badge');
    if(!badge) return;
    if(filter==='all'){row.style.display='';return;}
    const isIssued=badge.classList.contains('badge--issued');
    row.style.display=(filter==='issued'?isIssued:!isIssued)?'':'none';
  });
}

/* EXCEL 엑셀*/
function downloadExcel() {
  if(typeof XLSX==='undefined'){showToast('SheetJS 라이브러리를 불러오는 중입니다.','error');return;}
  showToast('엑셀 파일을 생성 중입니다…','info',1800);
  const url=new URL(window.location.href);
  const params=new URLSearchParams({keyword:url.searchParams.get('keyword')||'',method:url.searchParams.get('method')||'',paymentType:url.searchParams.get('paymentType')||'',startDate:url.searchParams.get('startDate')||'',endDate:url.searchParams.get('endDate')||''});
  fetch(`${CTX}/admin/donation/excel.do?${params}`).then(r=>r.json()).then(data=>{
    if(!data.success) throw new Error(data.message);
    const rows=(data.data||[]).map(d=>({'내역번호':d.donationId,'후원일자':d.donationDateStr||'','기부자명':d.donatorName||'','회원ID':d.memberId||'','금액(원)':d.donationAmount,'결제수단':d.donationMethod||'','후원유형':d.donationType||'','영수증신청':d.receiptYn==='Y'?'신청':'미신청'}));
    const wb=XLSX.utils.book_new(),ws=XLSX.utils.json_to_sheet(rows);
    const range=XLSX.utils.decode_range(ws['!ref']||'A1');
    ws['!cols']=Array.from({length:range.e.c+1},(_,C)=>{let max=10;for(let R=range.s.r;R<=range.e.r;R++){const cell=ws[XLSX.utils.encode_cell({r:R,c:C})];if(cell?.v!=null)max=Math.max(max,String(cell.v).length);}return{wch:Math.min(max+2,40)};});
    XLSX.utils.book_append_sheet(wb,ws,'후원내역');
    const today=new Date(),ymd=`${today.getFullYear()}${String(today.getMonth()+1).padStart(2,'0')}${String(today.getDate()).padStart(2,'0')}`;
    XLSX.writeFile(wb,`기부관리현황_${ymd}.xlsx`);
    setTimeout(()=>showToast('엑셀 다운로드가 완료되었습니다.','success',3000),400);
  }).catch(err=>{console.error(err);showToast('엑셀 생성 중 오류가 발생했습니다.','error');});
}

function initExcelDownload(){document.getElementById('btnExcel')?.addEventListener('click',downloadExcel);}

/* CALCULATOR 하... 복잡하다..*/
function initCalc() {
  const fab=document.getElementById('calcFab'),panel=document.getElementById('calcPanel');
  if(!fab||!panel) return;
  fab.addEventListener('click',()=>{state.calcOpen=!state.calcOpen;panel.classList.toggle('open',state.calcOpen);});
  document.getElementById('calcClose')?.addEventListener('click',()=>{state.calcOpen=false;panel.classList.remove('open');});
  document.querySelectorAll('.calc-key').forEach(k=>k.addEventListener('click',()=>calcInput(k.dataset.key)));
  document.addEventListener('keydown',e=>{
    if(!state.calcOpen) return;
    const map={'0':'0','1':'1','2':'2','3':'3','4':'4','5':'5','6':'6','7':'7','8':'8','9':'9','+':'+','-':'-','*':'×','/':'÷','.':'.','Enter':'=','Backspace':'⌫','Escape':'C','=':'='};
    if(map[e.key]){e.preventDefault();calcInput(map[e.key]);}
  });
  function calcInput(key){
    const disp=document.getElementById('calcDisplay'),expr=document.getElementById('calcExpr'),hist=document.getElementById('calcHistory');
    if(key==='C'){state.calcExpr='';state.calcDisplay='0';}
    else if(key==='⌫'){state.calcExpr=state.calcExpr.slice(0,-1);state.calcDisplay=state.calcExpr||'0';}
    else if(key==='='){
      if(!state.calcExpr) return;
      try{const norm=state.calcExpr.replace(/×/g,'*').replace(/÷/g,'/');const r=String(parseFloat(Function(`"use strict";return(${norm})`)().toFixed(10)));state.calcHistory.unshift(`${state.calcExpr} = ${r}`);if(state.calcHistory.length>5)state.calcHistory.pop();state.calcDisplay=r;state.calcExpr=r;}
      catch{state.calcDisplay='오류';state.calcExpr='';}
    }else if(key==='%'){
      try{const norm=state.calcExpr.replace(/×/g,'*').replace(/÷/g,'/');const r=String(parseFloat((Function(`"use strict";return(${norm})`)()/100).toFixed(10)));state.calcExpr=r;state.calcDisplay=r;}catch{}
    }else{state.calcExpr+=key;state.calcDisplay=state.calcExpr;}
    disp.textContent=state.calcDisplay;
    expr.textContent=state.calcExpr!==state.calcDisplay?state.calcExpr:'';
    hist.innerHTML=state.calcHistory.map(h=>`<div class="calc-history__item">${h}</div>`).join('');
  }
  document.getElementById('calcCopy')?.addEventListener('click',()=>navigator.clipboard?.writeText(state.calcDisplay).then(()=>showToast('결과가 클립보드에 복사되었습니다.','success',1800)));
  document.getElementById('calcInsert')?.addEventListener('click',()=>{const ra=document.getElementById('regAmount');if(ra&&!isNaN(+state.calcDisplay)){ra.value=state.calcDisplay;document.getElementById('registerModal')?.classList.add('show');state.calcOpen=false;panel.classList.remove('open');showToast('금액이 수동 등록 폼에 입력되었습니다.','info');}});
}

/* OUTSIDE CLICK */
document.addEventListener('click',e=>{
  if(!e.target.closest('[data-ctx]')&&!e.target.closest('.ctx-menu')) closeAllCtx();
  ['editModal','deleteModal','registerModal'].forEach(id=>{const el=document.getElementById(id);if(el&&e.target===el)el.classList.remove('show');});
});

/* ══════════════════════════════════════
   MANUAL REGISTER MODAL (기부 수동 등록)
══════════════════════════════════════ */
function initRegisterModal() {
    const btnOpen = document.getElementById('btnManualRegister'); 
    
    // 모달 및 닫기 버튼들
    const modal = document.getElementById('registerModal');
    const btnClose = document.getElementById('registerModalClose');
    const btnCancel = document.getElementById('registerModalCancel');
    const btnSave = document.getElementById('registerModalSave');

    // 1. 모달 열기
    if (btnOpen && modal) {
        btnOpen.addEventListener('click', () => {
            // 열 때마다 이전 입력값 초기화
            document.getElementById('regName').value = '';
            document.getElementById('regTel').value = '';
            document.getElementById('regEmail').value = '';
            document.getElementById('regAmount').value = '';
            document.getElementById('regType').value = '정기';
            document.getElementById('regMethod').value = '카드';
            
            modal.classList.add('show');
        });
    }

    // 2. 모달 닫기
    const closeModal = () => modal.classList.remove('show');
    if (btnClose) btnClose.addEventListener('click', closeModal);
    if (btnCancel) btnCancel.addEventListener('click', closeModal);

    // 3. 등록 버튼 클릭 시 (추후 Ajax 연동)
    if (btnSave) {
        btnSave.addEventListener('click', () => {
            const name = document.getElementById('regName').value.trim();
            const amount = document.getElementById('regAmount').value.trim();
            
            if(!name || !amount) {
                showToast('필수 항목(기부자명, 금액)을 입력해주세요.', 'error');
                return;
            }
            
            // TODO: 서버로 데이터 전송 (fetch API 사용)
            console.log("등록 진행:", { name, amount });
            showToast('수동 등록이 완료되었습니다.', 'success');
            closeModal();
        });
    }
}

/* ══════════════════════════════════════
   대시보드 통계 카드 클릭 이벤트 (비동기)
══════════════════════════════════════ */
function initDashboardCards() {
    // [보조 함수] 날짜를 YYYY-MM-DD 형식으로 변환 (타임존 오류 방지)
    const getLocalDateString = (date) => {
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const day = String(date.getDate()).padStart(2, '0');
        return `${year}-${month}-${day}`;
    };

    // 1. 이번 달 기부 건수 클릭
    document.getElementById('cardThisMonthCount')?.addEventListener('click', () => {
        const now = new Date();
        // 이번 달 1일
        const firstDay = getLocalDateString(new Date(now.getFullYear(), now.getMonth(), 1));
        // 오늘 날짜 
        const today = getLocalDateString(now);

        // 필터 UI 초기화
        const elSearch = document.getElementById('donSearch');
        const elType = document.getElementById('donTypeFilter');
        const elMethod = document.getElementById('donMethodFilter');
        
        if(elSearch) elSearch.value = '';
        if(elType) elType.value = '';
        if(elMethod) elMethod.value = '';
        
        // 날짜 세팅 (1일 ~ 오늘)
        const elStart = document.getElementById('startDate');
        const elEnd = document.getElementById('endDate');
        if(elStart) elStart.value = firstDay;
        if(elEnd) elEnd.value = today;

        // 상태(state) 동기화
        state.donFilter = { 
            keyword: '', paymentType: '', method: '', 
            startDate: firstDay, endDate: today 
        };
        state.currentPage = 1;

        // 안내 문구 출력
        showToast(`${now.getMonth() + 1}월 내역을 조회합니다.`, 'info');

        // 탭 이동 및 데이터 조회
        const allTab = document.querySelector('.tab-btn[data-tab="all"]');
        if(allTab) {
            allTab.click(); 
            submitFilter(1);
        }
    });

    // 2. 전체 기부 건수 클릭
    document.getElementById('cardTotalCount')?.addEventListener('click', () => {
        // 모든 필터 비우기
        ['donSearch', 'donTypeFilter', 'donMethodFilter', 'startDate', 'endDate'].forEach(id => {
            const el = document.getElementById(id);
            if(el) el.value = '';
        });

        state.donFilter = { keyword: '', paymentType: '', method: '', startDate: '', endDate: '' };
        state.currentPage = 1;

        showToast('전체 내역을 조회합니다.', 'success');

        document.querySelector('.tab-btn[data-tab="all"]')?.click();
        submitFilter(1);
    });

    // 3. 영수증 신청 건수 (기존 로직 유지)
    document.getElementById('cardReceiptCount')?.addEventListener('click', () => {
        const receiptTab = document.querySelector('.tab-btn[data-tab="receipt"]');
        if(receiptTab) {
            receiptTab.click();
            document.querySelector('.table-card')?.scrollIntoView({ behavior: 'smooth' });
        }
    });

    /*// 4. 누적 기부금 (기존 로직 유지)
    document.getElementById('cardTotalAmount')?.addEventListener('click', () => {
        location.href = CTX + '/admin/donationStats.do';
    });*/
}
/* ══════════════════════════════════════
   비동기 후원 목록 조회 공통 함수
   GET /admin/donationList.do
   → AdminDonationList.java 가 JSON 반환
   → 테이블 HTML 재생성 + 페이지네이션 갱신
══════════════════════════════════════ */
function fetchDonationData(params) {
  // 파라미터가 없으면 현재 state의 필터값 사용
  const query = new URLSearchParams({
    keyword:     params.keyword     !== undefined ? params.keyword     : state.donFilter.keyword,
    paymentType: params.paymentType !== undefined ? params.paymentType : state.donFilter.paymentType,
    method:      params.method      !== undefined ? params.method      : state.donFilter.method,
	startDate:   params.startDate   !== undefined ? params.startDate   : state.donFilter.startDate,
	endDate:     params.endDate     !== undefined ? params.endDate     : state.donFilter.endDate,
    page:        params.page        !== undefined ? params.page        : state.currentPage,
  }).toString();

  fetch(`${CTX}/admin/donationList.do?${query}`, {
    headers: { 'X-Requested-With': 'XMLHttpRequest' }
  })
  .then(res => {
    if (!res.ok) throw new Error('서버 오류 ' + res.status);
    return res.json();
  })
  .then(data => {
    // state 갱신 — renderPagination()이 state를 읽으므로 먼저 세팅
    state.totalCount   = data.totalCount   || 0;
    state.currentPage  = data.currentPage  || 1;
    state.pageSize     = data.pageSize     || 5;

    // 테이블 HTML 재생성
    renderDonationTable(data.donationList || []);

    // 페이지네이션 재생성 (파라미터 없이 state 사용)
    renderPagination();
  })
  .catch(err => {
    console.error('비동기 요청 실패:', err);
    showToast('데이터 조회 중 오류가 발생했습니다.', 'error');
  });
}

/* ══════════════════════════════════════
   비동기 테이블 렌더링 함수
   fetchDonationData()의 응답 데이터로 tbody 재구성
   ✅ JSP의 c:forEach 대신 JS로 동적 생성
══════════════════════════════════════ */
function renderDonationTable(list) {
  const tbody = document.getElementById('donTableBody');
  if (!tbody) return;

  if (!list || list.length === 0) {
    // 데이터 없을 때 빈 상태 표시
    tbody.innerHTML = `<tr><td colspan="6" class="empty-state">
      <span class="material-symbols-outlined">search_off</span>
      <p>후원 내역이 없습니다</p>
    </td></tr>`;
    return;
  }

  // 각 행 HTML 생성
  tbody.innerHTML = list.map(d => {
    
    const typeLabel = d.donationType === '정기'  ? '정기 후원'
                    : d.donationType === '일시불' ? '일시 후원'
                    : (d.donationType || '');
    const typeClass = (d.donationType === '정기') ? 'type-tag--regular' : 'type-tag--once';
    const methodLabel = d.donationMethod === '카드' ? '신용카드' : (d.donationMethod || '');
    const methodClass = (d.donationMethod === '카드' || d.donationMethod === '신용카드')
                        ? 'badge--complete' : 'badge--pending';

    return `
      <tr data-id="${d.donationId}">
        <td class="td-date">${d.donationDateStr || ''}</td>
        <td class="td-name">${d.donatorName || ''}</td>
        <td class="td-amount">₩${fmt(d.donationAmount)}</td>
        <td><span class="type-tag ${typeClass}">${typeLabel}</span></td>
        <td class="td-center"><span class="badge ${methodClass}">${methodLabel}</span></td>
        <td class="td-right" style="position:relative">
          <button class="more-btn" data-ctx="${d.donationId}" aria-label="더보기">
            <span class="material-symbols-outlined">more_vert</span>
          </button>
          <div class="ctx-menu" id="ctx-${d.donationId}">
            <div class="ctx-menu__item" data-action="detail" data-id="${d.donationId}">
              <span class="material-symbols-outlined">visibility</span> 상세보기
            </div>
            <div class="ctx-menu__item" data-action="edit" data-id="${d.donationId}">
              <span class="material-symbols-outlined">edit</span> 수정
            </div>
            <div class="ctx-menu__divider"></div>
            <div class="ctx-menu__item ctx-menu__item--danger" data-action="delete" data-id="${d.donationId}">
              <span class="material-symbols-outlined">delete</span> 삭제
            </div>
          </div>
        </td>
      </tr>`;
  }).join('');

  // 새로 생성된 행들에 이벤트 재바인딩
  bindCtxMenus();
  bindCtxActions();
}


/* INIT */
document.addEventListener('DOMContentLoaded',()=>{
  initTabs();
  initDonationFilters();
  initReceiptFilters();
  initDrawer();
  initEditModal();
  initDeleteModal();
  initExcelDownload();
  initHeaderSearch();
  initCalc();
  bindCtxMenus();
  bindCtxActions();
  renderPagination();
  initRegisterModal();
  initDashboardCards();
  
});
