<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<c:set var="ctp" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>${boardVo.title}-게시글</title>

<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link
	href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700;800&family=Manrope:wght@400;500;600;700&display=swap"
	rel="stylesheet">
<link
	href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined"
	rel="stylesheet">

<link rel="stylesheet" href="${ctp}/css/common/common.css">
<link rel="stylesheet" href="${ctp}/css/board-detail.css">
</head>
<body>

	<jsp:include page="/view/common/header.jsp" />

	<main class="detail-page">
		<div class="detail-inner">

			<a href="${ctp}/board/list.do" class="back-link"> <span
				class="material-symbols-outlined">arrow_back</span> 목록으로 돌아가기
			</a>

			<h2 class="detail-page-title">게시글 상세보기</h2>
			<p class="detail-page-sub">게시글의 상세 내용을 확인할 수 있습니다.</p>

			<%-- 세션 메시지 알림 --%>
			<c:if test="${not empty sessionScope.reportMsg}">
				<script>alert("${sessionScope.reportMsg}");</script>
				<c:remove var="reportMsg" scope="session" />
			</c:if>
			<c:if test="${not empty sessionScope.likeMsg}">
				<script>alert("${sessionScope.likeMsg}");</script>
				<c:remove var="likeMsg" scope="session" />
			</c:if>

			<%-- 게시글 박스 --%>
			<div class="detail-box">

				<span class="detail-type">${boardVo.boardType}</span>
				<h3 class="detail-title">${boardVo.title}</h3>

				<div class="detail-meta">
					<span> <span class="material-symbols-outlined">person</span>
						<c:choose>
						    <c:when test="${boardVo.role eq 'ADMIN'}">
						        관리자
						    </c:when>
						    <c:otherwise>
						        ${boardVo.memberId}
						    </c:otherwise>
						</c:choose>
					</span> <span> <span class="material-symbols-outlined">calendar_today</span>
						${boardVo.createDate}
					</span> <span> <span class="material-symbols-outlined">visibility</span>
						${boardVo.viewCount}
					</span> <span> <span class="material-symbols-outlined">thumb_up</span>
						${boardVo.likeCount}
					</span>
				</div>

				<%-- 본문: CKEditor HTML 그대로 출력 --%>
				<div class="detail-content">${boardVo.content}</div>

				<%-- 첨부파일 --%>
				<c:if test="${not empty fileList}">
					<div class="detail-file">
						<h4>
							<span class="material-symbols-outlined">attach_file</span> 첨부파일
						</h4>
						<ul>
							<c:forEach var="file" items="${fileList}">
								<li><a
									href="${ctp}/board/fileDownload.do?fileId=${file.fileId}">
										<span class="material-symbols-outlined">download</span>
										${file.originFileName}
								</a></li>
							</c:forEach>
						</ul>
					</div>
				</c:if>

				<%-- 액션 버튼 --%>
				<div class="detail-actions">

					<%-- 추천 --%>
					<form action="${ctp}/board/detail.do" method="post"
						style="display: inline;">
						<input type="hidden" name="boardId" value="${boardVo.boardId}">
						<button type="submit"
							class="action-btn ${isLiked ? 'action-btn--liked' : 'action-btn--like'}">
							<span class="material-symbols-outlined">thumb_up</span> ${isLiked ? '추천 취소' : '추천'}
							${boardVo.likeCount}
						</button>
					</form>

					<%-- 신고: 본인 글이 아닐 때만 --%>
					<c:if
						test="${not empty sessionScope.loginUser
                              and sessionScope.loginUser.memberId ne boardVo.memberId}">
						<button type="button" class="action-btn action-btn--report"
							onclick="openReportModal();">
							<span class="material-symbols-outlined">flag</span> 신고
						</button>
					</c:if>

					<%-- 수정/삭제: 본인 또는 ADMIN만 --%>
					<c:if
						test="${sessionScope.loginUser.memberId eq boardVo.memberId
                              or sessionScope.loginUser.role eq 'ADMIN'}">
						<a href="${ctp}/board/update.do?boardId=${boardVo.boardId}"
							class="action-btn action-btn--edit"> <span
							class="material-symbols-outlined">edit</span> 수정
						</a>
						<a href="${ctp}/board/delete.do?boardId=${boardVo.boardId}"
							class="action-btn action-btn--delete"
							onclick="return confirm('정말 삭제하시겠습니까?');"> <span
							class="material-symbols-outlined">delete</span> 삭제
						</a>
					</c:if>
				</div>
			</div>

			<%-- 댓글 영역 --%>
			<div class="comment-box">

				<h3 class="comment-title">
					댓글 <span class="comment-count">${fn:length(commentList)}</span>
				</h3>

				<%-- 댓글 작성 폼 --%>
				<c:choose>
					<c:when test="${empty sessionScope.loginUser}">
						<div class="comment-login-notice">
							<span class="material-symbols-outlined">lock</span> <a
								href="${ctp}/login.do">로그인</a> 후 댓글을 작성할 수 있습니다.
						</div>
					</c:when>
					<c:when test="${isBlocked}">
						<div class="comment-login-notice">
							<span class="material-symbols-outlined">block</span> 정지된 계정은 댓글을
							작성할 수 없습니다.
						</div>
					</c:when>
					<c:otherwise>
						<form method="post" action="${ctp}/board/comment.do"
							class="comment-form">
							<input type="hidden" name="boardId" value="${boardVo.boardId}">
							<div class="comment-writer">
								<span class="material-symbols-outlined">person</span>
								${sessionScope.loginUser.nickname}
							</div>
							<div class="comment-input-wrap">
								<textarea name="content" rows="3" placeholder="댓글을 입력해주세요."
									required></textarea>
								<button type="submit" class="comment-submit-btn">
									<span class="material-symbols-outlined">send</span> 등록
								</button>
							</div>
						</form>
					</c:otherwise>
				</c:choose>

				<%-- 댓글 목록 --%>
				<div class="comment-list">
					<c:choose>
						<c:when test="${empty commentList}">
							<div class="comment-empty">
								<span class="material-symbols-outlined">chat_bubble_outline</span>
								<p>첫 댓글을 작성해보세요!</p>
							</div>
						</c:when>
						<c:otherwise>
							<c:forEach var="comment" items="${commentList}">
								<div
    class="comment-item ${comment.parentCommentId != null ? 'reply-item' : ''} ${param.focusCommentId == comment.commentId ? 'comment-focus' : ''}"
    id="comment-${comment.commentId}"
    data-comment-id="${comment.commentId}">

									<div class="comment-meta">
										<%-- 왼쪽: 작성자 + 날짜 + 수정버튼 --%>
										<div class="comment-meta-left">
											<span class="comment-member"> <span
												class="material-symbols-outlined">person</span>
												<c:choose>
												    <c:when test="${comment.role eq 'ADMIN'}">
												        관리자
												    </c:when>
												    <c:otherwise>
												        ${comment.memberId}
												    </c:otherwise>
												</c:choose>
											</span> <span class="comment-date">${comment.createDate}</span>

											<%-- 수정: 본인만 --%>
											<c:if
												test="${sessionScope.loginUser.memberId eq comment.memberId}">
												<button type="button" class="comment-edit-btn"
													onclick="showEdit(${comment.commentId})">
													<span class="material-symbols-outlined">edit</span>
												</button>
											</c:if>
										</div>

										<%-- 오른쪽: 신고 + 삭제 버튼 --%>
										<div class="comment-right-actions">

											<%-- 신고: 본인 댓글이 아닐 때만 --%>
											<c:if
												test="${not empty sessionScope.loginUser
                                                      and sessionScope.loginUser.memberId ne comment.memberId}">
												<button type="button" class="comment-report-btn"
													onclick="openCommentReportModal(${comment.commentId})">
													<span class="material-symbols-outlined">flag</span>
												</button>
											</c:if>

											<%-- 삭제: 본인 또는 ADMIN만 --%>
											<c:if
												test="${sessionScope.loginUser.memberId eq comment.memberId
                                                      or sessionScope.loginUser.role eq 'ADMIN'}">
												<form method="post" action="${ctp}/board/commentDelete.do"
													style="display: inline;">
													<input type="hidden" name="commentId"
														value="${comment.commentId}"> <input type="hidden"
														name="boardId" value="${comment.boardId}">
													<button type="submit" class="comment-delete-btn"
														onclick="return confirm('댓글을 삭제하시겠습니까?');">
														<span class="material-symbols-outlined">close</span>
													</button>
												</form>
											</c:if>
										</div>
									</div>

									<%-- 댓글 내용 (보기 모드) --%>
									<div id="view${comment.commentId}">
										<div class="comment-content">${comment.content}</div>
									</div>

									<c:if test="${not empty sessionScope.loginUser}">
										<button type="button" class="reply-toggle-btn"
											onclick="toggleReply(${comment.commentId})">답글</button>

										<div id="replyForm${comment.commentId}"
											class="reply-form-wrap" style="display: none;">
											<form method="post" action="${ctp}/board/comment.do">
												<input type="hidden" name="boardId"
													value="${boardVo.boardId}"> <input type="hidden"
													name="parentCommentId" value="${comment.commentId}">

												<textarea name="content" rows="2" placeholder="답글을 입력하세요"
													required></textarea>
												<button type="submit" class="reply-submit-btn">등록</button>
											</form>
										</div>
									</c:if>
								</div>

								<%-- 댓글 수정 (편집 모드) --%>
								<div id="edit${comment.commentId}" style="display: none;">
									<textarea id="txt${comment.commentId}"
										class="comment-edit-textarea">${comment.content}</textarea>
									<div class="comment-edit-actions">
										<button type="button" class="comment-cancel-btn"
											onclick="cancelEdit(${comment.commentId})">취소</button>
										<button type="button" class="comment-save-btn"
											onclick="updateComment(${comment.commentId}, ${comment.boardId})">
											저장</button>

									</div>
								</div>
							</c:forEach>
						</c:otherwise>
					</c:choose>
				</div>
			</div>

		</div>
	</main>

	<%-- 게시글 신고 모달 --%>
	<div id="reportModal" class="report-modal" style="display: none;">
		<div class="report-modal__dim" onclick="closeReportModal();"></div>
		<div class="report-modal__box">
			<h3 class="report-modal__title">게시글 신고</h3>
			<p class="report-modal__sub">신고 사유를 선택해주세요.</p>
			<form action="${ctp}/board/report.do" method="post"
				onsubmit="return validateReportForm('reportModal');">
				<input type="hidden" name="boardId" value="${boardVo.boardId}">
				<div class="report-reason-list">
					<label class="report-reason-item"> <input type="radio"
						name="reason" value="스팸/광고"><span>스팸/광고</span>
					</label> <label class="report-reason-item"> <input type="radio"
						name="reason" value="욕설/비방"><span>욕설/비방</span>
					</label> <label class="report-reason-item"> <input type="radio"
						name="reason" value="음란/부적절한 내용"><span>음란/부적절한 내용</span>
					</label> <label class="report-reason-item"> <input type="radio"
						name="reason" value="허위정보"><span>허위정보</span>
					</label> <label class="report-reason-item"> <input type="radio"
						name="reason" value="기타"><span>기타</span>
					</label>
				</div>
				<div class="report-modal__actions">
					<button type="button" class="report-cancel-btn"
						onclick="closeReportModal();">취소</button>
					<button type="submit" class="report-submit-btn">신고하기</button>
				</div>
			</form>
		</div>
	</div>

	<%-- 댓글 신고 모달 --%>
	<div id="commentReportModal" class="report-modal"
		style="display: none;">
		<div class="report-modal__dim" onclick="closeCommentReportModal();"></div>
		<div class="report-modal__box">
			<h3 class="report-modal__title">댓글 신고</h3>
			<p class="report-modal__sub">신고 사유를 선택해주세요.</p>
			<form action="${ctp}/board/commentReport.do" method="post"
				onsubmit="return validateReportForm('commentReportModal');">
				<%-- JS에서 commentId를 동적으로 세팅 --%>
				<input type="hidden" name="commentId" id="reportCommentId">
				<div class="report-reason-list">
					<label class="report-reason-item"> <input type="radio"
						name="reason" value="스팸/광고"><span>스팸/광고</span>
					</label> <label class="report-reason-item"> <input type="radio"
						name="reason" value="욕설/비방"><span>욕설/비방</span>
					</label> <label class="report-reason-item"> <input type="radio"
						name="reason" value="음란/부적절한 내용"><span>음란/부적절한 내용</span>
					</label> <label class="report-reason-item"> <input type="radio"
						name="reason" value="허위정보"><span>허위정보</span>
					</label> <label class="report-reason-item"> <input type="radio"
						name="reason" value="기타"><span>기타</span>
					</label>
				</div>
				<div class="report-modal__actions">
					<button type="button" class="report-cancel-btn"
						onclick="closeCommentReportModal();">취소</button>
					<button type="submit" class="report-submit-btn">신고하기</button>
				</div>
			</form>
		</div>
	</div>

	<jsp:include page="/view/common/footer.jsp" />

	<script>
    // ── 게시글 신고 모달 ──────────────────────────────
    function openReportModal() {
        document.getElementById('reportModal').style.display = 'block';
    }

    function closeReportModal() {
        document.getElementById('reportModal').style.display = 'none';
    }

    // ── 댓글 신고 모달 ───────────────────────────────
    function openCommentReportModal(commentId) {
        // hidden input에 commentId 동적 세팅
        document.getElementById('reportCommentId').value = commentId;
        // 라디오 초기화
        document.querySelectorAll('#commentReportModal input[type="radio"]')
                .forEach(r => r.checked = false);
        document.getElementById('commentReportModal').style.display = 'block';
    }

    function closeCommentReportModal() {
        document.getElementById('commentReportModal').style.display = 'none';
    }

    // ── 신고 공통 유효성 검사 (modalId로 구분) ────────
    function validateReportForm(modalId) {
        const checked = document.querySelector('#' + modalId + ' input[name="reason"]:checked');
        if (!checked) {
            alert('신고 사유를 선택해주세요.');
            return false;
        }
        return confirm('선택한 사유로 신고하시겠습니까?');
    }
    
 // 답글 입력창 열기/닫기 토글 함수
    function toggleReply(commentId) {
        // 해당 댓글의 답글 입력창 가져오기
        const replyForm = document.getElementById('replyForm' + commentId);

        // 요소 없으면 종료 (안전 처리)
        if (!replyForm) {
            return;
        }

        // 현재 숨겨져 있으면 보이게
        if (replyForm.style.display === 'none' || replyForm.style.display === '') {
            replyForm.style.display = 'block';
        } 
        // 보이는 상태면 다시 숨김
        else {
            replyForm.style.display = 'none';
        }
    }

    // ── 댓글 수정 토글 ────────────────────────────────
    function showEdit(id) {
        document.getElementById('view' + id).style.display = 'none';
        document.getElementById('edit' + id).style.display = 'block';
    }

    function cancelEdit(id) {
        document.getElementById('edit' + id).style.display = 'none';
        document.getElementById('view' + id).style.display = 'block';
    }

    // ── 댓글 수정 저장 (AJAX) ────────────────────────
    function updateComment(id, boardId) {
        const content = document.getElementById('txt' + id).value.trim();

        if (!content) {
            alert('내용을 입력해주세요.');
            return;
        }

        fetch('${ctp}/board/commentUpdate.do', {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: new URLSearchParams({ commentId: id, boardId: boardId, content: content })
        })
        .then(res => res.text())
        .then(r => {
            r = r.trim();
            if (r === 'success') {
                document.querySelector('#view' + id + ' .comment-content').innerText = content;
                cancelEdit(id);
            } else if (r === 'unauthorized') {
                alert('본인만 수정 가능합니다.');
            } else if (r === 'login') {
                location.href = '${ctp}/login.do';
            } else if (r === 'blocked') {
                alert('정지된 계정은 수정할 수 없습니다.');
            } else if (r === 'empty') {
                alert('내용을 입력해주세요.');
            } else {
                alert('수정에 실패했습니다.');
            }
        })
        .catch(() => alert('요청 처리 중 오류가 발생했습니다.'));
    }
    
    document.addEventListener("DOMContentLoaded", function () {
        const focusCommentId = "${param.focusCommentId}";
        if (!focusCommentId) return;

        const target = document.querySelector('[data-comment-id="' + focusCommentId + '"]');
        if (!target) return;

        target.scrollIntoView({
            behavior: "smooth",
            block: "center"
        });

        target.classList.add("comment-focus-animate");

        setTimeout(function () {
            target.classList.remove("comment-focus-animate");
        }, 2500);
    });
</script>

</body>
</html>
