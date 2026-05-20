<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"   uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="ctp" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>너와 나의 연결고리 - 메인</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/gh/orioncactus/pretendard/dist/web/static/pretendard.css" rel="stylesheet">
    <link rel="stylesheet" href="${ctp}/css/common/common.css">
    <link rel="stylesheet" href="${ctp}/css/animalList.css">
    <link rel="stylesheet" href="${ctp}/css/mainpage.css">
</head>
<body>
<script>
    const contextPath = '${ctp}';
</script>
<script src="${ctp}/js/animalList.js" defer></script>

<jsp:include page="/view/common/header.jsp" />

<main class="main-page">

    <%-- ① 히어로 배너: 어두운 오버레이 + 흰 텍스트 + 오렌지 강조 (B 스타일) --%>
    <section class="hero-section">
        <div class="hero-bg">
            <img src="${ctp}/images/main-hero.jpg" alt="메인 배너" class="hero-bg__img">
            <div class="hero-bg__overlay"></div>
        </div>
        <div class="layout-container hero-content">
            <div class="hero-text">
                <span class="hero-badge">
                    <span class="material-symbols-outlined">pets</span>
                    ADOPT · VOLUNTEER · DONATE
                </span>
                <h1 class="hero-title">
                    사랑으로 잇는<br>
                    <em>너와 나의</em> 연결고리
                </h1>
                <p class="hero-desc">
                    버려진 아이들에게 새로운 가족이 되어주세요.<br>
                    작은 관심이 한 생명을 구하는 가장 큰 힘이 됩니다.
                </p>
                <div class="hero-btns">
                    <a href="${ctp}/animal/animalList.do" class="hero-btn hero-btn--primary">
                        <span class="material-symbols-outlined">pets</span>입양하기
                    </a>
                    <a href="${ctp}/donation/form.do" class="hero-btn hero-btn--ghost">
                        <span class="material-symbols-outlined">favorite</span>후원하기
                    </a>
                </div>
                <%-- 통계 리본 (템플릿 스타일: 가로 배치, 반투명 카드) --%>
                <div class="hero-stat-ribbon">
                    <div class="hero-stat-ribbon__item">
                        <span class="hero-stat-ribbon__num">${not empty totalAnimalCount ? totalAnimalCount : '0'}</span>
                        <span class="hero-stat-ribbon__label">보호 중인 동물</span>
                    </div>
                    <div class="hero-stat-ribbon__divider"></div>
                    <div class="hero-stat-ribbon__item">
                        <span class="hero-stat-ribbon__num">${not empty totalAdoptionCount ? totalAdoptionCount : '0'}</span>
                        <span class="hero-stat-ribbon__label">입양 완료</span>
                    </div>
                    <div class="hero-stat-ribbon__divider"></div>
                    <div class="hero-stat-ribbon__item">
                        <span class="hero-stat-ribbon__num">${not empty totalVolunteerCount ? totalVolunteerCount : '0'}</span>
                        <span class="hero-stat-ribbon__label">함께한 봉사</span>
                    </div>
                </div>
            </div>
        </div>
        <div class="hero-scroll-hint">
            <span class="material-symbols-outlined">keyboard_arrow_down</span>
        </div>
    </section>

    
    <%-- ③ 이번주 인기 동물: animalCard.jsp 재사용 슬라이더 --%>
    <section class="animal-section">
        <div class="layout-container">
            <div class="animal-section__head">
                <div>
                    <h2 class="animal-section__title">이번주 인기 동물</h2>
                    <p class="animal-section__desc">새로운 가족을 기다리는 사랑스러운 친구들입니다.</p>
                </div>
                <a href="${ctp}/animal/animalList.do" class="section-more-btn">
                    전체보기 <span class="material-symbols-outlined">arrow_forward</span>
                </a>
            </div>

            <c:choose>
                <c:when test="${empty popularAnimals}">
                    <div class="list-empty">
                        <span class="material-symbols-outlined">pets</span>
                        <p>등록된 동물이 없습니다.</p>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="main-animal-slider">
                        <button class="main-slider__btn main-slider__btn--prev" id="animalPrev">
                            <span class="material-symbols-outlined">chevron_left</span>
                        </button>
                        <div class="main-slider__viewport">
                            <div class="main-slider__track" id="animalTrack">
                                <c:forEach var="animal" items="${popularAnimals}">
                                    <div class="main-slider__item">
                                        <div class="animal-card">
                                            <div class="img-box">
                                                <c:choose>
                                                    <c:when test="${not empty animal.mainImage}">
                                                        <img src="${ctp}/animal/image?fileName=${animal.mainImage}"
                                                             alt="${animal.animalName}"
                                                             onerror="this.onerror=null;this.src='${ctp}/images/animals/default.jpg'"
                                                             class="img-box__img ${animal.adoptionStatus eq '입양완료' ? 'grayscale' : ''}">
                                                    </c:when>
                                                    <c:otherwise>
                                                        <img src="${ctp}/images/animals/default.jpg" alt="등록된 이미지 없음" class="img-box__img">
                                                    </c:otherwise>
                                                </c:choose>
                                                <button type="button"
                                                    class="btn-wish ${animal.isFavorite == 1 ? 'active' : ''}"
                                                    onclick="toggleWish(this, '${animal.animalId}')">
                                                    <span class="material-symbols-outlined">
                                                        ${animal.isFavorite == 1 ? 'favorite' : 'favorite_border'}
                                                    </span>
                                                </button>
                                            </div>
                                            <div class="info-box">
                                                <div class="card-header">
                                                    <h3>${animal.animalName}</h3>
                                                    <span class="reg-date">
                                                        <fmt:formatDate value="${animal.createDate}" pattern="yy.MM.dd 등록"/>
                                                    </span>
                                                </div>
                                                <div class="info-text">
                                                    ${animal.breed} · ${animal.age}살 · ${animal.gender == 'M' ? '남아' : '여아'}
                                                </div>
                                                <div class="tag-list">
                                                    <c:forTokens var="tag" items="${animal.personality}" delims=",">
                                                        <span class="tag-personality">#${tag}</span>
                                                    </c:forTokens>
                                                    <c:choose>
                                                        <c:when test="${animal.weight < 10}"><span class="tag-size">#소형</span></c:when>
                                                        <c:when test="${animal.weight < 25}"><span class="tag-size">#중형</span></c:when>
                                                        <c:otherwise><span class="tag-size">#대형</span></c:otherwise>
                                                    </c:choose>
                                                </div>
                                                <a href="${ctp}/animal/animalDetail.do?animalId=${animal.animalId}" class="btn-detail">자세히 보기</a>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>
                        <button class="main-slider__btn main-slider__btn--next" id="animalNext">
                            <span class="material-symbols-outlined">chevron_right</span>
                        </button>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </section>

	<%-- ② 참여 카드 섹션 (유지) --%>
    <section class="participate-section">
        <div class="layout-container">
            <div class="participate-header">
                <h2 class="participate-title">함께 참여해주세요</h2>
                <p class="participate-desc">도움이 필요한 유기동물들에게 당신의 손길을 내밀어주세요.</p>
            </div>
            <div class="participate-grid">
                <a href="${ctp}/volunteer/list.do" class="participate-card">
                    <div class="participate-card__icon-wrap participate-card__icon-wrap--orange">
                        <span class="material-symbols-outlined">volunteer_activism</span>
                    </div>
                    <div class="participate-card__body">
                        <h3 class="participate-card__title">봉사활동</h3>
                        <p class="participate-card__desc">아이들과 함께 시간을 보내주세요.</p>
                    </div>
                    <span class="participate-card__link">자세히 보기</span>
                </a>
                <a href="${ctp}/animal/guide.do" class="participate-card">
                    <div class="participate-card__icon-wrap participate-card__icon-wrap--orange">
                        <span class="material-symbols-outlined">menu_book</span>
                    </div>
                    <div class="participate-card__body">
                        <h3 class="participate-card__title">입양 가이드</h3>
                        <p class="participate-card__desc">입양 절차를 확인하세요.</p>
                    </div>
                    <span class="participate-card__link">가이드 보기</span>
                </a>
                <a href="${ctp}/board/list.do?boardType=입양후기" class="participate-card">
                    <div class="participate-card__icon-wrap participate-card__icon-wrap--orange">
                        <span class="material-symbols-outlined">favorite</span>
                    </div>
                    <div class="participate-card__body">
                        <h3 class="participate-card__title">입양 후기</h3>
                        <p class="participate-card__desc">행복한 이야기들을 확인하세요.</p>
                    </div>
                    <span class="participate-card__link">후기 보기</span>
                </a>
            </div>
        </div>
    </section>

    <%-- ④ 봉사활동(2/3) + 인기글(1/3): 템플릿 벤토 레이아웃 --%>
    <section class="main-content-section">
        <div class="layout-container main-bento-layout">

            <%-- 좌 col-span-2: 봉사활동 모집 중 --%>
            <div class="main-bento-left">
                <div class="bento-head">
                    <h2 class="bento-title">봉사활동 모집 중</h2>
                    <a href="${ctp}/volunteer/list.do" class="bento-more">모두 보기</a>
                </div>
                <div class="volunteer-bento-list">
                    <c:choose>
                        <c:when test="${empty popularVolunteers}">
                            <div class="list-empty">
                                <span class="material-symbols-outlined">event_busy</span>
                                <p>모집 중인 봉사활동이 없습니다.</p>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="vol" items="${popularVolunteers}">
                                <%-- D-day 계산: java.sql.Date 호환 방식 --%>
                                <%
                                    kr.or.ddit.volunteer_recruit.vo.Volunteer_RecruitVO volItem =
                                        (kr.or.ddit.volunteer_recruit.vo.Volunteer_RecruitVO)
                                        pageContext.getAttribute("vol");
                                    long ddayCalc = -1;
                                    if (volItem != null && volItem.getApplyDeadline() != null) {
                                        long deadlineMs = volItem.getApplyDeadline().getTime();
                                        long todayMs    = System.currentTimeMillis();
                                        ddayCalc = (long) Math.ceil((deadlineMs - todayMs) / 86400000.0);
                                    }
                                    pageContext.setAttribute("ddayCalc", ddayCalc);
                                %>

                                <div class="volunteer-bento-card">
                                    <div class="volunteer-bento-card__icon">
                                        <c:choose>
                                            <c:when test="${not empty vol.thumbnailImg}">
                                                <img src="${ctp}/volunteer/image?fileName=${vol.thumbnailImg}" alt="${vol.title}">
                                            </c:when>
                                            <c:otherwise>
                                                <span class="material-symbols-outlined">volunteer_activism</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                    <div class="volunteer-bento-card__info">
                                        <%-- D-day 배지: applyDeadline 기준 --%>
                                        <c:choose>
                                            <c:when test="${ddayCalc >= 0}">
                                                <span class="volunteer-bento-card__dday">
                                                    D-<fmt:formatNumber value="${ddayCalc}" maxFractionDigits="0"/>
                                                </span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="volunteer-bento-card__dday closed">마감</span>
                                            </c:otherwise>
                                        </c:choose>
                                        <h4 class="volunteer-bento-card__title">${vol.title}</h4>
                                        <p class="volunteer-bento-card__meta">${vol.location} · ${vol.volunteerDate}</p>
                                    </div>
                                    <c:choose>
                                        <c:when test="${ddayCalc >= 0}">
                                            <a href="${ctp}/volunteer/detail.do?recruitId=${vol.recruitId}" class="volunteer-bento-card__btn">신청하기</a>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="volunteer-bento-card__btn disabled">종료</span>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>

            <%-- 우 col-span-1: 이번주 인기글 --%>
            <div class="main-bento-right">
                <div class="bento-head">
                    <h2 class="bento-title">이번주 인기글</h2>
                    <a href="${ctp}/board/free.do" class="bento-more">전체보기</a>
                </div>
                <div class="popular-board-box">
                    <c:choose>
                        <c:when test="${empty popularBoards}">
                            <div class="list-empty">
                                <span class="material-symbols-outlined">article</span>
                                <p>인기글이 없습니다.</p>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <ul class="popular-board-list">
                                <c:forEach var="board" items="${popularBoards}" varStatus="st">
                                    <li class="popular-board-item">
                                        <span class="popular-board-item__rank ${st.index lt 3 ? 'top' : ''}">
                                            <c:choose>
                                                <c:when test="${st.count lt 10}">0${st.count}</c:when>
                                                <c:otherwise>${st.count}</c:otherwise>
                                            </c:choose>
                                        </span>
                                        <div class="popular-board-item__body">
                                            <a href="${ctp}/board/detail.do?boardId=${board.boardId}" class="popular-board-item__title">${board.title}</a>
                                            <p class="popular-board-item__meta">
                                                <span class="popular-board-item__type">${board.boardType}</span>
                                                <span><span class="material-symbols-outlined">favorite</span>${board.likeCount}</span>
                                            </p>
                                        </div>
                                    </li>
                                </c:forEach>
                            </ul>
                        </c:otherwise>
                    </c:choose>
                    <%-- 실시간 커뮤니티 참여 버튼 --%>
                    <% int randomJoinCount = (int)(Math.random() * 5); %>
                    <div class="community-join-bar">
                        <a href="${ctp}/board/free.do" class="community-join-btn">
                            <span class="community-join-btn__text">실시간 커뮤니티 참여</span>
                            <span class="community-join-btn__count">+<%= randomJoinCount %></span>
                            <span class="material-symbols-outlined">arrow_forward</span>
                        </a>
                    </div>
                </div>
            </div>

        </div>
    </section>

</main>

<jsp:include page="/view/common/footer.jsp" />

<script>
(function () {
    const track    = document.getElementById('animalTrack');
    const prevBtn  = document.getElementById('animalPrev');
    const nextBtn  = document.getElementById('animalNext');
    if (!track || !prevBtn || !nextBtn) return;

    const VISIBLE  = 4;   // 한 번에 보이는 카드 수
    let idx = 0;

    function getItems() {
        return track.querySelectorAll('.main-slider__item');
    }

    function getItemW() {
        const item = track.querySelector('.main-slider__item');
        if (!item) return 0;
        return item.offsetWidth + parseInt(getComputedStyle(track).gap || 24);
    }

    function maxIdx() {
        return Math.max(0, getItems().length - VISIBLE);
    }

    function move() {
        track.style.transform = 'translateX(-' + (idx * getItemW()) + 'px)';
        prevBtn.disabled = idx <= 0;
        nextBtn.disabled = idx >= maxIdx();
    }

    prevBtn.addEventListener('click', function () {
        if (idx > 0) { idx--; move(); }
    });
    nextBtn.addEventListener('click', function () {
        if (idx < maxIdx()) { idx++; move(); }
    });

    move(); // 초기 상태 버튼 비활성화 처리
})();
</script>

</body>
</html>
