/* ================================================
   adoptionForm.js
   - 방문일 최솟값 설정
   - 글자수 카운터
   - 카카오 우편번호 API
   - 유효성 검사
   - AJAX 제출
   ================================================ */
(function () {
    "use strict";

    /* ---- 방문일: 오늘 +1일 최솟값 ---- */
    const visitDateInput = document.getElementById("visitDate");
    if (visitDateInput) {
        const tomorrow = new Date();
        tomorrow.setDate(tomorrow.getDate() + 1);
        visitDateInput.min = tomorrow.toISOString().split("T")[0];
    }

    /* ---- 입양 동기 글자수 카운터 ---- */
    const reasonTextarea = document.getElementById("adoptionReason");
    const reasonCount    = document.getElementById("reasonCount");
    if (reasonTextarea && reasonCount) {
        reasonTextarea.addEventListener("input", function () {
            const len = this.value.length;
            reasonCount.textContent = len;
            reasonCount.style.color = len >= 100 ? "#ec5b13" : "#5f5b57";
        });
    }

    /* ---- 카카오 우편번호 API ---- */
    const btnPostcode = document.getElementById("btnPostcode");
    if (btnPostcode) {
        btnPostcode.addEventListener("click", function () {
            new daum.Postcode({
                oncomplete: function (data) {
                    document.getElementById("postcode").value    = data.zonecode;
                    document.getElementById("addressMain").value = data.roadAddress || data.jibunAddress;
                    document.getElementById("addressDetail").focus();
                }
            }).open();
        });
    }

    /* ---- 유효성 검사 헬퍼 ---- */
    function setInvalid(el) {
        el.classList.add("is-invalid");
        ["input", "change"].forEach(function (evt) {
            el.addEventListener(evt, function () {
                el.classList.remove("is-invalid");
            }, { once: true });
        });
    }

    function validate() {
        let ok = true;
        let firstError = null;

        // animalId
        const animalId = document.getElementById("animalId").value;
        if (!animalId || animalId === "0" || animalId === "") {
            alert("입양 신청할 동물을 먼저 선택해주세요.");
            return false;
        }

        // 이름
        const name = document.getElementById("applicantName");
        if (!name.value.trim()) {
            setInvalid(name);
            firstError = firstError || name;
            ok = false;
        }

        // 연락처 (숫자 + 하이픈, 9~14자)
        const phone = document.getElementById("phone");
        if (!/^[\d\-]{9,14}$/.test(phone.value.trim())) {
            setInvalid(phone);
            firstError = firstError || phone;
            ok = false;
        }

        // 기본 주소 (카카오 API 후 채워짐 → readonly)
        const addressMain = document.getElementById("addressMain");
        if (!addressMain.value.trim()) {
            alert("주소 검색 버튼을 눌러 주소를 입력해주세요.");
            return false;
        }

        // 직업
        const job = document.getElementById("job");
        if (!job.value.trim()) {
            setInvalid(job);
            firstError = firstError || job;
            ok = false;
        }

        // 주거형태
        const housingType = document.getElementById("housingType");
        if (!housingType.value) {
            setInvalid(housingType);
            firstError = firstError || housingType;
            ok = false;
        }

        // 반려동물 경험 (radio)
        const expChecked = document.querySelector('input[name="petExperience"]:checked');
        if (!expChecked) {
            alert("반려동물 경험 여부를 선택해주세요.");
            return false;
        }

        // 입양 동기 (빈값 체크만)
        const reason = document.getElementById("adoptionReason");
        if (!reason.value.trim()) {
            setInvalid(reason);
            firstError = firstError || reason;
            ok = false;
        }

        // 방문일
        const visitDate = document.getElementById("visitDate");
        if (!visitDate.value) {
            setInvalid(visitDate);
            firstError = firstError || visitDate;
            ok = false;
        }

        if (!ok && firstError) {
            firstError.focus();
            firstError.scrollIntoView({ behavior: "smooth", block: "center" });
        }

        return ok;
    }

    /* ---- 제출 버튼 ---- */
    const btnSubmit = document.getElementById("btnSubmit");
    if (btnSubmit) {
        btnSubmit.addEventListener("click", function () {
            if (!validate()) return;

            // 주소 합치기
            const addressMain   = document.getElementById("addressMain").value.trim();
            const addressDetail = document.getElementById("addressDetail").value.trim();
            const fullAddress   = addressDetail ? addressMain + " " + addressDetail : addressMain;

            const params = new URLSearchParams({
                animalId:       document.getElementById("animalId").value,
                visitDate:      document.getElementById("visitDate").value,
                job:            document.getElementById("job").value.trim(),
                housingType:    document.getElementById("housingType").value,
                petExperience:  document.querySelector('input[name="petExperience"]:checked').value,
                address:        fullAddress,
                adoptionReason: document.getElementById("adoptionReason").value.trim()
            });

            // 중복 제출 방지
            btnSubmit.disabled    = true;
            btnSubmit.textContent = "신청 중...";

            // window.contextPath 는 header.jsp 에서 주입됨
            const ctp = window.contextPath || "";

            fetch(ctp + "/adoption/adoptionApply.do", {
                method:  "POST",
                headers: { "Content-Type": "application/x-www-form-urlencoded; charset=UTF-8" },
                body:    params.toString()
            })
            .then(function (res) { return res.text(); })
            .then(function (result) {
                result = result.trim();
                if (result === "SUCCESS") {
                    alert("입양 신청이 완료되었습니다!\n담당 매니저가 2~3일 내로 연락드릴 예정입니다.");
                    location.href = ctp + "/animal/animalList.do";

                } else if (result === "DUPLICATE") {
                    alert("이미 해당 동물에 대한 입양 신청이 진행 중입니다.");
                    btnSubmit.disabled    = false;
                    btnSubmit.textContent = "입양 신청 완료하기";

                } else if (result === "NOLOGIN") {
                    alert("로그인이 필요합니다.");
                    location.href = ctp + "/login.do";

                } else {
                    alert("처리 중 오류가 발생했습니다. 다시 시도해주세요.");
                    btnSubmit.disabled    = false;
                    btnSubmit.textContent = "입양 신청 완료하기";
                }
            })
            .catch(function () {
                alert("서버와의 통신에 실패했습니다. 다시 시도해주세요.");
                btnSubmit.disabled    = false;
                btnSubmit.textContent = "입양 신청 완료하기";
            });
        });
    }

})();