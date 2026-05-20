<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctp" value="${pageContext.request.contextPath}" />

<footer class="site-footer">
    <div class="layout-container footer-inner">
        <div class="footer-grid">

            <div class="footer-brand">
                <div class="footer-logo">
                    <span class="material-symbols-outlined">pets</span>
                    <span>너와 나의 연결고리</span>
                </div>
                <p class="footer-description">
                    우리는 모든 생명이 존중받는 세상을 꿈꿉니다.
                    유기동물의 구조와 보호, 그리고 새로운 가족을 찾는 일에 앞장섭니다.
                </p>

                <%-- 신고하기 버튼 --%>
                <button type="button" class="footer-report-btn" id="footerReportBtn">
                    <span class="material-symbols-outlined">campaign</span>
                    신고 / 제보하기
                </button>
            </div>

            <div class="footer-column">
                <h4>바로가기</h4>
                <ul>
                    <li><a href="${ctp}/animal/animalList.do">보호동물 입양</a></li>
                    <li><a href="${ctp}/animal/guide.do">입양 신청안내</a></li>
                    <li><a href="${ctp}/board/list.do">커뮤니티</a></li>
                </ul>
            </div>

            <div class="footer-column">
                <h4>고객지원</h4>
                <ul>
                    <li><a href="${ctp}/support/faq.do">자주 묻는 질문</a></li>
                    <li><a href="${ctp}/support/list.do">1:1 문의하기</a></li>
                    <li><a href="${ctp}/board/list.do?boardType=공지">공지사항</a></li>
                    <li><a href="${ctp}/support/map.do">오시는 길</a></li>
                </ul>
            </div>

            <div class="footer-column">
                <h4>연락처</h4>
                <ul class="footer-contact">
                    <li><span class="material-symbols-outlined">call</span><span>02-1234-5678</span></li>
                    <li><span class="material-symbols-outlined">mail</span><span>contact@linkpets.org</span></li>
                    <li><span class="material-symbols-outlined">location_on</span><span>대덕인재개발원</span></li>
                </ul>
            </div>
        </div>

        <div class="footer-bottom">
            <p>© 2026 너와 나의 연결고리. All rights reserved.</p>
        </div>
    </div>
</footer>

<%-- =====================================================
     모달들은 footer 밖 body 레벨에 위치해야
     position:fixed 가 viewport 기준으로 정중앙에 뜸
     ===================================================== --%>

<%-- 신고 유형 선택 모달 --%>
<div id="reportSelectModal" class="fr-overlay" style="display:none;">
    <div class="fr-modal">
        <div class="fr-modal__header">
            <div class="fr-modal__header-title">
                <span class="material-symbols-outlined fr-modal__header-icon">campaign</span>
                <h3>신고 / 제보하기</h3>
            </div>
            <button type="button" class="fr-modal__close" id="reportSelectClose">
                <span class="material-symbols-outlined">close</span>
            </button>
        </div>
        <p class="fr-modal__desc">제보 유형을 선택해주세요. 소중한 제보가 생명을 구합니다.</p>

        <div class="fr-modal__body">
            <%-- 유기동물 제보 카드 --%>
            <button type="button" class="fr-select-card" id="goAnimalReport">
                <div class="fr-select-card__img">🐾</div>
                <div class="fr-select-card__text">
                    <strong>유기동물 제보</strong>
                    <span>길에서 발견한 유기견·고양이를 제보해주세요.</span>
                </div>
                <span class="material-symbols-outlined fr-select-card__arrow">chevron_right</span>
            </button>

            <%-- 회원 신고 카드 --%>
            <button type="button" class="fr-select-card" id="goMemberReport">
                <div class="fr-select-card__img fr-select-card__img--red">🚨</div>
                <div class="fr-select-card__text">
                    <strong>회원 신고</strong>
                    <span>부적절한 행동을 하는 회원을 신고해주세요.</span>
                </div>
                <span class="material-symbols-outlined fr-select-card__arrow">chevron_right</span>
            </button>
        </div>
    </div>
</div>

<%-- 유기동물 제보 모달 --%>
<div id="animalReportModal" class="fr-overlay" style="display:none;">
    <div class="fr-modal">
        <div class="fr-modal__header">
            <button type="button" class="fr-modal__back" id="animalReportBack">
                <span class="material-symbols-outlined">arrow_back</span>
            </button>
            <div class="fr-modal__header-title">
                <span class="fr-modal__header-emoji">🐾</span>
                <h3>유기동물 제보</h3>
            </div>
            <button type="button" class="fr-modal__close" id="animalReportClose">
                <span class="material-symbols-outlined">close</span>
            </button>
        </div>

        <div class="fr-modal__body">
            <form id="animalReportForm" action="${ctp}/report/animal.do"
                  method="post" enctype="multipart/form-data">

                <%-- 동물 종류 --%>
                <div class="fr-form-group">
                    <label class="fr-label">동물 종류 <span class="fr-required">*</span></label>
                    <div class="fr-type-toggle">
                        <label class="fr-type-btn">
                            <input type="radio" name="animalType" value="DOG" required>
                            <span>🐶 강아지</span>
                        </label>
                        <label class="fr-type-btn">
                            <input type="radio" name="animalType" value="CAT">
                            <span>🐱 고양이</span>
                        </label>
                    </div>
                </div>

                <%-- 발견 위치 --%>
                <div class="fr-form-group">
                    <label class="fr-label" for="animalLocation">
                        발견 위치 <span class="fr-required">*</span>
                    </label>
                    <input type="text" id="animalLocation" name="location"
                           class="fr-input" placeholder="예) 서울시 강남구 역삼동 123" required>
                </div>

                <%-- 상태 --%>
                <div class="fr-form-group">
                    <label class="fr-label" for="animalStatus">
                        동물 상태 <span class="fr-required">*</span>
                    </label>
                    <select id="animalStatus" name="status" class="fr-select" required>
                        <option value="">선택해주세요</option>
                        <option value="배회중">배회중</option>
                        <option value="부상">부상</option>
                        <option value="포획필요">포획필요</option>
                        <option value="기타">기타</option>
                    </select>
                </div>

                <%-- 상세 내용 --%>
                <div class="fr-form-group">
                    <label class="fr-label" for="animalContent">상세 내용</label>
                    <textarea id="animalContent" name="content" class="fr-textarea"
                              rows="3" placeholder="발견 상황, 특이사항 등을 입력해주세요."></textarea>
                </div>
                
                <%-- 처리 결과 받을 이메일 --%>
<div class="fr-form-group">
    <label class="fr-label" for="animalReplyEmail">
        처리 결과 받을 이메일 <span class="fr-required">*</span>
    </label>
    <input type="email"
           id="animalReplyEmail"
           name="replyEmail"
           class="fr-input"
           placeholder="예) example@email.com"
           required>
</div>

                <%-- 사진 첨부 --%>
                <div class="fr-form-group">
                    <label class="fr-label">사진 첨부</label>
                    <div class="fr-file-wrap">
                        <label for="animalImage" class="fr-file-label">
                            <span class="material-symbols-outlined">add_a_photo</span>
                            사진 선택
                        </label>
                        <input type="file" id="animalImage" name="imageFile"
                               accept="image/*" style="display:none;">
                        <span class="fr-file-name" id="animalImageName">선택된 파일 없음</span>
                    </div>
                </div>
                
                

                <button type="submit" class="fr-submit-btn">
                    <span class="material-symbols-outlined">send</span>
                    제보하기
                </button>
            </form>
        </div>
    </div>
</div>

<%-- 회원 신고 모달 --%>
<div id="memberReportModal" class="fr-overlay" style="display:none;">
    <div class="fr-modal">
        <div class="fr-modal__header">
            <button type="button" class="fr-modal__back" id="memberReportBack">
                <span class="material-symbols-outlined">arrow_back</span>
            </button>
            <div class="fr-modal__header-title">
                <span class="fr-modal__header-emoji">🚨</span>
                <h3>회원 신고</h3>
            </div>
            <button type="button" class="fr-modal__close" id="memberReportClose">
                <span class="material-symbols-outlined">close</span>
            </button>
        </div>

        <div class="fr-modal__body">
            <form id="memberReportForm" action="${ctp}/report/member.do" method="post">

                <%-- 신고 대상 ID --%>
                <div class="fr-form-group">
                    <label class="fr-label" for="targetMemberId">
                        신고할 회원 ID <span class="fr-required">*</span>
                    </label>
                    <input type="text" id="targetMemberId" name="targetId"
                           class="fr-input" placeholder="신고할 회원의 아이디를 입력해주세요." required>
                </div>

                <%-- 신고 사유 --%>
                <div class="fr-form-group">
                    <label class="fr-label">신고 사유 <span class="fr-required">*</span></label>
                    <div class="fr-reason-list">
                        <label class="fr-reason-item">
                            <input type="radio" name="reason" value="욕설/비방" required>
                            <span>욕설/비방</span>
                        </label>
                        <label class="fr-reason-item">
                            <input type="radio" name="reason" value="사기/허위정보">
                            <span>사기/허위정보</span>
                        </label>
                        <label class="fr-reason-item">
                            <input type="radio" name="reason" value="도용 의심">
                            <span>도용 의심</span>
                        </label>
                        <label class="fr-reason-item">
                            <input type="radio" name="reason" value="동물 학대 의심">
                            <span>동물 학대 의심</span>
                        </label>
                        <label class="fr-reason-item">
                            <input type="radio" name="reason" value="기타">
                            <span>기타</span>
                        </label>
                    </div>
                </div>
                <div class="fr-form-group">
        <label class="fr-label" for="memberReportContent">상세 내용</label>
        <textarea id="memberReportContent"
                  name="content"
                  class="fr-textarea"
                  rows="3"
                  placeholder="신고 사유에 대한 상세 내용을 입력해주세요."></textarea>
    </div>
    <%-- 처리 결과 받을 이메일 --%>
<div class="fr-form-group">
    <label class="fr-label" for="memberReplyEmail">
        처리 결과 받을 이메일 <span class="fr-required">*</span>
    </label>
    <input type="email"
           id="memberReplyEmail"
           name="replyEmail"
           class="fr-input"
           placeholder="예) example@email.com"
           required>
</div>

                <button type="submit" class="fr-submit-btn fr-submit-btn--red">
                    <span class="material-symbols-outlined">flag</span>
                    신고하기
                </button>
            </form>
        </div>
    </div>
</div>

<%-- 모달 스크립트 --%>
<script>
(function () {
    function openModal(id)  {
        document.getElementById(id).style.display = 'flex';
        document.body.style.overflow = 'hidden'; // 스크롤 방지
    }
    function closeModal(id) {
        document.getElementById(id).style.display = 'none';
        document.body.style.overflow = '';
    }

    /* 신고하기 버튼 */
    document.getElementById('footerReportBtn')
        .addEventListener('click', () => openModal('reportSelectModal'));

    /* 선택 모달 닫기 */
    document.getElementById('reportSelectClose')
        .addEventListener('click', () => closeModal('reportSelectModal'));

    /* 딤 클릭 시 닫기 */
    ['reportSelectModal','animalReportModal','memberReportModal'].forEach(id => {
        document.getElementById(id).addEventListener('click', function (e) {
            if (e.target === this) { // overlay 자체 클릭 시만 닫힘
                closeModal(id);
            }
        });
    });

    /* 유기동물 제보 선택 */
    document.getElementById('goAnimalReport').addEventListener('click', function () {
        closeModal('reportSelectModal');
        openModal('animalReportModal');
    });

    /* 회원 신고 선택 */
    document.getElementById('goMemberReport').addEventListener('click', function () {
        closeModal('reportSelectModal');
        openModal('memberReportModal');
    });

    /* 유기동물 모달 닫기 / 뒤로 */
    document.getElementById('animalReportClose')
        .addEventListener('click', () => closeModal('animalReportModal'));
    document.getElementById('animalReportBack').addEventListener('click', function () {
        closeModal('animalReportModal');
        openModal('reportSelectModal');
    });

    /* 회원 신고 모달 닫기 / 뒤로 */
    document.getElementById('memberReportClose')
        .addEventListener('click', () => closeModal('memberReportModal'));
    document.getElementById('memberReportBack').addEventListener('click', function () {
        closeModal('memberReportModal');
        openModal('reportSelectModal');
    });

    /* 사진 파일명 표시 */
    document.getElementById('animalImage').addEventListener('change', function () {
        const name = this.files.length > 0 ? this.files[0].name : '선택된 파일 없음';
        document.getElementById('animalImageName').textContent = name;
    });

    /* 유기동물 제보 폼 제출 */
    document.getElementById('animalReportForm').addEventListener('submit', function (e) {
        e.preventDefault();
        const formData = new FormData(this);
        fetch(this.action, { method: 'POST', body: formData })
            .then(res => res.json())
            .then(data => {
                if (data.success) {
                    alert('제보가 접수되었습니다. 감사합니다!');
                    closeModal('animalReportModal');
                    this.reset();
                    document.getElementById('animalImageName').textContent = '선택된 파일 없음';
                } else {
                    alert(data.message || '제보 처리 중 오류가 발생했습니다.');
                }
            })
            .catch(() => alert('요청 처리 중 오류가 발생했습니다.'));
    });

    /* 회원 신고 폼 제출 */
    document.getElementById('memberReportForm').addEventListener('submit', function (e) {
        e.preventDefault();
        const params = new URLSearchParams(new FormData(this));
        fetch(this.action, {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: params
        })
            .then(res => res.json())
            .then(data => {
                if (data.success) {
                    alert('신고가 접수되었습니다.');
                    closeModal('memberReportModal');
                    this.reset();
                } else {
                    alert(data.message || '신고 처리 중 오류가 발생했습니다.');
                }
            })
            .catch(() => alert('요청 처리 중 오류가 발생했습니다.'));
    });
})();
</script>