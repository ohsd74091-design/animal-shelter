document.addEventListener('DOMContentLoaded', function () {
    const ctp = document.body.dataset.ctp || '';

    /* ── 현재 선택된 adoptionId / animalId / 지도 인스턴스 ── */
    let currentAdoptionId = null;
    let currentAnimalId   = null; // ✅ 추가
    let map = null;

    /* ── 필터 버튼 클릭 ── */
    document.querySelectorAll('.adp-filter-btn').forEach(btn => {
        btn.addEventListener('click', function () {
            document.querySelectorAll('.adp-filter-btn').forEach(b => b.classList.remove('active'));
            this.classList.add('active');
            filterCards();
        });
    });

    /* ── 검색 입력 ── */
    document.getElementById('adpSearchInput').addEventListener('input', filterCards);

    /* ── 카드 필터링 함수 ── */
    function filterCards() {
        const activeFilter = document.querySelector('.adp-filter-btn.active')?.dataset.status || 'all';
        const keyword = document.getElementById('adpSearchInput').value.trim().toLowerCase();

        document.querySelectorAll('.adp-card').forEach(card => {
            const status = card.dataset.status || '';
            const search = card.dataset.search || '';

            const statusMatch  = (activeFilter === 'all') || (status === activeFilter);
            const keywordMatch = !keyword || search.toLowerCase().includes(keyword);

            card.style.display = (statusMatch && keywordMatch) ? '' : 'none';
        });
    }

    /* ── 입양 신청 상세 표시 ── */
    window.showAdoptionDetail = function (el) {
        document.querySelectorAll('.adp-card').forEach(c => c.classList.remove('active'));
        el.classList.add('active');

        const d = el.querySelector('.adp-data').dataset;
        currentAdoptionId = d.adoptionid;
        currentAnimalId   = d.animalid; // ✅ 추가

        /* ── 헤더 ── */
        const badge = document.getElementById('dd-priorityBadge');
        if (d.status === '심사중') {
            badge.textContent = 'Priority · 심사중';
            badge.className   = 'adp-priority-badge adp-priority-badge--pending';
        } else if (d.status === '승인') {
            badge.textContent = '승인 완료';
            badge.className   = 'adp-priority-badge adp-priority-badge--approved';
        } else {
            badge.textContent = '반려';
            badge.className   = 'adp-priority-badge adp-priority-badge--rejected';
        }

        document.getElementById('dd-applyDate').textContent = '신청일: ' + d.applydate;
        document.getElementById('dd-title').textContent =
            d.memberid + ' 님의 ' + d.animalname + ' 입양 신청';

        /* ── 신청자 프로필 ── */
        const profileImg = document.getElementById('dd-profileImg');
        if (d.profileimg && d.profileimg !== 'null' && d.profileimg !== '') {
            profileImg.src = ctp + '/member/profileImage.do?fileName=' + d.profileimg;
        } else {
            profileImg.src = ctp + '/images/default-profile.png';
        }

        document.getElementById('dd-memberId').textContent = d.memberid;
        document.getElementById('dd-job').textContent      = d.job || '직업 미기재';

        /* ── 신청 정보 ── */
        document.getElementById('dd-housingType').textContent   = d.housingtype   || '-';
        document.getElementById('dd-petExperience').textContent = d.petexperience || '-';
        document.getElementById('dd-visitDate').textContent     = d.visitdate     || '-';
        document.getElementById('dd-address').textContent       = d.address       || '-';

        /* ── 입양 사유 ── */
        document.getElementById('dd-adoptionReason').textContent =
            d.adoptionreason || '(입력된 내용 없음)';

        /* ── 동물 정보 ── */
        const petImg = document.getElementById('dd-petImage');
        if (d.mainimage && d.mainimage !== 'null' && d.mainimage !== '') {
            petImg.src = ctp + '/animal/image?fileName=' + d.mainimage;
        } else {
            petImg.src = ctp + '/view/images/default-animal.png';
        }

        document.getElementById('dd-petBadge').textContent   = 'MEET ' + d.animalname;
        document.getElementById('dd-animalName').textContent = d.animalname;
        document.getElementById('dd-animalDesc').textContent =
            (d.breed || '-') + ' · ' + (d.age ? d.age + '세' : '-');

        /* 성격 태그 */
        const tagsEl = document.getElementById('dd-personalityTags');
        tagsEl.innerHTML = '';
        if (d.personality) {
            d.personality.split(',').forEach(tag => {
                const span = document.createElement('span');
                span.className   = 'adp-pet-tag';
                span.textContent = '#' + tag.trim();
                tagsEl.appendChild(span);
            });
        }

        document.getElementById('dd-visitDatePet').textContent = d.visitdate || '미정';

        /* ── 처리 액션 영역 표시/숨김 ── */
        const actionArea   = document.getElementById('dd-actionArea');
        const rejectResult = document.getElementById('dd-rejectResult');
        const rejectTextEl = document.getElementById('dd-rejectResultText');

        if (d.status === '심사중') {
            actionArea.style.display   = 'flex';
            rejectResult.style.display = 'none';
            document.getElementById('dd-rejectReason').value = '';
        } else if (d.status === '반려') {
            actionArea.style.display   = 'none';
            rejectResult.style.display = 'flex';
            rejectTextEl.textContent   = d.rejectreason || '(반려 사유 없음)';
        } else {
            actionArea.style.display   = 'none';
            rejectResult.style.display = 'none';
        }

        /* ── 상세 패널 전환 ── */
        document.getElementById('adpDetailEmpty').style.display   = 'none';
        document.getElementById('adpDetailContent').style.display = 'flex';

        /* ── 주소 지도 표시 ── */
        const address = d.address || '';
        if (address && address !== '-') {
            renderMap(address);
        } else {
            const mapSection = document.getElementById('dd-mapSection');
            if (mapSection) mapSection.style.display = 'none';
        }
    };

    /* ── 주소 → 지도 렌더링 ── */
    function renderMap(address) {
        const mapSection = document.getElementById('dd-mapSection');
        const mapEl      = document.getElementById('dd-map');
        if (!mapSection || !mapEl) return;

        mapSection.style.display = 'flex';

        const encodedAddress = encodeURIComponent(address);
        fetch('https://nominatim.openstreetmap.org/search?format=json&q='
              + encodedAddress + '&countrycodes=kr&limit=1', {
            headers: { 'Accept-Language': 'ko' }
        })
        .then(res => res.json())
        .then(results => {
            if (map) { map.remove(); map = null; mapEl.innerHTML = ''; }

            let lat, lng;
            if (results && results.length > 0) {
                lat = parseFloat(results[0].lat);
                lng = parseFloat(results[0].lon);
            } else {
                lat = 37.5665; lng = 126.9780;
                document.getElementById('dd-mapStatus').textContent
                    = '⚠ 정확한 위치를 찾을 수 없어 기본 위치로 표시됩니다.';
                document.getElementById('dd-mapStatus').style.display = 'block';
            }

            map = L.map('dd-map').setView([lat, lng], 15);
            L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                attribution: '© <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a>',
                maxZoom: 19
            }).addTo(map);
            L.marker([lat, lng]).addTo(map)
             .bindPopup('<strong>' + address + '</strong>').openPopup();
        })
        .catch(() => { if (mapSection) mapSection.style.display = 'none'; });
    }

    /* ── 입양 상태 업데이트 (승인/반려) ── */
    window.updateAdoptionStatus = function (status) {
        if (!currentAdoptionId) return;

        let rejectReason = '';
        if (status === '반려') {
            rejectReason = document.getElementById('dd-rejectReason').value.trim();
            if (!rejectReason) {
                alert('반려 사유를 입력해주세요.');
                document.getElementById('dd-rejectReason').focus();
                return;
            }
        }

        const msg = status === '승인' ? '이 신청을 승인하시겠습니까?' : '이 신청을 반려하시겠습니까?';
        if (!confirm(msg)) return;

        const params = new URLSearchParams();
        params.append('adoptionId',   currentAdoptionId);
        params.append('animalId',     currentAnimalId);   // ✅ 추가
        params.append('status',       status);
        params.append('rejectReason', rejectReason);

        fetch(ctp + '/admin/adoption/status.do', {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: params
        })
        .then(res => res.json())
        .then(data => {
            if (data.success) {
                alert(status === '승인' ? '승인 처리되었습니다.' : '반려 처리되었습니다.');
                location.reload();
            } else {
                alert(data.message || '처리 중 오류가 발생했습니다.');
            }
        })
        .catch(() => alert('요청 처리 중 오류가 발생했습니다.'));
    };
	
	// ── URL 파라미터로 넘어온 adoptionId가 있으면 해당 카드 자동 선택 ──
	// 대시보드 피드에서 클릭해서 이동할 때 바로 상세 표시
	const urlParams = new URLSearchParams(location.search);
	const targetAdoptionId = urlParams.get('adoptionId');
	if (targetAdoptionId) {
	    // data-adoption-id 속성으로 해당 카드 찾기
	    const targetCard = document.querySelector(
	        '.adp-card[data-adoption-id="' + targetAdoptionId + '"]'
	    );
	    if (targetCard) {
	        // 해당 카드로 스크롤
	        targetCard.scrollIntoView({ behavior: 'smooth', block: 'center' });
	        // 클릭 이벤트 실행 → 오른쪽 상세 패널 표시
	        showAdoptionDetail(targetCard);
	    }
	}
});
