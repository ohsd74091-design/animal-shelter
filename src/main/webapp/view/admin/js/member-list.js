document.addEventListener("DOMContentLoaded", function () {
	const content = document.querySelector(".admin-content");
	const ctp = content ? content.dataset.ctp : "";

	const detailButtons = document.querySelectorAll(".member-detail-btn");

	detailButtons.forEach(button => {
		button.addEventListener("click", function () {
			const memberId = this.dataset.memberid;
			location.href = ctp + "/admin/member/detail.do?memberId=" + encodeURIComponent(memberId);
			
		});
	});
});