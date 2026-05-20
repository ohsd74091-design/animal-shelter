/* =====================================================
   animal-detail.jsp </body> 위 <script> 안에 교체
   ===================================================== */

// 보호소 좌표
var SHELTER_LAT = 36.3247;
var SHELTER_LNG = 127.4210;

var shelterPreviewMap  = null;
var shelterModalMap    = null;

/* ── 페이지 로드 시 미리보기 지도 초기화 ── */
window.addEventListener('load', function () {
    initPreviewMap();
});

function initPreviewMap() {
    var previewEl = document.getElementById('shelterMapPreview');
    if (!previewEl || shelterPreviewMap) return;

    shelterPreviewMap = L.map('shelterMapPreview', {
        center: [SHELTER_LAT, SHELTER_LNG],
        zoom: 16,
        zoomControl: false,       // 미리보기엔 컨트롤 숨김
        dragging: false,          // 드래그 비활성화
        scrollWheelZoom: false,
        doubleClickZoom: false,
        touchZoom: false,
        attributionControl: false
    });

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 19
    }).addTo(shelterPreviewMap);

    L.marker([SHELTER_LAT, SHELTER_LNG]).addTo(shelterPreviewMap);
}

/* ── 모달 열기 ── */
function openShelterMap() {
    var overlay = document.getElementById('shelterMapOverlay');
    overlay.style.display = 'flex';
    document.body.style.overflow = 'hidden';

    if (shelterModalMap) {
        shelterModalMap.invalidateSize();
        return;
    }

    shelterModalMap = L.map('shelterMap').setView([SHELTER_LAT, SHELTER_LNG], 17);

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '© <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a>',
        maxZoom: 19
    }).addTo(shelterModalMap);

    L.marker([SHELTER_LAT, SHELTER_LNG])
     .addTo(shelterModalMap)
     .bindPopup('<strong>대덕 인재 개발원</strong><br>대전광역시 중구 계룡로 846')
     .openPopup();
}

/* ── 모달 닫기 ── */
function closeShelterMap() {
    document.getElementById('shelterMapOverlay').style.display = 'none';
    document.body.style.overflow = '';
}

/* ── 모달 외부 클릭 시 닫기 ── */
document.getElementById('shelterMapOverlay').addEventListener('click', function (e) {
    if (e.target === this) closeShelterMap();
});