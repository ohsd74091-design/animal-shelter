<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctp" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>오시는 길 | 너와 나의 연결고리</title>

    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/gh/orioncactus/pretendard/dist/web/static/pretendard.css" rel="stylesheet">

    <%-- 공용 CSS (header/footer 스타일 포함) --%>
    <link rel="stylesheet" href="${ctp}/css/common/common.css">
    <%-- 지도 페이지 전용 CSS --%>
    <link rel="stylesheet" href="${ctp}/css/findmap.css">

    <%-- 카카오맵 SDK --%>
    <script src="//dapi.kakao.com/v2/maps/sdk.js?appkey=0f5594e9bc38e7523d321adbde506804"></script>
</head>
<body>

<jsp:include page="/view/common/header.jsp" />

<main class="map-page">
    <div class="layout-container">

        <%-- 페이지 타이틀 --%>
        <section class="map-page-title">
            <h2>오시는 길</h2>
            <p>소중한 생명을 만나는 곳, <strong>너와 나의 연결고리</strong>로 오시는 방법을 안내해 드립니다.</p>
        </section>

        <%-- 지도 영역 --%>
        <div class="map-wrap">
            <div id="map"></div>
        </div>

        <%-- 위치 상세 정보 --%>
        <section class="map-details">
            <div class="map-detail-item">
                <div class="map-detail-item__icon">
                    <span class="material-symbols-outlined">location_on</span>
                </div>
                <div class="map-detail-item__text">
                    <span class="map-detail-item__label">주소</span>
                    <p>대전광역시 중구 중앙로 121 (선화동 20, 대덕빌딩 2층)</p>
                </div>
            </div>
            <div class="map-detail-item">
                <div class="map-detail-item__icon">
                    <span class="material-symbols-outlined">call</span>
                </div>
                <div class="map-detail-item__text">
                    <span class="map-detail-item__label">연락처</span>
                    <p>042-222-8202</p>
                </div>
            </div>
            <div class="map-detail-item">
                <div class="map-detail-item__icon">
                    <span class="material-symbols-outlined">train</span>
                </div>
                <div class="map-detail-item__text">
                    <span class="map-detail-item__label">지하철</span>
                    <p>중앙로역 3, 4번 출구 도보 5분 (대덕인재개발원 방향)</p>
                </div>
            </div>
        </section>

    </div>
</main>

<jsp:include page="/view/common/footer.jsp" />

<script>
window.onload = function () {
    if (typeof kakao === 'undefined') {
        console.error('카카오 객체를 찾을 수 없습니다.');
        return;
    }

    kakao.maps.load(function () {
        var container = document.getElementById('map');
        var options = {
            center: new kakao.maps.LatLng(36.324905, 127.409385),
            level: 3
        };

        var map    = new kakao.maps.Map(container, options);
        var marker = new kakao.maps.Marker({
            position: new kakao.maps.LatLng(36.324905, 127.409385)
        });
        marker.setMap(map);

        var infowindow = new kakao.maps.InfoWindow({
            content: '<div style="padding:10px; font-size:12px; text-align:center; width:160px;">'
                   + '<b>너와 나의 연결고리</b><br>'
                   + '<a href="https://map.kakao.com/link/to/너와나의연결고리,36.324905,127.409385"'
                   + ' style="color:#e67e22;" target="_blank">길찾기</a>'
                   + '</div>'
        });
        infowindow.open(map, marker);
    });
};
</script>

</body>
</html>
