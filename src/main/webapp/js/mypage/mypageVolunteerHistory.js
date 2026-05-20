/**
 * mypageVolunteerHistory.js
 * 봉사 신청 내역 페이지 전용 스크립트
 *
 * 주요 기능:
 *   1. 달력 렌더링 - 3줄(21셀) 고정 분할 방식
 *      - 앞 절반: 1일 ~ 21일 범위
 *      - 뒤 절반: 15일 ~ 말일 범위
 *      - < > 버튼으로 앞/뒤 절반 전환
 *      - 월 경계에서 이전/다음 달로 이동
 *   2. 상태 필터링
 *   3. 봉사 신청 취소 AJAX
 */

'use strict';

/* --------------------------------------------------------
   달력 상태
-------------------------------------------------------- */
const today = new Date();
let calYear   = today.getFullYear();
let calMonth  = today.getMonth(); // 0-based

// 초기 절반: 오늘 날짜가 속한 행 기준으로 결정
// 1일 요일 + 오늘 날짜로 오늘이 몇 번째 행인지 계산
const _initFirstDay = new Date(calYear, calMonth, 1).getDay();
const _initTodayRow = Math.floor((_initFirstDay + today.getDate() - 1) / 7);
const _initLastDate = new Date(calYear, calMonth + 1, 0).getDate();
const _initTotalRows = Math.ceil((_initFirstDay + _initLastDate) / 7);
let calHalf = _initTodayRow <= 2 ? 0 : 1;

// 달력에 표시할 봉사 일정 {날짜(YYYY-MM-DD): {title, isCompleted}}
let calEventMap = {};

/* --------------------------------------------------------
   초기화
-------------------------------------------------------- */
document.addEventListener('DOMContentLoaded', () => {
    fetchCalendarEvents(calYear, calMonth + 1).then(() => {
        renderCalendar(calYear, calMonth, calHalf);
    });

    document.getElementById('calPrev').addEventListener('click', () => {
        if (calHalf === 1) {
            // 뒤 → 앞
            calHalf = 0;
            renderCalendar(calYear, calMonth, calHalf);
        } else {
            // 앞 → 이전 달 뒤 절반
            calMonth--;
            if (calMonth < 0) { calMonth = 11; calYear--; }
            calHalf = 1;
            fetchCalendarEvents(calYear, calMonth + 1).then(() => renderCalendar(calYear, calMonth, calHalf));
        }
    });

    document.getElementById('calNext').addEventListener('click', () => {
        if (calHalf === 0) {
            // 앞 → 뒤
            calHalf = 1;
            renderCalendar(calYear, calMonth, calHalf);
        } else {
            // 뒤 → 다음 달 앞 절반
            calMonth++;
            if (calMonth > 11) { calMonth = 0; calYear++; }
            calHalf = 0;
            fetchCalendarEvents(calYear, calMonth + 1).then(() => renderCalendar(calYear, calMonth, calHalf));
        }
    });
});


/* --------------------------------------------------------
   달력 이벤트 AJAX 조회
   @param {number} year
   @param {number} month - 1-based
-------------------------------------------------------- */
function fetchCalendarEvents(year, month) {
    return fetch(`${CONTEXT_PATH}/mypage/volunteerHistory/calendar.do?year=${year}&month=${month}`)
        .then(res => res.json())
        .then(data => {
            calEventMap = {};
            if (Array.isArray(data)) {
                data.forEach(item => {
                    calEventMap[item.dateStr] = {
                        title:       item.title,
                        isCompleted: item.isCompleted
                    };
                });
            }
        })
        .catch(err => {
            console.error('[fetchCalendarEvents] 오류:', err);
            calEventMap = {};
        });
}


/* --------------------------------------------------------
   달력 렌더링 - 3줄(21셀) 고정 분할
   @param {number} year
   @param {number} month  - 0-based
   @param {number} half   - 0: 앞 3줄, 1: 뒤 3줄
-------------------------------------------------------- */
function renderCalendar(year, month, half) {
    const MONTH_KO = ['1월','2월','3월','4월','5월','6월',
                      '7월','8월','9월','10월','11월','12월'];

    document.getElementById('calendarTitle').textContent =
        `${year}년 ${MONTH_KO[month]}`;

    const grid    = document.getElementById('calendarGrid');
    grid.innerHTML = '';

    const firstDay = new Date(year, month, 1).getDay(); // 1일 요일 (0=Sun)
    const lastDate = new Date(year, month + 1, 0).getDate(); // 말일

    // 전체 셀 배열: 빈칸(null) + 날짜(1~lastDate)
    const cells = [];
    for (let i = 0; i < firstDay; i++) cells.push(null);
    for (let d = 1; d <= lastDate; d++) cells.push(d);

    // 총 행 수
    const totalRows = Math.ceil(cells.length / 7);

    // 앞 절반: 앞 3행 / 뒤 절반: 뒤 3행
    // 단, 총 행이 3 이하면 앞=뒤 동일
    let startRow, endRow;
    if (half === 0) {
        startRow = 0;
        endRow   = 2;
    } else {
        endRow   = totalRows - 1;
        startRow = Math.max(0, endRow - 2);
    }

    const startCell = startRow * 7;
    const endCell   = Math.min(endRow * 7 + 6, cells.length - 1);

    // 항상 21셀(3행 × 7) 렌더링
    for (let i = startCell; i < startCell + 21; i++) {
        const d = (i <= endCell) ? cells[i] : undefined;

        const cell = document.createElement('div');

        if (d == null || d === undefined) {
            cell.className = 'cal-day cal-day--empty';
            if (d === undefined) cell.textContent = ''; // 말일 이후 빈칸
            grid.appendChild(cell);
            continue;
        }

        cell.className = 'cal-day';

        const dateStr = `${year}-${String(month + 1).padStart(2,'0')}-${String(d).padStart(2,'0')}`;
        const isToday = (year  === today.getFullYear() &&
                         month === today.getMonth()    &&
                         d     === today.getDate());
        const event   = calEventMap[dateStr];

        if (isToday) cell.classList.add('cal-day--today');
        if (event) {
            cell.classList.add(event.isCompleted ? 'cal-day--completed' : 'cal-day--has-event');
            const tooltip = document.createElement('div');
            tooltip.className = 'cal-day__tooltip';
            tooltip.textContent = event.title;
            cell.appendChild(tooltip);
        }

        const span = document.createElement('span');
        span.textContent = d;
        cell.appendChild(span);

        grid.appendChild(cell);
    }
}


/* --------------------------------------------------------
   상태 필터링
   @param {string} status - DB STATUS 값 or 'all' or '봉사완료'
-------------------------------------------------------- */
function filterByStatus(status) {
    const cards = document.querySelectorAll('.vol-card');

    cards.forEach(card => {
        const cardStatus    = card.dataset.status;
        const isCompleted   = card.dataset.completed === 'true';

        let show = false;

        if (!status) {
            show = true;
        } else if (status === '봉사완료') {
            show = isCompleted;
        } else if (status === '승인') {
            show = cardStatus === '승인' && !isCompleted;
        } else {
            show = cardStatus === status;
        }

        card.style.display = show ? '' : 'none';
    });
}


/* --------------------------------------------------------
   봉사 신청 취소 AJAX
   @param {number} volunteerId
-------------------------------------------------------- */
function cancelVolunteer(volunteerId) {
    if (!confirm('봉사 신청을 취소하시겠습니까?')) return;

    fetch(`${CONTEXT_PATH}/mypage/volunteerHistory/cancel.do`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: `volunteerId=${encodeURIComponent(volunteerId)}`
    })
    .then(res => res.json())
    .then(data => {
        if (data.result === 'success') {
            // 카드 DOM 제거
            const btn  = document.querySelector('.btn-vol--cancel[onclick*="' + volunteerId + '"]');
            const card = btn ? btn.closest('.vol-card') : null;
            if (card) {
                card.style.transition = 'opacity 0.3s';
                card.style.opacity = '0';
                setTimeout(() => card.remove(), 300);
            }
        } else {
            alert(data.message || '취소에 실패했습니다. 다시 시도해 주세요.');
        }
    })
    .catch(err => {
        console.error('[cancelVolunteer] 오류:', err);
        alert('서버와 통신 중 오류가 발생했습니다.');
    });
}
