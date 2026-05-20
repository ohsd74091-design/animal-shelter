<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="ctp" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>입양신청 상세</title>

	<link rel="preconnect" href="https://fonts.googleapis.com">
	<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
	<link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700;800&family=Manrope:wght@400;500;600;700;800&display=swap" rel="stylesheet">
	<link href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:wght@100..700" rel="stylesheet">

	<link rel="stylesheet" href="${ctp}/view/admin/css/common/admin-layout.css">
	<link rel="stylesheet" href="${ctp}/view/admin/css/adoption-history-detail.css">

	<script defer src="${ctp}/view/admin/js/admin-layout.js"></script>
</head>
<body>
<div class="admin-layout">

	<jsp:include page="/view/admin/common/admin-sidebar.jsp" />

	<div class="admin-main-wrap">
		<jsp:include page="/view/admin/common/admin-header.jsp" />

		<main class="admin-content">
			<section class="admin-page-title admin-page-title--detail">
				<div>
					<a href="${ctp}/admin/adoption/history.do" class="back-link">
						<span class="material-symbols-outlined">arrow_back</span>
						입양신청내역으로
					</a>
					<h1>입양신청 상세</h1>
					<p>신청자 정보와 대상 동물, 처리 결과를 확인합니다.</p>
				</div>

				<div class="detail-status-area">
					<span class="status-badge
						${adoption.status eq '승인' ? 'status-badge--approved' : ''}
						${adoption.status eq '반려' ? 'status-badge--rejected' : ''}
						${adoption.status eq '심사중' ? 'status-badge--pending' : ''}">
						${adoption.status}
					</span>
				</div>
			</section>

			<section class="detail-top-grid">
				<div class="detail-card detail-card--main">
					<div class="detail-card__head">
						<h2>
							<span class="material-symbols-outlined">person</span>
							신청자 정보
						</h2>
					</div>

					<div class="info-grid">
						<div class="info-item">
							<label>신청자명</label>
							<p>${adoption.memberName}</p>
						</div>

						<div class="info-item">
							<label>회원 아이디</label>
							<p>${adoption.memberId}</p>
						</div>

						<div class="info-item">
							<label>전화번호</label>
							<p>${adoption.phone}</p>
						</div>

						<div class="info-item">
							<label>이메일</label>
							<p>${adoption.email}</p>
						</div>

						<div class="info-item info-item--full">
	<label>주소</label>
	<p>${not empty adoption.detail ? adoption.detail.address : '-'}</p>
</div>

						<div class="info-item">
	<label>직업</label>
	<p>${not empty adoption.detail ? adoption.detail.job : '-'}</p>
</div>

						<div class="info-item">
	<label>주거형태</label>
	<p>${not empty adoption.detail ? adoption.detail.housingType : '-'}</p>
</div>

						<div class="info-item">
	<label>반려동물 경험</label>
	<p>${not empty adoption.detail ? adoption.detail.petExperience : '-'}</p>
</div>

						<div class="info-item">
							<label>방문예약일</label>
							<p><fmt:formatDate value="${adoption.visitDate}" pattern="yyyy-MM-dd" /></p>
						</div>

						<div class="info-item">
							<label>신청일</label>
							<p><fmt:formatDate value="${adoption.applyDate}" pattern="yyyy-MM-dd" /></p>
						</div>
					</div>
				</div>

				<div class="detail-card detail-card--side">
					<div class="detail-card__head">
						<h2>
							<span class="material-symbols-outlined">pets</span>
							대상 동물
						</h2>
					</div>

					<div class="animal-photo">
						<c:choose>
							<c:when test="${not empty adoption.mainImage}">
								<img src="${ctp}/imageView.do?fileName=${adoption.mainImage}" alt="동물 사진">
							</c:when>
							<c:otherwise>
								<div class="animal-photo__empty">
									<span class="material-symbols-outlined">pets</span>
								</div>
							</c:otherwise>
						</c:choose>
					</div>

					<div class="animal-info-list">
						<div class="animal-info-row">
							<span>이름</span>
							<strong>${adoption.animalName}</strong>
						</div>
						<div class="animal-info-row">
							<span>종류</span>
							<strong>${adoption.animalType}</strong>
						</div>
						<div class="animal-info-row">
							<span>품종</span>
							<strong>${adoption.breed}</strong>
						</div>
						<div class="animal-info-row">
							<span>성별</span>
							<strong>${adoption.gender}</strong>
						</div>
						<div class="animal-info-row">
							<span>나이</span>
							<strong>${adoption.age}</strong>
						</div>
						<div class="animal-info-row">
							<span>현재 상태</span>
							<strong>${adoption.adoptionStatus}</strong>
						</div>
					</div>
				</div>
			</section>

			<section class="detail-bottom-grid">
				<div class="detail-card">
					<div class="detail-card__head">
						<h2>
							<span class="material-symbols-outlined">chat</span>
							입양 동기
						</h2>
					</div>

					<div class="motivation-box">
						<c:choose>
							<c:when test="${not empty adoption.adoptionReason}">
								${adoption.adoptionReason}
							</c:when>
							<c:otherwise>
								입양 동기 정보가 없습니다.
							</c:otherwise>
						</c:choose>
					</div>
				</div>

				<div class="detail-card">
					<div class="detail-card__head">
						<h2>
							<span class="material-symbols-outlined">fact_check</span>
							처리 결과
						</h2>
					</div>

					<div class="result-box">
						<div class="result-row">
							<label>처리 상태</label>
							<p>${adoption.status}</p>
						</div>

						<div class="result-row">
							<label>신청번호</label>
							<p>${adoption.adoptionId}</p>
						</div>

						<c:if test="${adoption.status eq '반려'}">
							<div class="reject-box">
								<div class="reject-box__title">
									<span class="material-symbols-outlined">report</span>
									반려 사유
								</div>
								<div class="reject-box__content">
									<c:choose>
										<c:when test="${not empty adoption.rejectReason}">
											${adoption.rejectReason}
										</c:when>
										<c:otherwise>
											등록된 반려 사유가 없습니다.
										</c:otherwise>
									</c:choose>
								</div>
							</div>
						</c:if>

						<c:if test="${adoption.status eq '승인'}">
							<div class="approve-box">
								<span class="material-symbols-outlined">task_alt</span>
								승인 완료된 신청입니다. 대상 동물 상태를 함께 확인하세요.
							</div>
						</c:if>

						<c:if test="${adoption.status eq '심사중'}">
							<div class="pending-box">
								<span class="material-symbols-outlined">hourglass_top</span>
								현재 심사 진행중인 신청입니다.
							</div>
						</c:if>
					</div>
				</div>
			</section>
		</main>
	</div>
</div>
</body>
</html>