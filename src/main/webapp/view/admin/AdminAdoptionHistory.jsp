<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="ctp" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>입양신청내역</title>

	<link rel="preconnect" href="https://fonts.googleapis.com">
	<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
	<link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700;800&family=Manrope:wght@400;500;600;700;800&display=swap" rel="stylesheet">
	<link href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:wght@100..700" rel="stylesheet">

	<link rel="stylesheet" href="${ctp}/view/admin/css/common/admin-layout.css">
	<link rel="stylesheet" href="${ctp}/view/admin/css/adoption-history.css">

	<script defer src="${ctp}/view/admin/js/admin-layout.js"></script>
	<script defer src="${ctp}/view/admin/js/adoption-history.js"></script>
</head>
<body>
<div class="admin-layout">

	<jsp:include page="/view/admin/common/admin-sidebar.jsp" />

	<div class="admin-main-wrap">
		<jsp:include page="/view/admin/common/admin-header.jsp" />

		<main class="admin-content">
			<section class="admin-page-title">
				<div>
					<h1>입양신청내역</h1>
					<p>승인, 반려, 심사중 상태를 한 번에 관리하고 신청자 상세정보를 확인하세요.</p>
				</div>

				<div class="admin-page-title__actions">
					<button type="button"
					        class="admin-btn admin-btn--gray"
					        onclick="location.href='${ctp}/admin/adoption/list.do'">
						<span class="material-symbols-outlined">event_available</span>
						입양상담(예약)으로 이동
					</button>
				</div>
			</section>

			<section class="history-summary-grid">
				<div class="summary-card summary-card--all">
					<div class="summary-card__icon">
						<span class="material-symbols-outlined">receipt_long</span>
					</div>
					<div class="summary-card__body">
						<p>전체 신청</p>
						<strong>${totalCount}</strong>
						<span>건</span>
					</div>
				</div>

				<div class="summary-card summary-card--pending">
					<div class="summary-card__icon">
						<span class="material-symbols-outlined">hourglass_top</span>
					</div>
					<div class="summary-card__body">
						<p>심사중</p>
						<strong>${pendingCount}</strong>
						<span>건</span>
					</div>
				</div>

				<div class="summary-card summary-card--approved">
					<div class="summary-card__icon">
						<span class="material-symbols-outlined">task_alt</span>
					</div>
					<div class="summary-card__body">
						<p>승인</p>
						<strong>${approvedCount}</strong>
						<span>건</span>
					</div>
				</div>

				<div class="summary-card summary-card--rejected">
					<div class="summary-card__icon">
						<span class="material-symbols-outlined">cancel</span>
					</div>
					<div class="summary-card__body">
						<p>반려</p>
						<strong>${rejectedCount}</strong>
						<span>건</span>
					</div>
				</div>
			</section>

			<section class="history-panel">
				<div class="history-toolbar">
					<div class="history-filter-tabs">
						<a href="${ctp}/admin/adoption/history.do"
						   class="history-filter-tab ${empty statusFilter ? 'is-active' : ''}">
							전체
						</a>
						<a href="${ctp}/admin/adoption/history.do?statusFilter=심사중"
						   class="history-filter-tab ${statusFilter eq '심사중' ? 'is-active' : ''}">
							심사중
						</a>
						<a href="${ctp}/admin/adoption/history.do?statusFilter=승인"
						   class="history-filter-tab ${statusFilter eq '승인' ? 'is-active' : ''}">
							승인
						</a>
						<a href="${ctp}/admin/adoption/history.do?statusFilter=반려"
						   class="history-filter-tab ${statusFilter eq '반려' ? 'is-active' : ''}">
							반려
						</a>
					</div>

					<div class="history-toolbar__right">
						<div class="history-count-text">
							총 <strong>${pageVO.totalCount}</strong>건 중
							<strong>${pageVO.startRow}</strong> ~
							<strong>
								<c:choose>
									<c:when test="${pageVO.endRow > pageVO.totalCount}">
										${pageVO.totalCount}
									</c:when>
									<c:otherwise>
										${pageVO.endRow}
									</c:otherwise>
								</c:choose>
							</strong>건 표시
						</div>
					</div>
				</div>

				<div class="history-table-wrap">
					<table class="history-table">
						<thead>
						<tr>
							<th>신청일</th>
							<th>신청자</th>
							<th>연락처</th>
							<th>대상 동물</th>
							<th>방문예약일</th>
							<th>상태</th>
							<th>처리내용</th>
							<th>상세</th>
						</tr>
						</thead>
						<tbody>
						<c:choose>
							<c:when test="${not empty adoptionHistoryList}">
								<c:forEach var="item" items="${adoptionHistoryList}">
									<tr>
										<td>
											<fmt:formatDate value="${item.applyDate}" pattern="yyyy-MM-dd" />
										</td>
										<td>
											<div class="applicant-cell">
												<div class="applicant-thumb">
													<c:choose>
														<c:when test="${not empty item.profileImg}">
															<img src="${ctp}/member/profileImage.do?fileName=${item.profileImg}" alt="프로필">
														</c:when>
														<c:otherwise>
															<span>${fn:substring(item.memberName, 0, 1)}</span>
														</c:otherwise>
													</c:choose>
												</div>
												<div class="applicant-meta">
													<strong>${item.memberName}</strong>
													<span>${item.memberId}</span>
												</div>
											</div>
										</td>
										<td>${item.phone}</td>
										<td>
											<div class="animal-cell">
												<strong>${item.animalName}</strong>
												<span>${item.breed}</span>
											</div>
										</td>
										<td>
											<fmt:formatDate value="${item.visitDate}" pattern="yyyy-MM-dd" />
										</td>
										<td>
											<span class="status-badge
												${item.status eq '승인' ? 'status-badge--approved' : ''}
												${item.status eq '반려' ? 'status-badge--rejected' : ''}
												${item.status eq '심사중' ? 'status-badge--pending' : ''}">
												${item.status}
											</span>
										</td>
										<td>
											<c:choose>
												<c:when test="${item.status eq '승인'}">
													<span class="result-text result-text--approved">승인 완료</span>
												</c:when>
												<c:when test="${item.status eq '반려'}">
													<span class="result-text result-text--rejected">
														${empty item.rejectReason ? '반려 처리' : item.rejectReason}
													</span>
												</c:when>
												<c:otherwise>
													<span class="result-text result-text--pending">심사 진행중</span>
												</c:otherwise>
											</c:choose>
										</td>
										<td>
											<a class="detail-link"
											   href="${ctp}/admin/adoption/historyDetail.do?adoptionId=${item.adoptionId}">
												상세보기
											</a>
										</td>
									</tr>
								</c:forEach>
							</c:when>
							<c:otherwise>
								<tr>
									<td colspan="8">
										<div class="history-empty">
											<span class="material-symbols-outlined">folder_open</span>
											<p>조회된 입양신청내역이 없습니다.</p>
										</div>
									</td>
								</tr>
							</c:otherwise>
						</c:choose>
						</tbody>
					</table>
				</div>

				<c:if test="${not empty adoptionHistoryList}">
					<div class="history-pagination">
						<c:if test="${pageVO.startPage > 1}">
							<a class="page-btn"
							   href="${ctp}/admin/adoption/history.do?page=${pageVO.startPage - 1}&statusFilter=${statusFilter}">
								<span class="material-symbols-outlined">chevron_left</span>
							</a>
						</c:if>

						<c:forEach var="i" begin="${pageVO.startPage}" end="${pageVO.endPage}">
							<a href="${ctp}/admin/adoption/history.do?page=${i}&statusFilter=${statusFilter}"
							   class="page-btn ${pageVO.currentPage eq i ? 'is-active' : ''}">
								${i}
							</a>
						</c:forEach>

						<c:if test="${pageVO.endPage < pageVO.totalPage}">
							<a class="page-btn"
							   href="${ctp}/admin/adoption/history.do?page=${pageVO.endPage + 1}&statusFilter=${statusFilter}">
								<span class="material-symbols-outlined">chevron_right</span>
							</a>
						</c:if>
					</div>
				</c:if>
			</section>
		</main>
	</div>
</div>
</body>
</html>