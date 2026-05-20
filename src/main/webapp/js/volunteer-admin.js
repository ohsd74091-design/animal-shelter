document.addEventListener("DOMContentLoaded", function () {
  const fileInput = document.getElementById("thumbnailFile");
  const previewImage = document.getElementById("previewImage");
  const previewPlaceholder = document.getElementById("previewPlaceholder");

  if (fileInput && previewImage && previewPlaceholder) {
    fileInput.addEventListener("change", function () {
      const file = this.files && this.files[0];

      if (!file) {
        previewImage.src = "";
        previewImage.style.display = "none";
        previewPlaceholder.style.display = "flex";
        return;
      }

      const reader = new FileReader();

      reader.onload = function (e) {
        previewImage.src = e.target.result;
        previewImage.style.display = "block";
        previewPlaceholder.style.display = "none";
      };

      reader.readAsDataURL(file);
    });
  }

  document.querySelectorAll(".date-trigger").forEach(function (icon) {
    icon.addEventListener("click", function () {
      const targetId = this.dataset.target;
      const input = document.getElementById(targetId);

      if (!input) return;

      if (typeof input.showPicker === "function") {
        input.showPicker();
      } else {
        input.focus();
        input.click();
      }
    });
  });

  const btnSearchAddress = document.getElementById("btnSearchAddress");
  const postcode = document.getElementById("postcode");
  const roadAddress = document.getElementById("roadAddress");
  const detailAddress = document.getElementById("detailAddress");
  const extraAddress = document.getElementById("extraAddress");
  const location = document.getElementById("location");
  const form = document.querySelector(".write-form");

  function updateLocation() {
    if (!location) return;

    const base = roadAddress ? roadAddress.value.trim() : "";
    const detail = detailAddress ? detailAddress.value.trim() : "";
    const extra = extraAddress ? extraAddress.value.trim() : "";

    let fullAddress = base;

    if (detail) fullAddress += " " + detail;
    if (extra) fullAddress += " " + extra;

    location.value = fullAddress.trim();
  }

  function openPostcode() {
    if (typeof daum === "undefined" || !daum.Postcode) {
      alert("우편번호 API가 아직 로드되지 않았습니다.");
      return;
    }

    new daum.Postcode({
      oncomplete: function (data) {
        let addr = "";
        let extraAddr = "";

        if (data.userSelectedType === "R") {
          addr = data.roadAddress;
        } else {
          addr = data.jibunAddress;
        }

        if (data.userSelectedType === "R") {
          if (data.bname && /[동|로|가]$/g.test(data.bname)) {
            extraAddr += data.bname;
          }

          if (data.buildingName && data.apartment === "Y") {
            extraAddr += (extraAddr ? ", " + data.buildingName : data.buildingName);
          }

          if (extraAddr) {
            extraAddr = "(" + extraAddr + ")";
          }
        }

        if (postcode) postcode.value = data.zonecode;
        if (roadAddress) roadAddress.value = addr;
        if (extraAddress) extraAddress.value = extraAddr;

        updateLocation();

        if (detailAddress) {
          detailAddress.focus();
        }
      }
    }).open();
  }

  if (btnSearchAddress) {
    btnSearchAddress.addEventListener("click", openPostcode);
  }

  if (detailAddress) {
    detailAddress.addEventListener("input", updateLocation);
  }

  if (form) {
    form.addEventListener("submit", function (e) {
      updateLocation();

      if (!roadAddress || !roadAddress.value.trim()) {
        alert("주소 검색을 통해 기본주소를 입력해주세요.");
        e.preventDefault();
        return;
      }

      if (!detailAddress || !detailAddress.value.trim()) {
        alert("상세주소를 입력해주세요.");
        e.preventDefault();
        return;
      }

      if (!location || !location.value.trim()) {
        alert("장소 정보가 올바르게 입력되지 않았습니다.");
        e.preventDefault();
      }
    });
  }
});