<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctp" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>게시글 작성</title>

<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700;800&family=Manrope:wght@400;500;600;700&display=swap" rel="stylesheet">
<link href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined" rel="stylesheet">

<link rel="stylesheet" href="${ctp}/css/common/common.css">
<link rel="stylesheet" href="${ctp}/css/board-write.css">

<%-- CKEditor5 CDN --%>
<script src="https://cdn.ckeditor.com/ckeditor5/41.4.2/classic/ckeditor.js"></script>
</head>
<body>

<jsp:include page="/view/common/header.jsp"/>

<main class="write-page">
    <div class="write-inner">

        <a href="${ctp}/board/list.do" class="back-link">
            <span class="material-symbols-outlined">arrow_back</span>
            목록으로 돌아가기
        </a>

        <h2 class="write-page-title">게시글 작성</h2>
        <p class="write-page-sub">게시판 유형, 제목, 내용을 입력해주세요.</p>

        <div class="write-box">
            <form method="post"
                  action="${ctp}/board/write.do"
                  enctype="multipart/form-data"
                  id="writeForm">

                <table class="write-table">

                    <tr>
                        <th>작성자</th>
                        <td>
                            <div class="author-display">
                                <span class="material-symbols-outlined">person</span>
                                ${sessionScope.loginUser.nickname}
                            </div>
                        </td>
                    </tr>

                    <tr>
                        <th>게시판 유형</th>
                        <td>
                            <select name="boardType">
                                <c:if test="${sessionScope.loginUser.role eq 'ADMIN'}">
                                    <option value="공지" ${param.boardType eq '공지' ? 'selected' : ''}>공지사항</option>
                                </c:if>
                                <option value="자유"       ${param.boardType eq '자유'        ? 'selected' : ''}>자유게시판</option>
                                <option value="문의"       ${param.boardType eq '문의'        ? 'selected' : ''}>문의</option>
                                <option value="입양후기"    ${param.boardType eq '입양후기'    ? 'selected' : ''}>입양후기</option>
                                <option value="자원봉사후기" ${param.boardType eq '자원봉사후기' ? 'selected' : ''}>자원봉사후기</option>
                            </select>
                        </td>
                    </tr>

                    <tr>
                        <th>제목</th>
                        <td>
                            <input type="text" name="title"
                                   placeholder="제목을 입력해주세요." required>
                        </td>
                    </tr>

                    <tr>
                        <th>내용</th>
                        <td>
                            <%-- CKEditor가 이 textarea를 에디터로 변환 --%>
                            <textarea name="content" id="content"></textarea>
                        </td>
                    </tr>

                    <tr>
                        <th>첨부파일</th>
                        <td>
                            <div class="file-upload-box">
                                <label for="uploadFile" class="file-upload-label">
                                    <span class="material-symbols-outlined">attach_file</span>
                                    파일 선택
                                </label>
                                <input type="file" name="uploadFile" id="uploadFile"
                                       multiple style="display:none;">
                                <span class="file-name-display" id="fileNameDisplay">
                                    선택된 파일 없음
                                </span>
                            </div>
                        </td>
                    </tr>

                </table>

                <div class="write-btn-area">
                    <button type="submit" class="write-btn orange-btn">
                        <span class="material-symbols-outlined">edit_note</span>
                        등록 완료
                    </button>
                    <a href="${ctp}/board/list.do" class="write-btn gray-btn">
                        취소
                    </a>
                </div>

            </form>
        </div>
    </div>
</main>

<jsp:include page="/view/common/footer.jsp"/>

<%-- JS는 board-write.js로 분리 --%>
<script>const ctp = '${ctp}';</script>
<script src="${ctp}/js/board-write.js"></script>

</body>
</html>