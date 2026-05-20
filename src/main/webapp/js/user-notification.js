document.addEventListener("DOMContentLoaded", function () {
    const ctp = window.contextPath || "";

    const btn = document.getElementById("userNotificationBtn");
    const dropdown = document.getElementById("userNotificationDropdown");
    const listEl = document.getElementById("userNotificationList");
    const dot = document.getElementById("userNotificationDot");

    if (!btn || !dropdown || !listEl || !dot) return;

    let loaded = false;

    // 페이지 진입 시 바로 한번 조회해서 빨간 점 반영
    loadNotifications();

    btn.addEventListener("click", function (e) {
        e.stopPropagation();

        const isOpen = dropdown.style.display === "block";

        if (isOpen) {
            dropdown.style.display = "none";
        } else {
            dropdown.style.display = "block";

            // 처음 클릭 시 이미 로드 안 됐으면 다시 조회
            if (!loaded) {
                loadNotifications();
            }
        }
    });

    document.addEventListener("click", function () {
        dropdown.style.display = "none";
    });

    dropdown.addEventListener("click", function (e) {
        e.stopPropagation();
    });

    function loadNotifications() {
        fetch(ctp + "/notification/list.do")
            .then(res => res.json())
            .then(data => {
                loaded = true;

                if (!data.success) {
                    listEl.innerHTML = `<div class="user-notification-empty">알림을 불러오지 못했습니다.</div>`;
                    dot.style.display = "none";
                    return;
                }

                renderList(data.items || [], data.unreadCount || 0);
            })
            .catch(err => {
                console.error("알림 조회 실패:", err);
                listEl.innerHTML = `<div class="user-notification-empty">알림을 불러오지 못했습니다.</div>`;
                dot.style.display = "none";
            });
    }

    function renderList(items, unreadCount) {
        dot.style.display = unreadCount > 0 ? "block" : "none";

        if (!items || items.length === 0) {
            listEl.innerHTML = `<div class="user-notification-empty">새 알림이 없습니다.</div>`;
            return;
        }

        let html = "";

        items.forEach(item => {
            const unreadClass = item.isRead === "N" ? "is-unread" : "";
            const encodedUrl = encodeURIComponent(item.linkUrl || "/mypage/main.do");

            html += `
                <a href="${ctp}/notification/read.do?notiId=${item.notiId}&linkUrl=${encodedUrl}"
                   class="user-notification-item ${unreadClass}">
                    <div class="user-notification-item__msg">${escapeHtml(item.notiMsg)}</div>
                    <div class="user-notification-item__date">${formatDate(item.createDate)}</div>
                </a>
            `;
        });

        listEl.innerHTML = html;
    }

    function escapeHtml(str) {
        if (!str) return "";
        return str
            .replace(/&/g, "&amp;")
            .replace(/</g, "&lt;")
            .replace(/>/g, "&gt;");
    }

    function formatDate(dateStr) {
        if (!dateStr) return "";

        const d = new Date(dateStr);
        if (isNaN(d.getTime())) return dateStr;

        const yyyy = d.getFullYear();
        const mm = String(d.getMonth() + 1).padStart(2, "0");
        const dd = String(d.getDate()).padStart(2, "0");
        const hh = String(d.getHours()).padStart(2, "0");
        const mi = String(d.getMinutes()).padStart(2, "0");

        return `${yyyy}-${mm}-${dd} ${hh}:${mi}`;
    }
});