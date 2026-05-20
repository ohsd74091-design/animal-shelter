/* ──────────────────────────────────────────
   유틸: 숫자 포맷
────────────────────────────────────────── */
function fmt(n) {
    return Number(n || 0).toLocaleString('ko-KR');
}

const amountInput = document.getElementById('donationAmount');

/* ── 금액 프리셋 ── */
document.querySelectorAll('.preset-btn').forEach(btn => {
    btn.addEventListener('click', function () {
        document.querySelectorAll('.preset-btn').forEach(b => b.classList.remove('active'));
        this.classList.add('active');
        amountInput.value = this.dataset.val;
        updateSummary();
    });
});

amountInput.addEventListener('input', () => {
    document.querySelectorAll('.preset-btn').forEach(b => b.classList.remove('active'));
    updateSummary();
});

document.querySelectorAll('input[name="donationType"]').forEach(r => r.addEventListener('change', updateSummary));
document.querySelectorAll('input[name="donationMethod"]').forEach(r => r.addEventListener('change', updateSummary));

/* ── 은행 안내 토글 ── */
function toggleBankInfo(show) {
    document.getElementById('bankInfo').classList.toggle('show', show);
    updateSummary();
}

/* ──────────────────────────────────────────
   ✅ 영수증 토글 — change 이벤트로 수정
   (label onclick 대신 checkbox change 사용)
────────────────────────────────────────── */
document.getElementById('receiptCheck').addEventListener('change', function () {
    const isChecked = this.checked; // ✅ change 후 정확한 상태
    document.getElementById('receiptInfo').classList.toggle('show', isChecked);
    document.getElementById('receiptLabel').classList.toggle('checked', isChecked);

    if (!isChecked) {
        document.getElementById('regNo1').value = '';
        document.getElementById('regNo2').value = '';
    }
});

/* ── 요약 갱신 ── */
function updateSummary() {
    const amount = amountInput.value || 0;
    
    // 1. 후원 유형(Type) 처리
    const typeChecked = document.querySelector('input[name="donationType"]:checked');
    const typeLabel = (typeChecked && 
                       typeChecked.nextElementSibling && 
                       typeChecked.nextElementSibling.firstChild && 
                       typeChecked.nextElementSibling.firstChild.nextElementSibling) 
                       ? typeChecked.nextElementSibling.firstChild.nextElementSibling.textContent.trim() 
                       : '정기 후원';

    // 2. 결제 수단(Method) 처리
    const methodChecked = document.querySelector('input[name="donationMethod"]:checked');
    let methodLabel = '계좌이체';
    if (methodChecked && methodChecked.nextElementSibling) {
        const methodDiv = methodChecked.nextElementSibling.querySelector('div');
        if (methodDiv && methodDiv.childNodes[0]) {
            methodLabel = methodDiv.childNodes[0].textContent.trim();
        }
    }

    // 3. 화면 갱신
    document.getElementById('summaryType').textContent   = typeLabel;
    document.getElementById('summaryMethod').textContent = methodLabel;
    document.getElementById('summaryAmount').textContent = fmt(amount) + '원';
    document.getElementById('btnText').textContent       = fmt(amount) + '원 후원하기';
    document.getElementById('modalAmount').textContent   = fmt(amount) + '원';
}

/* ──────────────────────────────────────────
   ✅ 폼 제출 — 중복 제출 방지 + 주민번호 검증 추가
────────────────────────────────────────── */
let isSubmitted = false; // ✅ 중복 제출 방지 플래그

document.getElementById('donateForm').addEventListener('submit', function (e) {
    e.preventDefault();

    // ✅ 이미 제출됐으면 막기
    if (isSubmitted) {
        alert('이미 후원이 완료되었습니다. 다시 후원하시려면 페이지를 새로고침 해주세요.');
        return;
    }

    const amount = Number(amountInput.value);
    if (!amount || amount < 1000) {
        alert('후원 금액은 1,000원 이상이어야 합니다.');
        amountInput.focus();
        return;
    }

    // ✅ 영수증 체크 시 주민번호 입력 검증
    const receiptChecked = document.getElementById('receiptCheck').checked;
    if (receiptChecked) {
        const regNo1 = document.getElementById('regNo1').value.trim();
        const regNo2 = document.getElementById('regNo2').value.trim();
        if (regNo1.length !== 6 || regNo2.length !== 7) {
            alert('기부금 영수증 신청을 위해\n주민등록번호를 정확히 입력해주세요.\n(앞 6자리 - 뒤 7자리)');
            document.getElementById('regNo1').focus();
            return;
        }
    }

    // ✅ 제출 버튼 비활성화 (중복 클릭 방지)
    const submitBtn = document.querySelector('.submit-btn');
    submitBtn.disabled = true;
    submitBtn.style.opacity = '0.6';
    submitBtn.style.cursor  = 'not-allowed';

    const formData = new FormData(this);

    fetch(this.action, {
        method: 'POST',
        body: new URLSearchParams(formData)
    })
    .then(res => {
        if (!res.ok) throw new Error('서버 오류');
        return res.json();
    })
	.then(data => {
	    if (data.success) {
	        isSubmitted = true;

	        // 모달 금액 표시
	        document.getElementById('modalAmount').textContent = fmt(amount) + '원';

	        // ── 영수증 신청한 경우 다운로드 버튼 표시 ──
	        const receiptChecked = document.getElementById('receiptCheck').checked;
	        const receiptBtn     = document.getElementById('receiptDownloadBtn');
	        const receiptLink    = document.getElementById('receiptLink');

	        if (receiptChecked && data.donationId && receiptBtn && receiptLink) {
	            // DonationReceiptDownload.java 엔드포인트로 링크 연결
	            const ctp = document.querySelector('meta[name="contextPath"]')?.content
	                      || window.location.pathname.split('/').slice(0, 2).join('/');
	            receiptLink.href = (window.CTX || '') + '/donation/receipt.do?donationId=' + data.donationId;
	            receiptLink.setAttribute('download', ''); // 다운로드 모드
	            receiptLink.setAttribute('target', '_blank');
	            receiptBtn.style.display = 'block';
	        }

	        document.getElementById('successModal').classList.add('show');
	    } else {
	        submitBtn.disabled = false;
	        submitBtn.style.opacity = '1';
	        submitBtn.style.cursor  = 'pointer';
	        alert('후원 처리 중 오류가 발생했습니다: ' + (data.message || ''));
	    }
	})
});

/* ── 모달 배경 클릭 시 닫기 막기 (중복 방지 위해 배경 클릭 비활성화) ── */
document.getElementById('successModal').addEventListener('click', function(e) {
    // 배경 클릭해도 안 닫히게 → 버튼으로만 이동하도록
    e.stopPropagation();
});

/* ── 초기 요약 갱신 ── */
updateSummary();