'use strict';

/* ─────────────────────────────────────────────────────
   오늘 날짜 표시
───────────────────────────────────────────────────── */
document.addEventListener('DOMContentLoaded', function () {

	/* 이번 주 날짜 범위 헤더 출력 (월~일) */
	const weekLabel = document.getElementById('weekRangeLabel');
	if (weekLabel) {
	    const now   = new Date();
	    // 이번 주 월요일 계산 (Oracle IW 기준과 동일)
	    const day   = now.getDay(); // 0=일
	    const diffMon = (day === 0) ? -6 : 1 - day; // 월요일 오프셋
	    const mon   = new Date(now); mon.setDate(now.getDate() + diffMon);
	    const sun   = new Date(mon); sun.setDate(mon.getDate() + 6);
	    const fmt   = d => `${d.getMonth()+1}/${d.getDate()}`;
	    weekLabel.textContent = `${now.getFullYear()}년 ${fmt(mon)} ~ ${fmt(sun)}`;
	}
});

/* ─────────────────────────────────────────────────────
   일정 상세 모달
   - 파라미터: type(ADOPTION/VOLUNTEER), id, title, content, fullTime
───────────────────────────────────────────────────── */

/**
 * 오늘의 일정 항목 클릭 시 상세 모달 오픈
 * @param {string} type      - 'ADOPTION' 또는 'VOLUNTEER'
 * @param {string} id        - 레코드 PK
 * @param {string} title     - 일정 제목
 * @param {string} content   - 일정 내용
 * @param {string} fullTime  - 전체 시각 문자열 (YYYY-MM-DD HH:MI)
 */
function openDetailModal(type, id, title, content, fullTime) {
    // 유형 라벨 변환
    const typeLabel = type === 'ADOPTION'  ? '🐾 입양 상담 예약'
                    : type === 'VOLUNTEER' ? '🤝 봉사 활동 신청'
                    : type;

    // 모달 내용 채우기
    document.getElementById('detailModalTitle').textContent = title || '일정 상세';
    document.getElementById('detailTime').textContent       = fullTime || '—';
    document.getElementById('detailContent').textContent    = content  || '—';
    document.getElementById('detailType').textContent       = typeLabel;

    // 모달 표시
    document.getElementById('scheduleDetailModal').style.display = 'flex';
}

/** 일정 상세 모달 닫기 */
function closeDetailModal() {
    document.getElementById('scheduleDetailModal').style.display = 'none';
}

/* ─────────────────────────────────────────────────────
   캘린더 모달
   - window.ALL_SCHEDULE_LIST: JSP에서 주입된 이번 달 전체 일정 배열
   - calState: 현재 보여주는 년/월 상태 관리
───────────────────────────────────────────────────── */

// 캘린더 현재 년/월 상태 (기본값: 오늘)
const calState = {
    year  : new Date().getFullYear(),
    month : new Date().getMonth() // 0-indexed
};

/** 캘린더 모달 열기 */
function openCalendarModal() {
    document.getElementById('calendarModal').style.display = 'flex';
    renderCalendar(calState.year, calState.month);
}

/** 캘린더 모달 닫기 */
function closeCalendarModal() {
    document.getElementById('calendarModal').style.display = 'none';
    // 날짜 상세 패널 닫기
    document.getElementById('calDayDetail').style.display = 'none';
}

/**
 * 달 이동 버튼 처리 — 해당 달 일정을 AJAX로 로드 후 렌더링
 * @param {number} delta - +1(다음달) 또는 -1(이전달)
 */
function changeCalMonth(delta) {
    calState.month += delta;
    // 월 경계 처리
    if (calState.month > 11) { calState.month = 0;  calState.year++; }
    if (calState.month <  0) { calState.month = 11; calState.year--; }

    // 날짜 상세 패널 닫기
    document.getElementById('calDayDetail').style.display = 'none';

    const ctp = document.querySelector('meta[name="contextPath"]')?.content
              || window.CONTEXT_PATH
              || '';

    // 이번 달이면 JSP에서 주입된 데이터 재사용, 다른 달이면 AJAX 요청
    const now = new Date();
    if (calState.year === now.getFullYear() && calState.month === now.getMonth()) {
        // 현재 달: JSP에서 이미 주입된 window.ALL_SCHEDULE_LIST 사용
        renderCalendar(calState.year, calState.month);
        return;
    }

    // 다른 달: AJAX로 해당 달 일정 로드
    const url = ctp + '/admin/getMonthlySchedule.do'
              + '?year=' + calState.year
              + '&month=' + (calState.month + 1);

    fetch(url)
        .then(function (res) { return res.json(); })
        .then(function (data) {
            // 응답 데이터를 window.ALL_SCHEDULE_LIST에 임시 교체 후 렌더링
            // Oracle map은 대문자 키이므로 camelCase로 정규화
            window._TEMP_SCHEDULE = data.map(function (s) {
                return {
                    type    : s.TYPE     || s.type,
                    id      : s.ID       || s.id,
                    title   : s.TITLE    || s.title,
                    content : s.CONTENT  || s.content,
                    time    : s.TIME     || s.time,
                    fullTime: s.FULL_TIME || s.fullTime,
                    dateKey : s.DATE_KEY  || s.dateKey
                };
            });
            // 렌더링 시 임시 데이터 사용을 위해 buildScheduleMap에서 참조
            renderCalendar(calState.year, calState.month);
        })
        .catch(function (err) {
            console.error('달력 일정 로드 실패:', err);
            renderCalendar(calState.year, calState.month);
        });
}

/**
 * 캘린더 격자 렌더링
 * @param {number} year  - 4자리 연도
 * @param {number} month - 0-indexed 월
 */
function renderCalendar(year, month) {
    const title   = document.getElementById('calTitle');
    const grid    = document.getElementById('calGrid');
    const today   = new Date();

    // 제목 갱신
    title.textContent = `${year}년 ${month + 1}월`;

    // 해당 월 1일과 마지막 날
    const firstDay   = new Date(year, month, 1).getDay();  // 0=일
    const lastDate   = new Date(year, month + 1, 0).getDate();

    // 이번 달 일정 날짜 맵: { 'YYYY-MM-DD': [schedule, ...] }
    const scheduleMap = buildScheduleMap(year, month);

    let html = '';

    // 1일 이전 빈 셀
    for (let i = 0; i < firstDay; i++) {
        html += `<div class="cal-cell cal-cell--empty"></div>`;
    }

    // 날짜 셀
    for (let d = 1; d <= lastDate; d++) {
        // 날짜 키: YYYY-MM-DD
        const mm      = String(month + 1).padStart(2, '0');
        const dd      = String(d).padStart(2, '0');
        const dateKey = `${year}-${mm}-${dd}`;

        const isToday = (year === today.getFullYear()
                      && month === today.getMonth()
                      && d    === today.getDate());

        const events  = scheduleMap[dateKey] || [];
        const hasEv   = events.length > 0;

        // 일요일=0, 토요일=6
        const dow = (firstDay + d - 1) % 7;
        const isSun = (dow === 0);
        const isSat = (dow === 6);

        // 이벤트 도트 (최대 3개 표시)
        const dots = events.slice(0, 3).map(ev => {
            const color = ev.type === 'ADOPTION' ? '#e56b2e' : '#0f9d7a';
            return `<span class="cal-dot" style="background:${color};"></span>`;
        }).join('');

        html += `
            <div class="cal-cell ${isToday ? 'cal-cell--today' : ''} ${hasEv ? 'cal-cell--has-event' : ''}"
                 data-date="${dateKey}"
                 onclick="showDaySchedule('${dateKey}')">
                <span class="cal-date ${isSun ? 'cal-date--sun' : ''} ${isSat ? 'cal-date--sat' : ''}">${d}</span>
                <div class="cal-dots">${dots}</div>
            </div>`;
    }

    grid.innerHTML = html;
}

/**
 * 해당 년월 일정을 날짜별로 그룹핑
 * - 이번 달: window.ALL_SCHEDULE_LIST (JSP 주입)
 * - 다른 달: window._TEMP_SCHEDULE (AJAX 로드)
 */
function buildScheduleMap(year, month) {
    const mm  = String(month + 1).padStart(2, '0');
    const pfx = year + '-' + mm;

    const map  = {};
    // 현재 달이면 JSP 주입 데이터, 아니면 AJAX 임시 데이터 사용
    const now  = new Date();
    const list = (year === now.getFullYear() && month === now.getMonth())
                 ? (window.ALL_SCHEDULE_LIST || [])
                 : (window._TEMP_SCHEDULE    || []);

    list.forEach(function (s) {
        if (!s.dateKey || !s.dateKey.startsWith(pfx)) return;
        if (!map[s.dateKey]) map[s.dateKey] = [];
        map[s.dateKey].push(s);
    });

    return map;
}

/**
 * 캘린더에서 날짜 셀 클릭 → 해당 날짜의 일정 목록 표시
 * @param {string} dateKey - 'YYYY-MM-DD'
 */
function showDaySchedule(dateKey) {
    const detail  = document.getElementById('calDayDetail');
    const dayTitle= document.getElementById('calDayTitle');
    const dayList = document.getElementById('calDayList');

    // 선택 셀 강조 초기화 후 재적용
    document.querySelectorAll('.cal-cell').forEach(c => c.classList.remove('cal-cell--selected'));
    const selectedCell = document.querySelector(`.cal-cell[data-date="${dateKey}"]`);
    if (selectedCell) selectedCell.classList.add('cal-cell--selected');

    // 해당 날짜 일정 필터
    const list = (window.ALL_SCHEDULE_LIST || []).filter(s => s.dateKey === dateKey);

    dayTitle.textContent = `${dateKey} 일정 (${list.length}건)`;

    if (list.length === 0) {
        dayList.innerHTML = `<p style="font-size:13px;color:#94a3b8;text-align:center;padding:12px 0;">
            이 날은 일정이 없습니다.
        </p>`;
    } else {
		dayList.innerHTML = list.map(s => {
		    const icon  = s.type === 'ADOPTION' ? 'event_available'
		                : s.type === 'RECRUIT'  ? 'volunteer_activism'
		                : 'volunteer_activism';
		    const color = s.type === 'ADOPTION' ? '#e56b2e'
		                : s.type === 'RECRUIT'  ? '#7c3aed'   /* 보라: 모집글 구분 */
		                : '#0f9d7a';
		    const label = s.type === 'ADOPTION' ? '입양 상담'
		                : s.type === 'RECRUIT'  ? '봉사 모집'
		                : '봉사 활동';

		    // moveUrl 있으면 클릭 시 상세 이동, 없으면 그냥 표시
		    const clickHandler = s.moveUrl && s.moveUrl !== '#'
		        ? `onclick="location.href='${s.moveUrl}'" style="cursor:pointer;"`
		        : '';

		    return `
		        <div class="cal-schedule-item" style="border-left:3px solid ${color};" ${clickHandler}>
		            <div style="display:flex;align-items:center;gap:6px;margin-bottom:3px;">
		                <span class="material-symbols-outlined" style="font-size:14px;color:${color};">${icon}</span>
		                <strong style="font-size:13px;">${s.title || '—'}</strong>
		                <span style="font-size:11px;color:#94a3b8;margin-left:auto;">${s.time || ''}</span>
		            </div>
		            <p style="font-size:12px;color:#64748b;margin:0;">${s.content || ''}</p>
		            <div style="display:flex;align-items:center;justify-content:space-between;margin-top:6px;">
		                <span class="tag" style="background:${color}22;color:${color};">${label}</span>
		                ${s.moveUrl && s.moveUrl !== '#'
		                    ? `<span style="font-size:11px;color:${color};font-weight:700;">상세 보기 →</span>`
		                    : ''}
		            </div>
		        </div>`;
		}).join('');
    }

    detail.style.display = 'block';
}

/* ─────────────────────────────────────────────────────
   ESC 키로 모달 닫기
───────────────────────────────────────────────────── */
document.addEventListener('keydown', function (e) {
    if (e.key === 'Escape') {
        closeDetailModal();
        closeCalendarModal();
    }
});

/* ─────────────────────────────────────────────────────
   실시간 활동 피드 페이징 로직
───────────────────────────────────────────────────── */
document.addEventListener('DOMContentLoaded', function () {
    const feedData = window.ACTIVITY_FEED_DATA || [];
    const itemsPerPage = 5;
    let currentPage = 1;

	function renderFeed() {
	    const container = document.getElementById('feedListContainer');
	    if (!container) return;

	    const itemsPerPage = 5;
	    const totalPages = Math.ceil(window.ACTIVITY_FEED_DATA.length / itemsPerPage);
	    
	    if (window.ACTIVITY_FEED_DATA.length === 0) {
	        container.innerHTML = `<div style="text-align:center; padding-top:100px; color:var(--text-sub);">활동 내역이 없습니다.</div>`;
	        return;
	    }

	    const start = (currentPage - 1) * itemsPerPage;
	    const pageItems = window.ACTIVITY_FEED_DATA.slice(start, start + itemsPerPage);

		container.innerHTML = pageItems.map(item => {
		    const config = {
		        'ADOPTION':  { icon: 'pets',              color: '#e56b2e', bg: '#fff1e8', label: '입양' },
		        'RESCUE':    { icon: 'emergency',          color: '#d92d20', bg: '#fdecec', label: '구조' },
		        'VOLUNTEER': { icon: 'volunteer_activism', color: '#0f9d7a', bg: '#e8faf4', label: '봉사' },
		        'DONATION':  { icon: 'redeem',             color: '#3b82f6', bg: '#eff6ff', label: '후원' }
		    }[item.type] || { icon: 'notifications', color: '#64748b', bg: '#f1f5f9', label: '알림' };

		    // 타입별 상세 페이지 URL 생성 (contextPath는 admin-header의 data-ctp에서 가져옴)
		    const ctp = document.querySelector('.admin-header')?.dataset.ctp || '';
			// VOLUNTEER: id = "recruitId_volunteerId" 형식
			const urlMap = {
			    'ADOPTION' : ctp + '/admin/adoption/list.do?adoptionId=' + item.id,
			    'VOLUNTEER': (function() {
			        // "12_34" → recruitId=12, volunteerId=34
			        const parts = item.id.split('_');
			        const recruitId   = parts[0] || '';
			        const volunteerId = parts[1] || '';
			        return ctp + '/admin/volunteer/manage.do?recruitId=' + recruitId
			                   + '&volunteerId=' + volunteerId;
			    })(),
			    'RESCUE': ctp + '/admin/report/list.do?reportId=' + item.id,
			    'DONATION': ctp + '/admin/donation.do'
			};
		    const moveUrl = urlMap[item.type] || '#';

		    return `
		        <div class="feed-item"
		             style="cursor:pointer;"
		             onclick="location.href='${moveUrl}'">
		            <div class="feed-icon-box" style="background:${config.bg}; color:${config.color};">
		                <span class="material-symbols-outlined">${config.icon}</span>
		            </div>
		            <div class="feed-content">
		                <div class="feed-info">
		                    <span class="feed-label" style="color:${config.color};">${config.label}</span>
		                    <span class="feed-time">${item.date}</span>
		                </div>
		                <p class="feed-title">${item.title}</p>
		            </div>
		        </div>`;
		}).join('');

	    renderPagination(totalPages);
	}

	function renderPagination(totalPages) {
	    const nav = document.getElementById('feedPagination');
	    if (!nav || totalPages <= 1) return;

	    let html = '';
	    
	    // [이전] 버튼
	    html += `<button class="feed-nav-btn" onclick="moveFeedPage(${currentPage - 1})" ${currentPage === 1 ? 'disabled' : ''}>❮ 이전</button>`;

	    // 숫자 버튼 (최대 5개씩 표시)
	    const startPage = Math.max(1, currentPage - 2);
	    const endPage = Math.min(totalPages, startPage + 4);

	    for (let i = startPage; i <= endPage; i++) {
	        html += `<button class="feed-page-btn ${i === currentPage ? 'active' : ''}" onclick="moveFeedPage(${i})">${i}</button>`;
	    }

	    // [다음] 버튼
	    html += `<button class="feed-nav-btn" onclick="moveFeedPage(${currentPage + 1})" ${currentPage === totalPages ? 'disabled' : ''}>다음 ❯</button>`;

	    nav.innerHTML = html;
	}

    window.moveFeedPage = function(p) {
        currentPage = p;
        renderFeed();
    };

    renderFeed();
});