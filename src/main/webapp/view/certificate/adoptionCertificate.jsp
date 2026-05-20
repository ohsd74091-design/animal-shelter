<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctp" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>동물 입양 승인서</title>

<link rel="stylesheet" href="${ctp}/view/certificate/adoptionCertificate.css?v=2">

<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;600;700;800&family=Be+Vietnam+Pro:wght@400;500;600&display=swap" rel="stylesheet">
<link href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined" rel="stylesheet">

<!-- PDF 다운로드용 -->
<script src="https://cdn.jsdelivr.net/npm/html2canvas@1.4.1/dist/html2canvas.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/jspdf@2.5.1/dist/jspdf.umd.min.js"></script>
</head>
<body>

<div class="cert-page">

    <!-- 상단 버튼 -->
    <div class="cert-topbar no-print">
        <a href="${ctp}/mypage/main.do" class="cert-btn cert-btn--secondary">
            <span class="material-symbols-outlined">arrow_back</span>
            마이페이지로
        </a>

        <button type="button" class="cert-btn cert-btn--ghost" onclick="window.print()">
            <span class="material-symbols-outlined">print</span>
            승인서 출력
        </button>

        <button type="button" class="cert-btn cert-btn--primary" id="btnDownloadPdf">
            <span class="material-symbols-outlined">download</span>
            PDF 다운로드
        </button>
    </div>

    <!-- 승인서 영역 -->
    <div class="cert-sheet" id="certificateArea">

        <div class="cert-watermark">너와 나의 연결고리</div>

        <!-- 헤더 -->
        <header class="cert-header">
            <div class="cert-brand">너와나의연결고리</div>

            <div class="cert-document-meta">
                <div class="cert-document-meta__label">Document ID</div>
                <div class="cert-document-meta__value">${cert.approvalNo}</div>
            </div>

            <div class="cert-chip">Adoption Certificate</div>

            <h1 class="cert-title">동물 입양 승인서</h1>
            <p class="cert-subtitle">Official Adoption Approval &amp; Certificate</p>

            <div class="cert-approval-box">
                <div class="cert-approval-item">
                    <div class="cert-approval-item__label">승인 번호 (Approval No.)</div>
                    <div class="cert-approval-item__value">${cert.approvalNo}</div>
                </div>
                <div class="cert-approval-item">
                    <div class="cert-approval-item__label">승인 일자 (Approval Date)</div>
                    <div class="cert-approval-item__value">${cert.approvalDate}</div>
                </div>
            </div>
        </header>

        <!-- 동물 정보 -->
        <section class="cert-section">
            <div class="cert-section__head">
                <div class="cert-icon cert-icon--pet">
                    <span class="material-symbols-outlined">pets</span>
                </div>
                <h2>입양 동물 정보 (Pet Information)</h2>
            </div>

            <div class="cert-grid cert-grid--pet">
                <div class="cert-card">
                    <div class="cert-label">이름 (Name)</div>
                    <div class="cert-value">${cert.animalName}</div>
                </div>

                <div class="cert-card">
                    <div class="cert-label">품종 (Breed)</div>
                    <div class="cert-value">
                        <c:out value="${empty cert.breed ? '-' : cert.breed}" />
                    </div>
                </div>

                <div class="cert-card">
                    <div class="cert-label">나이 (Age)</div>
                    <div class="cert-value">
                        <c:choose>
                            <c:when test="${not empty cert.age}">${cert.age}세</c:when>
                            <c:otherwise>-</c:otherwise>
                        </c:choose>
                    </div>
                </div>

                <div class="cert-card">
                    <div class="cert-label">성별 (Gender)</div>
                    <div class="cert-value">
                        <c:choose>
                            <c:when test="${cert.gender eq 'M' && cert.neutered eq 'Y'}">수컷 (중성화)</c:when>
                            <c:when test="${cert.gender eq 'F' && cert.neutered eq 'Y'}">암컷 (중성화)</c:when>
                            <c:when test="${cert.gender eq 'M'}">수컷</c:when>
                            <c:when test="${cert.gender eq 'F'}">암컷</c:when>
                            <c:otherwise>-</c:otherwise>
                        </c:choose>
                    </div>
                </div>

                <div class="cert-card cert-card--full">
                    <div class="cert-label">구조 번호 (Rescue ID)</div>
                    <div class="cert-value cert-value--mono">RESCUE-${cert.animalId}</div>
                </div>

                <div class="cert-card">
                    <div class="cert-label">구조일 (Rescue Date)</div>
                    <div class="cert-value">
                        <c:out value="${empty cert.rescueDate ? '-' : cert.rescueDate}" />
                    </div>
                </div>

                <div class="cert-card">
                    <div class="cert-label">방문 예정일 (Visit Date)</div>
                    <div class="cert-value">
                        <c:out value="${empty cert.visitDate ? '-' : cert.visitDate}" />
                    </div>
                </div>
            </div>
        </section>

        <!-- 신청자 정보 -->
        <section class="cert-section">
            <div class="cert-section__head">
                <div class="cert-icon cert-icon--person">
                    <span class="material-symbols-outlined">person</span>
                </div>
                <h2>입양 신청자 정보 (Adopter Information)</h2>
            </div>

            <div class="cert-grid cert-grid--adopter">
                <div class="cert-card">
                    <div class="cert-label">성명 (Name)</div>
                    <div class="cert-value">${cert.memberName}</div>
                </div>

                <div class="cert-card">
                    <div class="cert-label">연락처 (Contact)</div>
                    <div class="cert-value">
                        <c:out value="${empty cert.phone ? '-' : cert.phone}" />
                    </div>
                </div>

                <div class="cert-card">
                    <div class="cert-label">직업 (Occupation)</div>
                    <div class="cert-value">
                        <c:out value="${empty cert.job ? '-' : cert.job}" />
                    </div>
                </div>

                <div class="cert-card cert-card--full">
                    <div class="cert-label">주소 (Address)</div>
                    <div class="cert-value cert-value--address">
                        <c:out value="${empty cert.address ? '-' : cert.address}" />
                    </div>
                </div>

                <div class="cert-card">
                    <div class="cert-label">신청일 (Apply Date)</div>
                    <div class="cert-value">
                        <c:out value="${empty cert.applyDate ? '-' : cert.applyDate}" />
                    </div>
                </div>

                <div class="cert-card">
                    <div class="cert-label">상태 (Status)</div>
                    <div class="cert-value cert-value--highlight">
                        <c:out value="${empty cert.status ? '-' : cert.status}" />
                    </div>
                </div>
            </div>
        </section>

        <!-- 공식 문구 -->
        <section class="cert-note">
            <div class="cert-note__title">
                <span class="material-symbols-outlined">gavel</span>
                공식 승인 선언 (Official Approval)
            </div>
            <p class="cert-note__text">
                위 신청자는 ‘너와 나의 연결고리’의 입양 심사 절차를 통과하여
                상기 동물에 대한 입양 승인을 받았음을 확인합니다.
                신청자는 예정된 방문일에 보호소를 방문하여 상담 및 인계 절차를 진행해야 하며,
                최종 인계 완료 전까지 보호소의 안내사항을 성실히 따라야 합니다.
            </p>
        </section>

        <!-- 하단 -->
        <footer class="cert-footer">
            <div class="cert-footer__text">
                본 승인서는 ‘너와 나의 연결고리’ 보호소 시스템을 통해 발급된 확인 문서입니다.<br>
                무단 변조 또는 부정 사용 시 효력이 인정되지 않습니다.
            </div>

            <div class="cert-sign-area">
                <div class="cert-sign">
                    <div class="cert-sign__label">발급 주체 (Issuer)</div>
                    <div class="cert-sign__name">센터 운영 위원회</div>
                </div>

                <div class="cert-seal">
                    <div class="cert-seal__inner">
                        <div class="cert-seal__small">ANIMAL RESCUE</div>
                        <div class="cert-seal__box">너와 나의</div>
                        <div class="cert-seal__box">연결고리</div>
                        <div class="cert-seal__small">OFFICIAL SEAL</div>
                    </div>
                </div>
            </div>
        </footer>
    </div>
</div>

<script>
document.getElementById('btnDownloadPdf').addEventListener('click', async function () {
    const { jsPDF } = window.jspdf;
    const target = document.getElementById('certificateArea');

    try {
        const canvas = await html2canvas(target, {
            scale: 2,
            useCORS: true,
            backgroundColor: '#ffffff'
        });

        const imgData = canvas.toDataURL('image/png');

        const pdf = new jsPDF('p', 'mm', 'a4');
        const pdfWidth = 210;
        const pdfHeight = 297;

        const imgWidth = pdfWidth;
        const imgHeight = canvas.height * pdfWidth / canvas.width;

        let heightLeft = imgHeight;
        let position = 0;

        pdf.addImage(imgData, 'PNG', 0, position, imgWidth, imgHeight);
        heightLeft -= pdfHeight;

        while (heightLeft > 0) {
            position = heightLeft - imgHeight;
            pdf.addPage();
            pdf.addImage(imgData, 'PNG', 0, position, imgWidth, imgHeight);
            heightLeft -= pdfHeight;
        }

        const fileName = '${cert.approvalNo}' ? '${cert.approvalNo}.pdf' : 'adoption-certificate.pdf';
        pdf.save(fileName);
    } catch (e) {
        console.error(e);
        alert('PDF 생성 중 오류가 발생했습니다.');
    }
});
</script>

</body>
</html>