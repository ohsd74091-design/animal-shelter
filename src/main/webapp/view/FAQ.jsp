<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${title}</title>

    <link href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/gh/orioncactus/pretendard/dist/web/static/pretendard.css" rel="stylesheet">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/common.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/FHQstyles.css">
</head>
<body>

    <jsp:include page="/view/common/header.jsp" />

    <main class="faq-page">
        <section class="faq-hero">
            <div class="layout-container">
                <span class="hero-badge">FAQ</span>
                <h1 class="hero-title">자주 묻는 질문</h1>
                <p class="hero-description">너와 나의 연결고리 서비스 이용에 대해 궁금한 점을 확인해 보세요.</p>
            </div>
        </section>

        <section class="faq-content-section">
            <div class="layout-container">
                <div class="faq-list">
                    
                    <div class="faq-item">
                        <input type="checkbox" id="faq-1" class="faq-input">
                        <label for="faq-1" class="faq-question">
                            입양 신청 후 데려오기까지 얼마나 걸리나요?
                            <span class="material-symbols-outlined icon">expand_more</span>
                        </label>
                        <div class="faq-answer">
                            <div class="faq-answer-content">
                                보통 신청서 접수부터 입양 확정까지 약 1주일에서 2주일 정도 소요됩니다. 
                                상담, 가정 방문, 그리고 아이와의 친밀감 형성 시간을 포함한 기간입니다.
                            </div>
                        </div>
                    </div>

                    <div class="faq-item">
                        <input type="checkbox" id="faq-2" class="faq-input">
                        <label for="faq-2" class="faq-question">
                            개인적인 사정으로 파양하게 될 경우 어떻게 하나요?
                            <span class="material-symbols-outlined icon">expand_more</span>
                        </label>
                        <div class="faq-answer">
                            <div class="faq-answer-content">
                                파양은 아이에게 큰 상처가 되므로 신중히 결정해 주셔야 합니다. 
                                부득이한 경우 반드시 보호소로 먼저 연락해 주셔야 하며, 임의 유기는 법적 책임을 물을 수 있습니다.
                            </div>
                        </div>
                    </div>

                    <div class="faq-item">
                        <input type="checkbox" id="faq-3" class="faq-input">
                        <label for="faq-3" class="faq-question">
                            일시 보호(임시 보호)도 신청할 수 있나요?
                            <span class="material-symbols-outlined icon">expand_more</span>
                        </label>
                        <div class="faq-answer">
                            <div class="faq-answer-content">
                                네, 가능합니다. 보호소 공간이 부족하거나 특별한 케어가 필요한 아이들을 위해 임시 보호 제도를 운영하고 있습니다. 
                                '봉사 신청' 탭을 확인해 주세요.
                            </div>
                        </div>
                    </div>
                    <div class="faq-item">
                       <input type="checkbox" id="faq-4" class="faq-input">
                       <label for="faq-4" class="faq-question">
                             봉사활동을 신청 하고 싶은데 어떻게 신청하면 되나요?
                             <span class="material-symbols-outlined icon">expand_more</span>
                       </label>
                       <div class="faq-answer">
                          <div class="faq-answer-content">
                              네, 봉사활동을 신청하고 싶으신가 보군요? 봉사활동을 신청하고 싶으시면 먼저 '봉사신청'탭으로 이동하여
                               '신청가능'으로 표시된 봉사활동의 상세보기로 들어가시면 '신청하기'가 있습니다.
                          </div>
                       </div>
                    </div>
                    <div class="faq-item">
                       <input type="checkbox" id="faq-5" class="faq-input">
                       <label for="faq-5" class="faq-question">
                             평일 상담가능 시간대는 몇시부터 몇시까지인가요?
                             <span class="material-symbols-outlined icon">expand_more</span>
                       </label>
                       <div class="faq-answer">
                          <div class="faq-answer-content">
                               네, 현재 평일 상담가능 시간대는 오전 7시부터 ~ 오후 6시 30분까지로 주말에는 오전 9시 부터 ~ 오후 2시까지
                               운영하며 공휴일에는 운영을 하지 않습니다
                          </div>
                       </div>
                    </div>
                    
                </div>
            </div>
        </section>
    </main>

    <jsp:include page="/view/common/footer.jsp" />

</body>
</html>