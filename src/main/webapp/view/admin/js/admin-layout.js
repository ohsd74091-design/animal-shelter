document.addEventListener("DOMContentLoaded", function () {
    const ctp = document.querySelector(".admin-header")?.dataset.ctp || "";

    /* =========================================
       1. 관리자 알림
    ========================================= */
    const notificationBtn = document.getElementById("adminNotificationBtn");
    const notificationDot = document.getElementById("adminNotificationDot");
    const notificationDropdown = document.getElementById("adminNotificationDropdown");
    const notificationList = document.getElementById("adminNotificationList");

    function loadNotifications() {
        if (!notificationBtn || !notificationDot || !notificationDropdown || !notificationList) return;

        fetch(ctp + "/admin/notification/list.do")
            .then(res => res.json())
            .then(data => {
                if (data.hasUnread) {
                    notificationDot.style.display = "block";
                } else {
                    notificationDot.style.display = "none";
                }

                if (!data.items || data.items.length === 0) {
                    notificationList.innerHTML = `
                        <div class="admin-notification-empty">새 알림이 없습니다.</div>
                    `;
                    return;
                }

                let html = "";

                data.items.forEach(item => {
                    let icon = "notifications";
                    if (item.type === "animal") icon = "pets";
                    else if (item.type === "member") icon = "flag";
                    else if (item.type === "support") icon = "support_agent";

                    html += `
                        <div class="admin-notification-item" data-url="${item.url}">
                            <div class="admin-notification-item__icon">
                                <span class="material-symbols-outlined">${icon}</span>
                            </div>
                            <div class="admin-notification-item__content">
                                <div class="admin-notification-item__title">${item.label}</div>
                                <div class="admin-notification-item__desc">${item.count}건 미처리</div>
                            </div>
                        </div>
                    `;
                });

                notificationList.innerHTML = html;
            })
            .catch(err => {
                console.error("알림 조회 실패:", err);
                notificationList.innerHTML = `
                    <div class="admin-notification-empty">알림을 불러오지 못했습니다.</div>
                `;
            });
    }

    if (notificationBtn && notificationDropdown) {
        notificationBtn.addEventListener("click", function (e) {
            e.stopPropagation();

            const isOpen = notificationDropdown.style.display === "block";
            notificationDropdown.style.display = isOpen ? "none" : "block";
        });
    }

    if (notificationList) {
        notificationList.addEventListener("click", function (e) {
            const item = e.target.closest(".admin-notification-item");
            if (!item) return;

            const url = item.dataset.url;
            if (url) {
                location.href = url;
            }
        });
    }

    /* =========================================
       2. 관리자 검색 자동완성
    ========================================= */
    const searchInput = document.getElementById("adminSearchInput");
    const searchDropdown = document.getElementById("adminSearchDropdown");
    const searchResult = document.getElementById("adminSearchResult");
    const searchEmpty = document.getElementById("adminSearchEmpty");

    let debounceTimer = null;

    function escapeHtml(str) {
        if (str == null) return "";
        return String(str)
            .replace(/&/g, "&amp;")
            .replace(/</g, "&lt;")
            .replace(/>/g, "&gt;")
            .replace(/"/g, "&quot;")
            .replace(/'/g, "&#39;");
    }

    function buildSearchItem(item) {
        let imageHtml = "";
        let subText = item.subText
            ? `<div class="admin-search-item__sub">${escapeHtml(item.subText)}</div>`
            : "";

        if (item.type === "member" && item.imageName) {
            imageHtml = `
                <img class="admin-search-item__thumb"
                     src="${ctp}/member/image.do?fileName=${encodeURIComponent(item.imageName)}"
                     alt="프로필">
            `;
        } else if (item.type === "animal" && item.imageName) {
            imageHtml = `
                <img class="admin-search-item__thumb"
                     src="${ctp}/animal/image?fileName=${encodeURIComponent(item.imageName)}"
                     alt="동물 이미지">
            `;
        } else {
            let icon = "search";
            if (item.type === "member") icon = "person";
            else if (item.type === "animal") icon = "pets";
            else if (item.type === "support") icon = "support_agent";

            imageHtml = `
                <div class="admin-search-item__thumb admin-search-item__thumb--icon">
                    <span class="material-symbols-outlined">${icon}</span>
                </div>
            `;
        }

        return `
            <div class="admin-search-item" data-url="${item.moveUrl}">
                ${imageHtml}
                <div class="admin-search-item__content">
                    <div class="admin-search-item__title">${escapeHtml(item.title)}</div>
                    ${subText}
                </div>
            </div>
        `;
    }

    function buildSearchSection(title, items) {
        if (!items || items.length === 0) return "";

        const inner = items.map(buildSearchItem).join("");

        return `
            <div class="admin-search-section">
                <div class="admin-search-section__title">${title}</div>
                ${inner}
            </div>
        `;
    }

    function loadSearchSuggestions(keyword) {
        if (!searchInput || !searchDropdown || !searchResult || !searchEmpty) return;

        if (!keyword || keyword.trim().length < 1) {
            searchDropdown.style.display = "none";
            searchResult.innerHTML = "";
            return;
        }

        fetch(ctp + "/admin/search/suggest.do?keyword=" + encodeURIComponent(keyword))
            .then(res => res.json())
            .then(data => {
                const animalHtml = buildSearchSection("동물", data.animals || []);
                const memberHtml = buildSearchSection("회원", data.members || []);
                const supportHtml = buildSearchSection("문의", data.supports || []);

                const finalHtml = animalHtml + memberHtml + supportHtml;

                if (!finalHtml) {
                    searchResult.innerHTML = "";
                    searchEmpty.style.display = "block";
                    searchEmpty.textContent = "검색 결과가 없습니다.";
                    searchDropdown.style.display = "block";
                    return;
                }

                searchEmpty.style.display = "none";
                searchResult.innerHTML = finalHtml;
                searchDropdown.style.display = "block";
            })
            .catch(err => {
                console.error("검색 자동완성 실패:", err);
                searchResult.innerHTML = "";
                searchEmpty.style.display = "block";
                searchEmpty.textContent = "검색 중 오류가 발생했습니다.";
                searchDropdown.style.display = "block";
            });
    }

    if (searchInput) {
        searchInput.addEventListener("input", function () {
            const keyword = this.value;

            clearTimeout(debounceTimer);
            debounceTimer = setTimeout(() => {
                loadSearchSuggestions(keyword);
            }, 250);
        });

        searchInput.addEventListener("focus", function () {
            const keyword = this.value;
            if (keyword && keyword.trim().length > 0) {
                loadSearchSuggestions(keyword);
            }
        });

        searchInput.addEventListener("keydown", function (e) {
            if (e.key === "Enter") {
                e.preventDefault();

                const firstItem = searchResult.querySelector(".admin-search-item");
                if (firstItem && firstItem.dataset.url) {
                    location.href = firstItem.dataset.url;
                }
            }
        });
    }

    if (searchResult) {
        searchResult.addEventListener("click", function (e) {
            const item = e.target.closest(".admin-search-item");
            if (!item) return;

            const url = item.dataset.url;
            if (url) {
                location.href = url;
            }
        });
    }

    /* =========================================
       3. 바깥 클릭 시 닫기
    ========================================= */
    document.addEventListener("click", function (e) {
        if (
            notificationDropdown &&
            notificationBtn &&
            !notificationDropdown.contains(e.target) &&
            !notificationBtn.contains(e.target)
        ) {
            notificationDropdown.style.display = "none";
        }

        if (
            searchDropdown &&
            searchInput &&
            !searchDropdown.contains(e.target) &&
            !searchInput.closest(".admin-search-wrap")?.contains(e.target)
        ) {
            searchDropdown.style.display = "none";
        }
    });

    loadNotifications();
});

/* =========================================
   전역 캘린더 모달 (헤더 달력 아이콘)
   - DOMContentLoaded 후 실행하여 모든 페이지에서 안정적으로 동작
   - 데이터는 /admin/getMonthlySchedule.do AJAX로 로드
========================================= */
document.addEventListener("DOMContentLoaded", function () {

    // ── contextPath: admin-header의 data-ctp 속성에서 가져옴 ──
    const ctp = document.querySelector(".admin-header")?.dataset.ctp || "";

    // ── 헤더 달력 버튼 확인 ──
    const calBtn = document.getElementById("adminHeaderCalBtn");
    if (!calBtn) return; // 헤더가 없는 페이지면 중단

    // ── 캘린더 상태 관리 ──
    const gCalState = {
        year : new Date().getFullYear(),
        month: new Date().getMonth() // 0-indexed
    };

    // ── 달별 일정 캐시 { 'YYYY-MM': [...] } ──
    const scheduleCache = {};

    // ── 전역 캘린더 모달 HTML 동적 삽입 (중복 삽입 방지) ──
    if (!document.getElementById("globalCalendarModal")) {
        document.body.insertAdjacentHTML("beforeend", `
            <div id="globalCalendarModal"
                 style="display:none;position:fixed;inset:0;
                        background:rgba(15,23,42,0.45);z-index:9000;
                        align-items:center;justify-content:center;">

                <div id="globalCalModalBox"
                     style="background:#fff;border-radius:20px;
                            width:min(780px,96vw);max-height:90vh;overflow:hidden;
                            box-shadow:0 24px 60px rgba(15,23,42,0.18);
                            display:flex;flex-direction:column;">

                    <!-- 헤더 -->
                    <div style="display:flex;align-items:center;justify-content:space-between;
                                padding:20px 24px 14px;border-bottom:1px solid #e5e7eb;">
                        <div style="display:flex;align-items:center;gap:12px;">
                            <button id="gCalPrev"
                                    style="width:34px;height:34px;border-radius:50%;
                                           display:flex;align-items:center;justify-content:center;
                                           background:#f1f3f7;border:none;cursor:pointer;font-size:20px;">‹</button>
                            <h3 id="gCalTitle"
                                style="font-size:18px;font-weight:800;min-width:130px;
                                       text-align:center;color:#1f2937;"></h3>
                            <button id="gCalNext"
                                    style="width:34px;height:34px;border-radius:50%;
                                           display:flex;align-items:center;justify-content:center;
                                           background:#f1f3f7;border:none;cursor:pointer;font-size:20px;">›</button>
                        </div>
                        <button id="gCalClose"
                                style="width:36px;height:36px;border-radius:50%;
                                       background:#f1f3f7;border:none;cursor:pointer;font-size:18px;
                                       display:flex;align-items:center;justify-content:center;">✕</button>
                    </div>

                    <!-- 요일 헤더 -->
                    <div style="display:grid;grid-template-columns:repeat(7,1fr);
                                padding:10px 20px 4px;gap:4px;">
                        <span style="text-align:center;font-size:12px;font-weight:700;color:#ef4444;">일</span>
                        <span style="text-align:center;font-size:12px;font-weight:700;color:#6b7280;">월</span>
                        <span style="text-align:center;font-size:12px;font-weight:700;color:#6b7280;">화</span>
                        <span style="text-align:center;font-size:12px;font-weight:700;color:#6b7280;">수</span>
                        <span style="text-align:center;font-size:12px;font-weight:700;color:#6b7280;">목</span>
                        <span style="text-align:center;font-size:12px;font-weight:700;color:#6b7280;">금</span>
                        <span style="text-align:center;font-size:12px;font-weight:700;color:#3b82f6;">토</span>
                    </div>

                    <!-- 달력 격자 -->
                    <div id="gCalGrid"
                         style="display:grid;grid-template-columns:repeat(7,1fr);
                                gap:4px;padding:0 20px 10px;flex:1;overflow-y:auto;
                                min-height:240px;"></div>

                    <!-- 날짜 상세 패널 -->
                    <div id="gCalDayDetail"
                         style="display:none;border-top:1px solid #e5e7eb;
                                padding:14px 20px;max-height:220px;overflow-y:auto;
                                background:#fafafa;">
                        <h4 id="gCalDayTitle"
                            style="font-size:14px;font-weight:800;margin-bottom:10px;color:#1f2937;"></h4>
                        <div id="gCalDayList"></div>
                    </div>

                    <!-- 로딩 -->
                    <div id="gCalLoading"
                         style="display:none;text-align:center;padding:30px;color:#6b7280;font-size:14px;">
                        일정을 불러오는 중...
                    </div>
                </div>
            </div>
        `);
    }

    // ── 모달 내부 요소 참조 ──
    const modal     = document.getElementById("globalCalendarModal");
    const grid      = document.getElementById("gCalGrid");
    const title     = document.getElementById("gCalTitle");
    const dayDetail = document.getElementById("gCalDayDetail");
    const dayTitle  = document.getElementById("gCalDayTitle");
    const dayList   = document.getElementById("gCalDayList");
    const loading   = document.getElementById("gCalLoading");

    /* 모달 열기 */
    function openGlobalCal() {
        modal.style.display = "flex";
        loadAndRender(gCalState.year, gCalState.month);
    }

    /* 모달 닫기 */
    function closeGlobalCal() {
        modal.style.display = "none";
        dayDetail.style.display = "none";
    }

    /* 달 변경 */
    function changeMonth(delta) {
        gCalState.month += delta;
        if (gCalState.month > 11) { gCalState.month = 0;  gCalState.year++; }
        if (gCalState.month <  0) { gCalState.month = 11; gCalState.year--; }
        dayDetail.style.display = "none";
        loadAndRender(gCalState.year, gCalState.month);
    }

    /**
     * 해당 년/월 일정 AJAX 로드 → 캘린더 렌더링
     * 캐시된 데이터는 재사용
     */
    function loadAndRender(year, month) {
        const cacheKey = year + "-" + String(month + 1).padStart(2, "0");
        title.textContent = year + "년 " + (month + 1) + "월";

        if (scheduleCache[cacheKey]) {
            // 캐시 히트: 바로 렌더링
            renderGrid(year, month, scheduleCache[cacheKey]);
            return;
        }

        // 로딩 표시, 격자 초기화
        loading.style.display = "block";
        grid.innerHTML = "";

        fetch(ctp + "/admin/getMonthlySchedule.do?year=" + year + "&month=" + (month + 1))
            .then(function (res) { return res.json(); })
            .then(function (data) {
                loading.style.display = "none";
                scheduleCache[cacheKey] = data; // 캐시 저장
                renderGrid(year, month, data);
            })
            .catch(function (err) {
                loading.style.display = "none";
                console.error("캘린더 일정 로드 실패:", err);
                grid.innerHTML = "<div style='grid-column:1/-1;text-align:center;padding:20px;color:#94a3b8;'>일정을 불러오지 못했습니다.</div>";
            });
    }

    /**
     * 달력 격자 렌더링
     * @param {number} year
     * @param {number} month  0-indexed
     * @param {Array}  list   Oracle resultType=map → 키 대문자
     */
    function renderGrid(year, month, list) {
        const today    = new Date();
        const firstDay = new Date(year, month, 1).getDay();
        const lastDate = new Date(year, month + 1, 0).getDate();

        // 날짜별 일정 맵 (키 대소문자 모두 대응)
        const scheduleMap = {};
        (list || []).forEach(function (s) {
            const dk = s.DATE_KEY || s.dateKey;
            if (!dk) return;
            if (!scheduleMap[dk]) scheduleMap[dk] = [];
            scheduleMap[dk].push(s);
        });

        let html = "";

        // 1일 이전 빈 셀
        for (let i = 0; i < firstDay; i++) {
            html += "<div></div>";
        }

        // 날짜 셀 생성
        for (let d = 1; d <= lastDate; d++) {
            const mm = String(month + 1).padStart(2, "0");
            const dd = String(d).padStart(2, "0");
            const dk = year + "-" + mm + "-" + dd;

            const isToday = (year === today.getFullYear()
                          && month === today.getMonth()
                          && d === today.getDate());

            const events = scheduleMap[dk] || [];
            const dow    = (firstDay + d - 1) % 7;

            // 도트 (최대 3개)
            const dots = events.slice(0, 3).map(function (ev) {
                const c = (ev.TYPE || ev.type) === "ADOPTION" ? "#e56b2e" : "#0f9d7a";
                return '<span style="display:inline-block;width:6px;height:6px;border-radius:50%;background:'
                       + c + ';margin:0 1px;"></span>';
            }).join("");

            const dateColor = dow === 0 ? "#ef4444" : dow === 6 ? "#3b82f6" : "#1f2937";
            const todayStyle = isToday
                ? "background:#fff1e8;border:2px solid #e56b2e;"
                : (events.length > 0 ? "background:#f8fffe;border:2px solid transparent;" : "border:2px solid transparent;");

            html += `<div data-gdate="${dk}"
                          style="min-height:56px;padding:6px 4px;border-radius:10px;
                                 cursor:pointer;text-align:center;transition:background 0.15s;${todayStyle}">
                        <span style="font-size:13px;font-weight:700;color:${dateColor};">${d}</span>
                        <div style="margin-top:4px;">${dots}</div>
                        ${events.length > 0
                            ? '<div style="font-size:10px;color:#64748b;margin-top:2px;">' + events.length + '건</div>'
                            : ""}
                    </div>`;
        }

        grid.innerHTML = html;

        // 날짜 셀 클릭 (이벤트 위임)
        grid.onclick = function (e) {
            const cell = e.target.closest("[data-gdate]");
            if (!cell) return;
            showGCalDay(cell.dataset.gdate, scheduleMap);
            grid.querySelectorAll("[data-gdate]").forEach(function (c) {
                c.style.outline = "none";
            });
            cell.style.outline = "2px solid #e56b2e";
        };
    }

    /** 날짜 클릭 → 일정 상세 패널 */
    function showGCalDay(dateKey, scheduleMap) {
        const events = scheduleMap[dateKey] || [];
        dayTitle.textContent = dateKey + " 일정 (" + events.length + "건)";

        if (events.length === 0) {
            dayList.innerHTML = "<p style='font-size:13px;color:#94a3b8;text-align:center;padding:10px 0;'>이 날은 일정이 없습니다.</p>";
        } else {
            dayList.innerHTML = events.map(function (s) {
                const type  = s.TYPE    || s.type    || "";
                const ttl   = s.TITLE   || s.title   || "—";
                const cont  = s.CONTENT || s.content || "";
                const time  = s.TIME    || s.time    || "";
                const color = type === "ADOPTION" ? "#e56b2e" : "#0f9d7a";
                const label = type === "ADOPTION" ? "입양 상담" : "봉사 활동";
                const icon  = type === "ADOPTION" ? "🐾" : "🤝";

                return `<div style="padding:10px 12px;border-radius:10px;background:#fff;
                                    margin-bottom:8px;border-left:3px solid ${color};
                                    box-shadow:0 2px 8px rgba(15,23,42,0.06);">
                            <div style="display:flex;align-items:center;gap:6px;margin-bottom:3px;">
             f                   <span>${icon}</span>
                                <strong style="font-size:13px;color:#1f2937;">${ttl}</strong>
                                <span style="font-size:11px;color:#94a3b8;margin-left:auto;">${time}</span>
                            </div>
                            <p style="font-size:12px;color:#64748b;margin:0 0 5px;">${cont}</p>
                            <span style="font-size:11px;font-weight:700;padding:2px 8px;border-radius:20px;
                                         background:${type === "ADOPTION" ? "#fff1e8" : "#e8faf4"};
                                         color:${color};">${label}</span>
                        </div>`;
            }).join("");
        }

        dayDetail.style.display = "block";
    }

    // ── 이벤트 바인딩 ──
    calBtn.addEventListener("click", openGlobalCal);
    document.getElementById("gCalClose").addEventListener("click", closeGlobalCal);
    document.getElementById("gCalPrev").addEventListener("click", function () { changeMonth(-1); });
    document.getElementById("gCalNext").addEventListener("click", function () { changeMonth(1); });

    modal.addEventListener("click", function (e) {
        if (e.target === modal) closeGlobalCal();
    });

    document.addEventListener("keydown", function (e) {
        if (e.key === "Escape" && modal.style.display === "flex") closeGlobalCal();
    });

}); // DOMContentLoaded 끝