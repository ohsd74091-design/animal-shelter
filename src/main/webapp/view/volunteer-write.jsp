<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="ctp" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>봉사 신청</title>

<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700;800&family=Be+Vietnam+Pro:wght@300;400;500;600;700&display=swap" rel="stylesheet">
<link href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined" rel="stylesheet">

<link rel="stylesheet" href="${ctp}/css/common/common.css">
<link rel="stylesheet" href="${ctp}/css/volunteer-write.css">
</head>

<body class="volunteer-apply-page">

<jsp:include page="/view/common/header.jsp"/>

<main class="main apply-main">
    <section class="apply-section">
        <div class="container container--narrow">
            <div class="apply-card">
                <div class="section-header">
                    <h2 class="section-title">봉사 신청</h2>
                    <p class="section-description">관심 분야를 선택하고 신청 내용을 작성해주세요.</p>
                </div>

                <form action="${ctp}/volunteer/apply.do" method="post" id="applyForm" class="apply-form">
                    <input type="hidden" name="recruitId" value="${recruit.recruitId}">

                    <div class="form-grid">
                        <div class="form-field">
                            <label class="form-label">모집번호</label>
                            <input type="text" class="form-input form-input--disabled" value="${recruit.recruitId}" disabled>
                        </div>

                        <div class="form-field">
                            <label class="form-label">이름</label>
                            <input type="text" class="form-input form-input--disabled" value="${sessionScope.loginUser.memberName}" disabled>
                        </div>
                    </div>

                    <div class="form-field">
                        <label class="form-label">연락처</label>
                        <input type="text" name="phone" class="form-input" value="${sessionScope.loginUser.phone}" required>
                    </div>

                    <div class="form-field">
                        <label class="form-label">관심 분야</label>

                        <div class="interest-grid">
                            <c:choose>
                                <c:when test="${not empty detailList}">
                                    <c:forEach var="dvo" items="${detailList}">
                                        <label class="select-card">
                                            <input type="radio"
                                                   name="interestType"
                                                   value="${dvo.interestType}"
                                                   class="select-card__input"
                                                   ${dvo.applyCount >= dvo.capacity ? 'disabled' : ''}
                                                   required>

                                            <div class="select-card__box">
                                                <div class="select-card__content">
                                                    <p class="select-card__title">${dvo.interestType}</p>
                                                    <p class="select-card__text">${dvo.applyCount} / ${dvo.capacity}명</p>
                                                </div>
                                            </div>
                                        </label>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <div class="empty-box">신청 가능한 분야 정보가 없습니다.</div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>

                    <div class="form-field">
                        <label class="form-label">신청 동기</label>
                        <textarea name="applyReason" class="form-textarea" required></textarea>
                    </div>

                    <div class="agreement">
                        <label class="agreement__label">
                            <input type="checkbox" name="agreeYn" class="agreement__checkbox" required>
                            <span>동의합니다</span>
                        </label>
                    </div>
                    <div class="submit-wrap">
    <c:choose>
        <c:when test="${recruit.status eq '모집중'}">
            <button type="submit" class="submit-button">신청하기</button>
        </c:when>
        <c:otherwise>
            <button type="button" class="submit-button" disabled>모집 마감</button>
        </c:otherwise>
    </c:choose>
</div>
                </form>
            </div>
        </div>
    </section>
</main>

<jsp:include page="/view/common/footer.jsp"/>

<script src="${ctp}/js/volunteer-apply.js"></script>
</body>
</html>