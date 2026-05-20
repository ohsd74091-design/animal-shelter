<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctp" value="${pageContext.request.contextPath}" />

<aside class="admin-sidebar">
	<div class="admin-sidebar__brand">
		<h2 class="admin-sidebar__logo">Animal Shelter</h2>
		<p class="admin-sidebar__subtitle">관리자 콘솔</p>
	</div>

	<nav class="admin-sidebar__nav">
		<a href="${ctp}/admin/main.do" class="admin-sidebar__link">
			<span class="material-symbols-outlined">dashboard</span>
			<span>대시보드</span>
		</a>

		<a href="${ctp}/admin/animal/register.do" class="admin-sidebar__link">
			<span class="material-symbols-outlined">pets</span>
			<span>동물등록</span>
		</a>

		<a href="${ctp}/admin/animal/list.do" class="admin-sidebar__link">
			<span class="material-symbols-outlined">list_alt</span>
			<span>동물목록관리</span>
		</a>

		<a href="${ctp}/admin/member/list.do" class="admin-sidebar__link">
			<span class="material-symbols-outlined">group</span>
			<span>회원관리</span>
		</a>

		<a href="${ctp}/admin/board/list.do" class="admin-sidebar__link">
			<span class="material-symbols-outlined">article</span>
			<span>게시판관리</span>
		</a>
		
		<a href="${ctp}/admin/report/list.do" class="admin-sidebar__link">
    	<span class="material-symbols-outlined">flag</span>
    	<span>신고 관리</span>
		</a>

		<a href="${ctp}/admin/adoption/list.do" class="admin-sidebar__link">
			<span class="material-symbols-outlined">event_available</span>
			<span>입양상담(예약)</span>
		</a>
		
		<a href="${ctp}/admin/adoption/history.do" class="admin-sidebar__link">
			<span class="material-symbols-outlined">fact_check</span>
			<span>입양신청 결과 내역</span>
		</a>

		<a href="${ctp}/admin/support/list.do" class="admin-sidebar__link">
			<span class="material-symbols-outlined">support_agent</span>
			<span>문의관리</span>
		</a>
		
		<a href="${ctp}/admin/volunteer/manage.do" class="admin-sidebar__link">
			<span class="material-symbols-outlined">volunteer_activism</span>
			<span>봉사활동 관리</span>
		</a>
		
		<a href="${ctp}/admin/donation.do" class="admin-sidebar__link">
			<span class="material-symbols-outlined">payments</span>
			<span>후원 관리</span>
		</a>
	</nav>

	<div class="admin-sidebar__bottom">
		<a href="${ctp}/logout.do" class="admin-sidebar__link">
			<span class="material-symbols-outlined">logout</span>
			<span>로그아웃</span>
		</a>
	</div>
</aside>