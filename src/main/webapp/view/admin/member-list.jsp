<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="ctp" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>회원 목록 관리</title>

	<link rel="preconnect" href="https://fonts.googleapis.com">
	<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
	<link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700;800&family=Manrope:wght@400;500;600;700&display=swap" rel="stylesheet">
	<link href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:wght@100..700" rel="stylesheet">

	<!-- 공통 레이아웃 -->
	<link rel="stylesheet" href="${ctp}/view/admin/css/common/admin-layout.css">

	<!-- 페이지 전용 -->
	<link rel="stylesheet" href="${ctp}/view/admin/css/member-list.css">

	<script defer src="${ctp}/view/admin/js/admin-layout.js"></script>
	<script defer src="${ctp}/view/admin/js/member-list.js"></script>
</head>
<body>
<div class="admin-layout">

	<jsp:include page="/view/admin/common/admin-sidebar.jsp" />

	<div class="admin-main-wrap">

		<jsp:include page="/view/admin/common/admin-header.jsp" />

		<main class="admin-content" data-ctp="${ctp}">
			<section class="member-page-top">
				<div>
					<h1>회원 목록 관리</h1>
					<p>시스템에 등록된 모든 회원 정보를 조회하고 관리합니다.</p>
				</div>

				<div class="member-summary">
					<div class="summary-card">
						<span class="summary-card__label">전체 회원</span>
						<strong class="summary-card__value">${totalCount}</strong>
					</div>
					<div class="summary-card">
						<span class="summary-card__label">신규(오늘)</span>
						<strong class="summary-card__value">${todayCount}</strong>
					</div>
				</div>
			</section>

			<section class="member-toolbar">
				<form action="${ctp}/admin/member/list.do" method="get" class="member-filter-form">
					<div class="member-search-box">
						<span class="material-symbols-outlined">search</span>
						<input
							type="text"
							name="keyword"
							value="${param.keyword}"
							placeholder="아이디, 닉네임, 이메일로 검색">
					</div>

					<div class="member-filter-group">
						<select name="role">
							<option value="">모든 권한</option>
							<option value="ADMIN" <c:if test="${param.role eq 'ADMIN'}">selected</c:if>>관리자</option>
							<option value="USER" <c:if test="${param.role eq 'USER'}">selected</c:if>>일반 사용자</option>
						</select>

						<select name="status">
							<option value="">모든 상태</option>
							<option value="Y" <c:if test="${param.status eq 'Y'}">selected</c:if>>활동 중</option>
							<option value="N" <c:if test="${param.status eq 'N'}">selected</c:if>>정지됨</option>
						</select>

						<button type="submit" class="member-filter-btn">
							<span class="material-symbols-outlined">filter_list</span>
							필터 적용
						</button>
					</div>
				</form>
			</section>

			<section class="member-table-wrap">
				<div class="member-table-scroll">
					<table class="member-table">
						<thead>
							<tr>
								<th>회원 ID</th>
								<th>닉네임 / 이메일</th>
								<th>가입일</th>
								<th>권한</th>
								<th>상태</th>
								<th class="is-right">관리</th>
							</tr>
						</thead>

						<tbody>
	<c:choose>
		<c:when test="${empty memberList}">
			<tr>
				<td colspan="6" class="empty-row">조회된 회원이 없습니다.</td>
			</tr>
		</c:when>

		<c:otherwise>
			<c:forEach var="m" items="${memberList}">
				<tr>
					<td class="member-id">${m.memberId}</td>

					<td>
						<div class="member-user-cell">
							<div class="member-user-cell__thumb">
								<c:choose>
									<c:when test="${not empty m.nickname and fn:length(m.nickname) >= 1}">
										${fn:substring(m.nickname, 0, 1)}
									</c:when>
									<c:otherwise>
										U
									</c:otherwise>
								</c:choose>
							</div>

							<div class="member-user-cell__info">
								<strong>${m.nickname}</strong>
								<span>${m.email}</span>
							</div>
						</div>
					</td>

					<td class="member-date">
					<c:choose>
						<c:when test="${not empty m.joinDate}">
							<fmt:formatDate value="${m.joinDate}" pattern="yyyy.MM.dd" />
					</c:when>
								<c:otherwise>
								-
						</c:otherwise>
					</c:choose>
					</td>

					<td>
						<c:choose>
							<c:when test="${m.role eq 'ADMIN'}">
								<span class="member-badge member-badge--admin">관리자</span>
							</c:when>
							<c:otherwise>
								<span class="member-badge member-badge--user">일반 사용자</span>
							</c:otherwise>
						</c:choose>
					</td>

					<td>
						<c:choose>
							<c:when test="${m.status eq 'Y'}">
								<span class="member-badge member-badge--active">활동 중</span>
							</c:when>
							<c:otherwise>
								<span class="member-badge member-badge--stop">정지됨</span>
							</c:otherwise>
						</c:choose>
					</td>

					<td class="is-right">
						<button
							type="button"
							class="member-detail-btn"
							data-memberid="${m.memberId}">
							상세보기
						</button>
					</td>
				</tr>
			</c:forEach>
		</c:otherwise>
	</c:choose>
</tbody>
					</table>
				</div>

				<div class="member-table-footer">
					<span>
						총 ${totalCount}명
					</span>

					<div class="pagination">
	<c:if test="${pageVO.startPage > 1}">
	<a href="?page=${pageVO.startPage - 1}&keyword=${param.keyword}&status=${param.status}&role=${param.role}">
		이전
	</a>
</c:if>

	<c:forEach var="i" begin="${pageVO.startPage}" end="${pageVO.endPage}">
		<a href="?page=${i}&keyword=${param.keyword}&status=${param.status}&role=${param.role}"
   class="${i == pageVO.currentPage ? 'is-active' : ''}">
	${i}
</a>
	</c:forEach>

	<c:if test="${pageVO.endPage < pageVO.totalPage}">
	<a href="?page=${pageVO.endPage + 1}&keyword=${param.keyword}&status=${param.status}&role=${param.role}">
		다음
	</a>
</c:if>
</div>
				</div>
			</section>

			<section class="member-help-box">
				<div class="member-help-box__icon">
					<span class="material-symbols-outlined">info</span>
				</div>
				<div>
					<h4>회원 관리 팁</h4>
					<p>부적절한 활동이 감지된 회원은 정지 상태로 전환하여 게시판 접근을 제한할 수 있습니다. 관리자 권한 부여는 신중하게 처리하세요.</p>
				</div>
			</section>
		</main>
	</div>
</div>
</body>
</html>