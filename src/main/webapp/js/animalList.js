
/**
 * 폼 전체의 초기 상태를 저장할 변수
 * 페이지 로드 시 또는 폼 제출 직후의 상태를 저장
 */
let globalInitialState = "";

/**
 * 선택된 필터를 태그로 표시하는 함수
 */
function renderActiveFilterTags() {
	const container = document.getElementById('activeFilterTags');
	if (!container) return;

	const tags = [];

	// 성격 체크박스
	document.querySelectorAll('input[name="personalityList"]:checked').forEach(el => {
		tags.push({ label: el.value, type: 'personalityList', value: el.value });
	});

	// 크기 체크박스
	const sizeLabels = { SMALL: '소형', MEDIUM: '중형', LARGE: '대형' };
	document.querySelectorAll('input[name="sizeList"]:checked').forEach(el => {
		tags.push({ label: sizeLabels[el.value] || el.value, type: 'sizeList', value: el.value });
	});

	// 성별 라디오
	const genderInput = document.querySelector('input[name="gender"]:checked');
	const genderLabels = { M: '남아', F: '여아' };
	if (genderInput && genderInput.value) {
		tags.push({ label: genderLabels[genderInput.value], type: 'gender', value: genderInput.value });
	}

	// 태그 렌더링
	container.innerHTML = tags.map(tag => `
		<span class="active-filter-tag">
			${tag.label}
			<span class="material-symbols-outlined" onclick="removeFilterTag('${tag.type}', '${tag.value}')">close</span>
		</span>
	`).join('');
}

/**
 * 필터 태그 x 클릭 시 해제 함수
 */
function removeFilterTag(type, value) {
	if (type === 'personalityList' || type === 'sizeList') {
		const checkbox = document.querySelector(`input[name="${type}"][value="${value}"]`);
		if (checkbox) checkbox.checked = false;
	} else if (type === 'gender') {
		const radio = document.querySelector('input[name="gender"][value=""]');
		if (radio) radio.checked = true;
	}
	renderActiveFilterTags();
	submitFilterForm();
}

// 페이지 로드 시 태그 렌더링
document.addEventListener("DOMContentLoaded", () => {
	globalInitialState = getFullFormState();
	renderActiveFilterTags();
});



/**
 * 폼 내의 모든 필터 요소(타입, 성격, 크기, 성별)의 상태를 하나의 문자열로 결합
 */
function getFullFormState() {
	const form = document.getElementById('filterForm');
	if (!form) return "";

	// 모든 input(hidden, checkbox, radio)과 select의 값을 수집
	const inputs = form.querySelectorAll('input, select');
	return Array.from(inputs)
		.map(input => {
			if (input.type === 'checkbox' || input.type === 'radio') {
				return `${input.name}:${input.checked}`;
			}
			return `${input.name}:${input.value}`;
		})
		.join('|');
}

/**
 * 드롭다운 토글 및 자동 닫힘 처리
 */
function toggleDropdown(id, event) {
	const menu = document.getElementById(id);
	const isHidden = menu.classList.contains("hidden");

	if (isHidden) {
		// 열기 - 다른 열려있는 드롭다운이 있다면 닫기
		closeAllDropdownsExcept(id);
		menu.classList.remove("hidden");
	} else {
		// 닫기 - 버튼을 다시 눌러서 닫는 경우에도 변경사항 체크 후 제출 시도
		menu.classList.add("hidden");
		checkAndSubmit();
	}

	// 이벤트가 상위로 퍼지는 것을 허용하되, 버튼 클릭임을 알림
	event.isDropdownButton = true;
}

/**
 * 다른 모든 드롭다운을 닫고, 변경 사항이 있다면 폼을 제출하는 함수
 */
function closeAllDropdownsExcept(currentId) {
	const menus = document.querySelectorAll(".dropdown-menu");

	menus.forEach(menu => {
		// 현재 클릭한 메뉴가 아닐경우
		if (menu.id !== currentId) {
			menu.classList.add("hidden");
		}
	});
}

/**
 * 바깥 클릭 시 모든 드롭다운 닫고, 최초 상태와 비교하여 변경 되었으면 제출
 */
document.addEventListener("click", function(e) {
	// 토글 버튼을 클릭한 경우는 toggleDropdown 함수에서 처리하므로 무시
	if (e.isDropdownButton) return;

	const dropdowns = document.querySelectorAll(".filter-group.dropdown");
	let isAnyMenuOpen = false;

	dropdowns.forEach(drop => {
		const menu = drop.querySelector(".dropdown-menu");

		// 메뉴 내부(체크박스 등)를 클릭한 게 아니고, 메뉴가 열려있다면
		if (!drop.contains(e.target) && !menu.classList.contains("hidden")) {
			menu.classList.add("hidden");
			isAnyMenuOpen = true;
		}
	});

	// 드롭다운이 닫혔다면 최종 변경 사항 확인 후 제출
	if (isAnyMenuOpen) {
		checkAndSubmit();
	}
});

/**
 * 전체 폼 상태를 비교하여 변경사항이 있을 때 제출
 */
function checkAndSubmit() {
	const currentState = getFullFormState();

	// 초기 로드 상태와 현재 상태가 다를 때만 실행
	if (globalInitialState !== currentState) {
		submitFilterForm();
		// 제출 후 상태 업데이트 (연속 호출 방지)
		globalInitialState = currentState;
	}
}

/**
 * 필터 폼 제출 함수
 */
function submitFilterForm(isResetPage = true) {
	const form = document.getElementById('filterForm');

	if (form) {
		let pageInput = form.querySelector('input[name="page"]');

		if (pageInput && isResetPage) {
			pageInput.value = 1;
		}

		form.submit();
	}
}


/**
 * 동물 타입(전체/강아지/고양이) 변경 함수
 * @param {string} type - 'DOG', 'CAT' 또는 빈값('')
 */
function filterType(type) {
	const typeInput = document.getElementById('animalType');
	if (typeInput) {
		typeInput.value = type;
		submitFilterForm();
	}
}


/**
 * 필터 폼 제출 함수
 * (필터 변경 시에는 항상 1페이지로 리셋)
 */
function submitFilterForm(isResetPage = true) {
	const form = document.getElementById('filterForm');

	if (form) {
		// 폼 안에 있는 'page' name을 가진 input을 찾습니다.
		let pageInput = form.querySelector('input[name="page"]');

		// 페이지 번호를 1로 초기화 (새로운 필터 적용 시 1페이지부터 보여주기 위함)
		if (pageInput && isResetPage) {
			pageInput.value = 1;
		}

		form.submit();
	}
}

/**
 * 관심 동물 처리(하트) 함수
 */
function toggleWish(btn, animalId) {
	fetch(`${contextPath}/animal/toggleWish.do`, {
		method: 'POST',
		headers: {
			'Content-Type': 'application/x-www-form-urlencoded',
		},
		body: `animalId=${animalId}`
	})
		.then(response => response.json())
		.then(data => {
			if (data.success) {
				
				// UI 업데이트 로직 (생략)
				const icon = btn.querySelector('.material-symbols-outlined');
				const isActive = btn.classList.toggle('active');
				icon.innerText = isActive ? 'favorite' : 'favorite_border';
			} else if (data.message === "LOGIN_REQUIRED") {

				if (confirm("로그인이 필요한 기능입니다. 로그인 페이지로 이동하시겠습니까?")) {

					// targetPath를 인코딩하여 login.do 뒤에 붙임
					const target = encodeURIComponent(data.targetPath);
					// 전역 변수 활용
					location.href = `${contextPath}/login.do?targetPath=${target}`;
				}
			}
		})
		.catch(err => console.error("Error:", err));
}


/**
 * 페이지 이동 함수
 * @param {number} pageNum - 이동할 페이지 번호
 */
function changePage(pageNum) {
	const pageInput = document.getElementById('pageNumber');

	if (pageInput) {
		pageInput.value = pageNum;
		submitFilterForm(false);
	}
}


/**
 * 상세 페이지 이동 함수
 * @param {number} animalId - 동물 PK
 */
function goDetail(animalId) {
	if (animalId) {
		location.href = `animalDetail.do?id=${animalId}`;
	}
}