<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctp" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>동물 등록 - 관리자</title>

    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700;800&family=Manrope:wght@400;500;600;700&display=swap" rel="stylesheet">
   <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@20..48,100..700,0..1,-50..200" />

    <link rel="stylesheet" href="${ctp}/view/admin/css/common/admin-layout.css">
    <link rel="stylesheet" href="${ctp}/view/admin/css/animal-register.css">
	
	<script>
    // JSP에서 JavaScript로 컨텍스트 패스 전달
    const ctp = '${ctp}';
</script>
<script defer src="${ctp}/view/admin/js/admin-layout.js"></script>
<script defer src="${ctp}/view/admin/js/animal-register.js"></script>
  
</head>
<body>
<div class="admin-layout">

    <jsp:include page="/view/admin/common/admin-sidebar.jsp" />

    <div class="admin-main-wrap">

        <jsp:include page="/view/admin/common/admin-header.jsp" />

        <main class="admin-content">
            <div class="register-container">
                
                <!-- Page Header -->
                <section class="admin-page-title">
                    <div>
                        <span class="page-badge">Management Portal</span>
                        <h1>동물 등록</h1>
                        <p>새로운 가족을 기다리는 동물의 상세 정보를 입력해주세요.</p>
                    </div>
                    <div class="admin-page-title__actions">
                        <button type="button" class="admin-btn admin-btn--gray" onclick="saveDraft()">임시 저장</button>
                        <button type="button" class="admin-btn admin-btn--primary" onclick="submitForm()">
                            <span class="material-symbols-outlined">check</span>
                            등록 완료
                        </button>
                    </div>
                </section>

                <form id="animalRegisterForm" enctype="multipart/form-data">
                    <div class="register-grid">
                        
                        <!-- Left Column: Media & Summary -->
                        <div class="register-left">
                            
                            <!-- Image Upload Section -->
                            <div class="admin-card media-upload-card">
                                <h3 class="card-title">
                                    <span class="material-symbols-outlined">photo_library</span>
                                    사진 등록
                                </h3>
                                
                                <!-- Main Image Upload -->
                                <div class="main-image-upload" id="mainImageUpload">
                                    <input type="file" id="mainImageInput" name="mainImage" 
                                           accept="image/*" style="display: none;">
                                    <span class="material-symbols-outlined">add_a_photo</span>
                                    <div class="upload-text">
                                        <p class="upload-title">메인 사진 업로드</p>
                                        <p class="upload-subtitle">최대 10MB, JPG/PNG</p>
                                    </div>
                                    <span class="main-badge">Main</span>
                                </div>

                                <!-- Additional Images -->
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

                            <!-- Registration Summary -->
                            <div class="admin-card summary-card">
                                <h3 class="summary-title">등록 요약</h3>
                                <div class="summary-list">
                                    <div class="summary-item">
                                        <span class="summary-label">등록 번호</span>
                                        <span class="summary-value" id="animalId">자동 생성</span>
                                    </div>
                                    <div class="summary-item">
                                        <span class="summary-label">담당 관리자</span>
                                        <span class="summary-value">${sessionScope.loginUser.nickname}</span>
                                    </div>
                                    <div class="summary-item">
                                        <span class="summary-label">등록 일시</span>
                                        <span class="summary-value" id="currentDate"></span>
                                    </div>
                                </div>
                            </div>

                        </div>

                        <!-- Right Column: Form Details -->
                        <div class="register-right">

                            <!-- Section 1: Basic Info -->
                            <section class="admin-card form-section">
                                <h3 class="section-title">
                                    <span class="section-bar"></span>
                                    기본 정보
                                </h3>

                                <div class="form-grid">
                                    
                                    <!-- Animal Type Toggle -->
                                    <div class="form-group full-width">
                                        <label class="form-label">동물 종류</label>
                                        <div class="type-toggle">
                                            <input type="radio" id="typeDog" name="animalType" value="DOG" checked>
                                            <label for="typeDog" class="toggle-btn active">강아지 (Dog)</label>
                                            
                                            <input type="radio" id="typeCat" name="animalType" value="CAT">
                                            <label for="typeCat" class="toggle-btn">고양이 (Cat)</label>
                                        </div>
                                    </div>

                                    <!-- Animal Name -->
                                    <div class="form-group">
                                        <label class="form-label required">동물 이름</label>
                                        <input type="text" name="animalName" class="form-input" 
                                               placeholder="예: 구름이" required>
                                    </div>

                                    <!-- Breed -->
                                    <div class="form-group">
                                        <label class="form-label">품종</label>
                                        <input type="text" name="breed" class="form-input" 
                                               placeholder="예: 골든 리트리버">
                                    </div>

                                    <!-- Gender -->
                                    <div class="form-group">
                                        <label class="form-label required">성별</label>
                                        <select name="gender" class="form-select" required>
                                            <option value="">선택</option>
                                            <option value="M">남아</option>
                                            <option value="F">여아</option>
                                        </select>
                                    </div>

                                    <!-- Age -->
                                    <div class="form-group">
                                        <label class="form-label">나이</label>
                                        <input type="number" name="age" class="form-input" 
                                               placeholder="예: 2" min="0">
                                    </div>

                                    <!-- Weight -->
                                    <div class="form-group">
                                        <label class="form-label">체중 (kg)</label>
                                        <input type="number" name="weight" class="form-input" 
                                               placeholder="예: 5.4" step="0.1" min="0">
                                    </div>

                                    <!-- Rescue Date -->
                                    <div class="form-group">
                                        <label class="form-label required">구조 날짜</label>
                                        <input type="date" name="rescueDate" class="form-input" required>
                                    </div>

                                    <!-- Adoption Status -->
                                    <div class="form-group">
                                        <label class="form-label">입양 상태</label>
                                        <select name="adoptionStatus" class="form-select">
                                             <option value="입양가능" selected>입양 가능</option>
   											 <option value="입양검토중">입양 검토 중</option>
  										     <option value="입양완료">입양 완료</option>
   	                                     </select>
                                    </div>

                                </div>
                            </section>

                            <!-- Section 2: Health Info -->
                            <section class="admin-card form-section">
                                <h3 class="section-title">
                                    <span class="section-bar"></span>
                                    건강 및 의료 정보
                                </h3>

                                <div class="health-toggles">
                                    
                                    <!-- Vaccination -->
                                    <div class="health-toggle-item">
                                        <div class="toggle-label">
                                            <span class="material-symbols-outlined">vaccines</span>
                                            <span>예방 접종 여부</span>
                                        </div>
                                        <label class="toggle-switch">
                                            <input type="checkbox" name="vaccination" value="Y">
                                            <span class="toggle-slider"></span>
                                        </label>
                                    </div>

                                    <!-- Neutered -->
                                    <div class="health-toggle-item">
                                        <div class="toggle-label">
                                            <span class="material-symbols-outlined">content_cut</span>
                                            <span>중성화 여부</span>
                                        </div>
                                        <label class="toggle-switch">
                                            <input type="checkbox" name="neutered" value="Y">
                                            <span class="toggle-slider"></span>
                                        </label>
                                    </div>

                                    <!-- Microchip -->
                                    <div class="health-toggle-item">
                                        <div class="toggle-label">
                                            <span class="material-symbols-outlined">fmd_good</span>
                                            <span>마이크로칩 인식</span>
                                        </div>
                                        <label class="toggle-switch">
                                            <input type="checkbox" name="microchip" value="Y">
                                            <span class="toggle-slider"></span>
                                        </label>
                                    </div>

                                </div>

                                <!-- Special Note -->
                                <div class="form-group">
                                    <label class="form-label">건강 및 특이사항 상세 메모</label>
                                    <textarea name="specialNote" class="form-textarea" rows="4"
                                        placeholder="알레르기, 과거 질병 이력, 현재 복용 중인 약물, 수술 기록 등 의료 관련 특이사항을 상세히 입력해주세요."></textarea>
                                </div>

                            </section>

                            <!-- Section 3: Story & Personality -->
                            <section class="admin-card form-section">
                                <h3 class="section-title">
                                    <span class="section-bar"></span>
                                    성격 및 구조 스토리
                                </h3>

                                <div class="form-grid">
                                    
                                    <!-- Personality Tag -->
                                    <div class="form-group">
                                        <label class="form-label">성격 해시태그</label>
                                        <div class="tag-input-wrapper">
                                            <div class="tag-container" id="personalityTagContainer">
                                                <!-- 동적 생성 -->
                                            </div>
                                            <input type="text" id="personalityTagInput" class="tag-input" 
                                                   placeholder="+ 태그 입력 (예: 활발함)">
                                        </div>
                                        <input type="hidden" name="personality" id="personalityValue">
                                    </div>

                                    <!-- Size Tag (자동 계산 또는 수동 입력) -->
                                    <div class="form-group">
                                        <label class="form-label">크기 (체중 기준 자동 분류)</label>
                                        <div class="size-display" id="sizeDisplay">
                                            <span class="size-badge">소형</span>
                                            <span class="size-info">10kg 미만</span>
                                        </div>
                                    </div>

                                </div>

                                <!-- Rescue Story -->
                                <div class="form-group">
                                    <label class="form-label">구조 스토리 / 성격 설명</label>
                                    <textarea name="story" class="form-textarea" rows="6"
                                        placeholder="구조 당시의 상황, 동물의 특별한 성격, 입양 희망 가족에게 전하고 싶은 말을 정성껏 적어주세요."></textarea>
                                </div>

                            </section>

                            <!-- Mobile Actions -->
                            <div class="mobile-actions">
                                <button type="button" class="admin-btn admin-btn--primary mobile-submit">
                                    동물 등록 완료
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