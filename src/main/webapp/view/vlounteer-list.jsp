<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="ctp" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>봉사 모집 게시판</title>

    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Public+Sans:wght@400;500;600;700;900&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:wght,FILL@100..700,0..1&display=swap" rel="stylesheet">

    <link rel="stylesheet" href="${ctp}/css/common/common.css">
    <link rel="stylesheet" href="${ctp}/css/volunteer-list.css">
</head>
<body class="volunteer-page">

    <jsp:include page="/view/common/header.jsp" />

    <main class="main container volunteer-main">
        <section class="page-header">
            <h2 class="page-title">함께하는 봉사활동</h2>
            <p class="page-description">
                따뜻한 손길을 기다리는 유기동물들을 위해 봉사에 참여해 주세요.
            </p>
        </section>

        <section class="toolbar">
            <form action="${ctp}/volunteer/list.do" method="get" class="toolbar-form">
                <div class="filter-group">
                    <button type="submit" name="status" value=""
                        class="chip ${empty param.status ? 'chip--active' : ''}">
                        전체보기
                    </button>

                    <button type="submit" name="status" value="모집중"
                        class="chip ${param.status eq '모집중' ? 'chip--active' : ''}">
                        신청가능
                    </button>

                    <button type="submit" name="status" value="모집마감"
                        class="chip ${param.status eq '모집마감' ? 'chip--active' : ''}">
                        신청마감
                    </button>
                </div>

                <div class="search-sort-wrap">
                    <input type="text" name="keyword" value="${param.keyword}"
                           placeholder="봉사글 검색" class="search-input">

                    <select name="sort" class="sort-select">
                        <option value="latest" ${param.sort eq 'latest' ? 'selected' : ''}>최신순</option>
                        <option value="deadline" ${param.sort eq 'deadline' ? 'selected' : ''}>마감임박순</option>
                    </select>

                    <button type="submit" class="search-btn">검색</button>
                </div>
            </form>
        </section>

        <section class="recruitment-list">
            <c:choose>
                <c:when test="${not empty recruitList}">
                    <c:forEach var="vo" items="${recruitList}">
                        <article class="recruitment-card ${vo.status eq '모집마감' ? 'recruitment-card--closed' : ''}">
                            <c:choose>
                                <c:when test="${empty vo.thumbnailImg}">
                                    <div class="recruitment-card__image"
                                         style="background-image:url('https://via.placeholder.com/300x200?text=No+Image');">
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="recruitment-card__image"
                                         style="background-image:url('${ctp}/volunteer/image?fileName=${vo.thumbnailImg}');">
                                    </div>
                                </c:otherwise>
                            </c:choose>

                            <div class="recruitment-card__content">
                                <div class="recruitment-card__top">
                                    <c:choose>
                                        <c:when test="${vo.status eq '모집중'}">
                                            <span class="status-badge status-badge--open">신청가능</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="status-badge status-badge--closed">신청마감</span>
                                        </c:otherwise>
                                    </c:choose>

                                    <h3 class="recruitment-card__title">${vo.title}</h3>

                                    <div class="meta-grid">
                                        <div class="meta-item">
                                            <span>봉사 날짜 : ${vo.volunteerDate}</span>
                                        </div>

                                        <div class="meta-item">
                                            <span>장소 : ${vo.location}</span>
                                        </div>

                                        <div class="meta-item">
                                            <span>신청 마감 : ${vo.applyDeadline}</span>
                                        </div>

                                        <div class="meta-item">
                                            <span>작성자 : ${vo.memberId}</span>
                                        </div>

                                        <div class="meta-item">
                                            <span>모집 현황 : ${vo.recruitSummary}</span>
                                        </div>
                                    </div>
                                </div>

                                <div class="recruitment-card__bottom">
                                    <a href="${ctp}/volunteer/detail.do?recruitId=${vo.recruitId}"
                                       class="button button--primary">
                                        상세보기
                                    </a>
                                </div>
                            </div>
                        </article>
                    </c:forEach>
                </c:when>

                <c:otherwise>
                    <div class="empty-box">
                        등록된 봉사 모집글이 없습니다.
                    </div>
                </c:otherwise>
            </c:choose>
        </section>

        <c:if test="${not empty pageVO}">
            <nav class="pagination">
                <c:if test="${pageVO.startPage > 1}">
                    <a href="${ctp}/volunteer/list.do?page=${pageVO.startPage - 1}&status=${param.status}&keyword=${param.keyword}&sort=${param.sort}"
                       class="pagination__button">이전</a>
                </c:if>

                <c:forEach var="i" begin="${pageVO.startPage}" end="${pageVO.endPage}">
                    <a href="${ctp}/volunteer/list.do?page=${i}&status=${param.status}&keyword=${param.keyword}&sort=${param.sort}"
                       class="pagination__button ${pageVO.currentPage eq i ? 'pagination__button--active' : ''}">
                        ${i}
                    </a>
                </c:forEach>

                <c:if test="${pageVO.endPage < pageVO.totalPage}">
                    <a href="${ctp}/volunteer/list.do?page=${pageVO.endPage + 1}&status=${param.status}&keyword=${param.keyword}&sort=${param.sort}"
                       class="pagination__button">다음</a>
                </c:if>
            </nav>
        </c:if>

        <c:if test="${not empty sessionScope.loginUser and sessionScope.loginUser.role eq 'ADMIN'}">
            <div class="floating-action">
                <a href="${ctp}/volunteer/writer.do" class="floating-action__button">
                    봉사 모집 등록하기
                </a>
            </div>
        </c:if>
    </main>

    <jsp:include page="/view/common/footer.jsp" />

    <script src="${ctp}/js/volunteer-list.js"></script>
</body>
</html>