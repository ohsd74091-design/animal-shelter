<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn"  uri="jakarta.tags.functions" %>
<c:set var="ctp" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>관리자 대시보드</title>

	<link rel="preconnect" href="https://fonts.googleapis.com">
	<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
	<link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700;800&family=Manrope:wght@400;500;600;700&display=swap" rel="stylesheet">
	<link href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:wght@100..700" rel="stylesheet">

	<link rel="stylesheet" href="${ctp}/view/admin/css/common/admin-layout.css">
	<link rel="stylesheet" href="${ctp}/view/admin/css/dashboard.css">

	<script defer src="${ctp}/view/admin/js/admin-layout.js"></script>
	<script defer src="${ctp}/view/admin/js/dashboard.js"></script>
</head>
<body>
<div class="admin-layout">

	<jsp:include page="/view/admin/common/admin-sidebar.jsp" />

	<div class="admin-main-wrap">

		<jsp:include page="/view/admin/common/admin-header.jsp" />

		<main class="admin-content">
			<section class="admin-page-title">
				<div>
					<h1>대시보드</h1>
					<p>오늘도 보호소 운영 현황을 확인해주세요.</p>
				</div>

				<div class="admin-page-title__actions">
					<!-- <button type="button" class="admin-btn admin-btn--gray">리포트 전체 보기</button> -->
					<button type="button" class="admin-btn admin-btn--primary"
					        onclick="location.href='${ctp}/admin/animal/register.do'">
						<span class="material-symbols-outlined">add</span>
						동물 신규 등록
					</button>
				</div>
			</section>

			<section class="admin-stats-grid">
				<div class="admin-card stat-card clickable-card"
				     onclick="location.href='${ctp}/admin/animal/list.do'"
				     title="동물 목록 관리로 이동">
				    <div class="stat-card__top">
				        <div class="stat-card__icon stat-card__icon--orange">
				            <span class="material-symbols-outlined">pets</span>
				        </div>
				        <c:if test="${animalStats.WEEK_NEW_COUNT > 0}">
				            <span class="stat-badge success">이번 주 +${animalStats.WEEK_NEW_COUNT}</span>
				        </c:if>
				    </div>
				
				    <h3>전체 보호 동물</h3>
				    
				    <div class="stat-card__value">
				        <strong>${animalStats.TOTAL_COUNT}</strong>
				        <span>마리</span>
				    </div>
				
				    <div class="stat-card__meta">
				        <span>강아지 ${animalStats.DOG_COUNT}</span>
				        <span>고양이 ${animalStats.CAT_COUNT}</span>
				    </div>
				</div>

				<%-- 상단에서 비율 미리 계산 (에러 방지) --%>
				<c:set var="total" value="${adoptionStats.TOTAL_COUNT}" />
				<c:choose>
				    <c:when test="${total > 0}">
				        <%-- 입양률 계산: (입양완료 / 전체) * 100 --%>
				        <c:set var="adoptRate" value="${(adoptionStats.MONTH_DONE_COUNT / total) * 100}" />
				        <c:set var="availableRate" value="${(adoptionStats.AVAILABLE_COUNT / total) * 100}" />
				        <c:set var="progressRate" value="${(adoptionStats.PROGRESS_COUNT / total) * 100}" />
				    </c:when>
				    <c:otherwise>
				        <c:set var="adoptRate" value="0" />
				        <c:set var="availableRate" value="0" />
				        <c:set var="progressRate" value="0" />
				    </c:otherwise>
				</c:choose>
				
				<div class="admin-card admin-card--wide clickable-card"
				     onclick="location.href='${ctp}/admin/adoption/list.do'"
				     title="입양 상담 목록으로 이동">
				    <div class="adoption-status-container" style="display: flex; align-items: center; justify-content: space-between;">
				        
				        
				        
				       <%-- 입양 진행 현황 계산 로직 시작 --%>
						<c:set var="totalCount" value="${adoptionStats.TOTAL_COUNT > 0 ? adoptionStats.TOTAL_COUNT : 1}" />
						<c:set var="availableRate" value="${(adoptionStats.AVAILABLE_COUNT * 100.0) / totalCount}" />
						<c:set var="progressRate"  value="${(adoptionStats.PROGRESS_COUNT * 100.0) / totalCount}" />
						<c:set var="adoptRate"     value="${(adoptionStats.MONTH_DONE_COUNT * 100.0) / totalCount}" />
						<%-- 계산 로직 끝 --%>
						
						<div class="progress-box" style="flex: 1; margin-right: 40px;">
						    <h3 class="card-title">입양 진행 현황</h3>
						    
						    <div class="progress-item">
						        <div class="progress-item__label">
						            <span>입양 가능</span>
						            <span>${adoptionStats.AVAILABLE_COUNT}마리</span>
						        </div>
						        <div class="progress-bar">
						            <%-- 변수명 availableRate 확인 --%>
						            <div class="progress-bar__fill progress-bar__fill--green" style="width:${availableRate}%;"></div>
						        </div>
						    </div>
						
						    <div class="progress-item">
						        <div class="progress-item__label">
						            <span>진행 중</span>
						            <span>${adoptionStats.PROGRESS_COUNT}마리</span>
						        </div>
						        <div class="progress-bar">
						            <%-- 변수명 progressRate 확인 --%>
						            <div class="progress-bar__fill progress-bar__fill--orange" style="width:${progressRate}%;"></div>
						        </div>
						    </div>
						
						    <div class="progress-item">
						        <div class="progress-item__label">
						            <span>이번 달 입양 완료</span>
						            <span>${adoptionStats.MONTH_DONE_COUNT}건</span>
						        </div>
						        <div class="progress-bar">
						            <%-- 변수명 adoptRate 확인 --%>
						            <div class="progress-bar__fill progress-bar__fill--gray" style="width:${adoptRate}%;"></div>
						        </div>
						    </div>
						</div>
				
				        <%-- adoptRate 정수 변환: fmt 태그로 안전하게 처리 --%>
						<c:set var="adoptRateInt">
						    <fmt:formatNumber value="${adoptRate}" maxFractionDigits="0" />
						</c:set>
						<c:if test="${empty adoptRateInt or adoptRateInt == ''}">
						    <c:set var="adoptRateInt" value="0" />
						</c:if>
						
						<%-- 3색 도넛: 입양가능(녹색) / 진행중(주황) / 완료(회색) 비율 분할 --%>
						<%-- CSS custom property에 각 비율 전달 --%>
						<c:set var="p1"><fmt:formatNumber value="${availableRate}" maxFractionDigits="1"/></c:set>
						<c:set var="p2"><fmt:formatNumber value="${progressRate}"  maxFractionDigits="1"/></c:set>
						<c:set var="p3"><fmt:formatNumber value="${adoptRate}"     maxFractionDigits="1"/></c:set>
						
						<%-- conic-gradient 중단점 계산: p1끝 = p1, p2끝 = p1+p2 --%>
						<c:set var="stop1" value="${availableRate}" />
						<c:set var="stop2" value="${availableRate + progressRate}" />
						
						<div class="chart-circle-wrapper">
						    <div class="chart-circle-3"
						         style="--stop1: ${stop1}%; --stop2: ${stop2}%;">
						        <span class="chart-percentage">${adoptRateInt}%</span>
						    </div>
						    <%-- 색상 범례 --%>
							<div style="margin-top:12px;display:flex;flex-direction:column;gap:5px;font-size:12px;font-weight:700;">
							    <span><span style="display:inline-block;width:10px;height:10px;border-radius:50%;background:#0f9d7a;margin-right:5px;"></span>입양가능</span>
							    <span><span style="display:inline-block;width:10px;height:10px;border-radius:50%;background:#e56b2e;margin-right:5px;"></span>진행 중</span>
							    <span><span style="display:inline-block;width:10px;height:10px;border-radius:50%;background:#b8c0cc;margin-right:5px;"></span>입양완료</span>
							</div>
						</div>
				    </div>
				</div>

				<%-- 증가율 계산 로직 --%>
				<c:set var="thisMonth" value="${stats.MONTH_DONE_COUNT}" />
				<c:set var="lastMonth" value="${stats.LAST_MONTH_DONE_COUNT}" />
				
				<c:choose>
				    <c:when test="${lastMonth > 0}">
				        <%-- (이번달 - 지난달) / 지난달 * 100 --%>
				        <c:set var="increaseRate" value="${((thisMonth - lastMonth) / lastMonth) * 100}" />
				    </c:when>
				    <c:otherwise>
				        <c:set var="increaseRate" value="${thisMonth > 0 ? 100 : 0}" />
				    </c:otherwise>
				</c:choose>
				
				<div class="success-story-card">
				    <div class="success-story-content">
				        <p class="story-title">이번 달 입양 성공 스토리</p>
				        
				        <div class="story-count-area">
				            <span class="story-number">${thisMonth}</span>
				            <span class="story-unit">가족</span>
				        </div>
				        
				        <p class="story-description">
				            지난 달 대비 
				            <span class="highlight-text">
				                <fmt:formatNumber value="${increaseRate < 0 ? -increaseRate : increaseRate}" pattern="#" />% 
				                ${increaseRate >= 0 ? '증가한' : '감소한'}
				            </span> 수치입니다.
				        </p>
				    </div>
				</div>
			</section>

			<section class="admin-bottom-grid">
				<div class="activity-feed-card">
				    <div class="feed-header">
				        <span class="material-symbols-outlined">schedule</span> <h3>실시간 활동 피드</h3>
				    </div>
				    
				    <div id="feedListContainer" class="feed-list"></div>
				    <div id="feedPagination" class="feed-pagination"></div>
				</div>		

				<div class="admin-side-widgets">
					<%-- ─────────────────────────────────────────────────
					     이주의 일정 위젯
					     - scheduleList: 이번 주 월~일 전체 일정 (Java에서 주입)
					     - 날짜별 그룹 헤더 표시
					─────────────────────────────────────────────────── --%>
					<div class="admin-card">
					    <div class="section-header">
					        <h2>
					            <span class="material-symbols-outlined">calendar_month</span>
					            이주의 일정
					        </h2>
					    </div>
					
					    <%-- 이번 주 날짜 범위 표시 (JS에서 채움) --%>
					    <div class="schedule-date" id="weekRangeLabel"></div>
					
					    <div class="schedule-list">
					        <%-- 이주의 일정 리스트 — TIME 대신 DATE_KEY(날짜) 표시 --%>
					        <c:set var="lastDate" value="" />
					        <c:forEach var="s" items="${scheduleList}">
					            <%-- 날짜가 바뀔 때마다 날짜 구분 헤더 출력 --%>
					            <c:if test="${s.DATE_KEY ne lastDate}">
					                <div class="schedule-date-group">${s.DATE_KEY}</div>
					                <c:set var="lastDate" value="${s.DATE_KEY}" />
					            </c:if>
					            <div class="schedule-item clickable-card"
					                 onclick="openDetailModal('${s.TYPE}','${s.ID}','${s.TITLE}','${s.CONTENT}','${s.FULL_TIME}')">
					                <div class="schedule-item__time">${fn:substring(s.DATE_KEY, 5, 10)}</div>
					                <div class="schedule-item__body">
					                    <span class="schedule-item__type-badge ${s.TYPE eq 'ADOPTION' ? 'badge--adoption' : 'badge--volunteer'}">
					                        ${s.TYPE eq 'ADOPTION' ? '입양' : '봉사'}
					                    </span>
					                    <h5>${s.TITLE}</h5>
					                    <p>${s.CONTENT}</p>
					                </div>
					            </div>
					        </c:forEach>
					        <c:if test="${empty scheduleList}">
					            <div style="padding:30px 0;text-align:center;color:var(--text-sub);">
					                <span class="material-symbols-outlined"
					                      style="font-size:32px;color:#cbd5e1;display:block;margin-bottom:8px;">event_busy</span>
					                <p style="font-size:14px;font-weight:600;">이번 주 예정된 일정이 없습니다.</p>
					            </div>
					        </c:if>
					    </div>
					
					    <%-- 전체 일정 캘린더 보기 버튼 --%>
					    <button type="button" class="admin-widget-btn" id="btnOpenCalendar"
					            onclick="openCalendarModal()">
					        <span class="material-symbols-outlined" style="vertical-align:middle;font-size:16px;">calendar_month</span>
					        전체 일정 캘린더 보기
					    </button>
					</div>


					<c:choose>
					    <%-- 1. 데이터가 있을 때: 기존 카드 디자인 출력 --%>
					    <c:when test="${not empty featuredAnimal}">
					        <div class="admin-card featured-animal" onclick="location.href='${ctp}/animal/animalDetail.do?animalId=${featuredAnimal.animalId}'" style="cursor:pointer;">
					            <div class="featured-animal__image">
					              
					                <img src="${ctp}/imageView.do?fileName=${featuredAnimal.mainImage}" alt="이달의 동물">
					                <span class="featured-animal__badge">조회수 1위 동물</span>
					            </div>
					        
					            <div class="featured-animal__body">
					                <div class="featured-animal__top">
					                    <h4>${featuredAnimal.animalName}</h4>
					                    <span class="tag tag--green">${featuredAnimal.adoptionStatus}</span>
					                </div>
					                <p>${featuredAnimal.personality}</p>
					                <button type="button" class="admin-btn admin-btn--outline">프로필 상세 보기</button>
					            </div>
					        </div>
					    </c:when>
					
					    <%-- 2. 데이터가 없을 때: 안내 문구 출력 (디자인 깨짐 방지) --%>
					    <c:otherwise>
					        <div class="admin-card featured-animal" style="display:flex; align-items:center; justify-content:center; min-height:280px; background:#f8fafc; border:2px dashed #e2e8f0;">
					            <div style="text-align:center; color:#94a3b8;">
					                <span class="material-symbols-outlined" style="font-size:48px;">pets</span>
					                <p style="margin-top:12px; font-weight:600;">현재 입양 가능한 인기 동물이 없습니다.</p>
					            </div>
					        </div>
					    </c:otherwise>
					</c:choose>
				</div>
			</section>
		</main>
		
		<%-- <button type="button" class="fab-btn" onclick="location.href='${ctp}/animalDetail.do?animalId=${animalStats.TOP_ANIMAL_ID}'">
			<span class="material-symbols-outlined">add</span>
		</button> --%>
	</div>
</div>

<nav class="admin-mobile-nav">
	<a href="${ctp}/admin/main.do">
		<span class="material-symbols-outlined">dashboard</span>
		<span>대시보드</span>
	</a>
	<a href="${ctp}/admin/animal/register.do">
		<span class="material-symbols-outlined">pets</span>
		<span>등록</span>
	</a>
	<a href="${ctp}/admin/animal/list.do">
		<span class="material-symbols-outlined">list_alt</span>
		<span>목록</span>
	</a>
	<a href="${ctp}/admin/board/list.do">
		<span class="material-symbols-outlined">article</span>
		<span>게시판</span>
	</a>
	<a href="${ctp}/admin/member/list.do">
		<span class="material-symbols-outlined">group</span>
		<span>회원</span>
	</a>
</nav>

<%-- ═══════════════════════════════════════════
     일정 상세 모달
═══════════════════════════════════════════ --%>
<div id="scheduleDetailModal" class="dash-modal-overlay" onclick="closeDetailModal()" style="display:none;">
    <div class="dash-modal" onclick="event.stopPropagation()">
        <div class="dash-modal__head">
            <h3 id="detailModalTitle">일정 상세</h3>
            <button class="dash-modal__close" onclick="closeDetailModal()">
                <span class="material-symbols-outlined">close</span>
            </button>
        </div>
        <div class="dash-modal__body">
            <div class="detail-row-m"><span class="detail-label">시간</span><span id="detailTime">—</span></div>
            <div class="detail-row-m"><span class="detail-label">내용</span><span id="detailContent">—</span></div>
            <div class="detail-row-m"><span class="detail-label">유형</span><span id="detailType">—</span></div>
        </div>
    </div>
</div>

<%-- ═══════════════════════════════════════════
     캘린더 모달 — 이번 달 전체 일정 달력 출력
═══════════════════════════════════════════ --%>
<div id="calendarModal" class="dash-modal-overlay" onclick="closeCalendarModal()" style="display:none;">
    <div class="dash-modal dash-modal--calendar" onclick="event.stopPropagation()">
        <div class="dash-modal__head">
            <div style="display:flex;align-items:center;gap:12px;">
                <%-- 이전 달 버튼 --%>
                <button class="cal-nav-btn" onclick="changeCalMonth(-1)">
                    <span class="material-symbols-outlined">chevron_left</span>
                </button>
                <%-- 년월 표시 --%>
                <h3 id="calTitle" style="font-size:18px;font-weight:800;min-width:120px;text-align:center;"></h3>
                <%-- 다음 달 버튼 --%>
                <button class="cal-nav-btn" onclick="changeCalMonth(1)">
                    <span class="material-symbols-outlined">chevron_right</span>
                </button>
            </div>
            <button class="dash-modal__close" onclick="closeCalendarModal()">
                <span class="material-symbols-outlined">close</span>
            </button>
        </div>
        <div class="dash-modal__body" style="padding:0 20px 20px;">
            <%-- 요일 헤더 --%>
            <div class="cal-week-header">
                <span>일</span><span>월</span><span>화</span><span>수</span>
                <span>목</span><span>금</span><span>토</span>
            </div>
            <%-- 날짜 격자 --%>
            <div id="calGrid" class="cal-grid"></div>
            <%-- 선택한 날의 일정 목록 --%>
            <div id="calDayDetail" class="cal-day-detail" style="display:none;">
                <h4 id="calDayTitle" style="font-size:14px;font-weight:800;margin-bottom:10px;"></h4>
                <div id="calDayList"></div>
            </div>
        </div>
    </div>
</div>

<%-- ─────────────────────────────────────────
     서버 데이터 → JS 변수 주입
     scheduleList(오늘) + allScheduleList(이번달) 를
     JSON 배열로 변환하여 dashboard.js 에서 사용
───────────────────────────────────────── --%>
<script>
/* ── 오늘의 일정 목록 (일정 상세 모달에서 사용) ── */
window.SCHEDULE_LIST = [
<c:forEach var="s" items="${scheduleList}" varStatus="vs">
  {
    type    : "${s.TYPE}",
    id      : "${s.ID}",
    title   : "${s.TITLE}",
    content : "${s.CONTENT}",
    time    : "${s.TIME}",
    fullTime: "${s.FULL_TIME}"
  }<c:if test="${!vs.last}">,</c:if>
</c:forEach>
];

/* ── 이번 달 전체 일정 (캘린더 모달에서 사용) ── */
window.ALL_SCHEDULE_LIST = [
<c:forEach var="s" items="${allScheduleList}" varStatus="vs">
  {
    type    : "${s.TYPE}",
    id      : "${s.ID}",
    title   : "${fn:replace(s.TITLE,   '\"', '')}",
    content : "${fn:replace(s.CONTENT, '\"', '')}",
    time    : "${s.TIME}",
    fullTime: "${s.FULL_TIME}",
    dateKey : "${s.DATE_KEY}",
    moveUrl : "${s.MOVE_URL}"
  }<c:if test="${!vs.last}">,</c:if>
</c:forEach>
];

/* ── 실시간 활동 피드 데이터 (콘솔에 찍힌 그 데이터를 JS로 넘김) ── */
window.ACTIVITY_FEED_DATA = [
<c:forEach var="f" items="${activityFeed}" varStatus="vs">
  {
    type : "${f.TYPE}",
    id   : "${f.ID}",      /* 👈 콘솔에 찍힌 ID: 29, 28 등을 여기 담습니다 */
    title: "${f.TITLE}",   /* 👈 콘솔에 찍힌 'user01님이...' 제목을 여기 담습니다 */
    date : "${f.REG_DATE}" /* 👈 날짜 정보를 담습니다 */
  }<c:if test="${!vs.last}">,</c:if>
</c:forEach>
];

</script>


</body>
</html>