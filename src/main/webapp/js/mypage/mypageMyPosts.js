/**
 * mypageMyPosts.js
 * 내 작성글 페이지 전용 스크립트
 *
 * 주요 기능:
 *   - 게시글 삭제 AJAX 요청
 *   - 삭제 확인 다이얼로그
 *   - 삭제 후 행 DOM 제거 및 빈 상태 처리
 */

'use strict';

/* --------------------------------------------------------
   게시글 삭제
   @param {number} boardId - 게시글 ID
-------------------------------------------------------- */
function deletePost(boardId) {

    if (!confirm('게시글을 삭제하시겠습니까?\n삭제된 글은 복구할 수 없습니다.')) return;

    const contextPath = getContextPath();

    fetch(`${contextPath}/mypage/myPosts/delete.do`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: `boardId=${encodeURIComponent(boardId)}`
    })
    .then(res => res.json())
    .then(data => {
        if (data.result === 'success') {
            removeRowFromDOM(boardId);
        } else {
            alert(data.message || '삭제에 실패했습니다. 다시 시도해 주세요.');
        }
    })
    .catch(err => {
        console.error('[deletePost] 오류:', err);
        alert('서버와 통신 중 오류가 발생했습니다.');
    });
}


/* --------------------------------------------------------
   행 DOM 제거 및 빈 상태 처리
   @param {number} boardId
-------------------------------------------------------- */
function removeRowFromDOM(boardId) {
    const btn = document.querySelector(`.btn-icon--del[onclick*="${boardId}"]`);
    if (!btn) return;

    const row = btn.closest('tr');
    if (!row) return;

    row.style.transition = 'opacity 0.3s';
    row.style.opacity = '0';

    setTimeout(() => {
        row.remove();
        checkEmptyState();
    }, 300);
}


/* --------------------------------------------------------
   행이 하나도 없으면 빈 상태 표시
-------------------------------------------------------- */
function checkEmptyState() {
    const tbody = document.querySelector('.posts-table tbody');
    if (!tbody) return;

    const remaining = tbody.querySelectorAll('tr').length;
    if (remaining > 0) return;

    const tableWrap  = document.querySelector('.posts-table-wrap');
    const footer     = document.querySelector('.posts-card__footer');
    if (tableWrap) tableWrap.remove();
    if (footer)    footer.remove();

    const card = document.querySelector('.posts-card');
    if (!card) return;

    const emptyEl = document.createElement('div');
    emptyEl.className = 'posts-empty';
    emptyEl.innerHTML = `
        <span class="material-symbols-outlined posts-empty__icon">article</span>
        <p class="posts-empty__msg">작성한 게시글이 없습니다.</p>
    `;
    card.appendChild(emptyEl);
}


/* --------------------------------------------------------
   contextPath 헬퍼
-------------------------------------------------------- */
function getContextPath() {
    const pathSegments = window.location.pathname.split('/');
    return pathSegments.length > 1 ? '/' + pathSegments[1] : '';
}
