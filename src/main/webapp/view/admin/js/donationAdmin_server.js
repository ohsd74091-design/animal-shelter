/* ═══════════════════════════════════════════════
   Amber Hearth — Donation Admin JS  (서버 연동 버전)
   donationAdmin.js
═══════════════════════════════════════════════ */

'use strict';

/*
 * ★ donationAdmin.jsp 하단에 아래 두 줄을 추가하세요.
 *    JS 파일 로드보다 반드시 먼저 와야 합니다.
 *
 *  <script>
 *    const CTX       = '${pageContext.request.contextPath}';
 *    const PAGE_INFO = {
 *      currentPage : ${currentPage},
 *      totalCount  : ${totalCount},
 *      pageSize    : ${pageSize}
 *    };
 *  </script>
 *  <script src="${ctp}/resources/js/donationAdmin.js"></script>
 */
const CTX = window.CTX || '';

/* ══════════════════════════════════════
   STATE
══════════════════════════════════════ */
const state = {
  donations:    [],
  totalCount:   0,
  currentPage:  1,
  pageSize:     5,
  donFilter: { keyword:'', method:'', paymentType:'', startDate:'', endDate:'' },
  receiptFilter: 'all',
  calcOpen:     false,
  calcExpr:     '',
  calcDisplay:  '0',
  calcHistory:  [],
  activeTab:    'all',
  activeDrawer: null,
};

/* ══════════════════════════════════════
   UTILS
══════════════════════════════════════ */
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

/* ══════════════════════════════════════
   TABS
══════════════════════════════════════ */
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

/* ══════════════════════════════════════
   ★ 목록 서버 재조회 (AJAX)
   GET /admin/donation.do?keyword=&method=&page=1
   ※ DonationAdminForm 이 X-Requested-With 헤더 감지 시
      JSON 으로 응답하도록 수정 필요 (아래 주석 참고)
══════════════════════════════════════ */
function loadDonationList(page) {
  if (page) state.currentPage = page;

  const params = new URLSearchParams({
    keyword:     state.donFilter.keyword,
    method:      state.donFilter.method,
    paymentType: state.donFilter.paymentType,
    startDate:   state.donFilter.startDate,
    endDate:     state.donFilter.endDate,
    page:        state.currentPage,
  });

  fetch(`${CTX}/admin/donation.do?${params}`, {
    headers: { 'X-Requested-With': 'XMLHttpRequest' }
  })
  .then(res => { if (!res.ok) throw new Error('서버 오류'); return res.json(); })
  .then(data => {
    if (data.success) {
      state.donations  = data.donationList;
      state.totalCount = data.totalCount;
      renderDonationTable();
    } else {
      showToast(data.message || '목록 조회 중 오류가 발생했습니다.', 'error');
    }
  })
  .catch(err => console.error('loadDonationList:', err));
}

/*
 * ★ DonationAdminForm.java 에 아래 분기를 추가하면
 *    AJAX 요청 시 JSON으로 응답합니다.
 *
 *  // doGet() 맨 앞 부분에 추가
 *  boolean isAjax = "XMLHttpRequest".equals(req.getHeader("X-Requested-With"));
 *  if (isAjax) {
 *      resp.setContentType("application/json; charset=UTF-8");
 *      // ... (서비스 호출 동일) ...
 *      JSONObject json = new JSONObject();          // 또는 직접 StringBuilder
 *      json.put("success", true);
 *      json.put("donationList", donationList);      // Jackson 있으면 ObjectMapper 사용
 *      json.put("totalCount", totalCount);
 *      resp.getWriter().print(json.toString());
 *      return;
 *  }
 *  // 기존 forward 로직 이어서...
 */

/* ══════════════════════════════════════
   DONATION TABLE 렌더링
   (AJAX 응답 후 state.donations 기반)
══════════════════════════════════════ */
function renderDonationTable() {
  const tbody = document.getElementById('donTableBody');
  if (!tbody) return;

  if (state.donations.length === 0) {
    tbody.innerHTML = `<tr><td colspan="6" class="empty-state">
      <span class="material-symbols-outlined">search_off</span><p>검색 결과가 없습니다</p>
    </td></tr>`;
  } else {
    tbody.innerHTML = state.donations.map(d => {
      const typeClass  = d.donationType === '정기' ? 'regular' : 'once';
      const statusCls  = d.donationMethod === '카드결제' ? 'complete' : 'pending';
      const statusText = d.donationMethod === '카드결제' ? '결제완료' : '처리중';
      return `
        <tr data-id="${d.donationId}">
          <td class="td-date">${d.donationDateStr || ''}</td>
          <td class="td-name">${d.donatorName || ''}</td>
          <td class="td-amount">₩${fmt(d.donationAmount)}</td>
          <td><span class="type-tag type-tag--${typeClass}">${d.donationType || ''}</span></td>
          <td class="td-center">
            <span class="badge badge--${statusCls}">${statusText}</span>
          </td>
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
  }

  renderPagination();
  bindCtxMenus();
  bindCtxActions();
}

/* ── Pagination ── */
function renderPagination() {
  const wrap = document.getElementById('donPagination');
  const info = document.getElementById('donPagInfo');
  if (!wrap) return;

  const total = state.totalCount;
  const size  = state.pageSize;
  const cur   = state.currentPage;
  const pages = Math.max(1, Math.ceil(total / size));
  const start = (cur - 1) * size + 1;
  const end   = Math.min(cur * size, total);

  if (info) info.textContent = `총 ${total}건의 내역 중 ${start}-${end} 표시`;

  let html = `<button class="page-btn" id="pagPrev"><span class="material-symbols-outlined">chevron_left</span></button>`;
  const range = Array.from({ length: pages }, (_, i) => i + 1)
    .filter(p => p === 1 || p === pages || (p >= cur - 1 && p <= cur + 1));
  range.forEach((p, i) => {
    if (i > 0 && p - range[i - 1] > 1) html += `<span style="padding:0 4px;color:var(--slate-400)">…</span>`;
    html += `<button class="page-btn${p === cur ? ' active' : ''}" data-page="${p}">${p}</button>`;
  });
  html += `<button class="page-btn" id="pagNext"><span class="material-symbols-outlined">chevron_right</span></button>`;
  wrap.innerHTML = html;

  wrap.querySelectorAll('[data-page]').forEach(b =>
    b.addEventListener('click', () => loadDonationList(+b.dataset.page)));
  wrap.querySelector('#pagPrev')?.addEventListener('click', () => { if (cur > 1) loadDonationList(cur - 1); });
  wrap.querySelector('#pagNext')?.addEventListener('click', () => { if (cur < pages) loadDonationList(cur + 1); });
}

/* ── Context Menus ── */
function bindCtxMenus() {
  document.querySelectorAll('[data-ctx]').forEach(btn => {
    btn.addEventListener('click', e => {
      e.stopPropagation();
      const menu = document.getElementById(`ctx-${btn.dataset.ctx}`);
      const isVisible = menu.classList.contains('visible');
      closeAllCtx();
      if (!isVisible) menu.classList.add('visible');
    });
  });
}

function bindCtxActions() {
  document.querySelectorAll('[data-action]').forEach(el => {
    el.addEventListener('click', e => {
      e.stopPropagation();
      const { action, id } = el.dataset;
      closeAllCtx();
      if (action === 'detail') openDrawer(+id);
      if (action === 'edit')   openEditModal(+id);
      if (action === 'delete') openDeleteConfirm(+id);
    });
  });
}

/* ── Filters ── */
function initDonationFilters() {
  document.getElementById('donSearch')?.addEventListener('input', e => {
    state.donFilter.keyword = e.target.value.trim();
  });
  document.getElementById('donSearch')?.addEventListener('keydown', e => {
    if (e.key === 'Enter') loadDonationList(1);
  });
  document.getElementById('donTypeFilter')?.addEventListener('change', e => {
    state.donFilter.paymentType = e.target.value;
    loadDonationList(1);
  });
  document.getElementById('donMethodFilter')?.addEventListener('change', e => {
    state.donFilter.method = e.target.value;
    loadDonationList(1);
  });
}

/* ══════════════════════════════════════
   RECEIPT TABLE
   — JSP c:forEach 렌더링 후 필터·토글만 JS 처리
══════════════════════════════════════ */
function initReceiptFilters() {
  document.querySelectorAll('.pill-btn').forEach(btn => {
    btn.addEventListener('click', () => {
      btn.closest('.filter-pills')
         ?.querySelectorAll('.pill-btn')
         .forEach(b => b.classList.remove('active'));
      btn.classList.add('active');
      filterReceiptRows(btn.dataset.filter);
    });
  });

  // 영수증 토글 (이벤트 위임)
  document.getElementById('receiptTableBody')?.addEventListener('change', e => {
    if (!e.target.matches('[data-receipt-id]')) return;
    const name  = e.target.closest('tr')?.querySelector('.td-name')?.textContent || '';
    const badge = e.target.closest('tr')?.querySelector('.badge');
    if (badge) {
      badge.className   = e.target.checked ? 'badge badge--issued' : 'badge badge--waiting';
      badge.textContent = e.target.checked ? '발급완료' : '대기중';
    }
    showToast(
      e.target.checked ? `${name}님 영수증이 발급 완료 처리되었습니다.`
                       : `${name}님 영수증이 대기 중으로 변경되었습니다.`,
      e.target.checked ? 'success' : 'info'
    );
  });
}

function filterReceiptRows(filter) {
  document.querySelectorAll('#receiptTableBody tr').forEach(row => {
    const badge = row.querySelector('.badge');
    if (!badge) return;
    if (filter === 'all') { row.style.display = ''; return; }
    const isIssued = badge.classList.contains('badge--issued');
    row.style.display = (filter === 'issued' ? isIssued : !isIssued) ? '' : 'none';
  });
}

/* ══════════════════════════════════════
   DETAIL DRAWER
══════════════════════════════════════ */
function openDrawer(id) {
  const row = document.querySelector(`tr[data-id="${id}"]`);
  if (!row) return;
  state.activeDrawer = id;

  const cells = row.querySelectorAll('td');
  document.getElementById('drawerDonorName').textContent = cells[1]?.textContent.trim() || '—';
  document.getElementById('drawerAmount').textContent    = cells[2]?.textContent.trim() || '—';
  document.getElementById('drawerDate').textContent      = cells[0]?.textContent.trim() || '—';
  document.getElementById('drawerType').innerHTML        = cells[3]?.innerHTML          || '—';
  document.getElementById('drawerStatus').innerHTML      = cells[4]?.innerHTML          || '—';
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
    const id = state.activeDrawer;
    closeDrawer();
    openEditModal(id);
  });
}

/* ══════════════════════════════════════
   ★ EDIT MODAL  → POST /admin/donation/update.do
══════════════════════════════════════ */
function openEditModal(id) {
  const row = document.querySelector(`tr[data-id="${id}"]`);
  if (!row) return;
  const cells = row.querySelectorAll('td');
  document.getElementById('editId').value     = id;
  document.getElementById('editName').value   = cells[1]?.textContent.trim() || '';
  document.getElementById('editAmount').value = (cells[2]?.textContent || '').replace(/[₩,원\s]/g, '');
  document.getElementById('editType').value   = cells[3]?.textContent.trim() === '정기' ? '정기' : '일시';
  document.getElementById('editModal').classList.add('show');
}

function initEditModal() {
  document.getElementById('editModalClose')?.addEventListener('click', () =>
    document.getElementById('editModal').classList.remove('show'));
  document.getElementById('editModalCancel')?.addEventListener('click', () =>
    document.getElementById('editModal').classList.remove('show'));

  document.getElementById('editModalSave')?.addEventListener('click', () => {
    const id     = document.getElementById('editId').value;
    const name   = document.getElementById('editName').value.trim();
    const amount = document.getElementById('editAmount').value;
    const type   = document.getElementById('editType').value;
    const method = document.getElementById('editMethod')?.value || '';

    if (!name || !amount) { showToast('이름과 금액을 입력해주세요.', 'error'); return; }

    fetch(`${CTX}/admin/donation/update.do`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
      body: new URLSearchParams({ donationId:id, donatorName:name, donationAmount:amount, donationType:type, donationMethod:method }),
    })
    .then(res => res.json())
    .then(data => {
      document.getElementById('editModal').classList.remove('show');
      showToast(data.success ? '후원 내역이 수정되었습니다.' : (data.message || '수정 중 오류'), data.success ? 'success' : 'error');
      if (data.success) loadDonationList(state.currentPage);
    })
    .catch(() => showToast('서버와 통신 중 문제가 발생했습니다.', 'error'));
  });
}

/* ══════════════════════════════════════
   ★ DELETE MODAL → POST /admin/donation/delete.do
══════════════════════════════════════ */
let deleteTargetId = null;

function openDeleteConfirm(id) {
  deleteTargetId = id;
  const row  = document.querySelector(`tr[data-id="${id}"]`);
  const name = row?.querySelectorAll('td')[1]?.textContent.trim() || '이 후원자';
  document.getElementById('deleteTargetName').textContent = name;
  document.getElementById('deleteModal').classList.add('show');
}

function initDeleteModal() {
  document.getElementById('deleteModalClose')?.addEventListener('click', () =>
    document.getElementById('deleteModal').classList.remove('show'));
  document.getElementById('deleteModalCancel')?.addEventListener('click', () =>
    document.getElementById('deleteModal').classList.remove('show'));

  document.getElementById('deleteModalConfirm')?.addEventListener('click', () => {
    if (deleteTargetId === null) return;

    fetch(`${CTX}/admin/donation/delete.do`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
      body: new URLSearchParams({ donationId: deleteTargetId }),
    })
    .then(res => res.json())
    .then(data => {
      document.getElementById('deleteModal').classList.remove('show');
      deleteTargetId = null;
      showToast(data.success ? '후원 내역이 삭제되었습니다.' : (data.message || '삭제 중 오류'), data.success ? 'error' : 'error');
      if (data.success) loadDonationList(state.currentPage);
    })
    .catch(() => showToast('서버와 통신 중 문제가 발생했습니다.', 'error'));
  });
}

/* ══════════════════════════════════════
   ★ EXCEL DOWNLOAD → GET /admin/donation/excel.do + SheetJS
══════════════════════════════════════ */
function downloadExcel() {
  if (typeof XLSX === 'undefined') {
    showToast('SheetJS 라이브러리를 불러오는 중입니다. 잠시 후 다시 시도해주세요.', 'error'); return;
  }
  showToast('엑셀 파일을 생성 중입니다…', 'info', 1800);

  const params = new URLSearchParams({
    keyword:     state.donFilter.keyword,
    method:      state.donFilter.method,
    paymentType: state.donFilter.paymentType,
    startDate:   state.donFilter.startDate,
    endDate:     state.donFilter.endDate,
  });

  fetch(`${CTX}/admin/donation/excel.do?${params}`)
    .then(res => res.json())
    .then(data => {
      if (!data.success) throw new Error(data.message);
      const rows = (data.data || []).map(d => ({
        '내역번호':   d.donationId,
        '후원일자':   d.donationDateStr || '',
        '기부자명':   d.donatorName     || '',
        '회원ID':     d.memberId        || '',
        '금액(원)':   d.donationAmount,
        '결제수단':   d.donationMethod  || '',
        '후원유형':   d.donationType    || '',
        '영수증신청': d.receiptYn === 'Y' ? '신청' : '미신청',
      }));

      const wb = XLSX.utils.book_new();
      const ws = XLSX.utils.json_to_sheet(rows);
      autoColWidth(ws);
      XLSX.utils.book_append_sheet(wb, ws, '후원내역');

      const today = new Date();
      const ymd   = `${today.getFullYear()}${String(today.getMonth()+1).padStart(2,'0')}${String(today.getDate()).padStart(2,'0')}`;
      XLSX.writeFile(wb, `기부관리현황_${ymd}.xlsx`);
      setTimeout(() => showToast('엑셀 다운로드가 완료되었습니다.', 'success', 3000), 400);
    })
    .catch(err => { console.error(err); showToast('엑셀 생성 중 오류가 발생했습니다.', 'error'); });
}

function autoColWidth(ws) {
  if (!ws['!ref']) return;
  const range = XLSX.utils.decode_range(ws['!ref']);
  ws['!cols'] = Array.from({ length: range.e.c + 1 }, (_, C) => {
    let max = 10;
    for (let R = range.s.r; R <= range.e.r; R++) {
      const cell = ws[XLSX.utils.encode_cell({ r:R, c:C })];
      if (cell?.v != null) max = Math.max(max, String(cell.v).length);
    }
    return { wch: Math.min(max + 2, 40) };
  });
}

function initExcelDownload() {
  document.getElementById('btnExcel')?.addEventListener('click', downloadExcel);
}

/* ══════════════════════════════════════
   HEADER SEARCH
══════════════════════════════════════ */
function initHeaderSearch() {
  const hs = document.getElementById('headerSearch');
  const ds = document.getElementById('donSearch');
  hs?.addEventListener('input', e => {
    state.donFilter.keyword = e.target.value.trim();
    if (ds) ds.value = e.target.value;
  });
  hs?.addEventListener('keydown', e => { if (e.key === 'Enter') loadDonationList(1); });
}

/* ══════════════════════════════════════
   CALCULATOR
══════════════════════════════════════ */
function initCalc() {
  const fab   = document.getElementById('calcFab');
  const panel = document.getElementById('calcPanel');
  if (!fab || !panel) return;

  fab.addEventListener('click', () => { state.calcOpen = !state.calcOpen; panel.classList.toggle('open', state.calcOpen); });
  document.getElementById('calcClose')?.addEventListener('click', () => { state.calcOpen = false; panel.classList.remove('open'); });

  document.querySelectorAll('.calc-key').forEach(k => k.addEventListener('click', () => calcInput(k.dataset.key)));

  document.addEventListener('keydown', e => {
    if (!state.calcOpen) return;
    const map = { '0':'0','1':'1','2':'2','3':'3','4':'4','5':'5','6':'6','7':'7','8':'8','9':'9',
                  '+':'+','-':'-','*':'×','/':'÷','.':'.','Enter':'=','Backspace':'⌫','Escape':'C','=':'=' };
    if (map[e.key]) { e.preventDefault(); calcInput(map[e.key]); }
  });

  function calcInput(key) {
    const disp = document.getElementById('calcDisplay');
    const expr = document.getElementById('calcExpr');
    const hist = document.getElementById('calcHistory');

    if (key === 'C') { state.calcExpr = ''; state.calcDisplay = '0'; }
    else if (key === '⌫') { state.calcExpr = state.calcExpr.slice(0,-1); state.calcDisplay = state.calcExpr || '0'; }
    else if (key === '=') {
      if (!state.calcExpr) return;
      try {
        const norm = state.calcExpr.replace(/×/g,'*').replace(/÷/g,'/');
        // eslint-disable-next-line no-new-func
        const r = String(parseFloat(Function(`"use strict";return(${norm})`)().toFixed(10)));
        state.calcHistory.unshift(`${state.calcExpr} = ${r}`);
        if (state.calcHistory.length > 5) state.calcHistory.pop();
        state.calcDisplay = r; state.calcExpr = r;
      } catch { state.calcDisplay = '오류'; state.calcExpr = ''; }
    } else if (key === '%') {
      try {
        const norm = state.calcExpr.replace(/×/g,'*').replace(/÷/g,'/');
        // eslint-disable-next-line no-new-func
        const r = String(parseFloat((Function(`"use strict";return(${norm})`)() / 100).toFixed(10)));
        state.calcExpr = r; state.calcDisplay = r;
      } catch { /* ignore */ }
    } else { state.calcExpr += key; state.calcDisplay = state.calcExpr; }

    disp.textContent = state.calcDisplay;
    expr.textContent = state.calcExpr !== state.calcDisplay ? state.calcExpr : '';
    hist.innerHTML   = state.calcHistory.map(h => `<div class="calc-history__item">${h}</div>`).join('');
  }

  document.getElementById('calcCopy')?.addEventListener('click', () =>
    navigator.clipboard?.writeText(state.calcDisplay)
      .then(() => showToast('결과가 클립보드에 복사되었습니다.', 'success', 1800)));

  document.getElementById('calcInsert')?.addEventListener('click', () => {
    const ra = document.getElementById('regAmount');
    if (ra && !isNaN(+state.calcDisplay)) {
      ra.value = state.calcDisplay;
      document.getElementById('registerModal')?.classList.add('show');
      state.calcOpen = false; panel.classList.remove('open');
      showToast('금액이 수동 등록 폼에 입력되었습니다.', 'info');
    }
  });
}

/* ══════════════════════════════════════
   OUTSIDE CLICK
══════════════════════════════════════ */
document.addEventListener('click', e => {
  if (!e.target.closest('[data-ctx]') && !e.target.closest('.ctx-menu')) closeAllCtx();
  ['editModal','deleteModal','registerModal'].forEach(id => {
    const el = document.getElementById(id);
    if (el && e.target === el) el.classList.remove('show');
  });
});

/* ══════════════════════════════════════
   INIT
══════════════════════════════════════ */
document.addEventListener('DOMContentLoaded', () => {
  initTabs();
  initDonationFilters();
  initReceiptFilters();
  initDrawer();
  initEditModal();
  initDeleteModal();
  initExcelDownload();
  initHeaderSearch();
  initCalc();

  // JSP 에서 이미 c:forEach 로 렌더링된 테이블에 이벤트 바인딩
  bindCtxMenus();
  bindCtxActions();

  // JSP 에서 주입한 페이지 정보로 페이지네이션 초기 렌더링
  if (window.PAGE_INFO) {
    state.currentPage = window.PAGE_INFO.currentPage || 1;
    state.totalCount  = window.PAGE_INFO.totalCount  || 0;
    state.pageSize    = window.PAGE_INFO.pageSize     || 5;
    renderPagination();
  }
});
