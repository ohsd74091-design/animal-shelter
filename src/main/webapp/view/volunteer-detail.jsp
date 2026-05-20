<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="ctp" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>${empty recruit.title ? '봉사 상세보기' : recruit.title} - 너와 나의 연결고리</title>

  <link rel="preconnect" href="https://fonts.googleapis.com" />
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
  <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700;800&family=Be+Vietnam+Pro:wght@300;400;500;600&display=swap" rel="stylesheet" />
  <link href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:wght,FILL@100..700,0..1&display=swap" rel="stylesheet" />

  <link rel="stylesheet" href="${ctp}/css/common/common.css" />
  <link rel="stylesheet" href="${ctp}/css/volunteer-detail.css" />

  <%-- Leaflet.js --%>
  <link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css"/>
  <script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>
</head>
<body class="volunteer-detail-page">

  <jsp:include page="/view/common/header.jsp" />

  <main class="main container detail-main">
    <section class="hero">
      <div class="hero__content">
        <div class="badge">
          <span class="material-symbols-outlined">volunteer_activism</span>
          <span>
            <c:choose>
              <c:when test="${recruit.status eq '모집중'}">Recruitment Active</c:when>
              <c:otherwise>Recruitment Closed</c:otherwise>
            </c:choose>
          </span>
        </div>

        <h1 class="page-title">${recruit.title}</h1>

        <div class="meta-list">
          <div class="meta-item">
            <span class="material-symbols-outlined">schedule</span>
            <span>작성일: ${recruit.createDate}</span>
          </div>
          <div class="meta-item meta-item--highlight">
            <span class="material-symbols-outlined">tag</span>
            <span>${recruit.recruitId}</span>
          </div>
        </div>

        <div class="hero-image card card--image">
          <c:choose>
            <c:when test="${empty recruit.thumbnailImg}">
              <img src="https://via.placeholder.com/900x500?text=No+Image" alt="봉사 모집 썸네일" />
            </c:when>
            <c:otherwise>
              <img src="${ctp}/volunteer/image?fileName=${recruit.thumbnailImg}" alt="봉사 모집 썸네일" />
            </c:otherwise>
          </c:choose>
        </div>
      </div>

      <aside class="hero__sidebar">
        <div class="card sticky-card">
          <h2 class="section-title section-title--sm">모집 정보 상세</h2>

          <ul class="info-list">
            <li class="info-list__item">
              <div class="info-list__icon">
                <span class="material-symbols-outlined">calendar_month</span>
              </div>
              <div>
                <p class="info-list__label">봉사 날짜</p>
                <p class="info-list__value">${recruit.volunteerDate}</p>
              </div>
            </li>
            <li class="info-list__item">
              <div class="info-list__icon">
                <span class="material-symbols-outlined">location_on</span>
              </div>
              <div>
                <p class="info-list__label">장소</p>
                <p class="info-list__value">${recruit.location}</p>
              </div>
            </li>
            <li class="info-list__item">
              <div class="info-list__icon info-list__icon--danger">
                <span class="material-symbols-outlined">alarm_on</span>
              </div>
              <div>
                <p class="info-list__label info-list__label--danger">신청 마감</p>
                <p class="info-list__value">${recruit.applyDeadline}</p>
              </div>
            </li>
          </ul>

          <div class="divider"></div>

          <div class="status-box">
            <div class="status-box__header">
              <span>모집 상태</span>
              <span class="status-pill">${recruit.status}</span>
            </div>
          </div>

          <div class="button-group">
            <c:if test="${not empty sessionScope.loginUser and sessionScope.loginUser.role eq 'ADMIN'}">
              <div class="button-group">
                <a href="${ctp}/volunteer/update.do?recruitId=${recruit.recruitId}" class="btn btn--outline">
                  수정하기
                </a>
                <form action="${ctp}/volunteer/delete.do" method="post" style="margin:0;">
                  <input type="hidden" name="recruitId" value="${recruit.recruitId}">
                  <button type="submit" class="btn btn--outline"
                          onclick="return confirm('정말 삭제하시겠습니까?');">
                    삭제하기
                  </button>
                </form>
              </div>
            </c:if>

            <c:choose>
              <c:when test="${recruit.status eq '모집중'}">
                <a href="${ctp}/volunteer/applyView.do?recruitId=${recruit.recruitId}" class="btn btn--primary">
                  <span class="material-symbols-outlined">volunteer_activism</span>
                  봉사 신청하기
                </a>
              </c:when>
              <c:otherwise>
                <button type="button" class="btn btn--outline" disabled>모집 마감</button>
              </c:otherwise>
            </c:choose>
          </div>

          <form action="${ctp}/support/list.do" method="get" style="margin:0;">
            <button type="submit" class="btn btn--outline">
              <span class="material-symbols-outlined">chat</span>
              문의하기
            </button>
          </form>
        </div>
      </aside>
    </section>

    <section class="content-layout">
      <div class="content-main">

        <%-- 상세 활동 내용 --%>
        <section class="content-section">
          <h2 class="section-title">
            <span class="material-symbols-outlined">description</span>
            상세 활동 내용
          </h2>
          <div class="card content-box">
            <p class="lead-text">${recruit.content}</p>
          </div>
        </section>

        <%-- 오시는 길: Leaflet 지도 --%>
        <section class="content-section">
          <h2 class="section-title">
            <span class="material-symbols-outlined">map</span>
            오시는 길
          </h2>

          <div class="map-card card">
            <%-- Leaflet 지도 렌더링 영역 --%>
            <div class="map-canvas" id="volunteerMap"></div>

            <%-- 주소 패널 오버레이 --%>
            <div class="map-card__overlay">
              <div class="map-card__panel">
                <span class="material-symbols-outlined map-card__icon">location_on</span>
                <p class="map-card__title">봉사 장소</p>
                <p class="map-card__address">${recruit.location}</p>
                <a href="https://www.google.com/maps/search/?api=1&query=${recruit.location}"
                   target="_blank" rel="noopener noreferrer" class="text-link">
                  지도 앱에서 보기
                </a>
              </div>
            </div>
          </div>
        </section>

      </div>

      <aside class="content-side">
        <section class="card faq-card">
          <h2 class="section-title section-title--sm">
            <span class="material-symbols-outlined">contact_support</span>
            자주 묻는 질문
          </h2>
          <details class="faq-item">
            <summary>
              <span>처음인데 괜찮을까요?</span>
              <span class="material-symbols-outlined">expand_more</span>
            </summary>
            <p>네, 담당자 안내 후 참여할 수 있도록 진행하면 됩니다.</p>
          </details>
          <details class="faq-item">
            <summary>
              <span>준비물이 있나요?</span>
              <span class="material-symbols-outlined">expand_more</span>
            </summary>
            <p>편한 복장과 운동화를 권장합니다. 세부 내용은 모집글 본문을 참고해주세요.</p>
          </details>
        </section>

        <section class="side-banner card card--image">
          <c:choose>
            <c:when test="${empty recruit.thumbnailImg}">
              <img src="https://via.placeholder.com/900x500?text=No+Image" alt="봉사 배너" />
            </c:when>
            <c:otherwise>
              <img src="${ctp}/volunteer/image?fileName=${recruit.thumbnailImg}" alt="봉사 배너" />
            </c:otherwise>
          </c:choose>
          <div class="side-banner__overlay">
            <p>당신의 작은 발걸음이<br />아이들에게는 커다란 행복입니다.</p>
          </div>
        </section>
      </aside>
    </section>
  </main>

  <jsp:include page="/view/common/footer.jsp" />

  <script src="${ctp}/js/volunteer-detail.js"></script>

  <%-- 봉사 장소 지도 초기화 --%>
  <script>
  (function () {
      var locationStr = '${recruit.location}';
      if (!locationStr) return;

      var encoded = encodeURIComponent(locationStr);

      fetch('https://nominatim.openstreetmap.org/search?format=json&q='
            + encoded + '&countrycodes=kr&limit=1', {
          headers: { 'Accept-Language': 'ko' }
      })
      .then(function (res) { return res.json(); })
      .then(function (results) {
          var lat, lng;

          if (results && results.length > 0) {
              lat = parseFloat(results[0].lat);
              lng = parseFloat(results[0].lon);
          } else {
              // 지오코딩 실패 시 대전 기본 좌표
              lat = 36.3504;
              lng = 127.3845;
          }

          var map = L.map('volunteerMap', { scrollWheelZoom: false })
                     .setView([lat, lng], 16);

          L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
              attribution: '© <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a>',
              maxZoom: 19
          }).addTo(map);

          L.marker([lat, lng])
           .addTo(map)
           .bindPopup('<strong>' + locationStr + '</strong>')
           .openPopup();
      })
      .catch(function () {
          var el = document.getElementById('volunteerMap');
          if (el) el.innerHTML = '<p style="text-align:center;padding:60px;color:#9ca3af;">지도를 불러올 수 없습니다.</p>';
      });
  })();
  </script>

</body>
</html>
