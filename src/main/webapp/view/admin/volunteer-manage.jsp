<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<c:set var="ctp" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>봉사 신청자 관리</title>

	<link rel="preconnect" href="https://fonts.googleapis.com">
	<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
	<link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700;800&family=Manrope:wght@400;500;600;700&display=swap" rel="stylesheet">
	<link href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:wght@100..700" rel="stylesheet">

	<link rel="stylesheet" href="${ctp}/view/admin/css/common/admin-layout.css">
	<link rel="stylesheet" href="${ctp}/view/admin/css/volunteer-manage.css">

	<script defer src="${ctp}/view/admin/js/admin-layout.js"></script>
	<script defer src="${ctp}/view/admin/js/volunteer-manage.js"></script>
</head>

<body data-ctp="${ctp}">
<div class="admin-layout">

	<jsp:include page="/view/admin/common/admin-sidebar.jsp" />

	<div class="admin-main-wrap">

		<jsp:include page="/view/admin/common/admin-header.jsp" />

		<main class="admin-content">
			<section class="page-header">
				<div>
					<h1>봉사 신청자 관리</h1>
					<p>봉사 모집글을 선택하고 신청 회원을 승인 또는 반려할 수 있습니다.</p>
				</div>
			</section>

			<div class="volunteer-manage-layout">

				<!-- 왼쪽: 봉사 모집글 목록 -->
				<section class="recruit-panel">
					<div class="panel-head">
						<h2>
							<span class="material-symbols-outlined">volunteer_activism</span>
							봉사 모집글
						</h2>
					</div>

					<div class="recruit-list">
						<c:choose>
							<c:when test="${empty recruitList}">
								<div class="empty-box">
									등록된 봉사 모집글이 없습니다.
								</div>
							</c:when>

							<c:otherwise>
								<c:forEach var="r" items="${recruitList}">
									<a href="${ctp}/admin/volunteer/manage.do?recruitId=${r.recruitId}"
									   class="recruit-item ${param.recruitId == r.recruitId ? 'is-active' : ''}">
										<div class="recruit-item__top">
											<h3>${r.title}</h3>
											<span class="recruit-badge ${r.status eq '모집중' ? 'is-open' : 'is-close'}">
												${r.status}
											</span>
										</div>

										<p class="recruit-item__summary">
											<c:choose>
												<c:when test="${not empty r.content}">
													${r.content}
												</c:when>
												<c:otherwise>
													요약 정보가 없습니다.
												</c:otherwise>
											</c:choose>
										</p>

										<div class="recruit-item__meta">
											<span>
												<span class="material-symbols-outlined">calendar_today</span>
												<fmt:formatDate value="${r.volunteerDate}" pattern="yyyy.MM.dd" />
											</span>
											<span>
												<span class="material-symbols-outlined">location_on</span>
												${r.location}
											</span>
										</div>
									</a>
								</c:forEach>
							</c:otherwise>
						</c:choose>
					</div>
				</section>

				<!-- 오른쪽: 신청자 목록 -->
				<section class="apply-panel">
					<c:choose>
						<c:when test="${empty recruit}">
							<div class="empty-select-box">
								<span class="material-symbols-outlined">touch_app</span>
								<p>왼쪽에서 봉사 모집글을 선택하세요.</p>
							</div>
						</c:when>

						<c:otherwise>
							<div class="panel-head panel-head--detail">
								<div>
									<h2>${recruit.title}</h2>
									<div class="selected-recruit-meta">
										<span>
											<span class="material-symbols-outlined">calendar_today</span>
											봉사일 :
											<fmt:formatDate value="${recruit.volunteerDate}" pattern="yyyy.MM.dd" />
										</span>
										<span>
											<span class="material-symbols-outlined">location_on</span>
											장소 : ${recruit.location}
										</span>
										<span>
											<span class="material-symbols-outlined">schedule</span>
											신청마감 :
											<fmt:formatDate value="${recruit.applyDeadline}" pattern="yyyy.MM.dd" />
										</span>
									</div>
								</div>

								<div class="summary-mini">
									<div class="summary-mini__item">
										<span>총 신청</span>
										<strong>${applyCount}</strong>
									</div>
								</div>
							</div>

							<div class="table-wrap">
								<table class="volunteer-table">
									<thead>
										<tr>
											<th>신청번호</th>
											<th>회원ID</th>
											<th>닉네임</th>
											<th>연락처</th>
											<th>신청일</th>
											<th>희망분야</th>
											<th>상태</th>
											<th class="manage-col">관리</th>
										</tr>
									</thead>

									<tbody>
										<c:choose>
											<c:when test="${empty applyList}">
												<tr>
													<td colspan="8" class="empty-row">
														신청한 회원이 없습니다.
													</td>
												</tr>
											</c:when>

											<c:otherwise>
												<c:forEach var="vo" items="${applyList}">
													<tr class="apply-row" data-target="reason-${vo.volunteerId}">
														<td>${vo.volunteerId}</td>
														<td class="clickable-cell">${vo.memberId}</td>
														<td class="clickable-cell">${vo.nickname}</td>
														<td>${vo.phone}</td>
														<td>
															<fmt:formatDate value="${vo.applyDate}" pattern="yyyy.MM.dd" />
														</td>
														<td>
															<c:choose>
																<c:when test="${not empty vo.interestType}">
																	${vo.interestType}
																</c:when>
																<c:otherwise>-</c:otherwise>
															</c:choose>
														</td>
														<td>
															<span class="status-badge
																${vo.status eq '대기' ? 'is-wait' : ''}
																${vo.status eq '승인' ? 'is-approve' : ''}
																${vo.status eq '반려' ? 'is-reject' : ''}">
																${vo.status}
															</span>
														</td>
														<td class="manage-col">
															<div class="action-group">
																<button type="button"
																        class="action-btn action-btn--approve"
																        data-id="${vo.volunteerId}"
																        data-status="승인">
																	승인
																</button>

																<button type="button"
																        class="action-btn action-btn--reject"
																        data-id="${vo.volunteerId}"
																        data-status="반려">
																	반려
																</button>
															</div>
														</td>
													</tr>

													<tr id="reason-${vo.volunteerId}" class="reason-row">
														<td colspan="8">
															<div class="reason-box">
																<strong>신청 동기</strong>
																<p>
																	<c:choose>
																		<c:when test="${not empty vo.applyReason}">
																			${vo.applyReason}
																		</c:when>
																		<c:otherwise>작성된 신청 동기가 없습니다.</c:otherwise>
																	</c:choose>
																</p>
															</div>
														</td>
													</tr>
												</c:forEach>
											</c:otherwise>
										</c:choose>
									</tbody>
								</table>
							</div>
						</c:otherwise>
					</c:choose>
				</section>

			</div>
		</main>
	</div>
</div>
<div class="modal" id="rejectModal">
    <div class="modal-content">
        <div class="modal-header">
            <h3>반려 사유 입력</h3>
            <button class="modal-close" onclick="closeRejectModal()">
                <span class="material-symbols-outlined">close</span>
            </button>
        </div>
        <div class="modal-body">
            <%-- 반려 대상 volunteerId 보관용 --%>
            <input type="hidden" id="rejectVolunteerId">
            <div class="form-group">
                <label style="font-size:0.875rem; font-weight:700; color:#5f5b57;">
                    반려 사유 <span style="color:#b02500">*</span>
                </label>
                <textarea id="rejectReasonInput" rows="4"
                    placeholder="반려 사유를 입력해주세요."
                    style="width:100%; margin-top:0.5rem; padding:0.75rem;
                           border-radius:0.75rem; border:1px solid #e4dbd6;
                           resize:vertical; font-size:0.9rem; font-family:inherit;">
                </textarea>
            </div>
        </div>
        <div class="modal-footer">
            <button class="action-btn" onclick="closeRejectModal()">취소</button>
            <button class="action-btn action-btn--reject" onclick="confirmReject()">반려 확인</button>
        </div>
    </div>
</div>
document.getElementById('rejectModal');
</body>
</html>