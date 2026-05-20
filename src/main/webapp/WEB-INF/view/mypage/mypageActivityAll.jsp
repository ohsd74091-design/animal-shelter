<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>최근 활동 내역 전체보기 - 너와 나의 연결고리</title>

<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined" rel="stylesheet">
<link href="https://cdn.jsdelivr.net/gh/orioncactus/pretendard/dist/web/static/pretendard.css" rel="stylesheet">

<%-- 공통 CSS --%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/common.css">

<%-- 마이페이지 공통 CSS --%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/mypage/mypage.css">

<%-- 활동 내역 전용 CSS --%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/mypage/mypageActivityAll.css">

</head>
<body>

    <%-- 공통 헤더 --%>
    <jsp:include page="/view/common/header.jsp" />

    <div class="mypage-container">

        <%-- 사이드바 --%>
        <jsp:include page="/view/common/sidebarNav.jsp">
            <jsp:param name="activeMenu" value="main"/>
        </jsp:include>

        <%-- 메인 콘텐츠 --%>
        <section class="mypage-content">

            <%-- 페이지 헤더 --%>
            <div class="page-header">
                <h2 class="page-title">최근 활동 내역 전체보기</h2>
                <p class="page-desc">회원님의 소중한 활동 기록을 한눈에 확인하실 수 있습니다.</p>
            </div>

            <%-- 필터 탭 --%>
            <div class="activity-tab-bar">
                <a href="?type=&page=1"
                   class="activity-tab ${empty currentType ? 'active' : ''}">전체</a>
                <a href="?type=favorite&page=1"
                   class="activity-tab ${currentType eq 'favorite' ? 'active' : ''}">관심</a>
                <a href="?type=adoption&page=1"
                   class="activity-tab ${currentType eq 'adoption' ? 'active' : ''}">입양</a>
                <a href="?type=donation&page=1"
                   class="activity-tab ${currentType eq 'donation' ? 'active' : ''}">후원</a>
                <a href="?type=volunteer&page=1"
                   class="activity-tab ${currentType eq 'volunteer' ? 'active' : ''}">봉사</a>
            </div>

            <%-- 활동 목록 --%>
            <c:choose>
                <c:when test="${not empty activityList}">
                    <div class="activity-all-list">
                        <c:forEach var="act" items="${activityList}">
                            <div class="activity-all-item">

                                <%-- 아이콘 --%>
                                <div class="activity-all-item__icon activity-all-item__icon--${act.iconType}">
                                    <span class="material-symbols-outlined">${act.icon}</span>
                                </div>

                                <%-- 내용 --%>
                                <div class="activity-all-item__body">
                                    <p class="activity-all-item__desc">${act.description}</p>
                                    <p class="activity-all-item__date">${act.actDate}</p>
                                </div>

                                <%-- 뱃지 --%>
                                <span class="badge badge-${act.badgeType}">${act.badgeLabel}</span>

                            </div>
                        </c:forEach>
                    </div>

                    <%-- 페이지네이션 --%>
                    <div class="pagination">
                        <c:choose>
                            <c:when test="${currentPage > 1}">
                                <a href="?type=${currentType}&page=${currentPage - 1}" class="page-btn page-btn--arrow">
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
                                    <a href="?type=${currentType}&page=${p}" class="page-btn">${p}</a>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>

                        <c:choose>
                            <c:when test="${currentPage < totalPages}">
                                <a href="?type=${currentType}&page=${currentPage + 1}" class="page-btn page-btn--arrow">
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
                    <div class="activity-all-empty">
                        <span class="material-symbols-outlined activity-all-empty__icon">history</span>
                        <p class="activity-all-empty__msg">활동 내역이 없습니다.</p>
                    </div>
                </c:otherwise>
            </c:choose>

        </section><%-- /.mypage-content --%>

    </div><%-- /.mypage-container --%>

    <%-- 공통 푸터 --%>
    <jsp:include page="/view/common/footer.jsp" />

</body>
</html>
