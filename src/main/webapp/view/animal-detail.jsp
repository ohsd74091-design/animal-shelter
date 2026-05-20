<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="ctp" value="${pageContext.request.contextPath}" />

<c:set var="animal"    value="${animalDetail.animal}" />
<c:set var="medical"   value="${animalDetail.medical}" />
<c:set var="rescue"    value="${animalDetail.rescue}" />
<c:set var="imageList" value="${animalDetail.imageList}" />
<c:set var="mainImage" value="${animalDetail.mainImage}" />
<c:set var="tagList"   value="${animalDetail.tagList}" />

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><c:if test="${not empty animal}">${animal.animalName}</c:if> 상세보기</title>

    <%-- ✅ view 폴더 기준 경로 --%>
    <link rel="stylesheet" href="${ctp}/css/common/common.css">
    <link rel="stylesheet" href="${ctp}/css/animal-detail.css">

    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700;800&family=Be+Vietnam+Pro:wght@300;400;500;600&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:wght@100..700" rel="stylesheet">

    <link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css"/>
    <script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>

    <style>
        .map-preview-wrap {
            position: relative;
            border-radius: 16px;
            overflow: hidden;
            cursor: pointer;
            height: 160px;
            margin-top: 4px;
        }
        .map-preview {
            width: 100%;
            height: 100%;
            pointer-events: none;
        }
        .map-preview__overlay {
            position: absolute;
            inset: 0;
            background: rgba(156,63,0,0);
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 8px;
            font-size: 13px;
            font-weight: 800;
            color: transparent;
            transition: all 0.25s;
            border-radius: 16px;
        }
        .map-preview__overlay .material-symbols-outlined { font-size: 20px; }
        .map-preview-wrap:hover .map-preview__overlay {
            background: rgba(156,63,0,0.55);
            color: #fff;
        }
        .shelter-map-overlay {
            position: fixed;
            inset: 0;
            background: rgba(0,0,0,0.6);
            backdrop-filter: blur(6px);
            z-index: 99999;
            display: none;
            align-items: center;
            justify-content: center;
            padding: 20px;
        }
        .shelter-map-modal {
            background: #fff;
            border-radius: 20px;
            width: 100%;
            max-width: 620px;
            overflow: hidden;
            box-shadow: 0 24px 64px rgba(0,0,0,0.25);
            animation: mapModalIn 0.28s cubic-bezier(0.34,1.56,0.64,1);
        }
        @keyframes mapModalIn {
            from { opacity:0; transform: translateY(24px) scale(0.96); }
            to   { opacity:1; transform: translateY(0) scale(1); }
        }
        .shelter-map-modal__header {
            display: flex;
            align-items: center;
            justify-content: space-between;
            padding: 20px 24px;
            border-bottom: 1px solid #f0ebe4;
        }
        .shelter-map-modal__title { display: flex; align-items: center; gap: 12px; }
        .shelter-map-modal__title .material-symbols-outlined { font-size: 26px; color: #9c3f00; }
        .shelter-map-modal__title h3 { font-size: 16px; font-weight: 800; color: #1f2937; margin: 0 0 2px; }
        .shelter-map-modal__title p  { font-size: 12px; color: #6b7280; margin: 0; }
        .shelter-map-modal__close {
            width: 36px; height: 36px; border-radius: 50%;
            border: none; background: #f5f5f5; color: #6b7280;
            display: flex; align-items: center; justify-content: center;
            cursor: pointer; transition: all 0.2s; flex-shrink: 0;
        }
        .shelter-map-modal__close:hover { background: #fee2e2; color: #ef4444; }
        .shelter-map-modal__close .material-symbols-outlined { font-size: 18px; }
        .shelter-map-modal__map { width: 100%; height: 380px; }
        .shelter-map-modal__footer {
            display: flex; align-items: center; gap: 6px;
            padding: 14px 24px; font-size: 13px; color: #6b7280;
            background: #faf8f5; border-top: 1px solid #f0ebe4;
        }
        .shelter-map-modal__footer .material-symbols-outlined { font-size: 16px; color: #9c3f00; }
    </style>
</head>
<body>

<%@ include file="/view/common/header.jsp" %>

<main class="animal-detail-page">
    <div class="animal-detail-container">
        <div class="animal-detail-layout">

            <c:if test="${empty animalDetail || empty animal}">
                <div style="padding: 50px; text-align: center;">
                    <h2>동물 정보를 불러올 수 없습니다.</h2>
                    <a href="${ctp}/animal/animalList.do" style="display:inline-block;margin-top:20px;padding:10px 20px;background:#4CAF50;color:white;text-decoration:none;border-radius:5px;">목록으로 돌아가기</a>
                </div>
            </c:if>

            <c:if test="${not empty animalDetail && not empty animal}">
            <section class="animal-main-section">

                <div class="animal-gallery-section">
                    <div class="main-image-wrap">
                        <c:choose>
                            <c:when test="${not empty mainImage && not empty mainImage.saveFileName}">
                                <img id="mainAnimalImage"
                                     src="${ctp}/animal/image?fileName=${mainImage.saveFileName}"
                                     alt="${animal.animalName}" class="main-animal-image">
                            </c:when>
                            <c:otherwise>
                                <img id="mainAnimalImage"
                                     src="${ctp}/images/default-animal.png"
                                     alt="기본 이미지" class="main-animal-image">
                            </c:otherwise>
                        </c:choose>
                        <div class="status-chip-wrap">
                            <span class="status-chip ${animal.adoptionStatus eq '입양가능' ? 'is-open' : 'is-close'}">
                                ${not empty animal.adoptionStatus ? animal.adoptionStatus : '상태 미정'}
                            </span>
                        </div>
                    </div>

                    <c:if test="${not empty imageList}">
                        <div class="thumbnail-list">
                            <c:forEach var="img" items="${imageList}">
                                <div class="thumbnail-item">
                                    <img src="${ctp}/animal/image?fileName=${img.saveFileName}"
                                         alt="${animal.animalName}" class="thumbnail-image"
                                         data-full="${ctp}/animal/image?fileName=${img.saveFileName}">
                                </div>
                            </c:forEach>
                        </div>
                    </c:if>
                </div>

                <div class="animal-info-section">
                    <header class="animal-title-area">
                        <div class="animal-meta-row">
                            <span class="animal-code">Animal ID: #${animal.animalId}</span>
                            <c:if test="${not empty rescue && not empty rescue.rescueDate}">
                                <span class="dot"></span>
                                <span class="rescue-date">${rescue.rescueDate}</span>
                            </c:if>
                        </div>
                        <h1 class="animal-name">${animal.animalName}</h1>
                        <c:if test="${not empty tagList}">
                            <div class="tag-list">
                                <c:forEach var="tag" items="${tagList}">
                                    <span class="tag-item">#${tag}</span>
                                </c:forEach>
                            </div>
                        </c:if>
                    </header>

                    <section class="info-block">
                        <h3 class="section-title">
                            <span class="section-bar"></span>구조 이야기
                        </h3>
                        <div class="section-box story-box">
                            <c:choose>
                                <c:when test="${not empty rescue && not empty rescue.story}">
                                    <p>${rescue.story}</p>
                                </c:when>
                                <c:otherwise><p>구조 이야기가 등록되지 않았습니다.</p></c:otherwise>
                            </c:choose>
                        </div>
                    </section>

                    <section class="info-block">
                        <h3 class="section-title">
                            <span class="section-bar"></span>건강 정보
                        </h3>
                        <c:choose>
                            <c:when test="${not empty medical}">
                                <div class="health-grid">
                                    <div class="health-card">
                                        <div class="health-icon blue">
                                            <span class="material-symbols-outlined">vaccines</span>
                                        </div>
                                        <div>
                                            <p class="health-label">예방접종</p>
                                            <p class="health-value">
                                                <c:choose>
                                                    <c:when test="${medical.vaccination eq 'Y'}">예방접종 완료</c:when>
                                                    <c:when test="${medical.vaccination eq 'N'}">미접종</c:when>
                                                    <c:otherwise>정보 없음</c:otherwise>
                                                </c:choose>
                                            </p>
                                        </div>
                                    </div>
                                    <div class="health-card">
                                        <div class="health-icon green">
                                            <span class="material-symbols-outlined">memory</span>
                                        </div>
                                        <div>
                                            <p class="health-label">마이크로칩</p>
                                            <p class="health-value">${medical.microchip eq 'Y' ? '삽입 완료' : '미삽입'}</p>
                                        </div>
                                    </div>
                                    <div class="health-card">
                                        <div class="health-icon red">
                                            <span class="material-symbols-outlined">monitor_heart</span>
                                        </div>
                                        <div>
                                            <p class="health-label">중성화</p>
                                            <p class="health-value">${medical.neutered eq 'Y' ? '완료' : '미완료'}</p>
                                        </div>
                                    </div>
                                    <div class="health-card">
                                        <div class="health-icon orange">
                                            <span class="material-symbols-outlined">medical_information</span>
                                        </div>
                                        <div>
                                            <p class="health-label">특이사항</p>
                                            <p class="health-value">${not empty medical.specialNote ? medical.specialNote : '없음'}</p>
                                        </div>
                                    </div>
                                </div>
                            </c:when>
                            <c:otherwise><p>건강 정보가 등록되지 않았습니다.</p></c:otherwise>
                        </c:choose>
                    </section>
                </div>
            </section>

            <aside class="animal-sidebar">
                <div class="sidebar-sticky">

                    <div class="spec-card">
                        <div class="spec-grid">
                            <div class="spec-item">
                                <p class="spec-label">품종</p>
                                <p class="spec-value">${not empty animal.breed ? animal.breed : '-'}</p>
                            </div>
                            <div class="spec-item">
                                <p class="spec-label">나이</p>
                                <p class="spec-value">${not empty animal.age ? animal.age : '-'}세</p>
                            </div>
                            <div class="spec-item">
                                <p class="spec-label">성별</p>
                                <p class="spec-value">${not empty animal.gender ? animal.gender : '-'}</p>
                            </div>
                            <div class="spec-item">
                                <p class="spec-label">몸무게</p>
                                <p class="spec-value">${animal.weight > 0 ? animal.weight : '-'}kg</p>
                            </div>
                        </div>
                        <div class="badge-row">
                            <c:if test="${not empty medical && medical.neutered eq 'Y'}">
                                <span class="mini-badge">
                                    <span class="material-symbols-outlined">check_circle</span>중성화 완료
                                </span>
                            </c:if>
                            <c:if test="${not empty animal.adoptionStatus && animal.adoptionStatus eq '입양가능'}">
                                <span class="mini-badge">
                                    <span class="material-symbols-outlined">verified_user</span>입양 가능
                                </span>
                            </c:if>
                        </div>
                    </div>

                    <%-- 반려 이력 배너 --%>
                    <c:if test="${isRejected}">
                        <div class="reject-banner">
                            <span class="material-symbols-outlined">info</span>
                            <div class="reject-banner__text">
                                <strong>이전 입양 신청이 반려되었습니다.</strong>
                                <c:if test="${not empty rejectReason}">
                                    <span>반려 사유: ${rejectReason}</span>
                                </c:if>
                            </div>
                        </div>
                    </c:if>

                    <div class="action-button-group">
                        <c:choose>
                            <c:when test="${not empty animal.adoptionStatus && animal.adoptionStatus eq '입양가능'}">
                                <c:choose>
                                    <c:when test="${alreadyApplied}">
                                        <button type="button" class="action-btn disabled" disabled>신청 완료</button>
                                    </c:when>
                                    <c:when test="${param.fromGuide eq 'true'}">
                                        <a href="${ctp}/adoption/adoptionForm.do?animalId=${animal.animalId}" class="action-btn primary">입양 신청하기</a>
                                    </c:when>
                                    <c:otherwise>
                                        <a href="${ctp}/animal/guide.do?animalId=${animal.animalId}" class="action-btn primary">입양 신청하기</a>
                                    </c:otherwise>
                                </c:choose>
                            </c:when>
                            <c:otherwise>
                                <button type="button" class="action-btn disabled" disabled>입양 불가</button>
                            </c:otherwise>
                        </c:choose>

                        <a href="${ctp}/support/list.do?animalId=${animal.animalId}" class="action-btn secondary">
                            <span class="material-symbols-outlined">chat_bubble</span>1:1 문의하기
                        </a>
                    </div>

                    <%-- 보호소 정보 --%>
                    <div class="shelter-card">
                        <h4 class="shelter-title">보호소 정보</h4>
                        <div class="shelter-info-list">
                            <div class="shelter-info-item">
                                <span class="material-symbols-outlined">location_on</span>
                                <div>
                                    <p class="shelter-name">대덕 인재 개발원</p>
                                    <p class="shelter-text">대전광역시 중구 계룡로 846</p>
                                </div>
                            </div>
                            <div class="shelter-info-item">
                                <span class="material-symbols-outlined">call</span>
                                <p class="shelter-text">041-1234-1234</p>
                            </div>
                            <div class="shelter-info-item">
                                <span class="material-symbols-outlined">schedule</span>
                                <p class="shelter-text">평일 09:00 - 18:00 (주말 휴무)</p>
                            </div>
                        </div>

                        <div class="map-preview-wrap" onclick="openShelterMap()">
                            <div id="shelterMapPreview" class="map-preview"></div>
                            <div class="map-preview__overlay">
                                <span class="material-symbols-outlined">open_in_full</span>
                                지도로 보기
                            </div>
                        </div>
                    </div>

                </div>
            </aside>
            </c:if>

        </div>
    </div>
</main>

<%-- 보호소 지도 모달 --%>
<div id="shelterMapOverlay" class="shelter-map-overlay">
    <div class="shelter-map-modal">
        <div class="shelter-map-modal__header">
            <div class="shelter-map-modal__title">
                <span class="material-symbols-outlined">location_on</span>
                <div>
                    <h3>대덕 인재 개발원</h3>
                    <p>대전광역시 중구 계룡로 846</p>
                </div>
            </div>
            <button type="button" class="shelter-map-modal__close" onclick="closeShelterMap()">
                <span class="material-symbols-outlined">close</span>
            </button>
        </div>
        <div id="shelterMap" class="shelter-map-modal__map"></div>
        <div class="shelter-map-modal__footer">
            <span class="material-symbols-outlined">call</span> 041-1234-1234
            &nbsp;|&nbsp;
            <span class="material-symbols-outlined">schedule</span> 평일 09:00 - 18:00 (주말 휴무)
        </div>
    </div>
</div>

<%@ include file="/view/common/footer.jsp" %>

<%-- ✅ 경로 수정 --%>
<script src="${ctp}/js/animal-detail.js"></script>

<script>
var SHELTER_LAT = 36.3247;
var SHELTER_LNG = 127.4210;
var shelterPreviewMap = null;
var shelterModalMap   = null;

window.addEventListener('load', function () {
    var previewEl = document.getElementById('shelterMapPreview');
    if (!previewEl) return;

    shelterPreviewMap = L.map('shelterMapPreview', {
        center: [SHELTER_LAT, SHELTER_LNG],
        zoom: 16,
        zoomControl:        false,
        dragging:           false,
        scrollWheelZoom:    false,
        doubleClickZoom:    false,
        touchZoom:          false,
        attributionControl: false
    });

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 19
    }).addTo(shelterPreviewMap);

    L.marker([SHELTER_LAT, SHELTER_LNG]).addTo(shelterPreviewMap);
});

function openShelterMap() {
    var overlay = document.getElementById('shelterMapOverlay');
    overlay.style.display = 'flex';
    document.body.style.overflow = 'hidden';

    if (shelterModalMap) {
        shelterModalMap.invalidateSize();
        return;
    }

    shelterModalMap = L.map('shelterMap').setView([SHELTER_LAT, SHELTER_LNG], 17);

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '© <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a>',
        maxZoom: 19
    }).addTo(shelterModalMap);

    L.marker([SHELTER_LAT, SHELTER_LNG])
     .addTo(shelterModalMap)
     .bindPopup('<strong>대덕 인재 개발원</strong><br>대전광역시 중구 계룡로 846')
     .openPopup();
}

function closeShelterMap() {
    document.getElementById('shelterMapOverlay').style.display = 'none';
    document.body.style.overflow = '';
}

document.getElementById('shelterMapOverlay').addEventListener('click', function (e) {
    if (e.target === this) closeShelterMap();
});
</script>

</body>
</html>
