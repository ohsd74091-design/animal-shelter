<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"  uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="ctp" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>입양 신청 관리 - 관리자</title>

<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700;800&family=Manrope:wght@400;500;600;700&display=swap" rel="stylesheet">
<link href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined" rel="stylesheet">

<link rel="stylesheet" href="${ctp}/view/admin/css/common/admin-layout.css">
<link rel="stylesheet" href="${ctp}/view/admin/css/admin-adoption.css">

<%-- Leaflet.js: 무료 오픈소스 지도 라이브러리 (키 불필요) --%>
<link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css"/>
<script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>
</head>

<body data-ctp="${ctp}">
<div class="admin-layout">

    <jsp:include page="/view/admin/common/admin-sidebar.jsp"/>

    <div class="admin-main-wrap">
        <jsp:include page="/view/admin/common/admin-header.jsp"/>

        <main class="admin-content">

            <%-- 페이지 타이틀 --%>
            <section class="admin-page-title">
                <div>
                    <span class="page-badge">Adoption Management</span>
                    <h1>입양 신청 관리</h1>
                    <p>입양 신청 목록을 조회하고 승인 또는 반려 처리합니다.</p>
                </div>
            </section>

            <%-- 통계 카드 --%>
            <div class="adp-stat-cards">
                <div class="adp-stat-card">
                    <div class="adp-stat-card__icon adp-stat-card__icon--primary">
                        <span class="material-symbols-outlined">pets</span>
                    </div>
                    <div>
                        <span class="adp-stat-card__label">전체 신청</span>
                        <strong class="adp-stat-card__value">${totalCount}</strong>
                    </div>
                </div>
                <div class="adp-stat-card">
                    <div class="adp-stat-card__icon adp-stat-card__icon--warn">
                        <span class="material-symbols-outlined">schedule</span>
                    </div>
                    <div>
                        <span class="adp-stat-card__label">심사중</span>
                        <strong class="adp-stat-card__value adp-stat-card__value--warn">${pendingCount}</strong>
                    </div>
                </div>
                <div class="adp-stat-card">
                    <div class="adp-stat-card__icon adp-stat-card__icon--success">
                        <span class="material-symbols-outlined">check_circle</span>
                    </div>
                    <div>
                        <span class="adp-stat-card__label">승인</span>
                        <strong class="adp-stat-card__value adp-stat-card__value--success">${approvedCount}</strong>
                    </div>
                </div>
                <div class="adp-stat-card">
                    <div class="adp-stat-card__icon adp-stat-card__icon--danger">
                        <span class="material-symbols-outlined">cancel</span>
                    </div>
                    <div>
                        <span class="adp-stat-card__label">반려</span>
                        <strong class="adp-stat-card__value adp-stat-card__value--danger">${rejectedCount}</strong>
                    </div>
                </div>
            </div>

            <%-- 필터 바: 서버 요청 방식 (a 태그) --%>
            <div class="adp-filter-bar">
                <div class="adp-filter-btns">
                    <a class="adp-filter-btn ${(empty statusFilter) ? 'active' : ''}"
                       href="${ctp}/admin/adoption/list.do">전체</a>
                    <a class="adp-filter-btn ${statusFilter eq '심사중' ? 'active' : ''}"
                       href="${ctp}/admin/adoption/list.do?statusFilter=심사중">
                        <span class="adp-filter-dot adp-filter-dot--warn"></span>심사중
                    </a>
                    <a class="adp-filter-btn ${statusFilter eq '승인' ? 'active' : ''}"
                       href="${ctp}/admin/adoption/list.do?statusFilter=승인">
                        <span class="adp-filter-dot adp-filter-dot--success"></span>승인
                    </a>
                    <a class="adp-filter-btn ${statusFilter eq '반려' ? 'active' : ''}"
                       href="${ctp}/admin/adoption/list.do?statusFilter=반려">
                        <span class="adp-filter-dot adp-filter-dot--danger"></span>반려
                    </a>
                </div>
                <div class="adp-search-box">
                    <span class="material-symbols-outlined">search</span>
                    <input type="text" id="adpSearchInput" placeholder="신청자 ID 또는 동물명 검색">
                </div>
            </div>

            <%-- 2단 레이아웃 --%>
            <div class="adp-layout">

                <%-- ====== 좌: 카드 목록 + 페이지네이션 ====== --%>
                <div class="adp-list-panel">

                    <%-- 카드 목록 --%>
                    <div class="adp-list-scroll" id="adpCardList">
                        <c:choose>
                            <c:when test="${empty adoptionList}">
                                <div class="adp-list-empty">
                                    <span class="material-symbols-outlined">inbox</span>
                                    <p>신청 내역이 없습니다.</p>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <c:forEach var="a" items="${adoptionList}">
                                    <div class="adp-card ${a.status eq '심사중' ? 'adp-card--pending' : ''}"
                                         data-status="${a.status}"
                                         data-search="${a.memberId} ${a.animalName}"
                                         data-adoption-id="${a.adoptionId}"
                                         onclick="showAdoptionDetail(this)">

                                        <div class="adp-card__top">
                                            <span class="adp-card__priority ${a.status eq '심사중' ? 'adp-card__priority--pending' : 'adp-card__priority--normal'}">
                                                ${a.status eq '심사중' ? 'Priority' : a.status}
                                            </span>
                                            <span class="adp-card__id">#ADP-${a.adoptionId}</span>
                                        </div>

                                        <h3 class="adp-card__name">${a.memberId}</h3>
                                        <p class="adp-card__pet">
                                            반려동물:
                                            <span class="${a.status eq '심사중' ? 'adp-card__pet--highlight' : ''}">
                                                ${a.animalName}
                                            </span>
                                        </p>

                                        <div class="adp-card__footer">
                                            <span>신청일: ${a.applyDate}</span>
                                            <span class="adp-card__status adp-card__status--${a.status eq '심사중' ? 'pending' : (a.status eq '승인' ? 'approved' : 'rejected')}">
                                                <span class="adp-card__status-dot"></span>
                                                ${a.status}
                                            </span>
                                        </div>

                                        <%-- JS에 넘길 hidden 데이터 --%>
                                        <span class="adp-data" style="display:none;"
                                              data-adoptionid="${a.adoptionId}"
                                              data-memberid="${a.memberId}"
                                              data-animalid="${a.animalId}"
                                              data-animalname="${a.animalName}"
                                              data-animaltype="${a.animalType}"
                                              data-breed="${a.breed}"
                                              data-age="${a.age}"
                                              data-personality="${a.personality}"
                                              data-mainimage="${a.mainImage}"
                                              data-visitdate="${a.visitDate}"
                                              data-applydate="${a.applyDate}"
                                              data-status="${a.status}"
                                              data-rejectreason="${a.rejectReason}"
                                              data-profileimg="${a.profileImg}"
                                              data-job="${not empty a.detail ? a.detail.job : ''}"
                                              data-housingtype="${not empty a.detail ? a.detail.housingType : ''}"
                                              data-petexperience="${not empty a.detail ? a.detail.petExperience : ''}"
                                              data-address="${not empty a.detail ? a.detail.address : ''}"
                                              data-adoptionreason="${not empty a.detail ? a.detail.adoptionReason : ''}">
                                        </span>
                                    </div>
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <%-- 페이지네이션 --%>
                    <div class="adp-pagination">
                        <%-- 이전 페이지 그룹 --%>
                        <c:if test="${pageVO.startPage > 1}">
                            <a class="adp-page-btn"
                               href="${ctp}/admin/adoption/list.do?page=${pageVO.startPage - 1}&statusFilter=${statusFilter}">
                                <span class="material-symbols-outlined">chevron_left</span>
                            </a>
                        </c:if>

                        <%-- 페이지 번호 --%>
                        <c:forEach var="i" begin="${pageVO.startPage}" end="${pageVO.endPage}">
                            <a class="adp-page-btn ${i == pageVO.currentPage ? 'active' : ''}"
                               href="${ctp}/admin/adoption/list.do?page=${i}&statusFilter=${statusFilter}">
                                ${i}
                            </a>
                        </c:forEach>

                        <%-- 다음 페이지 그룹 --%>
                        <c:if test="${pageVO.endPage < pageVO.totalPage}">
                            <a class="adp-page-btn"
                               href="${ctp}/admin/adoption/list.do?page=${pageVO.endPage + 1}&statusFilter=${statusFilter}">
                                <span class="material-symbols-outlined">chevron_right</span>
                            </a>
                        </c:if>
                    </div>

                </div><%-- end .adp-list-panel --%>

                <%-- ====== 우: 상세 패널 ====== --%>
                <div class="adp-detail-panel">

                    <%-- 기본: 아무것도 선택 안 된 상태 --%>
                    <div class="adp-detail-empty" id="adpDetailEmpty">
                        <span class="material-symbols-outlined">touch_app</span>
                        <p>왼쪽 목록에서 신청을 선택하세요.</p>
                    </div>

                    <%-- 상세 내용: JS로 채워짐 --%>
                    <div class="adp-detail-content" id="adpDetailContent" style="display:none;">

                        <%-- 헤더 --%>
                        <div class="adp-detail__header">
                            <div class="adp-detail__badges">
                                <span class="adp-priority-badge" id="dd-priorityBadge"></span>
                                <span class="adp-detail__date" id="dd-applyDate"></span>
                            </div>
                            <h2 class="adp-detail__title" id="dd-title"></h2>
                        </div>

                        <%-- 신청자 프로필 --%>
                        <div class="adp-detail__section adp-profile-card">
                            <div class="adp-profile-card__avatar">
                                <img id="dd-profileImg" src="" alt="프로필 이미지">
                            </div>
                            <div class="adp-profile-card__info">
                                <h3 id="dd-memberId"></h3>
                                <div class="adp-profile-card__tags">
                                    <span id="dd-job" class="adp-tag"></span>
                                    <span class="adp-tag">인증 회원</span>
                                </div>
                            </div>
                        </div>

                        <%-- 신청 정보 그리드 --%>
                        <div class="adp-detail__section">
                            <div class="adp-info-grid">
                                <div class="adp-info-item">
                                    <span class="adp-info-item__label">주거 형태</span>
                                    <span class="adp-info-item__value" id="dd-housingType"></span>
                                </div>
                                <div class="adp-info-item">
                                    <span class="adp-info-item__label">반려동물 경험</span>
                                    <span class="adp-info-item__value" id="dd-petExperience"></span>
                                </div>
                                <div class="adp-info-item">
                                    <span class="adp-info-item__label">방문 희망일</span>
                                    <span class="adp-info-item__value" id="dd-visitDate"></span>
                                </div>
                                <div class="adp-info-item">
                                    <span class="adp-info-item__label">주소</span>
                                    <span class="adp-info-item__value" id="dd-address"></span>
                                </div>
                            </div>
                        </div>

                        <%-- 주소 지도 섹션: 주소 있을 때만 JS에서 display:flex --%>
                        <div class="adp-detail__section adp-map-section" id="dd-mapSection" style="display:none;">
                            <h4 class="adp-detail__section-title">
                                <span class="material-symbols-outlined">location_on</span>
                                신청자 주소 위치
                            </h4>
                            <%-- 지오코딩 실패 시 경고 메시지 --%>
                            <p id="dd-mapStatus" style="display:none; font-size:12px; color:#b02500; margin:0;"></p>
                            <%-- Leaflet 지도 렌더링 영역 --%>
                            <div id="dd-map" style="width:100%; height:220px; border-radius:14px; border:1px solid #e4dbd6;"></div>
                        </div>

                        <%-- 입양 사유 --%>
                        <div class="adp-detail__section">
                            <h4 class="adp-detail__section-title">
                                <span class="material-symbols-outlined">favorite</span>
                                입양 신청 사유
                            </h4>
                            <blockquote class="adp-blockquote" id="dd-adoptionReason"></blockquote>
                        </div>

                        <%-- 동물 정보 카드 --%>
                        <div class="adp-detail__section adp-pet-card">
                            <div class="adp-pet-card__img-wrap">
                                <img id="dd-petImage" src="" alt="동물 사진">
                                <span class="adp-pet-card__badge" id="dd-petBadge"></span>
                            </div>
                            <div class="adp-pet-card__info">
                                <h3 id="dd-animalName"></h3>
                                <p id="dd-animalDesc"></p>
                                <div class="adp-pet-card__tags" id="dd-personalityTags"></div>
                                <div class="adp-pet-card__visit">
                                    <span class="material-symbols-outlined">event</span>
                                    <div>
                                        <p class="adp-pet-card__visit-label">방문 희망일</p>
                                        <p class="adp-pet-card__visit-date" id="dd-visitDatePet"></p>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <%-- 처리 액션: 심사중일 때만 표시 --%>
                        <div class="adp-detail__action" id="dd-actionArea" style="display:none;">
                            <h4 class="adp-detail__section-title">
                                <span class="material-symbols-outlined">gavel</span>
                                처리 결정
                            </h4>
                            <button class="adp-action-btn adp-action-btn--approve"
                                    onclick="updateAdoptionStatus('승인')">
                                <span class="material-symbols-outlined">check_circle</span>
                                승인하기
                            </button>
                            <div class="adp-reject-area">
                                <label class="adp-reject-label">반려 사유 입력</label>
                                <textarea id="dd-rejectReason" class="adp-reject-textarea"
                                          rows="3" placeholder="반려 사유를 입력해주세요..."></textarea>
                            </div>
                            <button class="adp-action-btn adp-action-btn--reject"
                                    onclick="updateAdoptionStatus('반려')">
                                <span class="material-symbols-outlined">cancel</span>
                                반려하기
                            </button>
                            <p class="adp-action-note">승인 시 신청자에게 방문 일정 안내 메시지가 발송됩니다.</p>
                        </div>

                        <%-- 반려 사유 표시: 반려된 경우만 --%>
                        <div class="adp-detail__reject-result" id="dd-rejectResult" style="display:none;">
                            <h4 class="adp-detail__section-title">
                                <span class="material-symbols-outlined">info</span>
                                반려 사유
                            </h4>
                            <p id="dd-rejectResultText"></p>
                        </div>

                    </div><%-- end .adp-detail-content --%>
                </div><%-- end .adp-detail-panel --%>

            </div><%-- end .adp-layout --%>

        </main>
    </div>
</div>

<script src="${ctp}/view/admin/js/admin-layout.js"></script>
<script src="${ctp}/view/admin/js/Admin-adoption.js"></script>
</body>
</html>
