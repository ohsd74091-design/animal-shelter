<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctp" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>너와 나의 연결고리 - 후원하기</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com/" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Public+Sans:wght@400;500;600;700;900&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:wght,FILL@100..700,0..1&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/common.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/donation.css">
</head>
<body>

<%-- ========== 기존 공통 헤더 include ========== --%>
<jsp:include page="/view/common/header.jsp" />

<%-- ========== 히어로 배너 ========== --%>
<div class="page-hero">
    <span class="material-symbols-outlined hero-icon">volunteer_activism</span>
    <h2>후원하기 <span>(Donate)</span></h2>
    <p>여러분의 따뜻한 마음이 유기동물들에게 새 삶을 선물합니다.</p>
</div>

<%-- ========== 메인 폼 ========== --%>
<div class="donate-wrap">
    <form id="donateForm" action="${ctp}/donation/insert.do" method="post">

        <%-- ── 1. 후원 방식 ── --%>
        <div class="card">
            <div class="section-title">
                <span class="material-symbols-outlined">sync_alt</span>
                후원 방식 선택
            </div>
            <div class="type-tabs">
                <input type="radio" id="regular" name="donationType" value="정기" checked>
                <label for="regular">
                    <span class="material-symbols-outlined tab-icon">autorenew</span>
                    정기 후원
                    <span class="tab-desc">매월 자동 결제</span>
                </label>

                <input type="radio" id="once" name="donationType" value="일시불">
                <label for="once">
                    <span class="material-symbols-outlined tab-icon">favorite</span>
                    일시 후원
                    <span class="tab-desc">1회 결제</span>
                </label>
            </div>
        </div>

        <%-- ── 2. 금액 ── --%>
        <div class="card">
            <div class="section-title">
                <span class="material-symbols-outlined">payments</span>
                후원 금액 입력
            </div>

            <div class="amount-presets">
                <button type="button" class="preset-btn" data-val="10000">1만원</button>
                <button type="button" class="preset-btn" data-val="30000">3만원</button>
                <button type="button" class="preset-btn" data-val="50000">5만원</button>
                <button type="button" class="preset-btn" data-val="100000">10만원</button>
            </div>

            <div class="amount-wrap">
                <input type="number" class="input-field" id="donationAmount"
                       name="donationAmount" placeholder="직접 입력 (1,000원 이상)"
                       min="1000" step="1000" required>
                <span class="unit">원</span>
            </div>
        </div>

        <%-- ── 3. 기부자 정보 ── --%>
        <div class="card">
            <div class="section-title">
                <span class="material-symbols-outlined">person_edit</span>
                기부자 정보
            </div>

            <div class="input-group">
                <label for="donatorName">성함 / 단체명 <span style="color:var(--primary)">*</span></label>
                <input type="text" class="input-field" id="donatorName" name="donatorName" value="${loginUser.memberName}"
                       placeholder="성함(단체명)을 입력해주세요" required>
            </div>

            <div class="input-row">
                <div class="input-group">
                    <label for="donatorTel">연락처</label>
                    <input type="tel" class="input-field" name="donatorTel" id="donatorTel" value="${loginUser.phone}"
                           placeholder="010-0000-0000" required>
                </div>
                <div class="input-group">
                    <label for="donatorEmail">이메일</label>
                    <input type="email" class="input-field" id="donatorEmail" name="donatorEmail" value="${loginUser.email}"
                           placeholder="example@email.com">
                </div>
            </div>

            <%-- 회원 ID (세션에서 자동 주입, hidden) --%>
            <input type="hidden" name="memberId"
                   value="${sessionScope.loginUser.memberId}">
        </div>

        <%-- ── 4. 기부금 영수증 ── --%>
        <div class="card">
            <div class="section-title">
                <span class="material-symbols-outlined">receipt_long</span>
                기부금 영수증 신청
            </div>

            <label class="checkbox-label" id="receiptLabel" for="receiptCheck">
                <input type="checkbox" id="receiptCheck" name="receiptYn" value="Y">
                <div class="check-box"><span class="check-mark">✓</span></div>
                <div>
                    기부금 영수증 신청
                    <div style="font-size:0.78rem;color:#aaa;font-weight:400;margin-top:2px;">
                        마이페이지 후원내역에서 PDF로 다운로드 가능합니다
                    </div>
                </div>
            </label>

            <div class="receipt-info" id="receiptInfo">
                <p>주민등록번호 / 사업자등록번호를 입력해주세요 (기부금 영수증 발행용)</p>
                <div class="reg-inputs">
                    <input type="text" name="regNo1" maxlength="6"
                           placeholder="생년월일 6자리" id="regNo1">
                    <span class="reg-sep">-</span>
                    <input type="password" name="regNo2" maxlength="7"
                           placeholder="*******" id="regNo2">
                </div>
            </div>
        </div>

        <%-- ── 5. 결제 수단 ── --%>
        <div class="card">
            <div class="section-title">
                <span class="material-symbols-outlined">account_balance_wallet</span>
                결제 수단 선택
            </div>

            <div class="payment-methods">
                <div class="method-card">
                    <input type="radio" id="bank" name="donationMethod"
                           value="계좌이체" checked onchange="toggleBankInfo(true)">
                    <label for="bank">
                        <span class="method-icon">🏦</span>
                        <div>
                            계좌이체
                            <span class="method-desc">가상계좌 / 직접이체</span>
                        </div>
                    </label>
                </div>
                <div class="method-card">
                    <input type="radio" id="card" name="donationMethod"
                           value="카드" onchange="toggleBankInfo(false)">
                    <label for="card">
                        <span class="method-icon">💳</span>
                        <div>
                            카드 결제
                            <span class="method-desc">신용 / 체크카드</span>
                        </div>
                    </label>
                </div>
            </div>

            <div class="bank-guide show" id="bankInfo">
                <p>🏦 계좌 후원 안내</p>
                <div class="bank-row">
                    <span><strong>은행명:</strong> 우리은행</span>
                    <span><strong>계좌번호:</strong> 1002-123-456789</span>
                </div>
                <div style="margin-top:8px;font-size:0.82rem;color:#a05010;">
                    입금자명을 위에 입력한 성함으로 기재해 주세요.
                </div>
            </div>
        </div>

        <%-- ── 6. 최종 요약 + 제출 ── --%>
        <div class="summary-box">
            <div class="summary-title">후원 요약</div>
            <div class="summary-rows">
                <div class="summary-row">
                    <span>후원 방식</span>
                    <span id="summaryType">정기 후원</span>
                </div>
                <div class="summary-row">
                    <span>결제 수단</span>
                    <span id="summaryMethod">계좌이체</span>
                </div>
                <div class="summary-row total">
                    <span>최종 후원 금액</span>
                    <span id="summaryAmount">0원</span>
                </div>
            </div>
        </div>

        <button type="submit" class="submit-btn">
            <span class="material-symbols-outlined">favorite</span>
            <span id="btnText">0원 후원하기</span>
        </button>

        <p class="notice">
            후원 정보는 안전하게 암호화되어 전송됩니다.<br>
            문의: <a href="${ctp}/support/list.do">1:1 문의하기</a> &nbsp;|&nbsp;
            <a href="#">개인정보 처리방침</a>
        </p>

    </form>
</div>

<%-- ========== 성공 모달 ========== --%>
<div class="modal-overlay" id="successModal">
    <div class="modal-box">
        <div class="modal-icon">🧡</div>
        <h3>후원 감사합니다!</h3>
        <p>
            따뜻한 마음 덕분에 유기동물 친구들이<br>
            더 나은 내일을 맞이할 수 있습니다.<br><br>
            <strong id="modalAmount"></strong>이 등록되었습니다.
        </p>

        

        <div class="modal-btns">
            <button class="modal-btn outline" onclick="location.href='${ctp}/main.do'">
                메인으로
            </button>
            <button class="modal-btn primary"
                    onclick="location.href='${ctp}/mypage/main.do'">
                마이페이지
            </button>
        </div>
    </div>
</div>

<%-- ========== 공통 푸터 ========== --%>
<jsp:include page="/view/common/footer.jsp" />
<script>window.CTX = '${ctp}';</script>
<script src="${pageContext.request.contextPath}/js/donation.js"></script>

</body>
</html>
