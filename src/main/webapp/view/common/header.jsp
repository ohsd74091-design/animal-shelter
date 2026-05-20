<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<script type="text/javascript">
    window.contextPath = "${pageContext.request.contextPath}";
</script>

<header class="site-header">
    <div class="layout-container header-inner">
        <div class="header-left">
            <a href="${pageContext.request.contextPath}/main.do" class="site-logo">
                <span class="material-symbols-outlined logo-icon">pets</span>
                <span class="logo-text">너와 나의 연결고리</span>
            </a>

            <nav class="main-nav">
                <c:set var="uri" value="${pageContext.request.requestURI}" />
                <c:set var="servletPath" value="${pageContext.request.servletPath}" />
                <c:set var="checkPath" value="${uri}${servletPath}" />

                <c:set var="animalActive" value="" />
                <c:set var="volunteerActive" value="" />
                <c:set var="donationActive" value="" />
                <c:set var="boardActive" value="" />

                <c:if test="${fn:contains(checkPath, 'animal')}">
                    <c:set var="animalActive" value="nav-link--active" />
                </c:if>
                <c:if test="${fn:contains(checkPath, 'volunteer') or fn:contains(checkPath, 'vlounteer')}">
                    <c:set var="volunteerActive" value="nav-link--active" />
                </c:if>
                <c:if test="${fn:contains(checkPath, 'donation')}">
                    <c:set var="donationActive" value="nav-link--active" />
                </c:if>
                <c:if test="${fn:contains(checkPath, 'board')}">
                    <c:set var="boardActive" value="nav-link--active" />
                </c:if>

                <a href="${pageContext.request.contextPath}/animal/animalList.do" class="nav-link ${animalActive}">입양하기</a>
                <a href="${pageContext.request.contextPath}/volunteer/list.do" class="nav-link ${volunteerActive}">봉사하기</a>
                <a href="${pageContext.request.contextPath}/donation/form.do" class="nav-link ${donationActive}">후원하기</a>
                <a href="${pageContext.request.contextPath}/board/free.do" class="nav-link ${boardActive}">커뮤니티</a>
            </nav>
        </div>

        <div class="header-right">
            <form action="${pageContext.request.contextPath}/animal/animalList.do" method="get" class="header-search-form">
                <input type="text" name="breed" value="${breed}" class="header-search-input" placeholder="친구를 찾아보세요">
                <button type="submit" class="header-search-button" aria-label="검색">
                    <span class="material-symbols-outlined">search</span>
                </button>
            </form>

            <c:if test="${not empty sessionScope.loginUser and sessionScope.loginUser.role eq 'ADMIN'}">
                <a href="${pageContext.request.contextPath}/admin/main.do" class="admin-link">관리자 패널</a>
            </c:if>

            <div class="header-divider"></div>

            <c:choose>
                <c:when test="${empty sessionScope.loginUser}">
                    <a href="${pageContext.request.contextPath}/login.do" class="profile-link">
                        <div class="profile-thumb">
                            <img src="${pageContext.request.contextPath}/images/default-profile.png" alt="기본 프로필">
                        </div>
                        <span class="profile-name">로그인</span>
                    </a>

                    <a href="${pageContext.request.contextPath}/member/signupView.do" class="icon-circle" aria-label="회원가입">
                        <span class="material-symbols-outlined">person_add</span>
                    </a>
                </c:when>

                <c:otherwise>
                    <!-- 사용자 알림 -->
                    <div class="user-notification-wrap">
                        <button type="button" class="icon-circle" id="userNotificationBtn" aria-label="알림">
                            <span class="material-symbols-outlined">notifications</span>
                            <span class="user-notification-dot" id="userNotificationDot" style="display:none;"></span>
                        </button>

                        <div class="user-notification-dropdown" id="userNotificationDropdown" style="display:none;">
                            <div class="user-notification-dropdown__header">
                                <strong>알림</strong>
                            </div>

                            <div class="user-notification-dropdown__body" id="userNotificationList">
                                <div class="user-notification-empty">새 알림이 없습니다.</div>
                            </div>
                        </div>
                    </div>

                    <a href="${pageContext.request.contextPath}/mypage/main.do" class="profile-link">
                        <div class="profile-thumb">
                            <c:choose>
                                <c:when test="${not empty sessionScope.loginUser.profileImg}">
                                    <img src="${pageContext.request.contextPath}/member/profileImage.do?fileName=${sessionScope.loginUser.profileImg}" alt="회원 프로필">
                                </c:when>
                                <c:otherwise>
                                    <img src="${pageContext.request.contextPath}/images/default-profile.png" alt="기본 프로필">
                                </c:otherwise>
                            </c:choose>
                        </div>
                        <span class="profile-name">${sessionScope.loginUser.nickname}</span>
                    </a>

                    <a href="${pageContext.request.contextPath}/logout.do" class="icon-circle" aria-label="로그아웃">
                        <span class="material-symbols-outlined">logout</span>
                    </a>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</header>

<c:if test="${not empty sessionScope.loginUser}">
   <script src="${pageContext.request.contextPath}/js/user-notification.js"></script>
</c:if>