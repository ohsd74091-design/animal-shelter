/**
 * mypageFavAnimal.js
 * 관심동물(찜 목록) 페이지 전용 스크립트
 *
 * 주요 기능:
 *   - 하트 버튼 클릭 → 찜 해제 AJAX 요청
 *   - 카드 페이드 아웃 후 DOM 제거
 *   - 남은 카드 수 체크 → 0개면 빈 화면(empty state) 표시
 */

'use strict';

/* --------------------------------------------------------
   찜 해제 (AJAX)
   @param {HTMLElement} btn  - 클릭된 하트 버튼
   @param {string}      animalId - 동물 ID
-------------------------------------------------------- */
function removeFavorite(btn, animalId) {

    // 이중 클릭 방지
    if (btn.disabled) return;
    btn.disabled = true;

    const contextPath = getContextPath();

    fetch(`${contextPath}/mypage/favorite/remove.do`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: `animalId=${encodeURIComponent(animalId)}`
    })
    .then(res => res.json())
    .then(data => {
        if (data.result === 'success') {
            removeCardFromDOM(btn);
        } else {
            alert(data.message || '찜 해제에 실패했습니다. 다시 시도해 주세요.');
            btn.disabled = false;
        }
    })
    .catch(err => {
        console.error('[removeFavorite] 오류:', err);
        alert('서버와 통신 중 오류가 발생했습니다.');
        btn.disabled = false;
    });
}


/* --------------------------------------------------------
   카드 DOM 제거 및 빈 상태 처리
   @param {HTMLElement} btn - 하트 버튼 (카드의 자식 요소)
-------------------------------------------------------- */
function removeCardFromDOM(btn) {
    const card = btn.closest('.fav-card');
    if (!card) return;

    // 페이드 아웃 애니메이션
    card.classList.add('removing');

    card.addEventListener('transitionend', () => {
        card.remove();
        checkEmptyState();
    }, { once: true });
}


/* --------------------------------------------------------
   카드가 하나도 없으면 빈 상태 화면 표시
-------------------------------------------------------- */
function checkEmptyState() {
    const grid = document.querySelector('.fav-grid');
    if (!grid) return;

    const remaining = grid.querySelectorAll('.fav-card').length;
    if (remaining > 0) return;

    // 그리드 + 페이지네이션 제거
    const pagination = document.querySelector('.pagination');
    if (pagination) pagination.remove();
    grid.remove();

    // 빈 상태 UI 삽입
    const content = document.querySelector('.mypage-content');
    if (!content) return;

    const contextPath = getContextPath();

    const emptyEl = document.createElement('div');
    emptyEl.className = 'fav-empty';
    emptyEl.innerHTML = `
        <span class="material-symbols-outlined fav-empty__icon">favorite_border</span>
        <p class="fav-empty__msg">아직 관심 동물이 없어요.</p>
        <a href="${contextPath}/animal/animalList.do" class="fav-empty__btn">
            입양 동물 둘러보기
        </a>
    `;
    content.appendChild(emptyEl);
}


/* --------------------------------------------------------
   contextPath 헬퍼
   (JSP에서 hidden input으로 주입하거나 meta 태그 활용 권장)
   현재는 pathname의 첫 번째 세그먼트를 contextPath로 가정
-------------------------------------------------------- */
function getContextPath() {
    // 예: /pawpal/mypage/favorite.do → /pawpal
    const pathSegments = window.location.pathname.split('/');
    return pathSegments.length > 1 ? '/' + pathSegments[1] : '';
}
