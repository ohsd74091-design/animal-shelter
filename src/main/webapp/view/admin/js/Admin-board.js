const ctp = document.body.dataset.ctp || '';

/* ================= 탭 ================= */
document.addEventListener('DOMContentLoaded', function () {
    document.querySelectorAll('.board-tab').forEach(btn => {
        btn.addEventListener('click', function () {
            const tabId = this.dataset.tab;
            if (!tabId) return;

            document.querySelectorAll('.tab-content').forEach(el => el.classList.remove('active'));
            document.querySelectorAll('.board-tab').forEach(b => b.classList.remove('active'));

            const target = document.getElementById('tab-' + tabId);
            if (target) target.classList.add('active');

            this.classList.add('active');
        });
    });
});


/* ================= 숨김 ================= */
function toggleHide(boardId, isHidden) {
    const msg = isHidden === 'Y'
        ? '이 게시글을 숨김 처리하시겠습니까?'
        : '이 게시글을 복구하시겠습니까?';

    if (!confirm(msg)) return;

    fetch(ctp + '/admin/board/hide.do', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: 'boardId=' + boardId + '&isHidden=' + isHidden
    })
    .then(res => res.json())
    .then(data => {
        if (data.success) {
            alert('처리 완료');
            location.reload();
        } else {
            alert('실패');
        }
    })
    .catch(() => alert('오류 발생'));
}


/* ================= 삭제 ================= */
function deleteBoard(boardId) {
    if (!confirm('삭제하시겠습니까?')) return;

    fetch(ctp + '/admin/board/delete.do', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: 'boardId=' + boardId
    })
    .then(res => res.json())
    .then(data => {
        if (data.success) {
            alert('삭제 완료');
            location.reload();
        } else {
            alert('삭제 실패');
        }
    })
    .catch(() => alert('오류 발생'));
}


/* ================= 신고 기각 ================= */
function dismissReport(boardId) {
    if (!confirm('신고를 기각하시겠습니까?')) return;

    fetch(ctp + '/admin/board/dismissReport.do', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: 'boardId=' + boardId
    })
    .then(res => res.json())
    .then(data => {
        if (data.success) {
            alert('기각 완료');
            location.reload();
        } else {
            alert('실패');
        }
    })
    .catch(() => alert('오류 발생'));
}


/* ================= 신고 사유 ================= */
function openReasonModal(boardId, title) {
    const modal = document.getElementById('reasonModal');
    const titleEl = document.getElementById('reasonModalTitle');
    const bodyEl = document.getElementById('reasonModalBody');

    if (!modal || !titleEl || !bodyEl) {
        console.error("모달 요소 없음");
        return;
    }

    titleEl.textContent = '신고 사유 - ' + title;
    bodyEl.innerHTML = '불러오는 중...';

    modal.style.display = 'block';

    fetch(ctp + '/admin/board/reportList.do?boardId=' + boardId)
    .then(res => res.json())
    .then(data => {
        if (!data || data.length === 0) {
            bodyEl.innerHTML = '신고 없음';
            return;
        }

        let html = '';
        data.forEach((r, i) => {
            html += `
                <div class="report-reason-item">
                    <strong>${i + 1}. ${r.memberId}</strong>
                    <div>${r.reason}</div>
                </div>
            `;
        });

        bodyEl.innerHTML = html;
    })
    .catch(() => {
        bodyEl.innerHTML = '불러오기 실패';
    });
}


/* ================= 모달 닫기 ================= */
function closeReasonModal() {
    const modal = document.getElementById('reasonModal');
    if (modal) modal.style.display = 'none';
}