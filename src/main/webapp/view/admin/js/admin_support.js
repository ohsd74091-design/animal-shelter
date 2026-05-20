
/**
 * 상세 내용을 토글하는 함수
 * @param {string} targetId - 보여주거나 숨길 행의 ID
 */
function toggleContent(targetId) {
    const targetElement = document.getElementById(targetId);
    
    // 현재 상태 확인 후 토글
    if (targetElement.style.display === "none") {
		// 다른 열려있는 행들을 닫고 싶다면 (선택사항)
		document.querySelectorAll('.detail-row').forEach(row => row.style.display = 'none');
        targetElement.style.display = "table-row";
    } else {
        targetElement.style.display = "none";
    }
}

/**
 * 모든 상세 행을 닫는 함수 (깔끔한 UI를 위해 추천)
 */
function closeAllDetails() {
    const allDetailRows = document.querySelectorAll('.detail-row');
    allDetailRows.forEach(row => {
        row.style.display = "none";
    });
}

/**
 * 기능 2: 답변 등록 버튼 클릭 시 서버로 전송 (비동기)
 */
function submitReply() {
    const id = document.getElementById('currentInquiryId').value;
    const content = document.getElementById('replyText').value;

    if(!content) {
        alert("답변 내용을 입력해주세요!");
        return;
    }

    // 3. 서버에 데이터 전송 (AJAX/fetch)
    /*
    fetch('/admin/updateReply', {
        method: 'POST',
        body: JSON.stringify({ inquiryId: id, reply: content })
    })
    .then(response => {
        if(response.ok) {
            // 성공 시 실행할 로직
        }
    });
    */

    // [비동기 반영 연출]
    alert("답변이 성공적으로 등록되었습니다!");

    // 4. 즉시 리스트의 상태를 '답변 완료'로 변경 (새로고침 없이!)
    const statusBadge = document.getElementById('status-' + id);
    statusBadge.innerText = "답변 완료";
    statusBadge.className = "badge status-complete"; // CSS 클래스 변경

    // 5. 입력창 초기화
    document.getElementById('replyText').value = "";
}

/**
 * 필터 변경 시 1페이지로 리셋 후 전송
 */
function submitForm() {
    const pageInput = document.getElementById('page');
    if(pageInput) pageInput.value = 1;
    document.getElementById('searchForm').submit();
}

/**
 * 페이지 번호 클릭 시 해당 페이지로 전송
 */
function changePage(pageNo) {
    const pageInput = document.getElementById('page');
    if(pageInput) {
        pageInput.value = pageNo;
        document.getElementById('searchForm').submit();
    }
}