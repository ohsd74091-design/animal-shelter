document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById("signupForm");

    const memberId = document.getElementById("memberId");
    const email = document.getElementById("email");
    const memberPw = document.getElementById("memberPw");
    const memberPwConfirm = document.getElementById("memberPwConfirm");

    const idCheckBtn = document.getElementById("idCheckBtn");
    const emailCheckBtn = document.getElementById("emailCheckBtn");

    const idCheck = document.getElementById("idCheck");
    const emailCheck = document.getElementById("emailCheck");

    const idMsg = document.getElementById("idMsg");
    const emailMsg = document.getElementById("emailMsg");
    const pwMsg = document.getElementById("pwMsg");

	const nickname = document.getElementById("nickname");
	const nickCheckBtn = document.getElementById("nickCheckBtn");
	const nickCheck = document.getElementById("nickCheck");
	const nickMsg = document.getElementById("nickMsg");
	
	
	
    // =========================
    // 비밀번호 확인
    // =========================
    function validatePassword() {
        const pw = memberPw.value.trim();
        const pwConfirm = memberPwConfirm.value.trim();

        if (pw === "" && pwConfirm === "") {
            pwMsg.textContent = "";
            pwMsg.className = "msg";
            return false;
        }

        if (pw.length > 0 && pw.length < 8) {
            pwMsg.textContent = "비밀번호는 8자 이상이어야 합니다.";
            pwMsg.className = "msg error";
            return false;
        }

        if (pwConfirm === "") {
            pwMsg.textContent = "비밀번호 확인을 입력해주세요.";
            pwMsg.className = "msg error";
            return false;
        }

        if (pw !== pwConfirm) {
            pwMsg.textContent = "비밀번호가 일치하지 않습니다.";
            pwMsg.className = "msg error";
            return false;
        }

        pwMsg.textContent = "비밀번호가 일치합니다.";
        pwMsg.className = "msg success";
        return true;
    }

    memberPw.addEventListener("input", validatePassword);
    memberPwConfirm.addEventListener("input", validatePassword);

    // =========================
    // 아이디 값 변경 시 재검사 처리
    // =========================
    memberId.addEventListener("input", function () {
        idCheck.value = "N";
        if (idMsg) {
            idMsg.textContent = "아이디 중복확인이 필요합니다.";
            idMsg.className = "msg error";
        }
    });

    // =========================
    // 이메일 값 변경 시 재검사 처리
    // =========================
    email.addEventListener("input", function () {
        emailCheck.value = "N";
        if (emailMsg) {
            emailMsg.textContent = "이메일 중복확인이 필요합니다.";
            emailMsg.className = "msg error";
        }
    });

    // =========================
    // 아이디 중복체크
    // =========================
    idCheckBtn.addEventListener("click", function () {
        const idValue = memberId.value.trim();

        if (idValue === "") {
            idMsg.textContent = "아이디를 입력해주세요.";
            idMsg.className = "msg error";
            memberId.focus();
            return;
        }

        fetch(window.contextPath + "/member/idCheck.do?memberId=" + encodeURIComponent(idValue), {
            method: "GET"
        })
        .then(response => response.json())
        .then(data => {
            // 서버에서 {"flag":"ok"} 또는 {"flag":"fail"} 형태로 준다고 가정
            if (data.flag === "ok") {
                idCheck.value = "Y";
                idMsg.textContent = "사용 가능한 아이디입니다.";
                idMsg.className = "msg success";
            } else {
                idCheck.value = "N";
                idMsg.textContent = "이미 사용 중인 아이디입니다.";
                idMsg.className = "msg error";
            }
        })
        .catch(err => {
            console.error(err);
            idCheck.value = "N";
            idMsg.textContent = "아이디 중복확인 중 오류가 발생했습니다.";
            idMsg.className = "msg error";
        });
    });

    // =========================
    // 이메일 중복체크
    // =========================
    emailCheckBtn.addEventListener("click", function () {
        const emailValue = email.value.trim();

        if (emailValue === "") {
            emailMsg.textContent = "이메일을 입력해주세요.";
            emailMsg.className = "msg error";
            email.focus();
            return;
        }

        fetch(window.contextPath + "/member/emailCheck.do?email=" + encodeURIComponent(emailValue), {
            method: "GET"
        })
        .then(response => response.json())
        .then(data => {
            if (data.flag === "ok") {
                emailCheck.value = "Y";
                emailMsg.textContent = "사용 가능한 이메일입니다.";
                emailMsg.className = "msg success";
            } else {
                emailCheck.value = "N";
                emailMsg.textContent = "이미 사용 중인 이메일입니다.";
                emailMsg.className = "msg error";
            }
        })
        .catch(err => {
            console.error(err);
            emailCheck.value = "N";
            emailMsg.textContent = "이메일 중복확인 중 오류가 발생했습니다.";
            emailMsg.className = "msg error";
        });
    });
	   // =========================
	   // 닉네임 변경시 재검사 
	   // =========================
	nickname.addEventListener("input", function () {
	    nickCheck.value = "N";
	    nickMsg.textContent = "닉네임 중복확인이 필요합니다.";
	    nickMsg.className = "msg error";
	});
	
	       // =========================
		   // 닉네임 중복체크
		   // =========================
	nickCheckBtn.addEventListener("click", function () {
	    const nickValue = nickname.value.trim();

	    if (nickValue === "") {
	        nickMsg.textContent = "닉네임을 입력해주세요.";
	        nickMsg.className = "msg error";
	        nickname.focus();
	        return;
	    }

	    fetch(window.contextPath + "/member/nickCheck.do?nickname=" + encodeURIComponent(nickValue))
	        .then(response => response.json())
	        .then(data => {
	            if (data.flag === "ok") {
	                nickCheck.value = "Y";
	                nickMsg.textContent = "사용 가능한 닉네임입니다.";
	                nickMsg.className = "msg success";
	            } else {
	                nickCheck.value = "N";
	                nickMsg.textContent = "이미 사용 중인 닉네임입니다.";
	                nickMsg.className = "msg error";
	            }
	        })
	        .catch(err => {
	            console.error(err);
	            nickMsg.textContent = "닉네임 중복확인 중 오류 발생";
	            nickMsg.className = "msg error";
	        });
	});
    // =========================
    // 최종 제출 전 검사
    // =========================
    form.addEventListener("submit", function (e) {
        const pwValid = validatePassword();

        if (idCheck.value !== "Y") {
            alert("아이디 중복확인을 해주세요.");
            e.preventDefault();
            return;
        }

        if (emailCheck.value !== "Y") {
            alert("이메일 중복확인을 해주세요.");
            e.preventDefault();
            return;
        }

        if (!pwValid) {
            alert("비밀번호를 확인해주세요.");
            e.preventDefault();
            return;
        }
		if (nickCheck.value !== "Y") {
		    alert("닉네임 중복확인을 해주세요.");
		    e.preventDefault();
		    return;
		}
    });
});