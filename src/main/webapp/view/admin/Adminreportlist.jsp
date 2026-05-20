<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"  uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="ctp" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>신고 관리 - 관리자</title>

<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700;800&family=Manrope:wght@400;500;600;700&display=swap" rel="stylesheet">
<link href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined" rel="stylesheet">

<link rel="stylesheet" href="${ctp}/view/admin/css/common/admin-layout.css">
<link rel="stylesheet" href="${ctp}/view/admin/css/Admin-report.css">
<script defer src="${ctp}/view/admin/js/admin-layout.js"></script>
<style>
.rpt-detail__img {
    width: 100%;
    max-width: 420px;
    max-height: 320px;
    object-fit: cover;
    border-radius: 14px;
    border: 1px solid #e5e7eb;
    display: block;
}
</style>
</head>

<body data-ctp="${ctp}">
<div class="admin-layout">

    <jsp:include page="/view/admin/common/admin-sidebar.jsp"/>

    <div class="admin-main-wrap">

        <jsp:include page="/view/admin/common/admin-header.jsp"/>

        <main class="admin-content">

            <section class="admin-page-title">
                <div>
                    <span class="page-badge">Report Management</span>
                    <h1>신고 관리</h1>
                    <p>유기동물 제보 및 회원 신고를 처리합니다.</p>
                </div>
            </section>

            <div class="rpt-stat-cards">
                <div class="rpt-stat-card">
                    <div class="rpt-stat-card__icon rpt-stat-card__icon--animal">
                        <span class="material-symbols-outlined">pets</span>
                    </div>
                    <div>
                        <span class="rpt-stat-card__label">유기동물 제보</span>
                        <strong class="rpt-stat-card__value">${animalTotal}</strong>
                    </div>
                </div>
                <div class="rpt-stat-card">
                    <div class="rpt-stat-card__icon rpt-stat-card__icon--member">
                        <span class="material-symbols-outlined">person_alert</span>
                    </div>
                    <div>
                        <span class="rpt-stat-card__label">회원 신고</span>
                        <strong class="rpt-stat-card__value">${memberTotal}</strong>
                    </div>
                </div>
                <div class="rpt-stat-card">
                    <div class="rpt-stat-card__icon rpt-stat-card__icon--pending">
                        <span class="material-symbols-outlined">schedule</span>
                    </div>
                    <div>
                        <span class="rpt-stat-card__label">미처리</span>
                        <strong class="rpt-stat-card__value rpt-stat-card__value--danger">${pendingTotal}</strong>
                    </div>
                </div>
            </div>

            <div class="rpt-tabs">
                <button class="rpt-tab ${(empty tab or tab eq 'animal') ? 'active' : ''}" data-tab="animal">
                    <span class="material-symbols-outlined">pets</span>
                    유기동물 제보
                    <c:if test="${animalPending > 0}">
                        <span class="rpt-tab-badge">${animalPending}</span>
                    </c:if>
                </button>
                <button class="rpt-tab ${tab eq 'member' ? 'active' : ''}" data-tab="member">
                    <span class="material-symbols-outlined">person_alert</span>
                    회원 신고
                    <c:if test="${memberPending > 0}">
                        <span class="rpt-tab-badge">${memberPending}</span>
                    </c:if>
                </button>
            </div>

            <div class="rpt-layout">

                <!-- ======== 유기동물 제보 탭 ======== -->
                <div id="tab-animal" class="rpt-tab-content ${(empty tab or tab eq 'animal') ? 'active' : ''}">
                    <div class="rpt-split">

                        <div class="rpt-list-panel">
                            <div class="rpt-list-header">
                                <span>제보 목록</span>
                                <div class="rpt-filter-btns">
                                    <button class="rpt-filter-btn active" data-filter="all">전체</button>
                                    <button class="rpt-filter-btn" data-filter="N">미처리</button>
                                    <button class="rpt-filter-btn" data-filter="Y">처리완료</button>
                                </div>
                            </div>

                            <div class="rpt-list">
                                <c:choose>
                                    <c:when test="${empty animalList}">
                                        <div class="rpt-list-empty">
                                            <span class="material-symbols-outlined">pets</span>
                                            <p>제보 내역이 없습니다.</p>
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach var="r" items="${animalList}">
                                            <div class="rpt-list-item ${r.processYn eq 'Y' ? 'rpt-list-item--done' : ''}"
                                                 data-id="${r.reportId}"
                                                 data-type="animal"
                                                 onclick="showAnimalDetail(this)">
                                                <div class="rpt-list-item__top">
                                                    <span class="rpt-type-badge rpt-type-badge--animal">
                                                        ${r.animalType eq 'DOG' ? '🐶 강아지' : '🐱 고양이'}
                                                    </span>
                                                    <span class="rpt-list-item__status ${r.processYn eq 'Y' ? 'rpt-status--done' : 'rpt-status--pending'}">
                                                        ${r.processYn eq 'Y' ? '처리완료' : '미처리'}
                                                    </span>
                                                </div>
                                                <p class="rpt-list-item__location">
                                                    <span class="material-symbols-outlined">location_on</span>
                                                    ${r.location}
                                                </p>
                                                <p class="rpt-list-item__content">${r.content}</p>
                                                <div class="rpt-list-item__meta">
                                                    <span>
                                                        <span class="material-symbols-outlined">person</span>
                                                        ${empty r.memberId ? '비회원' : r.memberId}
                                                    </span>
                                                    <span>
                                                        <span class="material-symbols-outlined">calendar_today</span>
                                                        <fmt:formatDate value="${r.reportDate}" pattern="yyyy.MM.dd"/>
                                                    </span>
                                                </div>

                                                <span class="rpt-data" style="display:none;"
                                                      data-reportid="${r.reportId}"
                                                      data-animaltype="${r.animalType}"
                                                      data-location="${r.location}"
                                                      data-status="${r.status}"
                                                      data-content="${r.content}"
                                                      data-memberid="${r.memberId}"
                                                      data-reportdate="<fmt:formatDate value='${r.reportDate}' pattern='yyyy.MM.dd'/>"
                                                      data-processyn="${r.processYn}"
                                                      data-imagename="${r.imageName}"
                                                      data-replyemail="${r.replyEmail}">
                                                </span>
                                            </div>
                                        </c:forEach>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>

                        <div class="rpt-detail-panel" id="animalDetailPanel">
                            <div class="rpt-detail-empty" id="animalDetailEmpty">
                                <span class="material-symbols-outlined">pets</span>
                                <p>왼쪽 목록에서 제보를 선택하세요.</p>
                            </div>

                            <div class="rpt-detail-content" id="animalDetailContent" style="display:none;">

                                <div class="rpt-detail__header">
                                    <div>
                                        <div class="rpt-detail__badges">
                                            <span class="rpt-type-badge rpt-type-badge--animal" id="ad-animalType"></span>
                                            <span class="rpt-detail__status-badge" id="ad-processYn"></span>
                                        </div>
                                        <h3 class="rpt-detail__title" id="ad-location"></h3>
                                        <p class="rpt-detail__date" id="ad-reportDate"></p>
                                    </div>
                                </div>

                                <div class="rpt-detail__info-grid">
                                    <div class="rpt-detail__info-item">
                                        <span class="rpt-detail__info-label">제보자</span>
                                        <span class="rpt-detail__info-value" id="ad-memberId"></span>
                                    </div>
                                    <div class="rpt-detail__info-item">
                                        <span class="rpt-detail__info-label">동물 상태</span>
                                        <span class="rpt-detail__info-value" id="ad-status"></span>
                                    </div>
                                    <div class="rpt-detail__info-item">
                                        <span class="rpt-detail__info-label">결과 회신 이메일</span>
                                        <span class="rpt-detail__info-value" id="ad-replyEmail"></span>
                                    </div>
                                </div>

                                <div class="rpt-detail__section">
                                    <h4 class="rpt-detail__section-title">상세 내용</h4>
                                    <p class="rpt-detail__desc" id="ad-content"></p>
                                </div>

                                <div class="rpt-detail__section" id="ad-imageSection" style="display:none;">
                                    <h4 class="rpt-detail__section-title">첨부 사진</h4>
                                    <img id="ad-image" class="rpt-detail__img" src="" alt="제보 사진">
                                </div>

                                <div class="rpt-detail__section">
                                    <h4 class="rpt-detail__section-title">처리 결과 내용</h4>
                                    <textarea id="ad-processContent"
                                              class="rpt-process-textarea"
                                              rows="5"
                                              placeholder="신고 처리 결과를 입력해주세요. 예) 현장 확인 후 구조를 완료했습니다."></textarea>
                                </div>

                                <div class="rpt-detail__action">
                                    <h4 class="rpt-detail__section-title">
                                        <span class="material-symbols-outlined">gavel</span>
                                        처리 액션
                                    </h4>
                                    <div class="rpt-action-btns">
                                        <button class="rpt-action-btn rpt-action-btn--complete" onclick="processAnimalReport('Y')">
                                            <span class="material-symbols-outlined">check_circle</span>
                                            처리 완료
                                        </button>
                                        <button class="rpt-action-btn rpt-action-btn--reject" onclick="processAnimalReport('R')">
                                            <span class="material-symbols-outlined">cancel</span>
                                            기각
                                        </button>
                                    </div>
                                </div>

                            </div>
                        </div>
                    </div>
                </div>

                <!-- ======== 회원 신고 탭 ======== -->
                <div id="tab-member" class="rpt-tab-content ${tab eq 'member' ? 'active' : ''}">
                    <div class="rpt-split">

                        <div class="rpt-list-panel">
                            <div class="rpt-list-header">
                                <span>신고 목록</span>
                                <div class="rpt-filter-btns">
                                    <button class="rpt-filter-btn active" data-filter="all">전체</button>
                                    <button class="rpt-filter-btn" data-filter="N">미처리</button>
                                    <button class="rpt-filter-btn" data-filter="Y">처리완료</button>
                                </div>
                            </div>

                            <div class="rpt-list">
                                <c:choose>
                                    <c:when test="${empty memberList}">
                                        <div class="rpt-list-empty">
                                            <span class="material-symbols-outlined">person_alert</span>
                                            <p>신고 내역이 없습니다.</p>
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach var="r" items="${memberList}">
                                            <div class="rpt-list-item ${r.status eq 'Y' ? 'rpt-list-item--done' : ''}"
                                                 data-id="${r.reportId}"
                                                 data-type="member"
                                                 onclick="showMemberDetail(this)">
                                                <div class="rpt-list-item__top">
                                                    <span class="rpt-type-badge rpt-type-badge--member">회원 신고</span>
                                                    <span class="rpt-list-item__status ${r.status eq 'Y' ? 'rpt-status--done' : 'rpt-status--pending'}">
                                                        ${r.status eq 'Y' ? '처리완료' : '미처리'}
                                                    </span>
                                                </div>
                                                <p class="rpt-list-item__location">
                                                    <span class="material-symbols-outlined">flag</span>
                                                    ${r.reason}
                                                </p>
                                                <p class="rpt-list-item__content">${r.content}</p>
                                                <div class="rpt-list-item__meta">
                                                    <span>
                                                        <span class="material-symbols-outlined">person</span>
                                                        신고자: ${r.reporterId}
                                                    </span>
                                                    <span>
                                                        <span class="material-symbols-outlined">calendar_today</span>
                                                        <fmt:formatDate value="${r.reportDate}" pattern="yyyy.MM.dd"/>
                                                    </span>
                                                </div>

                                                <span class="rpt-data" style="display:none;"
                                                      data-reportid="${r.reportId}"
                                                      data-reporterid="${r.reporterId}"
                                                      data-targetid="${r.targetId}"
                                                      data-reason="${r.reason}"
                                                      data-content="${r.content}"
                                                      data-reportdate="<fmt:formatDate value='${r.reportDate}' pattern='yyyy.MM.dd'/>"
                                                      data-status="${r.status}"
                                                      data-replyemail="${r.replyEmail}">
                                                </span>
                                            </div>
                                        </c:forEach>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>

                        <div class="rpt-detail-panel" id="memberDetailPanel">
                            <div class="rpt-detail-empty" id="memberDetailEmpty">
                                <span class="material-symbols-outlined">person_alert</span>
                                <p>왼쪽 목록에서 신고를 선택하세요.</p>
                            </div>

                            <div class="rpt-detail-content" id="memberDetailContent" style="display:none;">

                                <div class="rpt-detail__header">
                                    <div>
                                        <div class="rpt-detail__badges">
                                            <span class="rpt-type-badge rpt-type-badge--member">회원 신고</span>
                                            <span class="rpt-detail__status-badge" id="md-status"></span>
                                        </div>
                                        <h3 class="rpt-detail__title" id="md-reason"></h3>
                                        <p class="rpt-detail__date" id="md-reportDate"></p>
                                    </div>
                                </div>

                                <div class="rpt-detail__info-grid">
                                    <div class="rpt-detail__info-item">
                                        <span class="rpt-detail__info-label">신고자</span>
                                        <span class="rpt-detail__info-value" id="md-reporterId"></span>
                                    </div>
                                    <div class="rpt-detail__info-item">
                                        <span class="rpt-detail__info-label">신고 대상</span>
                                        <span class="rpt-detail__info-value rpt-detail__info-value--danger" id="md-targetId"></span>
                                    </div>
                                    <div class="rpt-detail__info-item">
                                        <span class="rpt-detail__info-label">결과 회신 이메일</span>
                                        <span class="rpt-detail__info-value" id="md-replyEmail"></span>
                                    </div>
                                </div>

                                <div class="rpt-detail__section">
                                    <h4 class="rpt-detail__section-title">신고 내용</h4>
                                    <p class="rpt-detail__desc" id="md-content"></p>
                                </div>

                                <div class="rpt-detail__section">
                                    <h4 class="rpt-detail__section-title">처리 결과 내용</h4>
                                    <textarea id="md-processContent"
                                              class="rpt-process-textarea"
                                              rows="5"
                                              placeholder="신고 처리 결과를 입력해주세요. 예) 신고 내용을 검토 후 해당 회원에게 경고 조치했습니다."></textarea>
                                </div>

                                <div class="rpt-detail__action">
                                    <h4 class="rpt-detail__section-title">
                                        <span class="material-symbols-outlined">gavel</span>
                                        처리 액션
                                    </h4>
                                    <div class="rpt-action-btns">
                                        <button class="rpt-action-btn rpt-action-btn--complete" onclick="processMemberReport('Y')">
                                            <span class="material-symbols-outlined">check_circle</span>
                                            처리 완료
                                        </button>
                                        <button class="rpt-action-btn rpt-action-btn--reject" onclick="processMemberReport('R')">
                                            <span class="material-symbols-outlined">cancel</span>
                                            기각
                                        </button>
                                    </div>
                                </div>

                            </div>
                        </div>
                    </div>
                </div>

            </div>
        </main>
    </div>
</div>

<script src="${ctp}/view/admin/js/Admin-report.js?v=20260403_1"></script>

</body>
</html>