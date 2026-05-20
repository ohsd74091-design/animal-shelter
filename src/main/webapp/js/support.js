/**
 * [기능] 1:1 문의 등록 비동기 처리 및 실시간 유효성 검사
 * [연결] support.jsp의 각 요소 ID와 연결됨
 */
console.log("✅ support.js 로드 완료");

// 1. 주요 요소 가져오기
const inquiryForm     = document.getElementById('inquiryForm');
const imageInput      = document.getElementById('imageInput');
const previewContainer = document.getElementById('previewContainer');
const submitBtn       = document.getElementById('submitBtn');

// 각 입력창 및 메시지 영역
const typeSelect      = document.getElementById('supportType');
const typeMsg         = document.getElementById('typeMsg');
const titleInput      = document.getElementById('title');
const titleMsg        = document.getElementById('titleMsg');
const contentInput    = inquiryForm.querySelector('textarea[name="content"]');
const contentMsg      = document.getElementById('contentMsg');

let allFiles = []; // 전송할 파일 보관함

// ---------------------------------------------------------
// 2. 실시간 안내 메시지 로직 (입력할 때마다 작동)
// ---------------------------------------------------------

// 공통 메시지 출력 함수 (코드 간결화)
function showMsg(element, message, isError) {
    element.textContent = message;
    element.className = isError ? "msg error" : "msg success";
}

// 문의 유형 변경 시
typeSelect.addEventListener('change', function() {
    if (!this.value) showMsg(typeMsg, "문의 유형을 선택해주세요.", true);
    else showMsg(typeMsg, "선택 완료", false);
});

// 제목 입력 시
titleInput.addEventListener('input', function() {
    if (this.value.trim().length === 0) showMsg(titleMsg, "제목을 입력해주세요.", true);
    else showMsg(titleMsg, "입력 완료", false);
});

// 내용 입력 시
contentInput.addEventListener('input', function() {
    if (this.value.trim().length === 0) showMsg(contentMsg, "내용을 입력해주세요.", true);
    else showMsg(contentMsg, "입력 완료", false);
});

// ---------------------------------------------------------
// 3. 이미지 미리보기 및 관리 (기존 로직 최적화)
// ---------------------------------------------------------
imageInput.addEventListener('change', (e) => {
    const newFiles = Array.from(e.target.files);
    if (allFiles.length + newFiles.length > 3) {
        alert("이미지는 최대 3장까지 가능합니다.");
        return;
    }

    newFiles.forEach((file) => {
        const fileId = Date.now() + Math.random();
        allFiles.push({ id: fileId, file: file });

        const reader = new FileReader();
        reader.onload = (event) => {
            const wrapper = document.createElement('div');
            wrapper.className = 'img-wrapper';
            wrapper.setAttribute('data-id', fileId);
            wrapper.innerHTML = `
                <img src="${event.target.result}">
                <button type="button" class="del-btn" onclick="removeImg(${fileId})">X</button>
            `;
            previewContainer.appendChild(wrapper);
        };
        reader.readAsDataURL(file);
    });
    e.target.value = ''; // 같은 파일 재선택 가능하도록 초기화
});

function removeImg(fileId) {
    // 1. filter 안의 화살표 함수(=>)를 일반 함수(function)로 변경
    allFiles = allFiles.filter(function(item) {
        return item.id !== fileId;
    });

    // 2. 물음표(?.) 대신 if문을 사용하여 요소를 찾고 삭제
    var target = document.querySelector('.img-wrapper[data-id="' + fileId + '"]');
    if (target) {
        target.remove();
    }
}

// ---------------------------------------------------------
// 4. 최종 전송 로직 (통합된 클릭 이벤트)
// ---------------------------------------------------------
submitBtn.addEventListener('click', function() {
    
    // [검증 1] 문의 유형
    if (!typeSelect.value) {
        showMsg(typeMsg, "문의 유형을 반드시 선택해주세요.", true);
        typeSelect.focus(); return;
    }
    // [검증 2] 제목
    if (!titleInput.value.trim()) {
        showMsg(titleMsg, "제목을 입력하지 않았습니다.", true);
        titleInput.focus(); return;
    }
    // [검증 3] 내용
    if (!contentInput.value.trim()) {
        showMsg(contentMsg, "내용을 입력하지 않았습니다.", true);
        contentInput.focus(); return;
    }

    // 전송 데이터 생성
    var formData = new FormData(inquiryForm);
    formData.delete('atchFile'); 
    
    // 화살표 함수 대신 일반 function 사용
    allFiles.forEach(function(item) {
        formData.append('atchFile', item.file);
    });

    // 버튼 상태 변경 (중복 클릭 방지)
    var originalText = submitBtn.innerHTML;
    submitBtn.disabled = true;
    submitBtn.innerHTML = '등록 중...';

	// [수정] fetch 이후 결과 처리 부분
	// 3. 서버로 전송 (비동기 통신)
	// [수정] fetch 내부를 .then() 방식으로 변경하여 에러 제거
	fetch(inquiryForm.action, {
	    method: 'POST',
	    body: formData
	})
	.then(function(response) {
	    // 서버 응답이 오면 JSON으로 변환
	    if (!response.ok) throw new Error("네트워크 응답 오류");
	    return response.json();
	})
	.then(function(result) {
	    // 데이터 처리 성공 시
	    if (result.success) {
	        alert(result.message || "문의가 등록되었습니다.");
	        // JSP에서 심어놓은 context-path 읽기
	        var contextPath = inquiryForm.getAttribute('data-context-path');
	        location.href = contextPath + "/support/list.do";
	    } else {
	        alert(result.message || "등록에 실패했습니다.");
	    }
	})
	.catch(function(error) {
	    // 오류 발생 시
	    console.error("❌ 오류 발생:", error);
	    alert("서버 연결에 실패했습니다.");
	})
	.finally(function() {
	    // [중요] 성공/실패 상관없이 버튼 복구 (등록 중... 글자 지우기)
	    submitBtn.disabled = false;
	    submitBtn.innerHTML = originalText;
	});
});
