/**
 * 
 */document.addEventListener("DOMContentLoaded", function () {
    const btnMessage = document.getElementById("btnMessage");
    const btnRoleChange = document.getElementById("btnRoleChange");
    const btnToggleStatus = document.getElementById("btnToggleStatus");
    const btnDeleteMember = document.getElementById("btnDeleteMember");

    if (btnMessage) {
        btnMessage.addEventListener("click", function () {
            alert("메시지 보내기 기능은 추후 연결됩니다.");
        });
    }

    if (btnRoleChange) {
        btnRoleChange.addEventListener("click", function () {
            const ok = confirm("이 회원의 권한을 변경하시겠습니까?");
            if (!ok) return;

            // location.href = ctp + "/admin/member/roleUpdate.do?memberId=" + memberId;
            alert("권한 변경 로직 연결 예정");
        });
    }

	const statusForm = document.getElementById("statusForm");
		if (statusForm) {
			statusForm.addEventListener("submit", function (e) {
				const ok = confirm("회원 상태를 변경하시겠습니까?");
				if (!ok) {
					e.preventDefault();
				}
			});
		}

		const deleteForm = document.getElementById("deleteForm");
			if (deleteForm) {
				deleteForm.addEventListener("submit", function (e) {
					const ok = confirm("정말로 이 회원을 삭제하시겠습니까?");
					if (!ok) {
						e.preventDefault();
					}
				});
			}
	
	document.addEventListener("DOMContentLoaded",function(){
		const ctp = document.querySelector(".admin-content").dataset.ctp;
		const btnBackToList =document.getElementById("btnBackToList");
		
		if(btnBackToList){
			btnBackToList.addEventListener("click", function(){
				location.href =ctp +"/admin/member/list.do";
			});
		}
	});	
});