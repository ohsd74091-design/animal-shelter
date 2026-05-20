<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 수정</title>

<!-- header/footer에서 쓰는 폰트, 아이콘 -->
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com/"
	crossorigin="anonymous">
<link
	href="https://fonts.googleapis.com/css2?family=Public+Sans:wght@400;500;600;700;900&display=swap"
	rel="stylesheet">
<link
	href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:wght,FILL@100..700,0..1&display=swap"
	rel="stylesheet">

<!-- css -->
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/common/common.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/board-update.css">
</head>
<body>

	<%@ include file="/view/common/header.jsp"%>

	<main class="update-page">
		<div class="update-inner">

			<div class="back-link">
				<a
					href="${pageContext.request.contextPath}/board/detail.do?boardId=${boardVo.boardId}">←
					상세보기로 돌아가기</a>
			</div>

			<h2 class="update-page-title">게시글 수정</h2>
			<p class="update-page-sub">게시판 유형, 제목, 내용을 수정할 수 있습니다.</p>

			<div class="update-box">
				<form method="post"
					action="${pageContext.request.contextPath}/board/update.do"
					enctype ="multiple/form-data"></form>
					<input type="hidden" name="boardId" value="${boardVo.boardId}">

					<table class="update-table">
						<tr>
							<th>게시판 유형</th>
							<td><select name="boardType">
									<option value="공지">공지사항</option>
									<option value="리뷰">리뷰</option>
									<option value="자유">자유게시판</option>
									<option value="문의">문의</option>
									<option value="입양후기">입양후기</option>
									<option value="자원봉사후기">자원봉사후기</option>
							</select></td>
						</tr>
						<tr>
							<th>제목</th>
							<td><input type="text" name="title" value="${boardVo.title}">
							</td>
						</tr>
						<tr>
							<th>내용</th>
							<td><textarea name="content" rows="10">${boardVo.content}</textarea>
							</td>
						</tr>
						
						<tr>
						    <th>첨부파일</th>
						    <td><input type="file" name="uploadFile" multiple></td>
						</tr>
					</table>

					<div class="update-btn-area">
						<button type="submit" class="update-btn orange-btn">수정완료</button>
						<a
							href="${pageContext.request.contextPath}/board/detail.do?boardId=${boardVo.boardId}"
							class="update-btn gray-btn">취소</a>
					</div>
				</form>
			</div>

		</div>
	</main>

	<%@ include file="/view/common/footer.jsp"%>

</body>
</html>