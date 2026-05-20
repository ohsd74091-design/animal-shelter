<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>내 작성글 - 너와 나의 연결고리</title>

<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined" rel="stylesheet">
<link href="https://cdn.jsdelivr.net/gh/orioncactus/pretendard/dist/web/static/pretendard.css" rel="stylesheet">

<%-- 공통 CSS --%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/common.css">

<%-- 마이페이지 공통 CSS --%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/mypage/mypage.css">

<%-- 내 작성글 전용 CSS --%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/mypage/mypageMyPosts.css">

</head>
<body>

    <%-- 공통 헤더 --%>
    <jsp:include page="/view/common/header.jsp" />

    <div class="mypage-container">

        <%-- 사이드바 --%>
        <jsp:include page="/view/common/sidebarNav.jsp">
            <jsp:param name="activeMenu" value="myPosts"/>
        </jsp:include>

        <%-- 메인 콘텐츠 --%>
        <section class="mypage-content">

            <%-- 페이지 헤더 --%>
            <div class="page-header">
                <h2 class="page-title">내 작성글</h2>
                <p class="page-desc">내가 커뮤니티에 작성한 모든 글을 확인하고 관리할 수 있습니다.</p>
            </div>

            <%-- 게시글 테이블 카드 --%>
            <div class="posts-card">

                <%-- 탭 + 검색 --%>
                <div class="posts-card__toolbar">

                    <%-- 탭 (pill 버튼 방식) --%>
                    <div class="posts-tab-wrap">
                        <a href="?boardType=&page=1&keyword=${keyword}"
                           class="posts-tab ${empty currentBoardType ? 'active' : ''}">전체</a>
                        <a href="?boardType=자유&page=1&keyword=${keyword}"
                           class="posts-tab ${currentBoardType eq '자유' ? 'active' : ''}">자유게시판</a>
                        <a href="?boardType=입양후기&page=1&keyword=${keyword}"
                           class="posts-tab ${currentBoardType eq '입양후기' ? 'active' : ''}">입양후기</a>
                        <a href="?boardType=자원봉사후기&page=1&keyword=${keyword}"
                           class="posts-tab ${currentBoardType eq '자원봉사후기' ? 'active' : ''}">봉사후기</a>
                    </div>

                    <%-- 검색 --%>
                    <form class="posts-search-form"
                          action="${pageContext.request.contextPath}/mypage/myPosts.do"
                          method="get">
                        <input type="hidden" name="boardType" value="${currentBoardType}">
                        <input type="hidden" name="page" value="1">
                        <span class="material-symbols-outlined">search</span>
                        <input type="text"
                               name="keyword"
                               value="${keyword}"
                               class="posts-search-input"
                               placeholder="내 작성글 내 검색">
                    </form>
                </div>

                <%-- 테이블 --%>
                <c:choose>
                    <c:when test="${not empty postList}">
                        <div class="posts-table-wrap">
                            <table class="posts-table">
                                <thead>
                                    <tr>
                                        <th class="col-type">카테고리</th>
                                        <th class="col-title">제목</th>
                                        <th class="col-date">작성일</th>
                                        <th class="col-view">조회</th>
                                        <th class="col-like">추천</th>
                                        <th class="col-edit">수정</th>
                                        <th class="col-del">삭제</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="post" items="${postList}">
                                        <tr>
                                            <%-- 카테고리 뱃지 --%>
                                            <td class="col-type">
                                                <c:choose>
                                                    <c:when test="${post.boardType eq '자유'}">
                                                        <span class="board-type-badge board-type-badge--free">자유게시판</span>
                                                    </c:when>
                                                    <c:when test="${post.boardType eq '입양후기'}">
                                                        <span class="board-type-badge board-type-badge--adoption">입양후기</span>
                                                    </c:when>
                                                    <c:when test="${post.boardType eq '자원봉사후기'}">
                                                        <span class="board-type-badge board-type-badge--volunteer">봉사후기</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="board-type-badge board-type-badge--etc">${post.boardType}</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>

                                            <%-- 제목 + 댓글 수 --%>
                                            <td class="col-title">
                                                <a href="${pageContext.request.contextPath}/board/detail.do?boardId=${post.boardId}"
                                                   class="post-title-link">
                                                    ${post.title}
                                                    <c:if test="${post.commentCount > 0}">
                                                        <span class="comment-count">[${post.commentCount}]</span>
                                                    </c:if>
                                                </a>
                                            </td>

                                            <%-- 작성일 --%>
                                            <td class="col-date">${post.createDate}</td>

                                            <%-- 조회수 --%>
                                            <td class="col-view">
                                                <span class="material-symbols-outlined">visibility</span>
                                                ${post.viewCount}
                                            </td>

                                            <%-- 추천수 --%>
                                            <td class="col-like">
                                                <span class="material-symbols-outlined">thumb_up</span>
                                                ${post.likeCount}
                                            </td>

                                            <%-- 수정 버튼 --%>
                                            <td class="col-edit">
                                                <a href="${pageContext.request.contextPath}/board/edit.do?boardId=${post.boardId}"
                                                   class="btn-icon btn-icon--edit"
                                                   title="수정">
                                                    <span class="material-symbols-outlined">edit</span>
                                                </a>
                                            </td>

                                            <%-- 삭제 버튼 --%>
                                            <td class="col-del">
                                                <button type="button"
                                                        class="btn-icon btn-icon--del"
                                                        onclick="deletePost(${post.boardId})"
                                                        title="삭제">
                                                    <span class="material-symbols-outlined">delete</span>
                                                </button>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>

                        <%-- 하단: 총 게시글 수 + 페이지네이션 --%>
                        <div class="posts-card__footer">
                            <p class="posts-total-info">
                                총 ${totalCount}개의 게시글 중 ${startRow}-${endRow} 표시
                            </p>

                            <div class="pagination">
                                <%-- 이전 버튼 --%>
                                <c:choose>
                                    <c:when test="${currentPage > 1}">
                                        <a href="?boardType=${currentBoardType}&page=${currentPage - 1}&keyword=${keyword}"
                                           class="page-btn page-btn--arrow">
                                            <span class="material-symbols-outlined">chevron_left</span>
                                        </a>
                                    </c:when>
                                    <c:otherwise>
                                        <button class="page-btn page-btn--arrow" disabled>
                                            <span class="material-symbols-outlined">chevron_left</span>
                                        </button>
                                    </c:otherwise>
                                </c:choose>

                                <%-- 페이지 번호 --%>
                                <c:forEach begin="${startPage}" end="${endPage}" var="p">
                                    <c:choose>
                                        <c:when test="${p == currentPage}">
                                            <button class="page-btn page-btn--active">${p}</button>
                                        </c:when>
                                        <c:otherwise>
                                            <a href="?boardType=${currentBoardType}&page=${p}&keyword=${keyword}"
                                               class="page-btn">${p}</a>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>

                                <%-- 다음 버튼 --%>
                                <c:choose>
                                    <c:when test="${currentPage < totalPages}">
                                        <a href="?boardType=${currentBoardType}&page=${currentPage + 1}&keyword=${keyword}"
                                           class="page-btn page-btn--arrow">
                                            <span class="material-symbols-outlined">chevron_right</span>
                                        </a>
                                    </c:when>
                                    <c:otherwise>
                                        <button class="page-btn page-btn--arrow" disabled>
                                            <span class="material-symbols-outlined">chevron_right</span>
                                        </button>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>

                    </c:when>
                    <c:otherwise>
                        <%-- 게시글 없을 때 --%>
                        <div class="posts-empty">
                            <span class="material-symbols-outlined posts-empty__icon">article</span>
                            <p class="posts-empty__msg">작성한 게시글이 없습니다.</p>
                        </div>
                    </c:otherwise>
                </c:choose>

            </div><%-- /.posts-card --%>

            <%-- 새 글 작성 버튼 --%>
            <div class="posts-write-wrap">
                <a href="${pageContext.request.contextPath}/board/write.do"
                   class="btn-write">
                    <span class="material-symbols-outlined">edit_note</span>
                    새 글 작성하기
                </a>
            </div>

        </section><%-- /.mypage-content --%>

    </div><%-- /.mypage-container --%>

    <%-- 공통 푸터 --%>
    <jsp:include page="/view/common/footer.jsp" />

    <%-- 내 작성글 전용 JS --%>
    <script src="${pageContext.request.contextPath}/js/mypage/mypageMyPosts.js"></script>

</body>
</html>
