<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="ctp" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>봉사 모집 등록</title>

    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link
        href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700;800&family=Be+Vietnam+Pro:wght@300;400;500;600&display=swap"
        rel="stylesheet">
    <link
        href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:wght,FILL@100..700,0..1&display=swap"
        rel="stylesheet">

    <link rel="stylesheet" href="${ctp}/css/common/common.css">
    <link rel="stylesheet" href="${ctp}/css/volunteer-admin.css">
</head>
<body class="volunteer-write-page">

    <jsp:include page="/view/common/header.jsp" />

    <main class="main volunteer-write-main">
        <div class="container volunteer-write-container">

            <section class="page-intro">
                <div class="page-intro__badge">
                    <span class="material-symbols-outlined page-intro__badge-icon">volunteer_activism</span>
                    <span>VOLUNTEER MANAGEMENT</span>
                </div>

                <h1 class="page-title">봉사 모집 등록</h1>
                <p class="page-description">
                    따뜻한 손길이 필요한 아이들을 위해 새로운 봉사 활동을 기획하고 모집 공고를 게시하세요.
                    관리자님의 정성이 아이들의 행복이 됩니다.
                </p>
            </section>

            <section class="write-card">
                <div class="write-card__accent"></div>

                <form class="write-form"
                      action="${ctp}/volunteer/writer.do"
                      method="post"
                      enctype="multipart/form-data">

                    <div class="form-section">
                        <label class="form-label">대표 이미지 (썸네일)</label>

                        <div class="thumbnail-grid">
                            <label class="upload-box">
                                <input type="file"
                                       name="thumbnailFile"
                                       accept="image/*"
                                       class="upload-box__input"
                                       id="thumbnailFile">

                                <div class="upload-box__content">
                                    <div class="upload-box__icon">
                                        <span class="material-symbols-outlined">add_photo_alternate</span>
                                    </div>
                                    <div class="upload-box__text">
                                        <p class="upload-box__title">클릭하여 썸네일 이미지를 업로드하세요</p>
                                        <p class="upload-box__desc">PNG, JPG, GIF (최대 5MB)</p>
                                    </div>
                                </div>
                            </label>

                            <div class="preview-box" id="previewBox">
                                <img id="previewImage" class="preview-box__image" alt="썸네일 미리보기">
                                <div class="preview-box__placeholder" id="previewPlaceholder">
                                    <span class="material-symbols-outlined preview-box__icon">image</span>
                                    <span class="preview-box__label">PREVIEW</span>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="form-divider"></div>

                    <div class="form-section">
                        <label for="title" class="form-label">모집 제목</label>
                        <input
                            id="title"
                            name="title"
                            type="text"
                            class="form-input form-input--large"
                            placeholder="예: [주말] 유기견 산책 및 견사 청소 봉사자 모집"
                            required>
                    </div>

                    <div class="form-grid form-grid--2col">
                        <div class="form-section">
                            <label for="volunteerDate" class="form-label">봉사 날짜</label>
                            <div class="input-with-icon">
    <input id="volunteerDate"
           name="volunteerDate"
           type="date"
           class="form-input"
           required>
    <span class="material-symbols-outlined input-with-icon__icon date-trigger"
          data-target="volunteerDate">calendar_today</span>
</div>
                        </div>

                        <div class="form-section">
                            <label for="applyDeadline" class="form-label">신청 마감일</label>
                            <div class="input-with-icon">
    <input id="applyDeadline"
           name="applyDeadline"
           type="date"
           class="form-input"
           required>
    <span class="material-symbols-outlined input-with-icon__icon date-trigger"
          data-target="applyDeadline">event_busy</span>
</div>
                        </div>
                    </div>
<div class="form-section">
    <label class="form-label">장소</label>

    <!-- 최종 저장용 -->
    <input type="hidden" id="location" name="location" required>

    <div class="form-grid form-grid--2col">
        <div>
            <label for="postcode" class="form-label">우편번호</label>
            <div class="input-with-icon">
                <input
                    id="postcode"
                    name="postcode"
                    type="text"
                    class="form-input"
                    placeholder="우편번호"
                    readonly>
            </div>
        </div>

        <div style="display:flex; align-items:end; gap:10px;">
            <button type="button" class="btn btn--outline" id="btnSearchAddress">
                주소 검색
            </button>
        </div>
    </div>

    <div class="form-section" style="margin-top:12px;">
        <label for="roadAddress" class="form-label">기본주소</label>
        <input
            id="roadAddress"
            name="roadAddress"
            type="text"
            class="form-input"
            placeholder="주소 검색 버튼을 눌러 선택하세요"
            readonly
            required>
    </div>

    <div class="form-section" style="margin-top:12px;">
        <label for="detailAddress" class="form-label">상세주소</label>
        <input
            id="detailAddress"
            name="detailAddress"
            type="text"
            class="form-input"
            placeholder="상세주소를 입력하세요. 예: 2층, 주차장 옆"
            required>
    </div>

    <div class="form-section" style="margin-top:12px;">
        <label for="extraAddress" class="form-label">참고항목</label>
        <input
            id="extraAddress"
            name="extraAddress"
            type="text"
            class="form-input"
            placeholder="참고항목"
            readonly>
    </div>
</div>

                    <div class="form-section">
                        <label for="content" class="form-label">상세 내용</label>
                        <textarea
                            id="content"
                            name="content"
                            class="form-textarea"
                            placeholder="봉사 활동 소개, 준비사항, 유의사항 등을 작성하세요."
                            required></textarea>
                    </div>

                    <div class="form-section">
                        <label class="form-label">모집 인원 설정</label>

                        <div class="recruit-type-list">
                            <div class="recruit-type-card">
                                <div class="recruit-type-card__left">
                                    <div class="recruit-type-card__icon">
                                        <span class="material-symbols-outlined">cleaning_services</span>
                                    </div>

                                    <div class="recruit-type-card__text">
                                        <p class="recruit-type-card__title">청소 봉사</p>
                                        <p class="recruit-type-card__desc">견사 및 내부 시설 정비</p>
                                    </div>
                                </div>

                                <div class="count-input">
                                    <input type="number"
                                           min="0"
                                           value="0"
                                           name="cleaningCapacity"
                                           class="count-input__field"
                                           required>
                                    <span class="count-input__unit">명</span>
                                </div>
                            </div>

                            <div class="recruit-type-card">
                                <div class="recruit-type-card__left">
                                    <div class="recruit-type-card__icon">
                                        <span class="material-symbols-outlined">pets</span>
                                    </div>

                                    <div class="recruit-type-card__text">
                                        <p class="recruit-type-card__title">산책 봉사</p>
                                        <p class="recruit-type-card__desc">아이들과의 교감 및 야외 활동</p>
                                    </div>
                                </div>

                                <div class="count-input">
                                    <input type="number"
                                           min="0"
                                           value="0"
                                           name="walkingCapacity"
                                           class="count-input__field"
                                           required>
                                    <span class="count-input__unit">명</span>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="form-actions">
                        <a href="${ctp}/volunteer/list.do" class="btn btn--ghost">취소</a>
                        <button type="submit" class="btn btn--primary">
                            <span>모집 공고 게시하기</span>
                            <span class="material-symbols-outlined">send</span>
                        </button>
                    </div>
                </form>
            </section>

            <section class="info-box">
                <span class="material-symbols-outlined info-box__icon">info</span>
                <div class="info-box__content">
                    <strong>안내사항:</strong>
                    게시된 공고는 즉시 사용자들의 ‘봉사하기’ 탭에 노출됩니다.
                    모집 인원이 충원되거나 신청 마감일이 지나면 자동으로 ‘모집 완료’ 상태로 전환되도록
                    이후 로직을 연결할 수 있습니다.
                </div>
            </section>
        </div>
    </main>

    <jsp:include page="/view/common/footer.jsp" />
   <script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
    <script src="${ctp}/js/volunteer-admin.js"></script>
 
</body>
</html>