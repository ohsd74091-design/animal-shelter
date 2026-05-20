/* ========================================
   Animal List Management Script
   ======================================== */

// 전역 변수
let currentPage = 1;
let totalPages = 12;
let currentFilter = { type: 'all', status: 'all' };
let currentSort = 'latest';
let selectedAnimalId = null;

// ========================================
// DOM 로드 완료 후 초기화
// ========================================
document.addEventListener('DOMContentLoaded', function() {
    initializeFilters();
    initializePagination();
    initializeViewToggle();
    initializeSortButton();
});

// ========================================
// 필터 초기화
// ========================================
function initializeFilters() {
    // 종류별 필터
    const typeFilters = document.querySelectorAll('.filter-btn');
    typeFilters.forEach(btn => {
        btn.addEventListener('click', function() {
            typeFilters.forEach(b => b.classList.remove('active'));
            this.classList.add('active');
            currentFilter.type = this.dataset.type;
            filterAnimals();
        });
    });

    // 상태별 필터
    const statusFilters = document.querySelectorAll('.status-filter-btn');
    statusFilters.forEach(btn => {
        btn.addEventListener('click', function() {
            const isActive = this.classList.contains('active');
            
            if (isActive) {
                this.classList.remove('active');
                currentFilter.status = 'all';
            } else {
                statusFilters.forEach(b => b.classList.remove('active'));
                this.classList.add('active');
                currentFilter.status = this.dataset.status;
            }
            
            filterAnimals();
        });
    });
}

// ========================================
// 동물 필터링
// ========================================
function filterAnimals() {
    const cards = document.querySelectorAll('.animal-card');
    
    cards.forEach(card => {
        const cardType = card.dataset.type;
        const cardStatus = card.dataset.status;
        
        let showCard = true;
        
        // 종류 필터
        if (currentFilter.type !== 'all' && cardType !== currentFilter.type) {
            showCard = false;
        }
        
        // 상태 필터
        if (currentFilter.status !== 'all') {
            const statusMap = {
                'available': '입양 가능',
                'preparing': '입양 준비 중',
                'completed': '입양 완료'
            };
            
            if (cardStatus !== statusMap[currentFilter.status]) {
                showCard = false;
            }
        }
        
        if (showCard) {
            card.style.display = 'flex';
            card.classList.add('fade-in');
        } else {
            card.style.display = 'none';
        }
    });
}

// ========================================
// 페이지네이션 초기화
// ========================================
function initializePagination() {
    renderPagination();
    
    document.getElementById('prevPage').addEventListener('click', () => {
        if (currentPage > 1) {
            currentPage--;
            renderPagination();
            scrollToTop();
        }
    });
    
    document.getElementById('nextPage').addEventListener('click', () => {
        if (currentPage < totalPages) {
            currentPage++;
            renderPagination();
            scrollToTop();
        }
    });
}

function renderPagination() {
    const pageNumbers = document.getElementById('pageNumbers');
    pageNumbers.innerHTML = '';
    
    const maxVisible = 5;
    let startPage = Math.max(1, currentPage - 2);
    let endPage = Math.min(totalPages, startPage + maxVisible - 1);
    
    if (endPage - startPage < maxVisible - 1) {
        startPage = Math.max(1, endPage - maxVisible + 1);
    }
    
    // 첫 페이지
    if (startPage > 1) {
        addPageButton(1);
        if (startPage > 2) {
            addEllipsis();
        }
    }
    
    // 중간 페이지들
    for (let i = startPage; i <= endPage; i++) {
        addPageButton(i);
    }
    
    // 마지막 페이지
    if (endPage < totalPages) {
        if (endPage < totalPages - 1) {
            addEllipsis();
        }
        addPageButton(totalPages);
    }
    
    // 이전/다음 버튼 상태
    document.getElementById('prevPage').disabled = currentPage === 1;
    document.getElementById('nextPage').disabled = currentPage === totalPages;
}

function addPageButton(pageNum) {
    const btn = document.createElement('button');
    btn.className = 'page-number' + (pageNum === currentPage ? ' active' : '');
    btn.textContent = pageNum;
    btn.addEventListener('click', () => {
        currentPage = pageNum;
        renderPagination();
        scrollToTop();
    });
    document.getElementById('pageNumbers').appendChild(btn);
}

function addEllipsis() {
    const ellipsis = document.createElement('span');
    ellipsis.className = 'page-ellipsis';
    ellipsis.textContent = '...';
    document.getElementById('pageNumbers').appendChild(ellipsis);
}

function scrollToTop() {
    window.scrollTo({ top: 0, behavior: 'smooth' });
}

// ========================================
// 뷰 토글 (그리드/리스트)
// ========================================
function initializeViewToggle() {
    const gridBtn = document.getElementById('gridViewBtn');
    const listBtn = document.getElementById('listViewBtn');
    const animalGrid = document.getElementById('animalGrid');
    
    gridBtn.addEventListener('click', () => {
        gridBtn.classList.add('active');
        listBtn.classList.remove('active');
        animalGrid.style.gridTemplateColumns = 'repeat(auto-fill, minmax(380px, 1fr))';
    });
    
    listBtn.addEventListener('click', () => {
        listBtn.classList.add('active');
        gridBtn.classList.remove('active');
        animalGrid.style.gridTemplateColumns = '1fr';
    });
}

// ========================================
// 정렬 버튼
// ========================================
function initializeSortButton() {
    const sortBtn = document.getElementById('sortBtn');
    const sortText = document.getElementById('sortText');
    
    const sortOptions = ['최신순', '오래된순', '이름순'];
    let currentSortIndex = 0;
    
    sortBtn.addEventListener('click', () => {
        currentSortIndex = (currentSortIndex + 1) % sortOptions.length;
        sortText.textContent = sortOptions[currentSortIndex];
        sortAnimals(sortOptions[currentSortIndex]);
    });
}

function sortAnimals(sortType) {
    const grid = document.getElementById('animalGrid');
    const cards = Array.from(document.querySelectorAll('.animal-card'));
    const addCard = document.querySelector('.add-card');
    
    // 정렬 로직 (실제로는 서버에서 처리)
    console.log('정렬 방식:', sortType);
    
    // 카드 재배치 애니메이션
    cards.forEach(card => card.classList.add('fade-in'));
}

// ========================================
// 동물 수정
// ========================================
function editAnimal(animalId) {
    location.href = `${ctp}/admin/animal/edit.do?animalId=${animalId}`;
}

// ========================================
// 상태 변경 모달
// ========================================
function changeStatus(animalId) {
    selectedAnimalId = animalId;
    const modal = document.getElementById('statusModal');
    modal.classList.add('active');
    document.body.style.overflow = 'hidden';
}

function closeStatusModal() {
    const modal = document.getElementById('statusModal');
    modal.classList.remove('active');
    document.body.style.overflow = 'auto';
    selectedAnimalId = null;
}

function confirmStatusChange() {
    const selectedStatus = document.querySelector('input[name="newStatus"]:checked');
    
    if (!selectedStatus) {
        alert('변경할 상태를 선택해주세요.');
        return;
    }
    
    const statusValue = selectedStatus.value;
    
    // AJAX 요청
    fetch(`${ctp}/admin/animal/updateStatus.do`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            animalId: selectedAnimalId,
            status: statusValue
        })
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            alert('상태가 변경되었습니다.');
            location.reload();
        } else {
            alert('상태 변경에 실패했습니다.');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('오류가 발생했습니다.');
    });
    
    closeStatusModal();
}

// 모달 외부 클릭 시 닫기
document.addEventListener('click', function(e) {
    const modal = document.getElementById('statusModal');
    if (e.target === modal) {
        closeStatusModal();
    }
});

// ========================================
// 동물 삭제
// ========================================
function deleteAnimal(animalId) {
    if (!confirm('정말로 이 동물 정보를 삭제하시겠습니까?\n삭제된 데이터는 복구할 수 없습니다.')) {
        return;
    }
    
    fetch(`${ctp}/admin/animal/delete.do`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ animalId: animalId })
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            alert('삭제되었습니다.');
            location.reload();
        } else {
            alert('삭제에 실패했습니다.');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('오류가 발생했습니다.');
    });
}

// ========================================
// 히스토리 보기 (FAB 버튼)
// ========================================
function showHistory() {
    location.href = `${ctp}/admin/animal/history.do`;
}

// ========================================
// 검색 기능 (헤더의 검색창)
// ========================================
function searchAnimals(keyword) {
    const cards = document.querySelectorAll('.animal-card');
    
    cards.forEach(card => {
        const animalName = card.querySelector('.card-title').textContent.toLowerCase();
        const animalId = card.querySelector('.card-id').textContent.toLowerCase();
        const breed = card.querySelector('.card-breed').textContent.toLowerCase();
        
        const searchTerm = keyword.toLowerCase();
        
        if (animalName.includes(searchTerm) || 
            animalId.includes(searchTerm) || 
            breed.includes(searchTerm)) {
            card.style.display = 'flex';
            card.classList.add('fade-in');
        } else {
            card.style.display = 'none';
        }
    });
}

// ========================================
// 키보드 단축키
// ========================================
document.addEventListener('keydown', function(e) {
    // ESC: 모달 닫기
    if (e.key === 'Escape') {
        closeStatusModal();
    }
    
    // Ctrl + N: 새 동물 등록
    if (e.ctrlKey && e.key === 'n') {
        e.preventDefault();
        location.href = `${ctp}/admin/animal/register.do`;
    }
    
    // 좌우 화살표: 페이지 이동
    if (e.key === 'ArrowLeft' && currentPage > 1) {
        currentPage--;
        renderPagination();
        scrollToTop();
    }
    
    if (e.key === 'ArrowRight' && currentPage < totalPages) {
        currentPage++;
        renderPagination();
        scrollToTop();
    }
});

// ========================================
// 카드 클릭 시 상세 페이지로 이동
// ========================================
document.addEventListener('click', function(e) {
    const card = e.target.closest('.animal-card');
    
    // 버튼 클릭이 아닌 경우에만 상세 페이지로 이동
    if (card && !e.target.closest('.card-actions')) {
        const animalId = card.querySelector('.card-id').textContent;
        location.href = `${ctp}/admin/animal/detail.do?animalId=${animalId}`;
    }
});

// ========================================
// 드래그 앤 드롭 정렬 (추후 구현)
// ========================================
let draggedElement = null;

function initializeDragAndDrop() {
    const cards = document.querySelectorAll('.animal-card');
    
    cards.forEach(card => {
        card.setAttribute('draggable', true);
        
        card.addEventListener('dragstart', function(e) {
            draggedElement = this;
            this.style.opacity = '0.5';
        });
        
        card.addEventListener('dragend', function(e) {
            this.style.opacity = '1';
        });
        
        card.addEventListener('dragover', function(e) {
            e.preventDefault();
        });
        
        card.addEventListener('drop', function(e) {
            e.preventDefault();
            if (draggedElement !== this) {
                const grid = document.getElementById('animalGrid');
                grid.insertBefore(draggedElement, this);
            }
        });
    });
}

// ========================================
// 무한 스크롤 (옵션)
// ========================================
function initializeInfiniteScroll() {
    let loading = false;
    
    window.addEventListener('scroll', () => {
        if (loading) return;
        
        const scrollPosition = window.innerHeight + window.scrollY;
        const documentHeight = document.documentElement.scrollHeight;
        
        if (scrollPosition >= documentHeight - 500) {
            loading = true;
            loadMoreAnimals();
        }
    });
}

function loadMoreAnimals() {
    // AJAX로 추가 데이터 로드
    fetch(`${ctp}/admin/animal/loadMore.do?page=${currentPage + 1}`)
        .then(response => response.json())
        .then(data => {
            if (data.animals && data.animals.length > 0) {
                appendAnimals(data.animals);
                currentPage++;
            }
            loading = false;
        })
        .catch(error => {
            console.error('Error:', error);
            loading = false;
        });
}

function appendAnimals(animals) {
    const grid = document.getElementById('animalGrid');
    const addCard = document.querySelector('.add-card');
    
    animals.forEach(animal => {
        const card = createAnimalCard(animal);
        grid.insertBefore(card, addCard);
    });
}

function createAnimalCard(animal) {
    const card = document.createElement('div');
    card.className = 'animal-card slide-up';
    card.dataset.type = animal.animalType;
    card.dataset.status = animal.adoptionStatus;
    
    card.innerHTML = `
        <div class="card-image">
            <img src="${ctp}/uploads/animals/${animal.mainImage}" alt="${animal.animalName}">
            <div class="status-badge status-${getStatusClass(animal.adoptionStatus)}">
                ${animal.adoptionStatus}
            </div>
            <div class="type-tags">
                <span class="type-tag">#${animal.animalType === 'dog' ? 'Dog' : 'Cat'}</span>
            </div>
        </div>
        <div class="card-content">
            <div class="card-header">
                <div class="card-title-group">
                    <h3 class="card-title">${animal.animalName}</h3>
                    <p class="card-id">${animal.animalId}</p>
                </div>
                <div class="card-info-group">
                    <p class="card-breed">${animal.breed}</p>
                    <p class="card-age">${animal.age}</p>
                </div>
            </div>
            <div class="card-details">
                <div class="detail-item">
                    <span class="material-symbols-outlined">calendar_today</span>
                    <span>구조일: ${animal.rescueDate}</span>
                </div>
                <div class="detail-item">
                    <span class="material-symbols-outlined">location_on</span>
                    <span>${animal.rescueLocation}</span>
                </div>
            </div>
            <div class="card-actions">
                <button class="action-btn btn-edit" onclick="editAnimal('${animal.animalId}')">수정</button>
                <button class="action-btn btn-status" onclick="changeStatus('${animal.animalId}')">상태 변경</button>
                <button class="action-btn btn-delete" onclick="deleteAnimal('${animal.animalId}')">
                    <span class="material-symbols-outlined">delete</span>
                </button>
            </div>
        </div>
    `;
    
    return card;
}

function getStatusClass(status) {
    const statusMap = {
        '입양 가능': 'available',
        '입양 준비 중': 'preparing',
        '입양 완료': 'completed'
    };
    return statusMap[status] || 'available';
}

// ========================================
// 일괄 작업 (체크박스 선택)
// ========================================
let selectedAnimals = [];

function toggleSelectMode() {
    const cards = document.querySelectorAll('.animal-card');
    const isSelectMode = document.body.classList.toggle('select-mode');
    
    if (isSelectMode) {
        cards.forEach(card => {
            const checkbox = document.createElement('input');
            checkbox.type = 'checkbox';
            checkbox.className = 'card-checkbox';
            checkbox.addEventListener('change', function() {
                const animalId = card.querySelector('.card-id').textContent;
                if (this.checked) {
                    selectedAnimals.push(animalId);
                } else {
                    selectedAnimals = selectedAnimals.filter(id => id !== animalId);
                }
                updateBulkActionBar();
            });
            card.querySelector('.card-image').appendChild(checkbox);
        });
        showBulkActionBar();
    } else {
        cards.forEach(card => {
            const checkbox = card.querySelector('.card-checkbox');
            if (checkbox) checkbox.remove();
        });
        hideBulkActionBar();
        selectedAnimals = [];
    }
}

function updateBulkActionBar() {
    const bar = document.getElementById('bulkActionBar');
    if (bar) {
        bar.querySelector('.selected-count').textContent = `${selectedAnimals.length}개 선택됨`;
    }
}

function bulkDelete() {
    if (selectedAnimals.length === 0) {
        alert('삭제할 항목을 선택해주세요.');
        return;
    }
    
    if (!confirm(`선택한 ${selectedAnimals.length}개의 동물 정보를 삭제하시겠습니까?`)) {
        return;
    }
    
    fetch(`${ctp}/admin/animal/bulkDelete.do`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ animalIds: selectedAnimals })
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            alert('삭제되었습니다.');
            location.reload();
        } else {
            alert('삭제에 실패했습니다.');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('오류가 발생했습니다.');
    });
}

// ========================================
// 엑셀 내보내기
// ========================================
function exportToExcel() {
    location.href = `${ctp}/admin/animal/export.do`;
}

// ========================================
// 통계 대시보드 열기
// ========================================
function openStatistics() {
    window.open(`${ctp}/admin/animal/statistics.do`, '_blank', 'width=1200,height=800');
}

// ========================================
// 유틸리티 함수
// ========================================
function formatDate(dateString) {
    const date = new Date(dateString);
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${year}년 ${month}월 ${day}일`;
}

function debounce(func, wait) {
    let timeout;
    return function executedFunction(...args) {
        const later = () => {
            clearTimeout(timeout);
            func(...args);
        };
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
    };
}

// ========================================
// 초기화 완료 로그
// ========================================
console.log('🐾 Animal List Management Initialized');
console.log(`📊 Current Page: ${currentPage}/${totalPages}`);
console.log(`🔍 Filter: Type=${currentFilter.type}, Status=${currentFilter.status}`);