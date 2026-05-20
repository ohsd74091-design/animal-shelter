/**
 * mypage.js
 * 마이페이지 전용 스크립트
 */

/* =============================================
   사이드바 - 현재 URL에 맞는 메뉴 active 처리
   (JSP에서 class="active"를 직접 쓰므로 JS는 보조 역할)
   ============================================= */
function initSidebarActive() {
    const currentPath = window.location.pathname;
    const sidebarLinks = document.querySelectorAll('.sidebar-nav a');

    sidebarLinks.forEach(link => {
        // 이미 active 클래스가 있으면 스킵
        if (link.classList.contains('active')) return;

        const href = link.getAttribute('href') || '';
        // URL 끝 부분이 현재 경로와 일치하면 active 추가
        if (href && currentPath.endsWith(href.split('/').pop())) {
            link.classList.add('active');
        }
    });
}

/* =============================================
   초기화
   ============================================= */
document.addEventListener('DOMContentLoaded', () => {
    initSidebarActive();
});
