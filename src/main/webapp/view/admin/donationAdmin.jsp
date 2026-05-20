<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"   uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"  %>
<c:set var="ctp" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8"/>
<meta name="viewport" content="width=device-width, initial-scale=1.0"/>
<title>기부 관리 현황 — Amber Hearth Admin</title>
<link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700;800&display=swap" rel="stylesheet"/>
<link href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:wght,FILL@100..700,0..1&display=swap" rel="stylesheet"/>
<link rel="stylesheet" href="${ctp}/view/admin/css/donationAdmin.css">
<link rel="stylesheet" href="${ctp}/view/admin/css/common/admin-layout.css">
</head>
<body>
<div class="admin-wrapper">
<jsp:include page="/view/admin/common/admin-sidebar.jsp" />
<div class="main-container">
<jsp:include page="/view/admin/common/admin-header.jsp" />

<main class="main-content">
<div class="page-inner">

  <!-- Page Header -->
  <div class="page-header">
    <div>
      <h2 class="page-header__title">기부 관리 현황</h2>
      <p class="page-header__sub">Shelter의 따뜻한 나눔을 관리하고 투명하게 보고하세요.</p>
    </div>
    <div class="page-header__actions">
      <button id="btnExcel" class="btn btn--outline">
        <span class="material-symbols-outlined">download</span> 엑셀 다운로드
      </button>
      <button id="btnManualRegister" class="btn btn--primary" data-open="registerModal">
        <span class="material-symbols-outlined">add</span> 기부 수동 등록
      </button>
    </div>
  </div>

  <!-- Stats Row — 서버 데이터 -->
  <div class="stats-grid">
    <div id="cardTotalAmount" class="stat-card">
      <div class="stat-card__head">
        <span id="scl" class="stat-card__label">누적 기부금</span>
        <span class="material-symbols-outlined stat-card__icon icon-fill">monetization_on</span>
      </div>
      <div class="stat-card__value">
        ₩<fmt:formatNumber value="${stats.TOTALAMOUNT}" pattern="#,###"/>
      </div>
      <div class="stat-card__hint stat-card__hint--up">
        <span class="material-symbols-outlined">trending_up</span> 누적 기부 총액
      </div>
    </div>
    <div id="cardThisMonthCount" class="stat-card clickable-card">
      <div class="stat-card__head">
        <span class="stat-card__label">이번 달 기부 건수</span>
        <span class="material-symbols-outlined stat-card__icon">person_add</span>
      </div>
      <div class="stat-card__value">${stats.THISMONTHCOUNT}건</div>
      <div class="stat-card__hint stat-card__hint--up">
        <span class="material-symbols-outlined">trending_up</span> 이번 달 기부 건수
      </div>
    </div>
    <div id="cardReceiptCount" class="stat-card clickable-card">
      <div class="stat-card__head">
        <span class="stat-card__label">영수증 신청 건수</span>
        <span class="material-symbols-outlined stat-card__icon">description</span>
      </div>
      <div class="stat-card__value">${stats.RECEIPTCOUNT}건</div>
      <div class="stat-card__hint stat-card__hint--warn">
        <span class="material-symbols-outlined">priority_high</span> 빠른 처리가 필요합니다
      </div>
    </div>
    <div id="cardTotalCount" class="stat-card clickable-card">
      <div class="stat-card__head">
        <span class="stat-card__label">총 기부 건수</span>
        <span class="material-symbols-outlined stat-card__icon">cached</span>
      </div>
      <div class="stat-card__value">${stats.TOTALCOUNT}건</div>
      <div class="stat-card__hint stat-card__hint--neutral">
        <span class="material-symbols-outlined">check_circle</span> 누적 후원 건수
      </div>
    </div>
  </div>

  <!-- Middle Row -->
  <div class="mid-row">

    <!-- TOP 5 랭킹 — 서버 데이터 -->
    <div class="ranking-card">
      <div class="card-head">
        <span class="card-head__title">이달의 우수 기부자 TOP 5</span>
        <span class="material-symbols-outlined card-head__icon">military_tech</span>
      </div>
      <ul class="ranking-list">
        <c:forEach var="donor" items="${topDonorList}" varStatus="vs">
          <li class="ranking-item" clickable-row" onclick="location.href='${ctp}/admin/member/detail.do?memberId=${donor.memberId}'" 
          	style="cursor: pointer;">
            <div class="ranking-item__rank ${vs.index==0?'rank-1':vs.index==1?'rank-2':vs.index==2?'rank-3':'rank-other'}">
              ${vs.count}
            </div>
            <div class="ranking-item__info">
              <div class="ranking-item__name">${donor.donatorName} 후원자</div>
              <div class="ranking-item__amount">
                누적 <fmt:formatNumber value="${donor.totalAmount}" pattern="#,###"/>원
              </div>
            </div>
            <c:if test="${vs.index==0}"><span class="badge-mvp">MVP</span></c:if>
          </li>
        </c:forEach>
        <c:if test="${empty topDonorList}">
          <li class="ranking-item" style="justify-content:center;color:#94a3b8;font-size:0.8rem;">
            이달의 기부자 데이터가 없습니다.
          </li>
        </c:if>
      </ul>
    </div>

    <!-- Donation Table Card -->
    <div class="table-card">
      <div class="tabs">
        <button class="tab-btn active" data-tab="all">후원 내역</button>
        <button class="tab-btn" data-tab="receipt">기부금 영수증 관리</button>
      </div>

      <!-- TAB: 전체 내역 -->
      <div class="tab-panel active" data-panel="all">
        <div class="filter-bar">
          <div class="filter-search">
            <span class="material-symbols-outlined filter-search__icon">search</span>
            <%-- 검색어 유지 --%>
            <input id="donSearch" type="text" placeholder="이름 검색…" value="${pvo.keyword}"/>
          </div>
          <%-- option value = DB 실제값(정기/일시) --%>
          <select id="donTypeFilter" class="filter-select">
            <option value="">전체 방식</option>
           <option value="정기" <c:if test="${pvo.paymentType eq '정기'}">selected</c:if>>정기 후원</option>
           <option value="일시불" <c:if test="${pvo.paymentType eq '일시불'}">selected</c:if>>일시 후원</option>
          </select>
          <select id="donMethodFilter" class="filter-select">
            <option value="">전체 수단</option>
            <option value="카드" ${pvo.method=='카드'?'selected':''}>카드 결제</option>
            <option value="계좌이체" ${pvo.method=='계좌이체'?'selected':''}>계좌 이체</option>
          </select>
          
          <div class="search-item date-range-item">
			    <div class="date-input-group">
			        <input type="date" id="startDate" class="form-control date-input">
			        <span class="date-separator">~</span>
			        <input type="date" id="endDate" class="form-control date-input">
			    </div>
			</div>
        </div>

        <%-- 테이블 — c:forEach 서버 데이터 --%>
        
          <table class="data-table">
            <thead>
              <tr>
                <th>날짜</th><th>후원자명</th><th>금액</th>
                <th>후원 방식</th><th class="td-center">결제수단</th><th class="td-right">관리</th>
              </tr>
            </thead>
            <tbody id="donTableBody">
              <c:forEach var="d" items="${pvo.donationList}">
                <tr data-id="${d.donationId}">
                  <td class="td-date">${d.donationDateStr}</td>
                  <td class="td-name">${d.donatorName}</td>
                  <td class="td-amount">₩<fmt:formatNumber value="${d.donationAmount}" pattern="#,###"/></td>
                  <td>
					  <span class="type-tag ${(d.donationType == '정기' || d.donationType == '정기 후원') ? 'type-tag--regular' : 'type-tag--once'}">
					    <%-- DB가 '일시불'이면 '일시 후원'으로, '정기'면 '정기 후원'으로 출력 --%>
					    ${d.donationType == '일시불' ? '일시 후원' : (d.donationType == '정기' ? '정기 후원' : d.donationType)}
					  </span>
				  </td>
					
					<td class="td-center">
					  <span class="badge ${(d.donationMethod == '카드' || d.donationMethod == '신용카드') ? 'badge--complete' : 'badge--pending'}">
					    <%-- DB가 '카드'면 '신용카드'로 출력 --%>
					    ${d.donationMethod == '카드' ? '신용카드' : d.donationMethod}
					  </span>
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
                </tr>
              </c:forEach>
              <c:if test="${empty pvo.donationList}">
                <tr><td colspan="6" class="empty-state">
                  <span class="material-symbols-outlined">search_off</span><p>후원 내역이 없습니다</p>
                </td></tr>
              </c:if>
            </tbody>
          </table>
        
        <div class="pagination-bar">
          <p class="pagination-bar__info" id="donPagInfo">총 ${pvo.totalCount}건의 내역</p>
          <div class="pagination-controls" id="donPagination"></div>
        </div>
      </div>

      <!-- TAB: 영수증 관리 -->
      <div class="tab-panel" data-panel="receipt">
        <div style="padding:16px 20px;background:rgba(248,250,252,0.4);border-bottom:1px solid #f1f5f9;display:flex;justify-content:flex-end;align-items:center;gap:10px;">
        <!--   <span style="font-size:1rem;font-weight:700;color:#64748b;"></span> -->
          <span style="font-size:0.75rem;font-weight:700;color:#64748b;">필터:</span>
          <div class="filter-pills"  id="receiptTabPills">
            <button class="pill-btn active" data-filter="all">전체</button>
            <button class="pill-btn" data-filter="waiting">대기중</button>
            <button class="pill-btn" data-filter="issued">발급완료</button>
          </div>
        </div>
        
          <table class="data-table">
            <thead>
              <tr>
                <th>요청일</th><th>후원자명</th><th>주민등록번호</th>
                <th>기부 총액 (연간)</th><th>상태</th><th class="td-right">발급 처리</th>
              </tr>
            </thead>
            <tbody id="receiptTableBody">
              <c:forEach var="r" items="${receiptList}">
                <tr>
                  <td class="td-date"><fmt:formatDate value="${r.createDate}" pattern="yyyy.MM.dd"/></td>
                  <td class="td-name">${r.donatorName}</td>
                  <td class="td-mono">${r.regNo1}-*******</td>
                  <td class="td-amount">₩<fmt:formatNumber value="${r.donationAmount}" pattern="#,###"/></td>
                  <%--  발급완료(I) / 대기중(Y) 상태 구분 --%>
                  <td>
                    <c:choose>
                      <c:when test="${r.receiptYn eq 'I'}"><span class="badge badge--issued">발급완료</span></c:when>
                      <c:otherwise><span class="badge badge--waiting">대기중</span></c:otherwise>
                    </c:choose>
                  </td>
                  <td class="td-right">
                    <label class="toggle">
                      <%-- 발급완료(I)면 checked 렌더링 --%>
                      <input type="checkbox" data-receipt-id="${r.donationId}" <c:if test="${r.receiptYn eq 'I'}">checked</c:if>>
                      <div class="toggle__track"></div>
                      <div class="toggle__thumb"></div>
                    </label>
                  </td>
                </tr>

              </c:forEach>
              <c:if test="${empty receiptList}">
                <tr><td colspan="6" class="empty-state">
                  <span class="material-symbols-outlined">receipt_long</span><p>영수증 신청 내역이 없습니다</p>
                </td></tr>
              </c:if>
            </tbody>
          </table>
       
      </div>
    </div>
  </div><!-- /mid-row -->

</div><!-- /page-inner -->
</main>
</div>
</div>
<!-- DETAIL DRAWER -->
<div id="drawerOverlay" class="drawer-overlay"></div>
<aside id="drawer" class="drawer">
  <div class="drawer__head">
    <span class="drawer__title">후원 상세 정보</span>
    <button id="drawerClose" class="modal__close"><span class="material-symbols-outlined">close</span></button>
  </div>
  <div class="drawer__body">
    <div class="drawer__section">
      <div class="drawer__section-title">기부자 정보</div>
      <div class="detail-row"><span class="detail-row__label">기부자명</span><span class="detail-row__value" id="drawerDonorName">—</span></div>
      <div class="detail-row"><span class="detail-row__label">내역 번호</span><span class="detail-row__value" id="drawerId">—</span></div>
    </div>
    <div class="drawer__section">
      <div class="drawer__section-title">후원 정보</div>
      <div class="detail-amount" id="drawerAmount">—</div>
      <div class="detail-row"><span class="detail-row__label">후원 일자</span><span class="detail-row__value" id="drawerDate">—</span></div>
      <div class="detail-row"><span class="detail-row__label">후원 유형</span><span class="detail-row__value" id="drawerType">—</span></div>
      <div class="detail-row"><span class="detail-row__label">결제 수단</span><span class="detail-row__value" id="drawerMethod">—</span></div>
    </div>
  </div>
  <div class="drawer__footer">
    <button id="drawerEditBtn" class="btn btn--outline"><span class="material-symbols-outlined">edit</span> 수정</button>
    <button class="btn btn--primary" onclick="closeDrawer()">확인</button>
  </div>
</aside>

<!-- EDIT MODAL -->
<div id="editModal" class="modal-overlay">
  <div class="modal">
    <div class="modal__head">
      <span class="modal__title">후원 내역 수정</span>
      <button id="editModalClose" class="modal__close"><span class="material-symbols-outlined">close</span></button>
    </div>
    <div class="modal__body">
      <input type="hidden" id="editId"/>
      <div class="form-row">
        <div class="form-group">
        	<label class="form-label">기부자명</label><input id="editName" type="text" class="form-input"/></div>
        <div class="form-group">
        	<label class="form-label">금액 (원)</label><input id="editAmount" type="number" class="form-input" min="1000" step="1000"/></div>
      </div>
      <div class="form-row">
        <div class="form-group">
          <label class="form-label">후원 유형</label>
          <%-- DB 실제값 --%>
          <select id="editType" class="form-input">
            <option value="정기">정기 후원</option>
            <option value="일시불">일시 후원</option>
          </select>
        </div>
        <div class="form-group">
          <label class="form-label">결제 수단</label>
          <select id="editMethod" class="form-input">
            <option value="카드">카드결제</option>
            <option value="계좌이체">계좌이체</option>
          </select>
        </div>
      </div>
    </div>
    <div class="modal__footer">
      <button id="editModalCancel" class="btn btn--outline">취소</button>
      <button id="editModalSave" class="btn btn--primary">저장</button>
    </div>
  </div>
</div>

<!-- DELETE MODAL -->
<div id="deleteModal" class="modal-overlay">
  <div class="modal" style="max-width:380px;">
    <div class="modal__head">
      <span class="modal__title">후원 내역 삭제</span>
      <button id="deleteModalClose" class="modal__close"><span class="material-symbols-outlined">close</span></button>
    </div>
    <div class="modal__body" style="text-align:center;padding:32px 24px 24px;">
      <div class="confirm-icon"><span class="material-symbols-outlined">delete_forever</span></div>
      <p style="font-size:0.9375rem;font-weight:700;color:#0f172a;margin-bottom:8px;">
        <span id="deleteTargetName">이 후원자</span>님의 내역을 삭제할까요?
      </p>
      <p style="font-size:0.8125rem;color:#64748b;">이 작업은 되돌릴 수 없습니다.</p>
    </div>
    <div class="modal__footer" style="justify-content:center;">
      <button id="deleteModalCancel" class="btn btn--outline">취소</button>
      <button id="deleteModalConfirm" class="btn btn--danger"><span class="material-symbols-outlined">delete</span> 삭제</button>
    </div>
  </div>
</div>

<!-- REGISTER MODAL -->
<div id="registerModal" class="modal-overlay">
  <div class="modal modal--wide">
    <div class="modal__head">
      <span class="modal__title">기부 수동 등록</span>
      <button id="registerModalClose" class="modal__close"><span class="material-symbols-outlined">close</span></button>
    </div>
    <div class="modal__body">
      <div class="form-row">
        <div class="form-group"><label class="form-label">기부자명 <span style="color:#ef4444;">*</span></label>
        	<input id="regName" type="text" class="form-input" placeholder="홍길동"/></div>
        <div class="form-group"><label class="form-label">연락처</label><input id="regTel" type="tel" class="form-input" placeholder="010-0000-0000"/></div>
      </div>
      <div class="form-group"><label class="form-label">이메일</label><input id="regEmail" type="email" class="form-input" placeholder="example@email.com"/></div>
      <div class="form-row">
        <div class="form-group"><label class="form-label">금액 (원) <span style="color:#ef4444;">*</span></label>
        	<input id="regAmount" type="number" class="form-input" placeholder="최소 1,000원" min="1000" step="1000"/></div>
        <div class="form-group">
          <label class="form-label">후원 유형</label>
          <select id="regType" class="form-input"><option value="정기">정기 후원</option><option value="일시불">일시 후원</option></select>
        </div>
      </div>
      <div class="form-group">
        <label class="form-label">결제 수단</label>
        <select id="regMethod" class="form-input"><option value="카드">카드결제</option><option value="계좌이체">계좌이체</option></select>
      </div>
    </div>
    <div class="modal__footer">
      <button id="registerModalCancel" class="btn btn--outline">취소</button>
      <button id="registerModalSave" class="btn btn--primary"><span class="material-symbols-outlined">add</span> 등록</button>
    </div>
  </div>
</div>

<div id="toastContainer" class="toast-container"></div>

<!-- CALCULATOR FAB -->
<button id="calcFab" class="fab" title="계산기"><span class="material-symbols-outlined">calculate</span></button>
<div id="calcPanel" class="calc-panel">
  <div class="calc-header">
    <span class="calc-header__title">💰 기부금 계산기</span>
    <button id="calcClose" class="calc-close"><span class="material-symbols-outlined">close</span></button>
  </div>
  <div class="calc-display">
    <div class="calc-display__expr" id="calcExpr"></div>
    <div class="calc-display__result" id="calcDisplay">0</div>
  </div>
  <div class="calc-history" id="calcHistory"></div>
  <div class="calc-keys">
    <button class="calc-key calc-key--clear" data-key="C">C</button>
    <button class="calc-key calc-key--backspace" data-key="⌫">⌫</button>
    <button class="calc-key calc-key--op" data-key="%">%</button>
    <button class="calc-key calc-key--op" data-key="÷">÷</button>
    <button class="calc-key" data-key="7">7</button><button class="calc-key" data-key="8">8</button><button class="calc-key" data-key="9">9</button>
    <button class="calc-key calc-key--op" data-key="×">×</button>
    <button class="calc-key" data-key="4">4</button><button class="calc-key" data-key="5">5</button><button class="calc-key" data-key="6">6</button>
    <button class="calc-key calc-key--op" data-key="-">−</button>
    <button class="calc-key" data-key="1">1</button><button class="calc-key" data-key="2">2</button><button class="calc-key" data-key="3">3</button>
    <button class="calc-key calc-key--op" data-key="+">+</button>
    <button class="calc-key calc-key--0" data-key="0">0</button>
    <button class="calc-key" data-key=".">.</button>
    <button class="calc-key calc-key--eq" data-key="=">=</button>
  </div>
  <div class="calc-footer">
    <button id="calcCopy" class="btn btn--outline"><span class="material-symbols-outlined">content_copy</span> 복사</button>
    <button id="calcInsert" class="btn btn--primary"><span class="material-symbols-outlined">add_circle</span> 등록에 사용</button>
  </div>
</div>

<%-- JS에 서버 데이터 주입 --%>
<script>
window.CTX = '${ctp}';
window.PAGE_INFO = {
  currentPage : ${currentPage},
  totalCount  : ${totalCount},
  pageSize    : ${pageSize}
};
</script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/xlsx/0.18.5/xlsx.full.min.js"></script>
<script src="${ctp}/view/admin/js/donationAdmin.js"></script>
<script defer src="${ctp}/view/admin/js/admin-layout.js"></script>
<script>
  function closeDrawer() {
    document.getElementById('drawerOverlay').classList.remove('show');
    document.getElementById('drawer').classList.remove('show');
  }

  function changePage(page) {
    // 1 미만이거나 전체 페이지 초과면 무시
    const totalPages = Math.max(1, Math.ceil(window.PAGE_INFO.totalCount / window.PAGE_INFO.pageSize));
    if (page < 1 || page > totalPages) return;

    // 현재 필터 값 읽기 (검색어·날짜 등 유지)
    const params = {
      keyword     : document.getElementById('donSearch')?.value     || '',
      paymentType : document.getElementById('donTypeFilter')?.value || '',
      method      : document.getElementById('donMethodFilter')?.value || '',
      startDate   : document.getElementById('startDate')?.value     || '',
      endDate     : document.getElementById('endDate')?.value       || '',
      page        : page
    };

    // donationAdmin.js의 비동기 함수 호출 → 테이블·페이징만 교체
    fetchDonationData(params);

    // 테이블 상단으로 부드럽게 스크롤
    document.querySelector('.table-card')?.scrollIntoView({ behavior: 'smooth', block: 'start' });
  }
</script>
</body>
</html>
