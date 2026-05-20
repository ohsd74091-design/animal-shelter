// 상세보기 토글
function toggleDetail(btn) {
    const clickedRow = btn.closest('tr');
    const detailRow = clickedRow.nextElementSibling; // 바로 다음 .detail-content 행
    const isVisible = detailRow.style.display === "table-row";

    // ✅ 클래스명을 JSP와 일치시킴: .detail-row → .detail-content
    document.querySelectorAll('.detail-content').forEach(r => {
        r.style.display = "none";
    });
    // 모든 버튼 텍스트 초기화
    document.querySelectorAll('.view-btn').forEach(b => {
        b.innerText = "상세 보기";
    });

    // 클릭한 것만 토글
    if (!isVisible) {
        detailRow.style.display = "table-row";
        btn.innerText = "닫기";
    }
    // isVisible이 true였으면(이미 열려 있던 것을 다시 클릭) 위에서 이미 닫혔으므로 추가 처리 불필요
}


function openEditForm(id, type, title, content) {

    const modal = document.getElementById('editModal');
    
    // 1. 어느 글을 수정하는지 ID 저장 (가장 중요!)
    // JSP에 <input type="hidden" name="support_id"> 가 있어야 합니다.
    const idInput = modal.querySelector('[name="support_id"]');
    if(idInput) idInput.value = id;
    
    // 2. 기존 데이터 채워넣기
    // select 박스의 선택값 변경
    const typeSelect = modal.querySelector('[name="supportType"]');
    if(typeSelect) typeSelect.value = type;
    
    // 제목과 내용 채우기
    modal.querySelector('[name="edit_title"]').value = title;
    modal.querySelector('[name="edit_content"]').value = content;
    
    // 3. 모달 화면에 보이기
    modal.style.display = 'block';
}

//  모달 닫기
function closeEditModal() {
    document.getElementById('editModal').style.display = 'none';
}

//  삭제하기 버튼 클릭 시 실행
function deleteInquiry(supportId) {
    if(!confirm("정말 삭제하시겠습니까? 복구할 수 없습니다.")) return;
    
    //  /support/delete.do 주소로 삭제할 ID를 보냄
    fetch('deleteSupport.do?id=' + supportId, { 
        method: 'POST' 
    })
    .then(res => res.json())
    .then(data => {
        if(data.success) {
            alert("삭제되었습니다.");
            location.reload(); // 행 삭제 후 목록 새로고침
        } else {
            alert("삭제 실패: " + data.message);
        }
    })
    .catch(err => alert("삭제 중 오류가 발생했습니다."));
}

// [수정 완료] 버튼 클릭 시 서버로 데이터 전송
function submitEdit() {
    console.log("수정 프로세스 시작"); // 작동 확인용
    
    const modal = document.getElementById('editModal');
    
    // 값 가져오기 (가장 확실한 ID 방식 사용)
    const id = document.getElementById('editSupportId').value;
    const type = document.getElementById('editSupportType').value;
    const title = document.getElementById('editTitle').value;
    const content = document.getElementById('editContent').value;
	console.log("가져온 ID값:", id); // 여기서 숫자가 잘 찍히는지 확인!
    // 검증
    if(!title.trim() || !content.trim()) {
        alert("제목과 내용을 모두 입력해주세요.");
        return;
    }

    // 데이터 바구니 만들기  //이미지까지 수정 할땐 필요
    /*const formData = new FormData();
    formData.append("supportId", id);
    formData.append("supportType", type);
    formData.append("title", title);
    formData.append("content", content);*/
	
	const params = new URLSearchParams();
    params.append("supportId", id);
    params.append("supportType", type);
    params.append("title", title);
    params.append("content", content);

    // 3. 서버 전송
    // 주의: .js 파일에서는 ${pageContext...}를 쓸 수 없으므로 실제 프로젝트명을 적거나
    // JSP에서 변수로 넘겨받아야 합니다. 일단 절대경로로 수정합니다.
    fetch('/Animal_Shelter/support/updateSupport.do', { 
        method: 'POST',
       /* body: formData*/ 
	   headers: {
           // 서버에 "이건 일반 텍스트 데이터야"라고 알려줍니다.
           'Content-Type': 'application/x-www-form-urlencoded'
       },
       body: params.toString()
    })
    .then(res => {
        if(!res.ok) throw new Error("서버 응답 오류 (404/500)");
        return res.json();
    })
    .then(data => {
        if(data.success) {
            alert("수정이 완료되었습니다.");
            location.reload(); 
        } else {
            alert("수정 실패: " + data.message);
        }
    })
    .catch(err => {
        console.error("오류 발생:", err);
        alert("서버 연결에 실패했습니다. 콘솔을 확인하세요.");
    });
}