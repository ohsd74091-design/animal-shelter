<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctp" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>입양 신청서 | 너와 나의 연결고리</title>

    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/gh/orioncactus/pretendard/dist/web/static/pretendard.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/common.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/adoption/adoptionForm.css">
</head>
<body>

    <%-- 공통 헤더 (window.contextPath 주입 포함) --%>
    <jsp:include page="/view/common/header.jsp" />

    <main class="adoption-main">

        <%-- 페이지 타이틀 --%>
        <div class="page-title-area layout-container">
            <span class="page-label">Adoption Application</span>
            <h1 class="page-title">입양 신청서 작성하기</h1>
            <p class="page-desc">새로운 가족을 맞이하기 위한 소중한 첫걸음입니다.</p>
        </div>

        <div class="layout-container">
            <form id="adoptionForm" class="adoption-form-grid" novalidate>

                <%-- hidden: animalId --%>
                <input type="hidden" id="animalId" name="animalId" value="${not empty animal ? animal.animalId : animalId}">

                <%-- ============ 좌측 컬럼 ============ --%>
                <div class="col-left">

                    <%-- 섹션 1: 입양 대상 동물 정보 --%>
                    <section class="form-card">
                        <div class="card-header">
                            <span class="card-bar"></span>
                            <h2>입양 대상 동물 정보</h2>
                        </div>
                        <c:choose>
                            <c:when test="${not empty animal}">
                                <div class="animal-info-row">
                                    <div class="animal-thumb">
                                        <c:choose>
                                            <c:when test="${not empty mainImage && not empty mainImage.saveFileName}">
                                				<img id="mainAnimalImage"
                                     			src="${ctp}/animal/image?fileName=${mainImage.saveFileName}"
                                     			alt="${animal.animalName}" class="main-animal-image">
                            				</c:when>
                                           
                                            <c:otherwise>
                                                <img src="${pageContext.request.contextPath}/images/no-image.png"
                                                     alt="이미지 없음">
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                    <div class="animal-meta">
                                        <div class="animal-id-label">동물 ID : #PA-${animal.animalId}</div>
                                        <div class="animal-name">${animal.animalName}</div>
                                        <span class="animal-badge">${animal.breed} &bull; ${animal.age}살</span>
                                    </div>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="animal-info-empty">
                                    <span class="material-symbols-outlined">pets</span>
                                    <p>동물 목록에서 입양 신청할 아이를 선택해주세요.</p>
                                    <a href="${pageContext.request.contextPath}/animal/animalList.do"
                                       class="btn-go-list">동물 목록 보기</a>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </section>

                    <%-- 섹션 2: 신청자 정보 (세션에서 자동 fill) --%>
                    <section class="form-card">
                        <div class="card-header">
                            <span class="card-bar"></span>
                            <h2>신청자 정보</h2>
                        </div>
                        <div class="form-grid-2">
                            <div class="form-group">
                                <label for="applicantName">이름 <span class="required">*</span></label>
                                <input type="text" id="applicantName" name="applicantName"
                                       value="${sessionScope.loginUser.memberName}"
                                       placeholder="성함을 입력해주세요" class="form-input">
                            </div>
                            <div class="form-group">
                                <label for="phone">연락처 <span class="required">*</span></label>
                                <input type="tel" id="phone" name="phone"
                                       value="${sessionScope.loginUser.phone}"
                                       placeholder="010-0000-0000" class="form-input">
                            </div>
                            <div class="form-group col-span-2">
                                <label>주소 <span class="required">*</span></label>
                                <div class="address-row">
                                    <input type="text" id="postcode" name="postcode"
                                           placeholder="우편번호" class="form-input postcode-input" readonly>
                                    <button type="button" id="btnPostcode" class="btn-postcode">주소 검색</button>
                                </div>
                                <input type="text" id="addressMain" name="addressMain"
                                       placeholder="기본 주소" class="form-input mt-8" readonly>
                                <input type="text" id="addressDetail" name="addressDetail"
                                       placeholder="상세 주소를 입력해주세요" class="form-input mt-8">
                            </div>
                            <div class="form-group">
                                <label for="job">직업 <span class="required">*</span></label>
                                <input type="text" id="job" name="job"
                                       placeholder="현재 직무 또는 학생 등" class="form-input">
                            </div>
                            <div class="form-group">
                                <label for="housingType">주거 형태 <span class="required">*</span></label>
                                <select id="housingType" name="housingType" class="form-select">
                                    <option value="">선택해주세요</option>
                                    <option value="아파트">아파트</option>
                                    <option value="연립주택">연립주택</option>
                                    <option value="단독주택">단독주택</option>
                                    <option value="기타">기타</option>
                                </select>
                            </div>
                        </div>
                    </section>

                    <%-- 섹션 3: 반려동물 경험 --%>
                    <section class="form-card">
                        <div class="card-header">
                            <span class="card-bar"></span>
                            <h2>반려동물 경험</h2>
                        </div>
                        <div class="form-group">
                            <p class="form-question">반려동물을 키워본 경험이 있으신가요? <span class="required">*</span></p>
                            <div class="radio-group">
                                <label class="radio-card">
                                    <input type="radio" name="petExperience" value="현재 키움">
                                    <span class="radio-label">현재 키우고 있습니다</span>
                                </label>
                                <label class="radio-card">
                                    <input type="radio" name="petExperience" value="과거에 키움">
                                    <span class="radio-label">과거에 키운 적 있습니다</span>
                                </label>
                                <label class="radio-card">
                                    <input type="radio" name="petExperience" value="없음">
                                    <span class="radio-label">처음입니다</span>
                                </label>
                            </div>
                        </div>
                        <div class="form-group mt-16">
                            <label for="adoptionReason">입양 동기 <span class="required">*</span></label>
                            <textarea id="adoptionReason" name="adoptionReason"
                                      class="form-textarea"
                                      placeholder="이 아이를 가족으로 맞이하고 싶은 이유를 자유롭게 적어주세요"
                                      rows="7"></textarea>
                            <div class="char-count"><span id="reasonCount">0</span>자</div>
                        </div>
                    </section>

                </div><%-- /col-left --%>

                <%-- ============ 우측 컬럼 ============ --%>
                <div class="col-right">

                    <%-- 섹션 4: 방문 상담 예약 --%>
                    <section class="form-card">
                        <div class="card-header">
                            <span class="card-bar"></span>
                            <h2>방문 상담 예약</h2>
                        </div>
                        <p class="visit-desc">
                            입양 전 아이와 직접 교감하는 시간을 가집니다.<br>
                            원하시는 방문 날짜를 선택해주세요.
                        </p>
                        <div class="form-group date-wrap">
                            <span class="material-symbols-outlined date-icon">calendar_today</span>
                            <input type="date" id="visitDate" name="visitDate" class="form-input">
                        </div>
                    </section>

                    <%-- 섹션 5: 유의사항 --%>
                    <section class="form-card notice-card">
                        <div class="card-header">
                            <span class="card-bar"></span>
                            <h2>유의사항</h2>
                        </div>
                        <ul class="notice-list">
                            <li>신청 후 담당 매니저가 2~3일 이내에 연락드립니다.</li>
                            <li>허위 정보 기재 시 신청이 취소될 수 있습니다.</li>
                            <li>동일 동물에 중복 신청은 불가합니다.</li>
                            <li>방문 상담 후 최종 입양 여부가 결정됩니다.</li>
                        </ul>
                    </section>

                    <%-- 제출 버튼 --%>
                    <div class="form-card submit-card">
                        <button type="button" id="btnSubmit" class="btn-submit">
                            입양 신청 완료하기
                        </button>
                        <p class="submit-desc">
                            신청 완료 후 담당 매니저가<br>2~3일 내로 연락을 드릴 예정입니다.
                        </p>
                    </div>

                </div><%-- /col-right --%>

            </form><%-- /adoption-form-grid --%>
        </div><%-- /layout-container --%>
    </main>

    <%-- 카카오 우편번호 API + adoptionForm.js: footer 이전에 로드, defer 제거 --%>
    <script src="https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
    <script src="${pageContext.request.contextPath}/js/adoption/adoptionForm.js"></script>

    <%-- 공통 푸터 --%>
    <jsp:include page="/view/common/footer.jsp" />

</body>
</html>
