<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="ctp" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>동물 수정 - 관리자</title>

    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700;800&family=Manrope:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@20..48,100..700,0..1,-50..200" />

    <%-- CSS는 등록 폼과 동일하게 재사용 --%>
    <link rel="stylesheet" href="${ctp}/view/admin/css/common/admin-layout.css">
    <link rel="stylesheet" href="${ctp}/view/admin/css/animal-register.css">

    <script>
        const ctp = '${ctp}';
        <%-- 기존 데이터를 JS로 전달 → animal-edit.js에서 폼 초기화에 사용 --%>
        const animalData = {
            animalId      : '${dto.animal.animalId}',
            animalType    : '${dto.animal.animalType}',
            animalName    : '${dto.animal.animalName}',
            breed         : '${dto.animal.breed}',
            gender        : '${dto.animal.gender}',
            age           : '${dto.animal.age}',
            weight        : '${dto.animal.weight}',
            adoptionStatus: '${dto.animal.adoptionStatus}',
            personality   : '${dto.animal.personality}',
            vaccination   : '${dto.medical.vaccination}',
            neutered      : '${dto.medical.neutered}',
            microchip     : '${dto.medical.microchip}',
            specialNote   : '${dto.medical.specialNote}',
            rescueDate    : '<fmt:formatDate value="${dto.rescue.rescueDate}" pattern="yyyy-MM-dd"/>',
            story         : '${dto.rescue.story}',
            <%-- 기존 메인 이미지 파일명 → JS에서 미리보기 URL 생성에 사용 --%>
            mainImage     : '${dto.mainImage.saveFileName}'
        };
    </script>
    <script defer src="${ctp}/view/admin/js/admin-layout.js"></script>
    <%-- 등록 JS가 아닌 수정 전용 JS 사용 --%>
    <script defer src="${ctp}/view/admin/js/animal-edit.js"></script>
</head>
<body>
<div class="admin-layout">

    <jsp:include page="/view/admin/common/admin-sidebar.jsp" />

    <div class="admin-main-wrap">

        <jsp:include page="/view/admin/common/admin-header.jsp" />

        <main class="admin-content">
            <div class="register-container">

                <section class="admin-page-title">
                    <div>
                        <span class="page-badge">Management Portal</span>
                        <%-- 등록과 다른 점 1: 제목 변경 --%>
                        <h1>동물 정보 수정</h1>
                        <p>등록된 동물의 정보를 수정합니다.</p>
                    </div>
                    <div class="admin-page-title__actions">
                        <button type="button" class="admin-btn admin-btn--gray" onclick="history.back()">취소</button>
                        <button type="button" class="admin-btn admin-btn--primary" onclick="submitForm()">
                            <span class="material-symbols-outlined">check</span>
                            수정 완료
                        </button>
                    </div>
                </section>

                <form id="animalRegisterForm" enctype="multipart/form-data">
                    <%-- 등록과 다른 점 2: animalId hidden input 추가 --%>
                    <input type="hidden" name="animalId" value="${dto.animal.animalId}">

                    <div class="register-grid">

                        <div class="register-left">

                            <div class="admin-card media-upload-card">
                                <h3 class="card-title">
                                    <span class="material-symbols-outlined">photo_library</span>
                                    사진 등록
                                </h3>

                                <%-- 등록과 다른 점 3: 기존 메인 이미지 미리보기 --%>
                                <div class="main-image-upload ${not empty dto.mainImage ? 'has-image' : ''}" id="mainImageUpload">
                                    <input type="file" id="mainImageInput" name="mainImage"
                                           accept="image/*" style="display: none;">
                                    <c:choose>
                                        <c:when test="${not empty dto.mainImage}">
                                            <%-- 기존 이미지가 있으면 미리보기 표시 --%>
                                            <img src="${ctp}/animal/image?fileName=${dto.mainImage.saveFileName}" alt="메인 이미지">
                                            <button type="button" class="image-remove-btn" onclick="removeMainImage()">
                                                <span class="material-symbols-outlined">close</span>
                                            </button>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="material-symbols-outlined">add_a_photo</span>
                                            <div class="upload-text">
                                                <p class="upload-title">메인 사진 업로드</p>
                                                <p class="upload-subtitle">최대 10MB, JPG/PNG</p>
                                            </div>
                                        </c:otherwise>
                                    </c:choose>
                                    <span class="main-badge">Main</span>
                                </div>

                                <div class="additional-images-grid">
                                    <div class="image-upload-slot" data-index="0">
                                        <span class="material-symbols-outlined">add</span>
                                    </div>
                                    <div class="image-upload-slot" data-index="1">
                                        <span class="material-symbols-outlined">add</span>
                                    </div>
                                    <div class="image-upload-slot" data-index="2">
                                        <span class="material-symbols-outlined">add</span>
                                    </div>
                                </div>
                                <input type="file" id="additionalImagesInput" name="additionalImages"
                                       accept="image/*" multiple style="display: none;">
                            </div>

                            <div class="admin-card summary-card">
                                <h3 class="summary-title">수정 정보</h3>
                                <div class="summary-list">
                                    <div class="summary-item">
                                        <span class="summary-label">동물 번호</span>
                                        <%-- 수정이므로 animalId 표시 --%>
                                        <span class="summary-value" id="animalId">#${dto.animal.animalId}</span>
                                    </div>
                                    <div class="summary-item">
                                        <span class="summary-label">담당 관리자</span>
                                        <span class="summary-value">${sessionScope.loginUser.nickname}</span>
                                    </div>
                                    <div class="summary-item">
                                        <span class="summary-label">수정 일시</span>
                                        <span class="summary-value" id="currentDate"></span>
                                    </div>
                                </div>
                            </div>

                        </div>

                        <div class="register-right">

                            <section class="admin-card form-section">
                                <h3 class="section-title">
                                    <span class="section-bar"></span>
                                    기본 정보
                                </h3>

                                <div class="form-grid">

                                    <div class="form-group full-width">
                                        <label class="form-label">동물 종류</label>
                                        <div class="type-toggle">
                                            <%-- 등록과 다른 점 4: 기존값으로 checked 처리 --%>
                                            <input type="radio" id="typeDog" name="animalType" value="DOG"
                                                   ${'DOG' eq dto.animal.animalType ? 'checked' : ''}>
                                            <label for="typeDog" class="toggle-btn ${'DOG' eq dto.animal.animalType ? 'active' : ''}">강아지 (Dog)</label>

                                            <input type="radio" id="typeCat" name="animalType" value="CAT"
                                                   ${'CAT' eq dto.animal.animalType ? 'checked' : ''}>
                                            <label for="typeCat" class="toggle-btn ${'CAT' eq dto.animal.animalType ? 'active' : ''}">고양이 (Cat)</label>
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <label class="form-label required">동물 이름</label>
                                        <input type="text" name="animalName" class="form-input"
                                               value="${dto.animal.animalName}" required>
                                    </div>

                                    <div class="form-group">
                                        <label class="form-label">품종</label>
                                        <input type="text" name="breed" class="form-input"
                                               value="${dto.animal.breed}">
                                    </div>

                                    <div class="form-group">
                                        <label class="form-label required">성별</label>
                                        <select name="gender" class="form-select" required>
                                            <option value="">선택</option>
                                            <option value="M" ${'M' eq dto.animal.gender ? 'selected' : ''}>남아</option>
                                            <option value="F" ${'F' eq dto.animal.gender ? 'selected' : ''}>여아</option>
                                        </select>
                                    </div>

                                    <div class="form-group">
                                        <label class="form-label">나이</label>
                                        <input type="number" name="age" class="form-input"
                                               value="${dto.animal.age}" min="0">
                                    </div>

                                    <div class="form-group">
                                        <label class="form-label">체중 (kg)</label>
                                        <input type="number" name="weight" class="form-input"
                                               value="${dto.animal.weight}" step="0.1" min="0">
                                    </div>

                                    <div class="form-group">
                                        <label class="form-label required">구조 날짜</label>
                                        <input type="date" name="rescueDate" class="form-input"
                                               value="<fmt:formatDate value='${dto.rescue.rescueDate}' pattern='yyyy-MM-dd'/>" required>
                                    </div>

                                    <div class="form-group">
                                        <label class="form-label">입양 상태</label>
                                        <select name="adoptionStatus" class="form-select">
                                            <option value="입양가능"  ${'입양가능'  eq dto.animal.adoptionStatus ? 'selected' : ''}>입양 가능</option>
                                            <option value="입양검토중" ${'입양검토중' eq dto.animal.adoptionStatus ? 'selected' : ''}>입양 검토 중</option>
                                            <option value="입양완료"  ${'입양완료'  eq dto.animal.adoptionStatus ? 'selected' : ''}>입양 완료</option>
                                        </select>
                                    </div>

                                </div>
                            </section>

                            <section class="admin-card form-section">
                                <h3 class="section-title">
                                    <span class="section-bar"></span>
                                    건강 및 의료 정보
                                </h3>

                                <div class="health-toggles">
                                    <div class="health-toggle-item">
                                        <div class="toggle-label">
                                            <span class="material-symbols-outlined">vaccines</span>
                                            <span>예방 접종 여부</span>
                                        </div>
                                        <label class="toggle-switch">
                                            <%-- 기존값이 Y면 checked --%>
                                            <input type="checkbox" name="vaccination" value="Y"
                                                   ${'Y' eq dto.medical.vaccination ? 'checked' : ''}>
                                            <span class="toggle-slider"></span>
                                        </label>
                                    </div>

                                    <div class="health-toggle-item">
                                        <div class="toggle-label">
                                            <span class="material-symbols-outlined">content_cut</span>
                                            <span>중성화 여부</span>
                                        </div>
                                        <label class="toggle-switch">
                                            <input type="checkbox" name="neutered" value="Y"
                                                   ${'Y' eq dto.medical.neutered ? 'checked' : ''}>
                                            <span class="toggle-slider"></span>
                                        </label>
                                    </div>

                                    <div class="health-toggle-item">
                                        <div class="toggle-label">
                                            <span class="material-symbols-outlined">fmd_good</span>
                                            <span>마이크로칩 인식</span>
                                        </div>
                                        <label class="toggle-switch">
                                            <input type="checkbox" name="microchip" value="Y"
                                                   ${'Y' eq dto.medical.microchip ? 'checked' : ''}>
                                            <span class="toggle-slider"></span>
                                        </label>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label class="form-label">건강 및 특이사항 상세 메모</label>
                                    <textarea name="specialNote" class="form-textarea" rows="4">${dto.medical.specialNote}</textarea>
                                </div>

                            </section>

                            <section class="admin-card form-section">
                                <h3 class="section-title">
                                    <span class="section-bar"></span>
                                    성격 및 구조 스토리
                                </h3>

                                <div class="form-grid">
                                    <div class="form-group">
                                        <label class="form-label">성격 해시태그</label>
                                        <div class="tag-input-wrapper">
                                            <div class="tag-container" id="personalityTagContainer"></div>
                                            <input type="text" id="personalityTagInput" class="tag-input"
                                                   placeholder="+ 태그 입력 (예: 활발함)">
                                        </div>
                                        <%-- 기존 personality 값 hidden에 담아둠 → JS에서 태그로 파싱 --%>
                                        <input type="hidden" name="personality" id="personalityValue"
                                               value="${dto.animal.personality}">
                                    </div>

                                    <div class="form-group">
                                        <label class="form-label">크기 (체중 기준 자동 분류)</label>
                                        <div class="size-display" id="sizeDisplay">
                                            <span class="size-badge">소형</span>
                                            <span class="size-info">10kg 미만</span>
                                        </div>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label class="form-label">구조 스토리 / 성격 설명</label>
                                    <textarea name="story" class="form-textarea" rows="6">${dto.rescue.story}</textarea>
                                </div>

                            </section>

                            <div class="mobile-actions">
                                <button type="button" class="admin-btn admin-btn--primary mobile-submit" onclick="submitForm()">
                                    수정 완료
                                </button>
                                <button type="button" class="admin-btn admin-btn--gray" onclick="history.back()">
                                    취소
                                </button>
                            </div>

                        </div>
                    </div>
                </form>
            </div>
        </main>
    </div>
</div>
</body>
</html>