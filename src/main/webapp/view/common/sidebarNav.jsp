<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%--
    /view/common/sidebarNav.jsp
    마이페이지 공통 사이드바

    사용법:
        <jsp:include page="/view/common/sidebarNav.jsp">
            <jsp:param name="activeMenu" value="main"/>
        </jsp:include>

    activeMenu 값 목록:
        main            → 내 정보
        edit            → 회원 정보 수정
        favorite        → 관심 동물
        adoptionHistory → 입양 신청 내역
        myPosts         → 내 작성 글
        donationHistory → 후원 내역
        volunteerHistory→ 봉사 신청 내역
--%>

<%-- 현재 페이지 파라미터 수신 --%>
<c:set var="activeMenu" value="${param.activeMenu}"/>

<aside class="mypage-sidebar">

    <%-- 내비게이션 --%>
    <nav class="sidebar-nav">
        <a href="${pageContext.request.contextPath}/mypage/main.do"
           class="${activeMenu eq 'main' ? 'active' : ''}">
            <span class="material-symbols-outlined">person</span>내 정보
        </a>
        <a href="${pageContext.request.contextPath}/mypage/edit.do"
           class="${activeMenu eq 'edit' ? 'active' : ''}">
            <span class="material-symbols-outlined">edit</span>회원 정보 수정
        </a>
        <a href="${pageContext.request.contextPath}/mypage/favorite.do"
           class="${activeMenu eq 'favorite' ? 'active' : ''}">
            <span class="material-symbols-outlined">favorite</span>관심 동물
        </a>
        <a href="${pageContext.request.contextPath}/mypage/adoptionHistory.do"
           class="${activeMenu eq 'adoptionHistory' ? 'active' : ''}">
            <span class="material-symbols-outlined">assignment</span>입양 신청 내역
        </a>
        <a href="${pageContext.request.contextPath}/mypage/myPosts.do"
           class="${activeMenu eq 'myPosts' ? 'active' : ''}">
            <span class="material-symbols-outlined">article</span>내 작성 글
        </a>
        <a href="${pageContext.request.contextPath}/mypage/donationHistory.do"
           class="${activeMenu eq 'donationHistory' ? 'active' : ''}">
            <span class="material-symbols-outlined">volunteer_activism</span>후원 내역
        </a>
        <a href="${pageContext.request.contextPath}/mypage/volunteerHistory.do"
           class="${activeMenu eq 'volunteerHistory' ? 'active' : ''}">
            <span class="material-symbols-outlined">event_available</span>봉사 신청 내역
        </a>
    </nav>

</aside>
