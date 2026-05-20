/**
 * mypageEdit.js
 * 회원 정보 수정 전용 스크립트
 *
 * 담당 기능:
 *   1. 프로필 이미지 미리보기
 *   2. 전화번호 자동 하이픈
 *   3. 닉네임 중복 확인 (Ajax)
 *   4. 새 비밀번호 확인 일치 검사
 *   5. 폼 제출 전 최종 유효성 검사
 *   6. 회원 탈퇴 확인
 */

const ctx = window.contextPath || '';  // header.jsp 에서 주입한 contextPath
var nicknameChecked = false;
var originalNickname = '';

/* 1. 프로필 이미지 미리보기 */
function initProfilePreview() {
	const input = document.getElementById('profileImgInput');
	const preview = document.getElementById('previewImg');
	if (!input || !preview) return;

	input.addEventListener('change', () => {
		const file = input.files[0];
		if (!file) return;

		// 이미지 파일만 허용
		if (!file.type.startsWith('image/')) {
			showMsg('profileMsg', '이미지 파일만 업로드 가능합니다.', 'error');
			input.value = '';
			return;
		}

		const reader = new FileReader();
		reader.onload = (e) => { preview.src = e.target.result; };
		reader.readAsDataURL(file);
	});
}

/* 2. 전화번호 자동 하이픈 */
function initPhoneFormat() {
	const phoneInput = document.getElementById('phone');
	const phoneMsg = document.getElementById('phoneMsg');
	if (!phoneInput) return;

	phoneInput.addEventListener('input', () => {
		// 숫자만 추출
		let digits = phoneInput.value.replace(/\D/g, '');

		// 010-0000-0000 형식으로 변환
		if (digits.length <= 3) {
			phoneInput.value = digits;
		} else if (digits.length <= 7) {
			phoneInput.value = digits.slice(0, 3) + '-' + digits.slice(3);
		} else {
			phoneInput.value = digits.slice(0, 3) + '-' + digits.slice(3, 7) + '-' + digits.slice(7, 11);
		}

		// 유효성 메시지
		const valid = /^01[0-9]-\d{3,4}-\d{4}$/.test(phoneInput.value);
		if (phoneInput.value.length > 0) {
			showMsg(phoneMsg,
				valid ? '올바른 전화번호 형식입니다.' : '010-0000-0000 형식으로 입력하세요.',
				valid ? 'success' : 'error');
		} else {
			clearMsg(phoneMsg);
		}
	});
}

/* 3. 닉네임 중복 확인 */
function initNicknameCheck() {
	var btn = document.getElementById('btnNicknameCheck');
	var input = document.getElementById('nickname');
	var msgEl = document.getElementById('nicknameMsg');
	if (!btn || !input) return;

	originalNickname = input.value;

	input.addEventListener('input', function() {
		nicknameChecked = false;
		clearMsg(msgEl);
	});

	btn.addEventListener('click', function() {
		var val = input.value.trim();
		if (!val) {
			showMsg(msgEl, '닉네임을 입력해주세요.', 'error');
			return;
		}
		if (val === originalNickname) {
			nicknameChecked = true;
			showMsg(msgEl, '현재 사용 중인 닉네임입니다.', 'success');
			return;
		}
		//fetch(`${ctx}/member/nickCheck.do?nickname=` + encodeURIComponent(val)) // 밑에 오류나면 교체
		fetch(ctx + '/member/nickCheck.do?nickname=' + encodeURIComponent(val))
			.then(function(res) { return res.json(); })
			.then(function(data) {
				if (data.flag === 'ok') {
					nicknameChecked = true;
					showMsg(msgEl, '사용 가능한 닉네임입니다.', 'success');
				} else {
					nicknameChecked = false;
					showMsg(msgEl, '이미 사용 중인 닉네임입니다.', 'error');
				}
			})
			.catch(function() {
				showMsg(msgEl, '서버 오류가 발생했습니다. 다시 시도해주세요.', 'error');
			});
	});
}

/* 4. 새 비밀번호 확인 일치 검사 */
function initPasswordCheck() {
	const newPw = document.getElementById('newPw');
	const newPwConfirm = document.getElementById('newPwConfirm');
	const newPwMsg = document.getElementById('newPwMsg');
	const confirmMsg = document.getElementById('newPwConfirmMsg');
	if (!newPw || !newPwConfirm) return;

	// 새 비밀번호 길이 검사
	newPw.addEventListener('input', () => {
		const val = newPw.value;
		if (!val) { clearMsg(newPwMsg); return; }
		const valid = val.length >= 8;
		showMsg(newPwMsg, 
			valid ? '사용 가능한 비밀번호입니다.' : '8자 이상 입력해주세요.', 
			valid ? 'success' : 'error');

		// 확인 입력 칸도 다시 검사
		checkConfirm();
	});

	// 비밀번호 확인 일치 검사
	newPwConfirm.addEventListener('input', checkConfirm);

	function checkConfirm() {
		const val = newPwConfirm.value;
		if (!val) { clearMsg(confirmMsg); return; }
		const match = val === newPw.value;
		showMsg(confirmMsg, 
			match ? '비밀번호가 일치합니다.' : '비밀번호가 일치하지 않습니다.', 
			match ? 'success' : 'error');
	}
}

/* 5. 폼 제출 전 최종 유효성 검사 */
function initFormSubmit() {
	const form = document.getElementById('editForm');
	const nicknameEl = document.getElementById('nickname');
	const newPw = document.getElementById('newPw');
	const newPwConfirm = document.getElementById('newPwConfirm');
	const currentPw = document.getElementById('currentPw');
	if (!form) return;

	form.addEventListener('submit', (e) => {

		// 닉네임을 변경했는데 중복확인 안 한 경우
		if (nicknameEl && nicknameEl.value.trim() !== originalNickname && !nicknameChecked) {
			e.preventDefault();
			showMsg(document.getElementById('nicknameMsg'), '닉네임 중복 확인을 해주세요.', 'error');
			nicknameEl.focus();
			return;
		}

		// 새 비밀번호 입력 시 현재 비밀번호 필수
		if (newPw && newPw.value) {
			if (!currentPw || !currentPw.value) {
				e.preventDefault();
				showMsg(document.getElementById('currentPwMsg'), '현재 비밀번호를 입력해주세요.', 'error');
				currentPw.focus();
				return;
			}
			if (newPw.value.length < 8) {
				e.preventDefault();
				showMsg(document.getElementById('newPwMsg'), '새 비밀번호는 8자 이상이어야 합니다.', 'error');
				newPw.focus();
				return;
			}
			if (newPw.value !== newPwConfirm.value) {
				e.preventDefault();
				showMsg(document.getElementById('newPwConfirmMsg'), '비밀번호가 일치하지 않습니다.', 'error');
				newPwConfirm.focus();
				return;
			}
		}
	});
}

/* 6. 회원 탈퇴 */
function initWithdraw() {
    var btn = document.getElementById('btnWithdraw');
    if (!btn) return;
 
    btn.addEventListener('click', function() {
        var confirmed = confirm('정말 탈퇴하시겠습니까?\n탈퇴 후 모든 정보는 복구되지 않습니다.');
        if (confirmed) {
            window.location.href = ctx + '/mypage/withdraw.do';
			//window.location.href = `${ctx}/mypage/withdraw.do`; // 위에 오류나면 교체
        }
    });
}

/* 유틸 함수 */
function showMsg(el, text, type) {
	if (typeof el === 'string') el = document.getElementById(el);
	if (!el) return;
	el.textContent = text;
	el.className = `field-msg ${type}`;   // 'success' or 'error'
	//el.className   = 'field-msg ' + type; // 위에 오류나면 교체
}

function clearMsg(el) {
	if (typeof el === 'string') el = document.getElementById(el);
	if (!el) return;
	el.textContent = '';
	el.className = 'field-msg';
}

/* 초기화 */
document.addEventListener('DOMContentLoaded', () => {
	initProfilePreview();
	initPhoneFormat();
	initNicknameCheck();
	initPasswordCheck();
	initFormSubmit();
	initWithdraw();

	// 저장 성공 시 alert 후 main.do로 이동
	const params = new URLSearchParams(window.location.search);
	if (params.get('success') === 'true') {
		alert('정보가 성공적으로 수정되었습니다.');
		window.location.href = ctx + '/mypage/main.do';
	}
});