document.addEventListener("DOMContentLoaded", function () {
	// 현재는 별도 동작이 많지 않아서
	// 추후 정렬/검색/행 클릭 기능 추가 시 여기 확장하면 됨.

	const rows = document.querySelectorAll(".history-table tbody tr");

	rows.forEach(row => {
		row.addEventListener("mouseenter", function () {
			this.style.transition = "background 0.2s ease";
		});
	});
});