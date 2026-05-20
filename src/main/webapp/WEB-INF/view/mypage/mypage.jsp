<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>너와 나의 연결고리 - 마이페이지</title>

<%-- 공통 CSS --%>
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined" rel="stylesheet">
<link href="https://cdn.jsdelivr.net/gh/orioncactus/pretendard/dist/web/static/pretendard.css" rel="stylesheet">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/common.css">

<%-- mypage 전용 CSS / JS --%>
<link  rel="stylesheet" href="${pageContext.request.contextPath}/css/mypage/mypage.css">
<script src="${pageContext.request.contextPath}/js/mypage/mypage.js" defer></script>

</head>
<body>

    <%-- header --%>
    <jsp:include page="/view/common/header.jsp" />

    <div class="mypage-container">

        <%-- ===== 사이드바 ===== --%>
		<jsp:include page="/view/common/sidebarNav.jsp">
		    <jsp:param name="activeMenu" value="main"/>
		</jsp:include>

        <%-- ===== 메인 콘텐츠 ===== --%>
        <section class="mypage-content">

            <%-- 웰컴 배너 --%>
            <div class="welcome-banner">
                <div class="welcome-avatar">
                    <img
                        src="${not empty loginUser.profileImg
                                ? pageContext.request.contextPath.concat('/member/profileImage.do?fileName=').concat(loginUser.profileImg)
                                : pageContext.request.contextPath.concat('/images/mypage/default.png')}"
                        alt="프로필 사진"
                    >
                </div>
                <div class="welcome-info">
                    <h2>안녕하세요, <span class="highlight"><c:out value="${loginUser.nickname}"/></span> 님!</h2>
                    <div class="welcome-meta welcome-meta--compact">
                        <span>
                            <span class="material-symbols-outlined">person</span>
                            <c:out value="${loginUser.memberId}"/>
                        </span>
                        <span>
                            <span class="material-symbols-outlined">mail</span>
                            <c:out value="${loginUser.email}"/>
                        </span>
                    </div>
                    <div class="welcome-meta">
                        <span>
                            <span class="material-symbols-outlined">calendar_today</span>
                            가입일: <c:out value="${loginUser.joinDate}"/>
                        </span>
                        <span>
                            <span class="material-symbols-outlined">verified_user</span>
                            <c:out value="${loginUser.role}"/>
                        </span>
                    </div>
                </div>
            </div>

            <%-- 통계 카드 --%>
            <div class="stat-grid">

                <a href="${pageContext.request.contextPath}/mypage/favorite.do" class="stat-card">
                    <span class="stat-icon">
                        <span class="material-symbols-outlined">favorite</span>
                    </span>
                    <span class="stat-label">관심동물</span>
                    <span class="stat-value"><c:out value="${mypageStats.favoriteCount}"/></span>
                    <span class="material-symbols-outlined stat-arrow">arrow_forward_ios</span>
                </a>

                <a href="${pageContext.request.contextPath}/mypage/adoptionHistory.do" class="stat-card">
                    <span class="stat-icon">
                        <span class="material-symbols-outlined">assignment</span>
                    </span>
                    <span class="stat-label">입양신청</span>
                    <span class="stat-value"><c:out value="${mypageStats.adoptionCount}"/></span>
                    <span class="material-symbols-outlined stat-arrow">arrow_forward_ios</span>
                </a>

                <a href="${pageContext.request.contextPath}/mypage/myPosts.do" class="stat-card">
                    <span class="stat-icon">
                        <span class="material-symbols-outlined">article</span>
                    </span>
                    <span class="stat-label">작성글</span>
                    <span class="stat-value"><c:out value="${mypageStats.postCount}"/></span>
                    <span class="material-symbols-outlined stat-arrow">arrow_forward_ios</span>
                </a>

            </div><%-- /.stat-grid --%>

            <%-- 최근 활동 내역 --%>
            <div class="activity-section">
                <div class="activity-header">
                    <h3>최근 활동 내역</h3>
                    <a href="${pageContext.request.contextPath}/mypage/activityAll.do" class="btn-view-all">전체보기</a>
                </div>

                <div class="activity-list">
                    <c:choose>
                        <c:when test="${not empty recentActivities}">
                            <c:forEach var="act" items="${recentActivities}">
                                <div class="activity-item">
                                    <div class="activity-left">
                                        <div class="activity-icon activity-icon--${act.iconType}">
                                            <span class="material-symbols-outlined"><c:out value="${act.icon}"/></span>
                                        </div>
                                        <div>
                                            <p class="activity-desc"><c:out value="${act.description}"/></p>
                                            <p class="activity-date"><c:out value="${act.actDate}"/></p>
                                        </div>
                                    </div>
                                    <span class="badge badge-${act.badgeType}">
                                        <c:out value="${act.badgeLabel}"/>
                                    </span>
                                </div>
                            </c:forEach>
                        </c:when>
                        
                        <c:otherwise>
                            <div class="no-data">
                                <p>최근 활동 내역이 없습니다.</p>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div><%-- /.activity-section --%>

        </section><%-- /.mypage-content --%>

    </div><%-- /.mypage-container --%>

    <%-- footer --%>
    <jsp:include page="/view/common/footer.jsp" />

</body>
</html>
