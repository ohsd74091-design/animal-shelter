<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>입양 신청 내역 - 너와 나의 연결고리</title>

<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined" rel="stylesheet">
<link href="https://cdn.jsdelivr.net/gh/orioncactus/pretendard/dist/web/static/pretendard.css" rel="stylesheet">

<%-- 공통 CSS --%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/common.css">

<%-- 마이페이지 공통 CSS (사이드바 등) --%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/mypage/mypage.css">

<%-- 입양 신청 내역 전용 CSS --%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/mypage/mypageAdoptionHistory.css">

</head>
<body>

    <%-- 공통 헤더 --%>
    <jsp:include page="/view/common/header.jsp" />

    <div class="mypage-container">

        <%-- 사이드바 --%>
        <jsp:include page="/view/common/sidebarNav.jsp">
            <jsp:param name="activeMenu" value="adoptionHistory"/>
        </jsp:include>

        <%-- 메인 콘텐츠 --%>
        <section class="mypage-content">

            <%-- 페이지 헤더 --%>
            <div class="page-header">
                <h2 class="page-title">입양 신청 내역</h2>
                <p class="page-desc">회원님이 신청하신 입양 절차의 진행 상황을 투명하게 확인하실 수 있습니다.</p>
            </div>

            <%-- 탭 필터 --%>
            <div class="tab-bar">
                <c:forEach var="tab" items="${tabs}">
                    <a href="?status=${tab.value}&page=1"
                       class="tab-item ${currentStatus eq tab.value ? 'active' : ''}">
                        ${tab.label}
                    </a>
                </c:forEach>
            </div>

            <%-- 신청 목록 --%>
            <c:choose>
                <c:when test="${not empty adoptionList}">
                    <div class="adoption-list">
                        <c:forEach var="adoption" items="${adoptionList}">
                            <div class="adoption-item ${adoption.status eq '반려' ? 'is-rejected' : ''}">

                                <%-- 동물 이미지 --%>
                                <div class="adoption-item__img-wrap">
                                    <c:choose>
                                        <c:when test="${not empty adoption.mainImage}">
                                            <img
                                                src="${pageContext.request.contextPath}/images/animals/${adoption.mainImage}"
                                                alt="${adoption.animalName}"
                                                class="adoption-item__img ${adoption.status eq '반려' ? 'grayscale' : ''}"
                                                onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/images/animals/default.jpg'">
                                        </c:when>
                                        <c:otherwise>
                                            <img
                                                src="${pageContext.request.contextPath}/images/animals/default.jpg"
                                                alt="등록된 이미지 없음"
                                                class="adoption-item__img ${adoption.status eq '반려' ? 'grayscale' : ''}">
                                        </c:otherwise>
                                    </c:choose>
                                </div>

                                <%-- 신청 정보 --%>
                                <div class="adoption-item__body">
                                    <%-- 상태 뱃지 + 신청번호 --%>
                                    <div class="adoption-item__top">
                                        <span class="status-badge status-badge--${adoption.status}">
                                            ${adoption.status}
                                        </span>
                                        <span class="adoption-item__no">신청번호: ${adoption.adoptionNo}</span>
                                    </div>
                                    
                                    <%-- 동물명 --%>
                                    <h3 class="adoption-item__name">${adoption.animalName}</h3>

                                    <%-- 품종 · 성별 · 나이 --%>
                                    <p class="adoption-item__meta">
                                        ${adoption.breed} |
                                        <c:choose>
                                            <c:when test="${adoption.gender eq 'M'}">수컷</c:when>
                                            <c:otherwise>암컷</c:otherwise>
                                        </c:choose>
                                        | ${adoption.age}살
                                    </p>

                                    <%-- 신청일 --%>
                                    <div class="adoption-item__date">
                                        <span class="material-symbols-outlined">calendar_today</span>
                                        <span>신청일: <fmt:formatDate value="${adoption.applyDate}" pattern="yyyy.MM.dd"/></span>
                                    </div>

                                    <%-- 반려 사유 (반려 상태일 때만) --%>
                                    <c:if test="${adoption.status eq '반려' and not empty adoption.rejectReason}">
                                        <p class="adoption-item__reject-reason">
                                            반려 사유: ${adoption.rejectReason}
                                        </p>
                                    </c:if>
                                    
                                </div>

                                <%-- 액션 버튼 --%>
                                <div class="adoption-item__actions">
                                    <c:choose>
                                        <%-- 심사중 상태: 상세보기 + 신청 취소 --%>
                                        <c:when test="${adoption.status eq '심사중'}">
                                            <a href="${pageContext.request.contextPath}/animal/animalDetail.do?animalId=${adoption.animalId}"
                                               class="btn-detail">
                                                상세보기
                                            </a>
                                            <button type="button"
                                                    class="btn-cancel"
                                                    onclick="cancelAdoption('${adoption.adoptionId}')">
                                                신청 취소
                                            </button>
                                        </c:when>

                                        <%-- 승인 상태: 상세보기 + 승인서 발급 --%>
                                        <c:when test="${adoption.status eq '승인'}">
                                            <a href="${pageContext.request.contextPath}/animal/animalDetail.do?animalId=${adoption.animalId}"
                                               class="btn-detail">
                                                상세보기
                                            </a>
                                            <a href="${pageContext.request.contextPath}/certificate/adoption.do?adoptionId=${adoption.adoptionId}"
                                               class="btn-cert" target="_blank">
                                                승인서 발급
                                            </a>
                                        </c:when>

                                        <%-- 반려 상태: 상세보기만 --%>
                                        <c:otherwise>
                                            <a href="${pageContext.request.contextPath}/animal/animalDetail.do?animalId=${adoption.animalId}"
                                               class="btn-detail">
                                                상세보기
                                            </a>
                                        </c:otherwise>
                                    </c:choose>
                                </div>

                            </div><%-- /.adoption-item --%>
                        </c:forEach>
                    </div><%-- /.adoption-list --%>

                    <%-- 페이지네이션 --%>
                    <div class="pagination">
                        <c:choose>
                            <c:when test="${currentPage > 1}">
                                <a href="?status=${currentStatus}&page=${currentPage - 1}" class="page-btn page-btn--arrow">
                                    <span class="material-symbols-outlined">chevron_left</span>
                                </a>
                            </c:when>
                            <c:otherwise>
                                <button class="page-btn page-btn--arrow" disabled>
                                    <span class="material-symbols-outlined">chevron_left</span>
                                </button>
                            </c:otherwise>
                        </c:choose>

                        <c:forEach begin="${startPage}" end="${endPage}" var="p">
                            <c:choose>
                                <c:when test="${p == currentPage}">
                                    <button class="page-btn page-btn--active">${p}</button>
                                </c:when>
                                <c:otherwise>
                                    <a href="?status=${currentStatus}&page=${p}" class="page-btn">${p}</a>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>

                        <c:choose>
                            <c:when test="${currentPage < totalPages}">
                                <a href="?status=${currentStatus}&page=${currentPage + 1}" class="page-btn page-btn--arrow">
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

                </c:when>
                <c:otherwise>
                    <%-- 신청 내역 없을 때 --%>
                    <div class="adoption-empty">
                        <span class="material-symbols-outlined adoption-empty__icon">assignment</span>
                        <p class="adoption-empty__msg">입양 신청 내역이 없습니다.</p>
                        <a href="${pageContext.request.contextPath}/animal/animalList.do" class="adoption-empty__btn">
                            입양 동물 둘러보기
                        </a>
                    </div>
                </c:otherwise>
            </c:choose>

        </section><%-- /.mypage-content --%>

    </div><%-- /.mypage-container --%>

    <%-- 공통 푸터 --%>
    <jsp:include page="/view/common/footer.jsp" />

    <%-- 입양 신청 내역 전용 JS --%>
    <script src="${pageContext.request.contextPath}/js/mypage/mypageAdoptionHistory.js"></script>

</body>
</html>
