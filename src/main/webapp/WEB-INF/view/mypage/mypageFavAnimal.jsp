<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>관심 동물 - 너와 나의 연결고리</title>

<%-- 공통 CSS --%>
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined" rel="stylesheet">
<link href="https://cdn.jsdelivr.net/gh/orioncactus/pretendard/dist/web/static/pretendard.css" rel="stylesheet">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/common.css">

<%-- 마이페이지 공통 CSS (사이드바 등 공유) --%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/mypage/mypage.css">

<%-- 관심동물 전용 CSS --%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/mypage/mypageFavAnimal.css">

</head>
<body>

    <%-- 공통 헤더 --%>
    <jsp:include page="/view/common/header.jsp" />

    <div class="mypage-container">

        <%-- 사이드바 --%>
        <jsp:include page="/view/common/sidebarNav.jsp">
            <jsp:param name="activeMenu" value="favorite"/>
        </jsp:include>

        <%-- 메인 콘텐츠 --%>
        <section class="mypage-content">

            <%-- 페이지 헤더 --%>
            <div class="page-header">
                <h2 class="page-title">관심 동물</h2>
                <p class="page-desc">찜한 소중한 친구들이 새로운 가족을 기다리고 있어요.</p>
            </div>

            <%-- 동물 카드 그리드 --%>
            <c:choose>
                <c:when test="${not empty favoriteList}">
                    <div class="fav-grid">
                        <c:forEach var="animal" items="${favoriteList}">
                            <div class="fav-card" data-animal-id="${animal.animalId}">

                                <%-- 이미지 영역 --%>
                                <div class="fav-card__img-wrap">
                                    <c:choose>
                                        <c:when test="${not empty animal.mainImage}">
                                            <img
                                                src="${pageContext.request.contextPath}/images/animals/${animal.mainImage}"
                                                alt="${animal.animalName}"
                                                class="fav-card__img"
                                                onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/images/animals/default.jpg'">
                                        </c:when>
                                        <c:otherwise>
                                            <img
                                                src="${pageContext.request.contextPath}/images/animals/default.jpg"
                                                alt="등록된 이미지 없음"
                                                class="fav-card__img">
                                        </c:otherwise>
                                    </c:choose>

                                    <%-- 찜 해제 버튼 --%>
                                    <button
                                        type="button"
                                        class="fav-card__wish-btn active"
                                        onclick="removeFavorite(this, '${animal.animalId}')"
                                        title="관심 해제">
                                        <span class="material-symbols-outlined">favorite</span>
                                    </button>
                                </div>

                                <%-- 정보 영역 --%>
                                <div class="fav-card__body">
                                    <div class="fav-card__name-row">
                                        <h3 class="fav-card__name">${animal.animalName}</h3>
                                    </div>

                                    <%-- 품종 · 나이 · 성별 --%>
                                    <p class="fav-card__meta">
                                        ${animal.breed} · ${animal.age}살 ·
                                        <c:choose>
                                            <c:when test="${animal.gender == 'M'}">수컷</c:when>
                                            <c:otherwise>암컷</c:otherwise>
                                        </c:choose>
                                    </p>

                                    <%-- 성격 태그 --%>
                                    <div class="fav-card__tags">
                                        <c:forTokens var="tag" items="${animal.personality}" delims=",">
                                            <span class="fav-tag">#${tag}</span>
                                        </c:forTokens>
                                    </div>

                                    <%-- 상세 보기 버튼 --%>
                                    <a
                                        href="${pageContext.request.contextPath}/animal/animalDetail.do?animalId=${animal.animalId}"
                                        class="fav-card__btn">
                                        자세히 보기
                                    </a>
                                </div>

                            </div><%-- /.fav-card --%>
                        </c:forEach>
                    </div><%-- /.fav-grid --%>

                    <%-- 페이지네이션 --%>
                    <div class="pagination">
                        <%-- 이전 버튼 --%>
                        <c:choose>
                            <c:when test="${currentPage > 1}">
                                <a href="?page=${currentPage - 1}" class="page-btn page-btn--arrow">
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
                                    <a href="?page=${p}" class="page-btn">${p}</a>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>

                        <%-- 다음 버튼 --%>
                        <c:choose>
                            <c:when test="${currentPage < totalPages}">
                                <a href="?page=${currentPage + 1}" class="page-btn page-btn--arrow">
                                    <span class="material-symbols-outlined">chevron_right</span>
                                </a>
                            </c:when>
                            <c:otherwise>
                                <button class="page-btn page-btn--arrow" disabled>
                                    <span class="material-symbols-outlined">chevron_right</span>
                                </button>
                            </c:otherwise>
                        </c:choose>
                    </div><%-- /.pagination --%>

                </c:when>
                <c:otherwise>
                    <%-- 관심 동물 없을 때 --%>
                    <div class="fav-empty">
                        <span class="material-symbols-outlined fav-empty__icon">favorite_border</span>
                        <p class="fav-empty__msg">아직 관심 동물이 없어요.</p>
                        <a href="${pageContext.request.contextPath}/animal/animalList.do" class="fav-empty__btn">
                            입양 동물 둘러보기
                        </a>
                    </div>
                </c:otherwise>
            </c:choose>

        </section><%-- /.mypage-content --%>

    </div><%-- /.mypage-container --%>

    <%-- 공통 푸터 --%>
    <jsp:include page="/view/common/footer.jsp" />

    <%-- 관심동물 전용 JS --%>
    <script src="${pageContext.request.contextPath}/js/mypage/mypageFavAnimal.js"></script>

</body>
</html>
