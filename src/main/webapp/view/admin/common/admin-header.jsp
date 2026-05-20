<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctp" value="${pageContext.request.contextPath}" />

<header class="admin-header" data-ctp="${ctp}">
	<div class="admin-header__left">
		<div class="admin-search-wrap">
			<div class="admin-search">
				<span class="material-symbols-outlined">search</span>
				<input type="text" id="adminSearchInput" placeholder="동물, 회원, 문의 검색" autocomplete="off">
			</div>

			<div class="admin-search-dropdown" id="adminSearchDropdown" style="display:none;">
				<div class="admin-search-empty" id="adminSearchEmpty">검색 결과가 없습니다.</div>
				<div id="adminSearchResult"></div>
			</div>
		</div>
	</div>

	<div class="admin-header__right">

		<a href="${ctp}/main.do" class="admin-btn admin-btn--primary">
			<span class="material-symbols-outlined">home</span>
			메인으로
		</a>

		<div class="admin-notification-wrap">
			<button type="button" class="admin-icon-btn" id="adminNotificationBtn">
				<span class="material-symbols-outlined">notifications</span>
				<span class="admin-notification-dot" id="adminNotificationDot" style="display:none;"></span>
			</button>

			<div class="admin-notification-dropdown" id="adminNotificationDropdown" style="display:none;">
				<div class="admin-notification-dropdown__header">
					<strong>관리자 알림</strong>
				</div>

				<div class="admin-notification-dropdown__body" id="adminNotificationList">
					<div class="admin-notification-empty">새 알림이 없습니다.</div>
				</div>
			</div>
		</div>

		<button type="button" class="admin-icon-btn">
			<span class="material-symbols-outlined">mail</span>
		</button>

		<%-- ─────────────────────────────────────────────────────
		     헤더 달력 아이콘 — 어느 페이지에서나 전역 캘린더 모달 오픈
		     admin-layout.js 에서 이 id를 참조하여 이벤트 바인딩
		───────────────────────────────────────────────────── --%>
		<button type="button"
		        class="admin-icon-btn"
		        id="adminHeaderCalBtn"
		        aria-label="전체 일정 캘린더"
		        title="일정 캘린더 보기">
		    <span class="material-symbols-outlined">calendar_month</span>
		</button>
			<!-- 
			<div id="calendarModal" class="admin-modal" style="display:none;">
			    </div> -->

		<div class="admin-profile">
			<div class="admin-profile__thumb">
				<c:choose>
					<c:when test="${not empty sessionScope.loginUser.profileImg}">
						<img src="${ctp}/member/profileImage.do?fileName=${sessionScope.loginUser.profileImg}" alt="관리자 프로필">
					</c:when>
					<c:otherwise>
						<img src="https://via.placeholder.com/80x80?text=ADMIN" alt="관리자 프로필">
					</c:otherwise>
				</c:choose>
			</div>
			<div class="admin-profile__info">
				<strong>${sessionScope.loginUser.nickname}</strong>
				<span>관리자</span>
			</div>
		</div>
	</div>
</header>

<script src="${ctp}/view/admin/js/admin-header.js"></script>
