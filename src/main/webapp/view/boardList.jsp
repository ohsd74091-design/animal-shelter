<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"  uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="ctp" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>커뮤니티 게시판</title>

<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700;800&family=Manrope:wght@400;500;600;700&display=swap" rel="stylesheet">
<link href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined" rel="stylesheet">

<link rel="stylesheet" href="${ctp}/css/common/common.css">
<link rel="stylesheet" href="${ctp}/css/board-list.css">
</head>
<body>

<jsp:include page="/view/common/header.jsp"/>

<main class="board-page">
    <div class="board-inner">

        <h2 class="page-title">함께 나누는 이야기</h2>
        <p class="page-sub">유기동물 보호소의 소식과 이야기를 함께 나누는 공간입니다.</p>

        <div class="board-layout">

            <div class="board-main">

                <div class="board-search-wrap">
                    <div class="category-wrap">
                        <a href="${ctp}/board/list.do"
                           class="category-btn ${empty boardType ? 'active' : ''}">전체</a>
                        <a href="${ctp}/board/list.do?boardType=공지"
                           class="category-btn ${'공지' eq boardType ? 'active' : ''}">공지사항</a>
                        <a href="${ctp}/board/list.do?boardType=자유"
                           class="category-btn ${'자유' eq boardType ? 'active' : ''}">자유게시판</a>
                        <a href="${ctp}/board/list.do?boardType=입양후기"
                           class="category-btn ${'입양후기' eq boardType ? 'active' : ''}">입양후기</a>
                        <a href="${ctp}/board/list.do?boardType=자원봉사후기"
                           class="category-btn ${'자원봉사후기' eq boardType ? 'active' : ''}">자원봉사후기</a>
                    </div>

                    <form method="get" action="${ctp}/board/list.do" class="search-box">
                        <input type="hidden" name="boardType" value="${boardType}">
                        <input type="text" name="keyword" value="${keyword}" placeholder="검색어를 입력하세요">
                        <button type="submit">
                            <span class="material-symbols-outlined">search</span>
                        </button>
                    </form>
                </div>

                <table class="board-table">
                    <colgroup>
                        <col style="width: 80px;">
                        <col style="width: 100px;">
                        <col>
                        <col style="width: 130px;">
                        <col style="width: 100px;">
                        <col style="width: 60px;">
                        <col style="width: 60px;">
                    </colgroup>
                    <thead>
                        <tr>
                            <th>번호</th>
                            <th>유형</th>
                            <th>제목</th>
                            <th>작성자</th>
                            <th>작성일</th>
                            <th>조회</th>
                            <th>추천</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${empty boardList}">
                                <tr>
                                    <td colspan="7" class="empty-row">
                                        등록된 게시글이 없습니다.
                                    </td>
                                </tr>
                            </c:when>
                            <c:otherwise>
                                <c:forEach var="board" items="${boardList}">
                                    <tr>
                                        <td>${board.boardId}</td>
                                        <td>
                                            <span class="type-badge type-${board.boardType}">
                                                ${board.boardType}
                                            </span>
                                        </td>
                                        <td class="title-td">
                                            <a href="${ctp}/board/detail.do?boardId=${board.boardId}">
                                                ${board.title}
                                            </a>
                                        </td>
                                        <td>
                                            <div class="author-cell">
                                                <div class="author-thumb">
                                                    <c:choose>
                                                       <c:when test="${not empty board.profileImg}">
    <img src="${ctp}/member/profileImage.do?fileName=${board.profileImg}"
         alt="${board.memberId}">
</c:when>
                                                        <c:otherwise>
                                                            <span>${fn:substring(board.memberId, 0, 1)}</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </div>
                                                <span class="author-id">
                                                    <c:choose>
                                                        <c:when test="${board.role eq 'ADMIN'}">
                                                            관리자
                                                        </c:when>
                                                        <c:otherwise>
                                                            ${board.memberId}
                                                        </c:otherwise>
                                                    </c:choose>
                                                </span>
                                            </div>
                                        </td>
                                        <td>${board.createDate}</td>
                                        <td>${board.viewCount}</td>
                                        <td>${board.likeCount}</td>
                                    </tr>
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>
                    </tbody>
                </table>

                <div class="pagination">
                    <c:if test="${pageVO.startPage > 1}">
                        <a class="page-btn"
                           href="?page=${pageVO.startPage - 1}&boardType=${boardType}&keyword=${keyword}">
                            <span class="material-symbols-outlined">chevron_left</span>
                        </a>
                    </c:if>

                    <c:forEach var="i" begin="${pageVO.startPage}" end="${pageVO.endPage}">
                        <a class="page-btn ${i == pageVO.currentPage ? 'active' : ''}"
                           href="?page=${i}&boardType=${boardType}&keyword=${keyword}">
                            ${i}
                        </a>
                    </c:forEach>

                    <c:if test="${pageVO.endPage < pageVO.totalPage}">
                        <a class="page-btn"
                           href="?page=${pageVO.endPage + 1}&boardType=${boardType}&keyword=${keyword}">
                            <span class="material-symbols-outlined">chevron_right</span>
                        </a>
                    </c:if>
                </div>

                <div class="btn-area">
                    <c:choose>
                        <c:when test="${empty sessionScope.loginUser}">
                        </c:when>
                        <c:when test="${sessionScope.loginUser.status eq 'N'}">
                            <button class="write-btn" disabled
                                    style="opacity:0.4; cursor:not-allowed;"
                                    title="정지된 계정은 글쓰기가 제한됩니다.">
                                글쓰기
                            </button>
                        </c:when>
                        <c:otherwise>
                            <c:if test="${not (sessionScope.loginUser.role eq 'USER' and boardType eq '공지')}">
                                <a href="${ctp}/board/write.do" class="write-btn">
                                    <span class="material-symbols-outlined">edit_note</span>
                                    글쓰기
                                </a>
                            </c:if>
                        </c:otherwise>
                    </c:choose>
                </div>

            </div>

            <aside class="board-sidebar">
                <div class="quick-menu" id="quickMenu">

                    <div class="quick-menu-header">
                        <span class="material-symbols-outlined">local_fire_department</span>
                        이번 주 인기글
                    </div>

                    <div class="quick-tabs">
                        <button type="button" class="quick-tab active" onclick="switchTab(this, 'view')">조회순</button>
                        <button type="button" class="quick-tab" onclick="switchTab(this, 'like')">추천순</button>
                    </div>

                    <ul class="popular-list" id="tab-view">
                        <c:choose>
                            <c:when test="${empty popularList}">
                                <li class="popular-empty">인기글이 없습니다.</li>
                            </c:when>
                            <c:otherwise>
                                <c:forEach var="p" items="${popularList}" varStatus="vs">
                                    <li class="popular-item">
                                        <span class="popular-rank rank-${vs.index + 1}">${vs.index + 1}</span>
                                        <a href="${ctp}/board/detail.do?boardId=${p.boardId}"
                                           class="popular-title">${p.title}</a>
                                        <span class="popular-stat">
                                            <span class="material-symbols-outlined">visibility</span>
                                            ${p.viewCount}
                                        </span>
                                    </li>
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>
                    </ul>

                    <ul class="popular-list" id="tab-like" style="display:none;">
                        <c:choose>
                            <c:when test="${empty popularList}">
                                <li class="popular-empty">인기글이 없습니다.</li>
                            </c:when>
                            <c:otherwise>
                                <c:forEach var="p" items="${popularListByLike}" varStatus="vs">
                                    <li class="popular-item">
                                        <span class="popular-rank rank-${vs.index + 1}">${vs.index + 1}</span>
                                        <a href="${ctp}/board/detail.do?boardId=${p.boardId}"
                                           class="popular-title">${p.title}</a>
                                        <span class="popular-stat">
                                            <span class="material-symbols-outlined">thumb_up</span>
                                            ${p.likeCount}
                                        </span>
                                    </li>
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>
                    </ul>

                </div>
            </aside>

        </div>

    </div>
</main>

<jsp:include page="/view/common/footer.jsp"/>

<script>
    function switchTab(btn, tabId) {
        document.querySelectorAll('.quick-tab').forEach(t => t.classList.remove('active'));
        btn.classList.add('active');

        document.querySelectorAll('.popular-list').forEach(list => list.style.display = 'none');
        document.getElementById('tab-' + tabId).style.display = 'block';
    }
</script>

</body>
</html>