<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"  uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="ctp" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>게시판 관리 - 관리자</title>

<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700;800&family=Manrope:wght@400;500;600;700&display=swap" rel="stylesheet">
<link href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined" rel="stylesheet">

<link rel="stylesheet" href="${ctp}/view/admin/css/common/admin-layout.css">
<link rel="stylesheet" href="${ctp}/view/admin/css/admin-board.css">
</head>

<body data-ctp="${ctp}">
<div class="admin-layout">

    <jsp:include page="/view/admin/common/admin-sidebar.jsp"/>

    <div class="admin-main-wrap">

        <jsp:include page="/view/admin/common/admin-header.jsp"/>

        <main class="admin-content">

            <section class="admin-page-title">
                <div>
                    <span class="page-badge">Communication Control</span>
                    <h1>게시판 관리</h1>
                    <p>게시글 목록 조회, 숨김/복구, 삭제 및 신고 처리를 할 수 있습니다.</p>
                </div>
            </section>

            <div class="board-stat-cards">
                <div class="stat-card">
                    <div class="stat-card__icon stat-card__icon--primary">
                        <span class="material-symbols-outlined">article</span>
                    </div>
                    <div class="stat-card__info">
                        <span class="stat-card__label">전체 게시글</span>
                        <strong class="stat-card__value">${totalCount}</strong>
                    </div>
                </div>
                <div class="stat-card">
                    <div class="stat-card__icon stat-card__icon--danger">
                        <span class="material-symbols-outlined">report</span>
                    </div>
                    <div class="stat-card__info">
                        <span class="stat-card__label">신고 대기</span>
                        <strong class="stat-card__value stat-card__value--danger">${reportedCount}</strong>
                    </div>
                </div>
                <div class="stat-card">
                    <div class="stat-card__icon stat-card__icon--warn">
                        <span class="material-symbols-outlined">visibility_off</span>
                    </div>
                    <div class="stat-card__info">
                        <span class="stat-card__label">숨김 처리</span>
                        <strong class="stat-card__value">${hiddenCount}</strong>
                    </div>
                </div>
            </div>

            <div class="board-tabs">
                <button class="board-tab ${(empty tab or tab eq 'all') ? 'active' : ''}"
                        data-tab="all">
                    <span class="material-symbols-outlined">list</span>
                    전체 게시글
                </button>
                <button class="board-tab ${tab eq 'report' ? 'active' : ''}"
                        data-tab="report">
                    <span class="material-symbols-outlined">flag</span>
                    신고 게시글
                    <c:if test="${reportedCount > 0}">
                        <span class="tab-badge">${reportedCount}</span>
                    </c:if>
                </button>
            </div>

            <div id="tab-all" class="tab-content ${(empty tab or tab eq 'all') ? 'active' : ''}">

                <form method="get" action="${ctp}/admin/board/list.do" class="board-filter-wrap">
                    <input type="hidden" name="tab" value="all">
                    <div class="filter-type-wrap">
                        <button type="submit" name="boardType" value=""
                                class="filter-btn ${empty boardType ? 'active' : ''}">전체</button>
                        <button type="submit" name="boardType" value="공지"
                                class="filter-btn ${'공지' eq boardType ? 'active' : ''}">공지</button>
                        <button type="submit" name="boardType" value="자유"
                                class="filter-btn ${'자유' eq boardType ? 'active' : ''}">자유게시판</button>
                        <button type="submit" name="boardType" value="문의"
                                class="filter-btn ${'문의' eq boardType ? 'active' : ''}">문의</button>
                        <button type="submit" name="boardType" value="입양후기"
                                class="filter-btn ${'입양후기' eq boardType ? 'active' : ''}">입양후기</button>
                        <button type="submit" name="boardType" value="자원봉사후기"
                                class="filter-btn ${'자원봉사후기' eq boardType ? 'active' : ''}">자원봉사후기</button>
                    </div>
                    <div class="filter-search-wrap">
                        <div class="search-input-box">
                            <span class="material-symbols-outlined">search</span>
                            <input type="text" name="keyword" value="${keyword}"
                                   placeholder="제목 또는 작성자 검색">
                        </div>
                        <button type="submit" class="admin-btn admin-btn--primary">검색</button>
                    </div>
                </form>

                <div class="admin-card table-card">
                    <table class="admin-table">
                        <thead>
                            <tr>
                                <th style="width:70px;">번호</th>
                                <th style="width:100px;">유형</th>
                                <th>제목</th>
                                <th style="width:110px;">작성자</th>
                                <th style="width:100px;">작성일</th>
                                <th style="width:60px;">조회</th>
                                <th style="width:80px;">상태</th>
                                <th style="width:140px;">관리</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:choose>
                                <c:when test="${empty boardList}">
                                    <tr><td colspan="8" class="table-empty">게시글이 없습니다.</td></tr>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="board" items="${boardList}">
                                        <tr class="${board.isHidden eq 'Y' ? 'row-hidden' : ''} ${board.reportCount > 0 ? 'row-reported' : ''}">
                                            <td>${board.boardId}</td>
                                            <td>
                                                <span class="type-badge" data-type="${board.boardType}">
                                                    ${board.boardType}
                                                </span>
                                            </td>
                                            <td class="title-cell">
                                                <a href="${ctp}/board/detail.do?boardId=${board.boardId}"
                                                   target="_blank">${board.title}</a>
                                                <c:if test="${board.reportCount > 0}">
                                                    <span class="report-badge">
                                                        <span class="material-symbols-outlined">flag</span>
                                                        신고 ${board.reportCount}건
                                                    </span>
                                                </c:if>
                                                <c:if test="${board.isHidden eq 'Y'}">
                                                    <span class="hidden-badge">숨김</span>
                                                </c:if>
                                            </td>
                                            <td>${board.memberId}</td>
                                            <td>${board.createDate}</td>
                                            <td>${board.viewCount}</td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${board.isHidden eq 'Y'}">
                                                        <span class="status-badge status-hidden">숨김</span>
                                                    </c:when>
                                                    <c:when test="${board.reportCount > 0}">
                                                        <span class="status-badge status-reported">신고</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="status-badge status-normal">정상</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <div class="action-group">
                                                    <c:choose>
                                                        <c:when test="${board.isHidden eq 'Y'}">
                                                            <button type="button"
                                                                    class="action-btn action-btn--restore"
                                                                    onclick="toggleHide(${board.boardId}, 'N')"
                                                                    title="복구">
                                                                <span class="material-symbols-outlined">visibility</span>
                                                            </button>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <button type="button"
                                                                    class="action-btn action-btn--hide"
                                                                    onclick="toggleHide(${board.boardId}, 'Y')"
                                                                    title="숨김">
                                                                <span class="material-symbols-outlined">visibility_off</span>
                                                            </button>
                                                        </c:otherwise>
                                                    </c:choose>
                                                    <button type="button"
                                                            class="action-btn action-btn--delete"
                                                            onclick="deleteBoard(${board.boardId})"
                                                            title="삭제">
                                                        <span class="material-symbols-outlined">delete</span>
                                                    </button>
                                                </div>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>
                        </tbody>
                    </table>
                </div>

                <div class="pagination">
                    <c:if test="${pageVO.startPage > 1}">
                        <a class="page-btn" href="?tab=all&page=${pageVO.startPage-1}&boardType=${boardType}&keyword=${keyword}">
                            <span class="material-symbols-outlined">chevron_left</span>
                        </a>
                    </c:if>
                    <c:forEach var="i" begin="${pageVO.startPage}" end="${pageVO.endPage}">
                        <a class="page-btn ${i == pageVO.currentPage ? 'active' : ''}"
                           href="?tab=all&page=${i}&boardType=${boardType}&keyword=${keyword}">${i}</a>
                    </c:forEach>
                    <c:if test="${pageVO.endPage < pageVO.totalPage}">
                        <a class="page-btn" href="?tab=all&page=${pageVO.endPage+1}&boardType=${boardType}&keyword=${keyword}">
                            <span class="material-symbols-outlined">chevron_right</span>
                        </a>
                    </c:if>
                </div>
            </div>

            <div id="tab-report" class="tab-content ${tab eq 'report' ? 'active' : ''}">

                <div class="admin-card table-card">
                    <table class="admin-table">
                        <thead>
                            <tr>
                                <th style="width:70px;">번호</th>
                                <th style="width:100px;">유형</th>
                                <th>제목</th>
                                <th style="width:110px;">작성자</th>
                                <th style="width:70px;">신고수</th>
                                <th style="width:100px;">작성일</th>
                                <th style="width:80px;">상태</th>
                                <th style="width:150px;">처리</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:choose>
                                <c:when test="${empty reportedList}">
                                    <tr><td colspan="8" class="table-empty">신고된 게시글이 없습니다.</td></tr>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="board" items="${reportedList}">
                                        <tr class="${board.isHidden eq 'Y' ? 'row-hidden' : 'row-reported'}">
                                            <td>${board.boardId}</td>
                                            <td>
                                                <span class="type-badge" data-type="${board.boardType}">
                                                    ${board.boardType}
                                                </span>
                                            </td>
                                            <td class="title-cell">
                                                <a href="${ctp}/board/detail.do?boardId=${board.boardId}"
                                                   target="_blank">${board.title}</a>
                                                <button type="button"
                                                        class="reason-btn"
                                                        onclick="openReasonModal(${board.boardId}, '${board.title}')">
                                                    <span class="material-symbols-outlined">info</span>
                                                    사유 보기
                                                </button>
                                            </td>
                                            <td>${board.memberId}</td>
                                            <td>
                                                <span class="report-count-badge">${board.reportCount}건</span>
                                            </td>
                                            <td>${board.createDate}</td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${board.isHidden eq 'Y'}">
                                                        <span class="status-badge status-hidden">숨김</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="status-badge status-reported">신고접수</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <div class="action-group">
                                                    <c:choose>
                                                        <c:when test="${board.isHidden eq 'Y'}">
                                                            <button type="button"
                                                                    class="action-btn action-btn--restore"
                                                                    onclick="toggleHide(${board.boardId}, 'N')"
                                                                    title="복구">
                                                                <span class="material-symbols-outlined">visibility</span>
                                                            </button>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <button type="button"
                                                                    class="action-btn action-btn--hide"
                                                                    onclick="toggleHide(${board.boardId}, 'Y')"
                                                                    title="숨김">
                                                                <span class="material-symbols-outlined">visibility_off</span>
                                                            </button>
                                                        </c:otherwise>
                                                    </c:choose>
                                                    <button type="button"
                                                            class="action-btn action-btn--dismiss"
                                                            onclick="dismissReport(${board.boardId})"
                                                            title="신고 기각">
                                                        <span class="material-symbols-outlined">block</span>
                                                    </button>
                                                    <button type="button"
                                                            class="action-btn action-btn--delete"
                                                            onclick="deleteBoard(${board.boardId})"
                                                            title="삭제">
                                                        <span class="material-symbols-outlined">delete</span>
                                                    </button>
                                                </div>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>
                        </tbody>
                    </table>
                </div>

                <div class="pagination">
                    <c:if test="${reportPageVO.startPage > 1}">
                        <a class="page-btn" href="?tab=report&page=${reportPageVO.startPage-1}">
                            <span class="material-symbols-outlined">chevron_left</span>
                        </a>
                    </c:if>
                    <c:forEach var="i" begin="${reportPageVO.startPage}" end="${reportPageVO.endPage}">
                        <a class="page-btn ${i == reportPageVO.currentPage ? 'active' : ''}"
                           href="?tab=report&page=${i}">${i}</a>
                    </c:forEach>
                    <c:if test="${reportPageVO.endPage < reportPageVO.totalPage}">
                        <a class="page-btn" href="?tab=report&page=${reportPageVO.endPage+1}">
                            <span class="material-symbols-outlined">chevron_right</span>
                        </a>
                    </c:if>
                </div>
            </div>

        </main>
    </div>
</div>

<div id="reasonModal" class="admin-modal" style="display:none;">
    <div class="admin-modal__dim" onclick="closeReasonModal()"></div>
    <div class="admin-modal__box">
        <div class="admin-modal__header">
            <h3 id="reasonModalTitle">신고 사유</h3>
            <button type="button" class="admin-modal__close" onclick="closeReasonModal()">
                <span class="material-symbols-outlined">close</span>
            </button>
        </div>
        <div class="admin-modal__body" id="reasonModalBody">
            <div class="loading-text">불러오는 중...</div>
        </div>
    </div>
</div>

<script src="${ctp}/view/admin/js/admin-layout.js"></script>
<script src="${ctp}/view/admin/js/Admin-board.js"></script>
</body>
</html>