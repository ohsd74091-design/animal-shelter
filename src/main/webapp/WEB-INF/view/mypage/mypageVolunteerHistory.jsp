<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>봉사 신청 내역 - 너와 나의 연결고리</title>

<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined" rel="stylesheet">
<link href="https://cdn.jsdelivr.net/gh/orioncactus/pretendard/dist/web/static/pretendard.css" rel="stylesheet">

<%-- 공통 CSS --%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/common.css">

<%-- 마이페이지 공통 CSS --%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/mypage/mypage.css">

<%-- 봉사 신청 내역 전용 CSS --%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/mypage/mypageVolunteerHistory.css">

</head>
<body>

    <%-- 공통 헤더 --%>
    <jsp:include page="/view/common/header.jsp" />

    <div class="mypage-container">

        <%-- 사이드바 --%>
        <jsp:include page="/view/common/sidebarNav.jsp">
            <jsp:param name="activeMenu" value="volunteerHistory"/>
        </jsp:include>

        <%-- 메인 콘텐츠 --%>
        <section class="mypage-content">

            <%-- 페이지 헤더 --%>
            <div class="page-header">
                <h2 class="page-title">봉사 신청 내역</h2>
                <p class="page-desc">참여하신 봉사 활동의 상세 일정과 상태를 확인하실 수 있습니다.</p>
            </div>

            <%-- 상단: 통계카드 + 달력 --%>
            <div class="vol-top-grid">

                <%-- 통계 카드 --%>
                <div class="vol-stat-card">
                    <div class="vol-stat-card__deco"></div>
                    <div class="vol-stat-card__inner">
                        <div class="vol-stat-card__icon">
                            <span class="material-symbols-outlined">volunteer_activism</span>
                        </div>
                        <p class="vol-stat-card__label">올해 총 봉사 신청 횟수</p>
                        <div class="vol-stat-card__value">
                            <span class="vol-stat-card__num">${volunteerStats}</span>
                            <span class="vol-stat-card__unit">회</span>
                        </div>
                    </div>
                    <p class="vol-stat-card__desc">
                        따뜻한 마음으로 소중한 생명을 지켜주셔서 감사합니다.<br>앞으로도 많은 관심 부탁드립니다.
                    </p>
                </div>

                <%-- 달력 --%>
                <div class="vol-calendar">
                    <div class="vol-calendar__header">
                        <div class="vol-calendar__title">
                            <span class="material-symbols-outlined">calendar_month</span>
                            <span id="calendarTitle"></span>
                        </div>
                        <div class="vol-calendar__nav">
                            <button type="button" id="calPrev" class="cal-nav-btn">
                                <span class="material-symbols-outlined">chevron_left</span>
                            </button>
                            <button type="button" id="calNext" class="cal-nav-btn">
                                <span class="material-symbols-outlined">chevron_right</span>
                            </button>
                        </div>
                    </div>
                    <div class="vol-calendar__weekdays">
                        <span>Sun</span><span>Mon</span><span>Tue</span>
                        <span>Wed</span><span>Thu</span><span>Fri</span><span>Sat</span>
                    </div>
                    <div class="vol-calendar__grid" id="calendarGrid"></div>
                </div>

            </div><%-- /.vol-top-grid --%>

            <%-- 신청 목록 --%>
            <div class="vol-list-section">
                <div class="vol-list-header">
                    <h3 class="vol-list-title">신청 목록</h3>
                    <select class="vol-filter-select" id="statusFilter" onchange="filterByStatus(this.value)">
                        <option value="">전체 보기</option>
                        <option value="승인">신청완료</option>
                        <option value="대기">승인대기</option>
                        <option value="반려">취소됨</option>
                        <option value="봉사완료">봉사완료</option>
                    </select>
                </div>

                <%-- 카드 목록 --%>
                <c:choose>
                    <c:when test="${not empty volunteerList}">
                        <div class="vol-card-list" id="volCardList">
                            <c:forEach var="vol" items="${volunteerList}">

                                <%-- 봉사완료 여부 판단: 승인 상태이고 봉사일이 오늘 이전 --%>
                                <c:set var="isCompleted" value="${vol.status eq '승인' and vol.volunteerDate ne null and vol.volunteerDate lt today}"/>
                                <c:set var="isCancelled" value="${vol.status eq '반려'}"/>

                                <div class="vol-card ${isCancelled ? 'is-cancelled' : ''}"
                                     data-status="${vol.status}"
                                     data-completed="${isCompleted}">

                                    <%-- 날짜 박스 --%>
                                    <div class="vol-card__date-box">
                                        <span class="vol-card__date-month">
                                            <fmt:formatDate value="${vol.volunteerDate}" pattern="MMM" />
                                        </span>
                                        <span class="vol-card__date-day">
                                            <fmt:formatDate value="${vol.volunteerDate}" pattern="dd" />
                                        </span>
                                    </div>

                                    <%-- 정보 영역 --%>
                                    <div class="vol-card__body">
                                        <div class="vol-card__title-row">
                                            <h4 class="vol-card__title">${vol.title}</h4>
                                            <%-- 상태 뱃지 --%>
                                            <c:choose>
                                                <c:when test="${isCompleted}">
                                                    <span class="vol-status-badge vol-status-badge--complete">봉사완료</span>
                                                </c:when>
                                                <c:when test="${vol.status eq '승인'}">
                                                    <span class="vol-status-badge vol-status-badge--approved">신청완료</span>
                                                </c:when>
                                                <c:when test="${vol.status eq '대기'}">
                                                    <span class="vol-status-badge vol-status-badge--pending">승인대기</span>
                                                </c:when>
                                                <c:when test="${vol.status eq '반려'}">
                                                    <span class="vol-status-badge vol-status-badge--rejected">취소됨</span>
                                                </c:when>
                                            </c:choose>
                                        </div>

                                        <div class="vol-card__info">
                                            <span class="vol-card__info-item">
                                                <span class="material-symbols-outlined">location_on</span>
                                                ${vol.location}
                                            </span>
                                        </div>
                                    </div>

                                    <%-- 우측 액션 영역 --%>
                                    <div class="vol-card__actions">
                                        <span class="vol-card__apply-date">신청일: ${vol.applyDateStr}</span>
                                        <c:choose>
                                            <%-- 봉사완료 → 후기작성 --%>
                                            <c:when test="${isCompleted}">
                                                <a href="${pageContext.request.contextPath}/board/write.do?boardType=자원봉사후기&recruitId=${vol.recruitId}"
                                                   class="btn-vol btn-vol--review">후기작성</a>
                                            </c:when>
                                            <%-- 신청완료/승인대기 → 취소 --%>
                                            <c:when test="${vol.status eq '승인' or vol.status eq '대기'}">
                                                <button type="button"
                                                        class="btn-vol btn-vol--cancel"
                                                        onclick="cancelVolunteer(${vol.volunteerId})">취소</button>
                                            </c:when>
                                            <%-- 반려 → 반려 사유 표시 --%>
                                            <c:otherwise>
                                                <c:choose>
                                                    <c:when test="${not empty vol.rejectReason}">
                                                        <span class="vol-card__reject-reason">반려 사유 : ${vol.rejectReason}</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="vol-card__no-action">-</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>

                                </div><%-- /.vol-card --%>
                            </c:forEach>
                        </div>

                        <%-- 페이지네이션 --%>
                        <div class="pagination">
                            <c:choose>
                                <c:when test="${currentPage > 1}">
                                    <a href="?page=${currentPage - 1}" class="page-btn page-btn--arrow">
                                        <span class="material-symbols-outlined">chevron_left</span>
                                    </a>
                                </c:when>
                                <c:otherwise>
                                    <button class="page-btn page-btn--arrow" disabled>
                                        <span class="material-symbols-outlined">chevron_left</span>
                                    </button>
                                </c:otherwise>
                            </c:choose>

                            <c:forEach begin="${startPage}" end="${endPage}" var="p">
                                <c:choose>
                                    <c:when test="${p == currentPage}">
                                        <button class="page-btn page-btn--active">${p}</button>
                                    </c:when>
                                    <c:otherwise>
                                        <a href="?page=${p}" class="page-btn">${p}</a>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>

                            <c:choose>
                                <c:when test="${currentPage < totalPages}">
                                    <a href="?page=${currentPage + 1}" class="page-btn page-btn--arrow">
                                        <span class="material-symbols-outlined">chevron_right</span>
                                    </a>
                                </c:when>
                                <c:otherwise>
                                    <button class="page-btn page-btn--arrow" disabled>
                                        <span class="material-symbols-outlined">chevron_right</span>
                                    </button>
                                </c:otherwise>
                            </c:choose>
                        </div>

                    </c:when>
                    <c:otherwise>
                        <div class="vol-empty">
                            <span class="material-symbols-outlined vol-empty__icon">event_available</span>
                            <p class="vol-empty__msg">봉사 신청 내역이 없습니다.</p>
                        </div>
                    </c:otherwise>
                </c:choose>

            </div><%-- /.vol-list-section --%>

        </section><%-- /.mypage-content --%>

    </div><%-- /.mypage-container --%>

    <%-- 공통 푸터 --%>
    <jsp:include page="/view/common/footer.jsp" />

    <%-- 달력 데이터 (JS에서 사용) --%>
    <script>
        const CONTEXT_PATH = '${pageContext.request.contextPath}';
    </script>
    <script src="${pageContext.request.contextPath}/js/mypage/mypageVolunteerHistory.js"></script>

</body>
</html>
