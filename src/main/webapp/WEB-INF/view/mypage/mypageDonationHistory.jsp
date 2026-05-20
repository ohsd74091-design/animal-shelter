<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>후원 내역 - 너와 나의 연결고리</title>

<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined" rel="stylesheet">
<link href="https://cdn.jsdelivr.net/gh/orioncactus/pretendard/dist/web/static/pretendard.css" rel="stylesheet">

<%-- 공통 CSS --%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/common.css">

<%-- 마이페이지 공통 CSS --%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/mypage/mypage.css">

<%-- 후원 내역 전용 CSS --%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/mypage/mypageDonationHistory.css">

</head>
<body>

    <%-- 공통 헤더 --%>
    <jsp:include page="/view/common/header.jsp" />

    <div class="mypage-container">

        <%-- 사이드바 --%>
        <jsp:include page="/view/common/sidebarNav.jsp">
            <jsp:param name="activeMenu" value="donationHistory"/>
        </jsp:include>

        <%-- 메인 콘텐츠 --%>
        <section class="mypage-content">

            <%-- 페이지 헤더 --%>
            <div class="page-header">
                <h2 class="page-title">후원 내역</h2>
                <p class="page-desc">지금까지 따뜻한 마음을 나누어 주신 후원 내역입니다.</p>
            </div>

            <%-- 통계 카드 --%>
            <div class="donation-stat-grid">
                <div class="donation-stat-card">
                    <div class="donation-stat-card__top">
                        <p class="donation-stat-card__label">총 후원 횟수</p>
                        <div class="donation-stat-card__icon">
                            <span class="material-symbols-outlined">analytics</span>
                        </div>
                    </div>
                    <p class="donation-stat-card__value">
                        <fmt:formatNumber value="${donationStats.totalCount}" type="number"/>
                        <span class="donation-stat-card__unit">회</span>
                    </p>
                </div>

                <div class="donation-stat-card">
                    <div class="donation-stat-card__top">
                        <p class="donation-stat-card__label">누적 후원 금액</p>
                        <div class="donation-stat-card__icon">
                            <span class="material-symbols-outlined">payments</span>
                        </div>
                    </div>
                    <p class="donation-stat-card__value">
                        <fmt:formatNumber value="${donationStats.totalAmount}" type="number"/>
                        <span class="donation-stat-card__unit">원</span>
                    </p>
                </div>
            </div>

            <%-- 후원 내역 테이블 --%>
            <div class="donation-card">
                <c:choose>
                    <c:when test="${not empty donationList}">
                        <div class="donation-table-wrap">
                            <table class="donation-table">
                                <thead>
                                    <tr>
                                        <th class="col-date">후원 날짜</th>
                                        <th class="col-type">후원 방식</th>
                                        <th class="col-method">결제 수단</th>
                                        <th class="col-amount">후원 금액</th>
                                        <th class="col-receipt">영수증 발급</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="donation" items="${donationList}">
                                        <tr>
                                            <%-- 후원 날짜 --%>
                                            <td class="col-date">${donation.donationDateStr}</td>

                                            <%-- 후원 항목 (PAYMENT_TYPE) --%>
                                            <td class="col-type">
                                                <c:choose>
                                                    <c:when test="${donation.donationType eq '정기'}">
                                                        <span class="donation-type-badge donation-type-badge--regular">정기후원</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="donation-type-badge donation-type-badge--once">일시후원</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>

                                            <%-- 결제 수단 (DONATION_METHOD) --%>
                                            <td class="col-method">
                                                <c:choose>
                                                    <c:when test="${donation.donationMethod eq '카드'}">
                                                    	<span class="donation-method-badge donation-method-badge--card">신용카드</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                    	<span class="donation-method-badge donation-method-badge--account">계좌이체</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>

                                            <%-- 후원 금액 --%>
                                            <td class="col-amount">
                                                <fmt:formatNumber value="${donation.donationAmount}" type="number"/>원
                                            </td>

                                            <%-- 영수증 발급 (RECEIPT_YN) --%>
											<td class="col-receipt">
											    <c:choose>											
											        <%--
											            ✅ 'I' = 관리자가 발급 승인 완료
											               → 다운로드 버튼 표시
											               → 클릭 시 GET /donation/receipt.do?donationId=XXX
											               → DonationReceiptDownload.java 가 PDF 생성 후 다운로드
											        --%>
											        <c:when test="${donation.receiptYn eq 'I'}">
											            <a href="${pageContext.request.contextPath}/donation/receipt.do?donationId=${donation.donationId}"
											               class="receipt-download-btn"
											               title="기부금 영수증 PDF 다운로드">
											                <span class="material-symbols-outlined">download</span>
											                영수증 다운로드
											            </a>
											        </c:when>
											        <c:when test="${donation.receiptYn eq 'Y'}">
											            <span class="receipt-status receipt-status--pending">
											                <span class="receipt-dot"></span>대기중
											            </span>
											        </c:when>										
											        <c:otherwise>
											            <span class="receipt-status receipt-status--available">
											                <span class="receipt-dot"></span>신청가능
											            </span>
											        </c:otherwise>
											
											    </c:choose>
											</td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>

                        <%-- 하단: 총 개수 + 페이지네이션 --%>
                        <div class="donation-card__footer">
                            <p class="donation-total-info">
                                ${donationStats.totalCount}개 중 ${startRow}-${endRow}개 표시 중
                            </p>

                            <div class="pagination">
                                <c:choose>
                                    <c:when test="${currentPage > 1}">
                                        <a href="?page=${currentPage - 1}" class="page-btn page-btn--arrow">
                                            <span class="material-symbols-outlined">chevron_left</span>
                                        </a>
                                    </c:when>
                                    <c:otherwise>
                                        <button class="page-btn page-btn--arrow" disabled>
                                            <span class="material-symbols-outlined">chevron_left</span>
                                        </button>
                                    </c:otherwise>
                                </c:choose>

                                <c:forEach begin="${startPage}" end="${endPage}" var="p">
                                    <c:choose>
                                        <c:when test="${p == currentPage}">
                                            <button class="page-btn page-btn--active">${p}</button>
                                        </c:when>
                                        <c:otherwise>
                                            <a href="?page=${p}" class="page-btn">${p}</a>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>

                                <c:choose>
                                    <c:when test="${currentPage < totalPages}">
                                        <a href="?page=${currentPage + 1}" class="page-btn page-btn--arrow">
                                            <span class="material-symbols-outlined">chevron_right</span>
                                        </a>
                                    </c:when>
                                    <c:otherwise>
                                        <button class="page-btn page-btn--arrow" disabled>
                                            <span class="material-symbols-outlined">chevron_right</span>
                                        </button>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>

                    </c:when>
                    <c:otherwise>
                        <div class="donation-empty">
                            <span class="material-symbols-outlined donation-empty__icon">volunteer_activism</span>
                            <p class="donation-empty__msg">후원 내역이 없습니다.</p>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>

        </section><%-- /.mypage-content --%>

    </div><%-- /.mypage-container --%>

    <%-- 공통 푸터 --%>
    <jsp:include page="/view/common/footer.jsp" />

    <script src="${pageContext.request.contextPath}/js/mypage/mypageDonationHistory.js"></script>

</body>
</html>
