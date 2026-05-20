/**
 * mypageAdoptionHistory.js
 * 입양 신청 내역 페이지 전용 스크립트
 *
 * 주요 기능:
 *   - 신청 취소 AJAX 요청
 *   - 취소 확인 다이얼로그
 *   - 취소 후 카드 DOM 제거 및 빈 상태 처리
 */

'use strict';

/* --------------------------------------------------------
   신청 취소
   @param {string} adoptionId - 입양 신청 ID
-------------------------------------------------------- */
function cancelAdoption(adoptionId) {

    if (!confirm('입양 신청을 취소하시겠습니까?')) return;

    const contextPath = getContextPath();

    fetch(`${contextPath}/adoption/cancel.do`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: `adoptionId=${encodeURIComponent(adoptionId)}`
    })
    .then(res => res.json())
    .then(data => {
        if (data.result === 'success') {
            removeItemFromDOM(adoptionId);
        } else {
            alert(data.message || '신청 취소에 실패했습니다. 다시 시도해 주세요.');
        }
    })
    .catch(err => {
        console.error('[cancelAdoption] 오류:', err);
        alert('서버와 통신 중 오류가 발생했습니다.');
    });
}


/* --------------------------------------------------------
   카드 DOM 제거 및 빈 상태 처리
   @param {string} adoptionId
-------------------------------------------------------- */
function removeItemFromDOM(adoptionId) {

    // 취소 버튼의 data-adoption-id 또는 onclick 으로 찾기
    const btn = document.querySelector(`.btn-cancel[onclick*="${adoptionId}"]`);
    if (!btn) return;

    const item = btn.closest('.adoption-item');
    if (!item) return;

    item.style.transition = 'opacity 0.3s, transform 0.3s';
    item.style.opacity = '0';
    item.style.transform = 'scale(0.97)';

    setTimeout(() => {
        item.remove();
        checkEmptyState();
    }, 300);
}


/* --------------------------------------------------------
   아이템이 하나도 없으면 빈 상태 표시
-------------------------------------------------------- */
function checkEmptyState() {
    const list = document.querySelector('.adoption-list');
    if (!list) return;

    const remaining = list.querySelectorAll('.adoption-item').length;
    if (remaining > 0) return;

    const pagination = document.querySelector('.pagination');
    if (pagination) pagination.remove();
    list.remove();

    const contextPath = getContextPath();
    const content = document.querySelector('.mypage-content');
    if (!content) return;

    const emptyEl = document.createElement('div');
    emptyEl.className = 'adoption-empty';
    emptyEl.innerHTML = `
        <span class="material-symbols-outlined adoption-empty__icon">assignment</span>
        <p class="adoption-empty__msg">입양 신청 내역이 없습니다.</p>
        <a href="${contextPath}/animal/animalList.do" class="adoption-empty__btn">
            입양 동물 둘러보기
        </a>
    `;
    content.appendChild(emptyEl);
}


/* --------------------------------------------------------
   contextPath 헬퍼
-------------------------------------------------------- */
function getContextPath() {
    const pathSegments = window.location.pathname.split('/');
    return pathSegments.length > 1 ? '/' + pathSegments[1] : '';
}
