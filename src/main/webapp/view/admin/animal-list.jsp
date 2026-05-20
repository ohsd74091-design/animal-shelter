<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="ctp" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>동물 목록 관리 - 관리자</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700;800&family=Manrope:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@20..48,100..700,0..1,-50..200" />
    <link rel="stylesheet" href="${ctp}/view/admin/css/common/admin-layout.css">
    <link rel="stylesheet" href="${ctp}/view/admin/css/animal-list.css">
    <script defer src="${ctp}/view/admin/js/admin-layout.js"></script>
</head>
<body>
<div class="admin-layout">

    <jsp:include page="/view/admin/common/admin-sidebar.jsp" />

    <div class="admin-main-wrap">

        <jsp:include page="/view/admin/common/admin-header.jsp" />

        <main class="admin-main-content">
            <div class="list-container">

                <!-- Page Header -->
                <div class="page-header">
                    <div class="page-header__content">
                        <span class="page-badge">Database Management</span>
                        <h1 class="page-title">동물 목록 관리</h1>
                        <p class="page-description">등록된 모든 동물의 정보를 관리하고 상태를 업데이트하세요</p>
                    </div>
                    <div class="page-header__actions">
                        <button class="btn-primary" onclick="location.href='${ctp}/admin/animal/register.do'">
                            <span class="material-symbols-outlined">add</span>
                            새 동물 등록
                        </button>
                    </div>
                </div>

                <%--
                    서버 필터링 Form
                    버튼 클릭 시 JS의 setFilter()가 hidden input 값을 바꾸고 submit
                    페이지 이동 시에도 URL 파라미터로 필터값 유지
                --%>
                <form action="${ctp}/admin/animal/list.do" method="get" id="filterForm">
                    <input type="hidden" name="page"           value="1">
                    <input type="hidden" name="animalType"     value="${pageVO.animalType}">
                    <input type="hidden" name="adoptionStatus" value="${pageVO.adoptionStatus}">
                    <input type="hidden" name="sort"           value="${pageVO.sort}">

                    <section class="filter-bar">
                        <div class="filter-group">
                            <label class="filter-label">종류별 필터</label>
                            <div class="filter-buttons">
                                <%-- pageVO.animalType이 비어있으면 전체(active) --%>
                                <button type="button"
                                        class="filter-btn ${empty pageVO.animalType ? 'active' : ''}"
                                        onclick="setFilter('animalType', '')">전체</button>
                                <button type="button"
                                        class="filter-btn ${'DOG' eq pageVO.animalType ? 'active' : ''}"
                                        onclick="setFilter('animalType', 'DOG')">강아지</button>
                                <button type="button"
                                        class="filter-btn ${'CAT' eq pageVO.animalType ? 'active' : ''}"
                                        onclick="setFilter('animalType', 'CAT')">고양이</button>
                            </div>
                        </div>

                        <div class="filter-divider"></div>

                        <div class="filter-group">
                            <label class="filter-label">상태별 필터</label>
                            <div class="filter-buttons">
                                <button type="button"
                                        class="status-filter-btn ${'입양가능' eq pageVO.adoptionStatus ? 'active' : ''}"
                                        onclick="setFilter('adoptionStatus', '입양가능')">
                                    <span class="status-dot status-available"></span>입양 가능
                                </button>
                                <button type="button"
                                        class="status-filter-btn ${'입양검토중' eq pageVO.adoptionStatus ? 'active' : ''}"
                                        onclick="setFilter('adoptionStatus', '입양검토중')">
                                    <span class="status-dot status-preparing"></span>입양 검토 중
                                </button>
                                <button type="button"
                                        class="status-filter-btn ${'입양완료' eq pageVO.adoptionStatus ? 'active' : ''}"
                                        onclick="setFilter('adoptionStatus', '입양완료')">
                                    <span class="status-dot status-completed"></span>입양 완료
                                </button>
                            </div>
                        </div>

                        <div class="filter-actions">
                            <button type="button" class="sort-btn" onclick="toggleSort()">
                                <span class="material-symbols-outlined">sort</span>
                                <span id="sortText">${pageVO.sort eq 'views' ? '조회수순' : '최신순'}</span>
                            </button>
                            <button type="button" class="view-toggle-btn active" id="gridViewBtn" onclick="setViewMode('grid')">
                                <span class="material-symbols-outlined">grid_view</span>
                            </button>
                            <button type="button" class="view-toggle-btn" id="listViewBtn" onclick="setViewMode('list')">
                                <span class="material-symbols-outlined">view_list</span>
                            </button>
                        </div>
                    </section>
                </form>

                <!-- 동물 카드 그리드 -->
                <div class="animal-grid" id="animalGrid">
                    <c:choose>
                        <c:when test="${empty animalList}">
                            <div class="empty-state">
                                <span class="material-symbols-outlined">pets</span>
                                <p>등록된 동물이 없습니다.</p>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="animal" items="${animalList}">
                                <div class="animal-card ${'입양완료' eq animal.adoptionStatus ? 'adopted' : ''}">

                                    <div class="card-image">
                                        <%-- not empty로 null과 빈문자열 둘 다 체크 --%>
                                        <c:choose>
                                            <c:when test="${not empty animal.mainImage}">
                                                <img src="${ctp}/animal/image?fileName=${animal.mainImage}" alt="${animal.animalName}">
                                            </c:when>
                                            <c:otherwise>
                                                <img src="${ctp}/view/admin/img/no-image.png" alt="이미지 없음">
                                            </c:otherwise>
                                        </c:choose>

                                        <%-- DB값과 일치하는 상태값으로 CSS 클래스 적용 --%>
                                        <div class="status-badge
                                            ${'입양가능'  eq animal.adoptionStatus ? 'status-available'  : ''}
                                            ${'입양검토중' eq animal.adoptionStatus ? 'status-preparing' : ''}
                                            ${'입양완료'  eq animal.adoptionStatus ? 'status-completed'  : ''}">
                                            ${animal.adoptionStatus}
                                        </div>

                                        <div class="type-tags">
                                            <span class="type-tag">#${'DOG' eq animal.animalType ? 'Dog' : 'Cat'}</span>
                                        </div>
                                    </div>

                                    <div class="card-content">
                                        <div class="card-header">
                                            <div class="card-title-group">
                                                <h3 class="card-title">${animal.animalName}</h3>
                                                <p class="card-id">#${animal.animalId}</p>
                                            </div>
                                            <div class="card-info-group">
                                                <p class="card-breed">${animal.breed}</p>
                                                <p class="card-age">${animal.age}살</p>
                                            </div>
                                        </div>

                                        <div class="card-details">
                                            <div class="detail-item">
                                                <span class="material-symbols-outlined">calendar_today</span>
                                                <span>등록일: <fmt:formatDate value="${animal.createDate}" pattern="yyyy.MM.dd"/></span>
                                            </div>
                                            <div class="detail-item">
                                                <span class="material-symbols-outlined">${'M' eq animal.gender ? 'male' : 'female'}</span>
                                                <span>${'M' eq animal.gender ? '수컷' : '암컷'}</span>
                                            </div>
                                        </div>

                                        <div class="card-actions">
                                            <button class="action-btn btn-edit"
                                                    onclick="editAnimal(${animal.animalId})">수정</button>
                                            <%-- 현재 상태도 같이 넘겨서 모달에서 현재 상태 라디오 선택해줌 --%>
                                            <button class="action-btn btn-status"
                                                    onclick="changeStatus(${animal.animalId}, '${animal.adoptionStatus}')">상태 변경</button>
                                            <button class="action-btn btn-delete"
                                                    onclick="deleteAnimal(${animal.animalId})">
                                                <span class="material-symbols-outlined">delete</span>
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>

                    <!-- 새 동물 등록 카드 -->
                    <div class="add-card" onclick="location.href='${ctp}/admin/animal/register.do'">
                        <div class="add-card__icon">
                            <span class="material-symbols-outlined">add_circle</span>
                        </div>
                        <p class="add-card__title">새로운 동물 친구 등록하기</p>
                        <p class="add-card__subtitle">구조된 동물의 정보를 추가하세요</p>
                    </div>
                </div>

                <%--
                    페이지네이션
                    필터값을 URL 파라미터로 유지해야 페이지 이동해도 필터가 풀리지 않음
                --%>
                <div class="pagination">
                    <c:if test="${pageVO.startPage > 1}">
                        <a class="pagination-btn"
                           href="?page=${pageVO.startPage - 1}&animalType=${pageVO.animalType}&adoptionStatus=${pageVO.adoptionStatus}&sort=${pageVO.sort}">
                            <span class="material-symbols-outlined">chevron_left</span>
                        </a>
                    </c:if>

                    <div class="pagination-numbers">
                        <c:forEach var="i" begin="${pageVO.startPage}" end="${pageVO.endPage}">
                            <a class="page-number ${i == pageVO.currentPage ? 'active' : ''}"
                               href="?page=${i}&animalType=${pageVO.animalType}&adoptionStatus=${pageVO.adoptionStatus}&sort=${pageVO.sort}">
                                ${i}
                            </a>
                        </c:forEach>
                    </div>

                    <c:if test="${pageVO.endPage < pageVO.totalPage}">
                        <a class="pagination-btn"
                           href="?page=${pageVO.endPage + 1}&animalType=${pageVO.animalType}&adoptionStatus=${pageVO.adoptionStatus}&sort=${pageVO.sort}">
                            <span class="material-symbols-outlined">chevron_right</span>
                        </a>
                    </c:if>
                </div>

            </div>
        </main>
    </div>
</div>

<!-- FAB -->
<button class="fab-btn" onclick="showHistory()">
    <span class="material-symbols-outlined">history</span>
</button>

<!-- 상태 변경 모달 -->
<div class="modal" id="statusModal">
    <div class="modal-content">
        <div class="modal-header">
            <h3>입양 상태 변경</h3>
            <button class="modal-close" onclick="closeStatusModal()">
                <span class="material-symbols-outlined">close</span>
            </button>
        </div>
        <div class="modal-body">
            <div class="status-options">
                <%-- value값이 DB 제약조건과 일치해야 함: '입양가능' / '입양검토중' / '입양완료' --%>
                <label class="status-option">
                    <input type="radio" name="newStatus" value="입양가능">
                    <span class="status-option-content">
                        <span class="status-dot status-available"></span>
                        <span>입양 가능</span>
                    </span>
                </label>
                <label class="status-option">
                    <input type="radio" name="newStatus" value="입양검토중">
                    <span class="status-option-content">
                        <span class="status-dot status-preparing"></span>
                        <span>입양 검토 중</span>
                    </span>
                </label>
                <label class="status-option">
                    <input type="radio" name="newStatus" value="입양완료">
                    <span class="status-option-content">
                        <span class="status-dot status-completed"></span>
                        <span>입양 완료</span>
                    </span>
                </label>
            </div>
        </div>
        <div class="modal-footer">
            <button class="btn-secondary" onclick="closeStatusModal()">취소</button>
            <button class="btn-primary" onclick="confirmStatusChange()">변경하기</button>
        </div>
    </div>
</div>

<script>
    const ctp = '${ctp}';
    let currentAnimalId = null; // 상태변경/삭제 대상 animalId 저장용

    // 필터 버튼 클릭 → hidden input 값 변경 후 form submit
    function setFilter(name, value) {
        document.querySelector('input[name="' + name + '"]').value = value;
        document.querySelector('input[name="page"]').value = 1;
        document.getElementById('filterForm').submit();
    }

    // 정렬 토글: 최신순 ↔ 조회수순
    function toggleSort() {
        const sortInput = document.querySelector('input[name="sort"]');
        const sortText  = document.getElementById('sortText');
        if (sortInput.value === 'views') {
            sortInput.value = '';
            sortText.textContent = '최신순';
        } else {
            sortInput.value = 'views';
            sortText.textContent = '조회수순';
        }
        document.querySelector('input[name="page"]').value = 1;
        document.getElementById('filterForm').submit();
    }

    // 뷰 모드 토글: 그리드 ↔ 리스트 (CSS 클래스만 변경, 서버 요청 없음)
    function setViewMode(mode) {
        const grid    = document.getElementById('animalGrid');
        const gridBtn = document.getElementById('gridViewBtn');
        const listBtn = document.getElementById('listViewBtn');
        if (mode === 'grid') {
            grid.classList.remove('animal-list-view');
            gridBtn.classList.add('active');
            listBtn.classList.remove('active');
        } else {
            grid.classList.add('animal-list-view');
            listBtn.classList.add('active');
            gridBtn.classList.remove('active');
        }
    }

    // 수정 페이지 이동
    function editAnimal(animalId) {
        location.href = ctp + '/admin/animal/edit.do?animalId=' + animalId;
    }

    // 상태 변경 모달 열기
    // currentStatus를 받아서 현재 상태 라디오 버튼 선택해줌
    function changeStatus(animalId, currentStatus) {
        currentAnimalId = animalId;
        document.querySelectorAll('input[name="newStatus"]').forEach(r => {
            r.checked = (r.value === currentStatus);
        });
        document.getElementById('statusModal').classList.add('active');
    }

    function closeStatusModal() {
        document.getElementById('statusModal').classList.remove('active');
        currentAnimalId = null;
    }

    // 상태 변경 확인 → AdminAnimalActionController로 AJAX POST
    function confirmStatusChange() {
        const selected = document.querySelector('input[name="newStatus"]:checked');
        if (!selected) { alert('변경할 상태를 선택해주세요.'); return; }

        fetch(ctp + '/admin/animal/status.do', {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: 'animalId=' + currentAnimalId + '&newStatus=' + encodeURIComponent(selected.value)
        })
        .then(res => res.json())
        .then(data => {
            if (data.success) {
                alert(data.message);
                closeStatusModal();
                location.reload(); // 상태 변경 후 목록 새로고침
            } else {
                alert('오류: ' + data.message);
            }
        })
        .catch(err => console.error(err));
    }

    // 삭제 → AdminAnimalActionController로 AJAX POST
    function deleteAnimal(animalId) {
        if (!confirm('정말 삭제하시겠습니까?\n관련 이미지, 의료정보, 구조정보가 모두 삭제됩니다.')) return;

        fetch(ctp + '/admin/animal/delete.do', {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: 'animalId=' + animalId
        })
        .then(res => res.json())
        .then(data => {
            if (data.success) {
                alert(data.message);
                location.reload();
            } else {
                alert('오류: ' + data.message);
            }
        })
        .catch(err => console.error(err));
    }

    function showHistory() {
        alert('준비 중인 기능입니다.');
    }
</script>
</body>
</html>
