document.addEventListener('DOMContentLoaded', function () {
    const ctp = document.body.dataset.ctp || '';

    /* ── 탭 전환 ── */
    document.querySelectorAll('.rpt-tab').forEach(btn => {
        btn.addEventListener('click', function () {
            const tab = this.dataset.tab;
            location.href = ctp + '/admin/report/list.do?tab=' + encodeURIComponent(tab);
        });
    });

    /* ── 필터 버튼 (미처리/처리완료/전체) ── */
    document.querySelectorAll('.rpt-filter-btn').forEach(btn => {
        btn.addEventListener('click', function () {
            const panel = this.closest('.rpt-list-panel');
            panel.querySelectorAll('.rpt-filter-btn').forEach(b => b.classList.remove('active'));
            this.classList.add('active');

            const filter = this.dataset.filter;
            panel.querySelectorAll('.rpt-list-item').forEach(item => {
                if (filter === 'all') {
                    item.style.display = '';
                } else {
                    const processYn = item.querySelector('.rpt-data')?.dataset.processyn
                                   || item.querySelector('.rpt-data')?.dataset.status;
                    item.style.display = (processYn === filter) ? '' : 'none';
                }
            });
        });
    });

    let currentAnimalReportId = null;
    let currentMemberReportId = null;
    let currentAnimalReplyEmail = '';
    let currentMemberReplyEmail = '';

    /* ── 유기동물 제보 상세 표시 ── */
    window.showAnimalDetail = function (el) {
        document.querySelectorAll('#tab-animal .rpt-list-item').forEach(i => i.classList.remove('active'));
        el.classList.add('active');

        const d = el.querySelector('.rpt-data').dataset;
        currentAnimalReportId = d.reportid;
        currentAnimalReplyEmail = d.replyemail || '';

        document.getElementById('ad-animalType').textContent =
            d.animaltype === 'DOG' ? '🐶 강아지' : '🐱 고양이';
        document.getElementById('ad-location').textContent = d.location || '-';
        document.getElementById('ad-reportDate').textContent = d.reportdate || '-';
        document.getElementById('ad-memberId').textContent = d.memberid || '비회원';
        document.getElementById('ad-status').textContent = d.status || '-';
        document.getElementById('ad-content').textContent = d.content || '내용 없음';
        document.getElementById('ad-replyEmail').textContent = d.replyemail || '-';
        document.getElementById('ad-processContent').value = '';

        const badge = document.getElementById('ad-processYn');
        if (d.processyn === 'Y') {
            badge.textContent = '처리완료';
            badge.className = 'rpt-detail__status-badge rpt-status--done';
        } else if (d.processyn === 'R') {
            badge.textContent = '기각';
            badge.className = 'rpt-detail__status-badge rpt-status--reject';
        } else {
            badge.textContent = '미처리';
            badge.className = 'rpt-detail__status-badge rpt-status--pending';
        }

        const imageSection = document.getElementById('ad-imageSection');
        const img = document.getElementById('ad-image');

        if (d.imagename && d.imagename !== 'null' && d.imagename !== '') {
            img.src = ctp + '/report/image.do?fileName=' + encodeURIComponent(d.imagename);
            imageSection.style.display = '';
        } else {
            img.src = '';
            imageSection.style.display = 'none';
        }

        document.getElementById('animalDetailEmpty').style.display = 'none';
        document.getElementById('animalDetailContent').style.display = 'flex';
    };

    /* ── 회원 신고 상세 표시 ── */
    window.showMemberDetail = function (el) {
        document.querySelectorAll('#tab-member .rpt-list-item').forEach(i => i.classList.remove('active'));
        el.classList.add('active');

        const d = el.querySelector('.rpt-data').dataset;
        currentMemberReportId = d.reportid;
        currentMemberReplyEmail = d.replyemail || '';

        document.getElementById('md-reason').textContent = d.reason || '-';
        document.getElementById('md-reportDate').textContent = d.reportdate || '-';
        document.getElementById('md-reporterId').textContent = d.reporterid || '-';
        document.getElementById('md-targetId').textContent = d.targetid || '-';
        document.getElementById('md-content').textContent = d.content || '내용 없음';
        document.getElementById('md-replyEmail').textContent = d.replyemail || '-';
        document.getElementById('md-processContent').value = '';

        const badge = document.getElementById('md-status');
        if (d.status === 'Y') {
            badge.textContent = '처리완료';
            badge.className = 'rpt-detail__status-badge rpt-status--done';
        } else if (d.status === 'R') {
            badge.textContent = '기각';
            badge.className = 'rpt-detail__status-badge rpt-status--reject';
        } else {
            badge.textContent = '미처리';
            badge.className = 'rpt-detail__status-badge rpt-status--pending';
        }

        document.getElementById('memberDetailEmpty').style.display = 'none';
        document.getElementById('memberDetailContent').style.display = 'flex';
    };

    /* ── 유기동물 제보 처리 ── */
    window.processAnimalReport = function (action) {
        if (!currentAnimalReportId) {
            alert('처리할 제보를 먼저 선택하세요.');
            return;
        }

        const processContent = document.getElementById('ad-processContent').value.trim();
        if (!processContent) {
            alert('처리 결과 내용을 입력하세요.');
            return;
        }

        if (!currentAnimalReplyEmail) {
            alert('회신 이메일 정보가 없습니다.');
            return;
        }

        const msg = action === 'Y' ? '처리 완료로 변경하시겠습니까?' : '이 제보를 기각하시겠습니까?';
        if (!confirm(msg)) return;

        fetch(ctp + '/admin/report/animal.do', {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8' },
            body: 'reportId=' + encodeURIComponent(currentAnimalReportId)
                + '&action=' + encodeURIComponent(action)
                + '&replyEmail=' + encodeURIComponent(currentAnimalReplyEmail)
                + '&processContent=' + encodeURIComponent(processContent)
        })
        .then(res => res.json())
        .then(data => {
            if (data.success) {
                alert(action === 'Y' ? '처리 완료 및 메일 발송이 완료되었습니다.' : '기각 처리 및 메일 발송이 완료되었습니다.');
                location.reload();
            } else {
                alert(data.message || '처리 중 오류가 발생했습니다.');
            }
        })
        .catch(() => alert('요청 처리 중 오류가 발생했습니다.'));
    };

    /* ── 회원 신고 처리 ── */
    window.processMemberReport = function (action) {
        if (!currentMemberReportId) {
            alert('처리할 신고를 먼저 선택하세요.');
            return;
        }

        const processContent = document.getElementById('md-processContent').value.trim();
        if (!processContent) {
            alert('처리 결과 내용을 입력하세요.');
            return;
        }

        if (!currentMemberReplyEmail) {
            alert('회신 이메일 정보가 없습니다.');
            return;
        }

        const msg = action === 'Y' ? '처리 완료로 변경하시겠습니까?' : '이 신고를 기각하시겠습니까?';
        if (!confirm(msg)) return;

        fetch(ctp + '/admin/report/member.do', {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8' },
            body: 'reportId=' + encodeURIComponent(currentMemberReportId)
                + '&action=' + encodeURIComponent(action)
                + '&replyEmail=' + encodeURIComponent(currentMemberReplyEmail)
                + '&processContent=' + encodeURIComponent(processContent)
        })
        .then(res => res.json())
        .then(data => {
            if (data.success) {
                alert(action === 'Y' ? '처리 완료 및 메일 발송이 완료되었습니다.' : '기각 처리 및 메일 발송이 완료되었습니다.');
                location.reload();
            } else {
                alert(data.message || '처리 중 오류가 발생했습니다.');
            }
        })
        .catch(() => alert('요청 처리 중 오류가 발생했습니다.'));
    };
	
	const urlParams = new URLSearchParams(location.search);
	// reportId 파라미터로 해당 제보 자동 선택
	const targetReportId = urlParams.get('reportId');
	if (targetReportId) {
	    const targetItem = document.querySelector(
	        '#tab-animal .rpt-list-item .rpt-data[data-reportid="' + targetReportId + '"]'
	    )?.closest('.rpt-list-item');
	    if (targetItem) {
	        targetItem.scrollIntoView({ behavior: 'smooth', block: 'center' });
	        showAnimalDetail(targetItem);
	    }
	}
});