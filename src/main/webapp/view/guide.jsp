<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctp" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>입양 안내 | 너와 나의 연결고리</title>
<link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@20..48,100..700,0..1,-50..200" />
<link rel="stylesheet" href="https://cdn.jsdelivr.net/gh/orioncactus/pretendard@v1.3.9/dist/web/static/pretendard.css" />

<%-- 공통 CSS --%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/common.css">
<%-- 입양 안내 CSS --%>
<link rel="stylesheet" href="${ctp}/css/guide.css">
    
</head>
<body>

<div class="admin-wrapper">
    <jsp:include page="/view/common/header.jsp" />
    
    <div class="main-content">
       <main>
           <section class="guide-hero">
               <div class="layout-container">
                   <p>ADOPTION GUIDE</p>
                   <h1>새로운 가족을 맞이하는<br>가장 아름다운 방법</h1>
               </div>
           </section>
           
           <section class="layout-container section-padding">
    <div class="section-title text-left">
        <h2>입양 신청 자격<span>조건</span></h2>
        <p>생명을 책임지는 소중한 약속입니다. 아래 조건을 확인해 주세요.</p>
    </div>
    
    <div class="eligibility-grid">
        <div class="eligibility-content">
            <ul class="check-list">
                <li>
                    <strong>경제적 자립 및 성인 기준</strong>
                    만 25세 이상의 경제적으로 독립된 성인 (미성년자 및 학생 불가)
                </li>
                <li>
                    <strong>가족 구성원의 전원 동의</strong>
                    함께 거주하는 모든 가족 구성원이 입양에 찬성해야 합니다.
                </li>
                <li>
                    <strong>반려 환경 적합성</strong>
                    동물이 충분히 활동할 수 있는 적절한 주거환경이 마련되어야 합니다.
                </li>
            </ul>
        </div>
        
        <div class="eligibility-image-wrap">
            <img src="${ctp}/images/animalguide/family-dog.jpg" alt="입양가족" class="guide-img">
        </div>
    </div>
</section>
           
           <section class="section-padding bg-light">
                <div class="layout-container">
                     <div class="section-title">
                        <h2>입양 진행 절차</h2>
                        <p>체계적인 과정을 통해 아이들의 평생 가족이 되어주세요.</p>
                     </div>
                     
                     <div class="process-steps">
                         <div class="step-item" data-step="01">
                              <div class="step-number">1</div>
                              <div class="step-content">
                                   <h3>01. 입양 상담 및 신청</h3>
                                   <p>온라인 또는 오프라인을 통해 입양 희망 동물에 대한 정보를 확인하고 신청서를 작성합니다.</p> 
                              </div>
                         </div>
                      <div class="step-item" data-step="02">
                          <div class="step-number">2</div>
                          <div class="step-conent">
                               <h3>02. 1차 서류심사</h3>
                               <p>작성하신 신청서를 토대로 전문가가 심사를 진행하여 적합성을 검토합니다</p>
                          </div>
                      </div>
                      <div class="step-item" data-step="03">
                          <div class="step-number">3</div>
                          <div class="step-conent">
                               <h3>03. 가정 방문 심사</h3>
                               <p>매니저가 직접 가정을 방문하여 실제환경을 확인하고 안전 수칙을 안내합니다</p>
                          </div>
                      </div>
                      <div class="step-item" data-step="04">
                          <div class="step-number">4</div>
                          <div class="step-conent">
                               <h3>04. 만남 및 입양 확정</h3>
                               <p>최종 승인 후 아이와 대면 시간을 가지고 입양 계약서를 작성합니다</p>
                          </div>
                      </div>
                     </div>
                </div>
           </section>
           
           <section class="layout-container section-padding">
                <div class="after-care-header">
                    <div class="section-title text-left" style="margin:0;">
                        <h2>지속적인 사후 관리 프로그램</h2>
                        <p>행복한 반려 생활이 지속될 수 있도록 다양한 혜택을 지원합니다.</p>
                </div>
       
                </div>
                
                <div class="program-grid">
                   <div class="program-card">
                       <div class="icon-box orange">
                          <span class="material-symbols-outlined">psychology</span>
                       </div>
                       <h4>행동 교육 컨설팅</h4>
                       <p>전문 지도사가 방문하여 입양 초기 적응을 위한 맞춤형 교육을 지원합니다.</p>
                   </div>
                   <div class="program-card">
                      <div class="icon-box red">
                         <span class="material-symbols-outlined">health_and_safety</span>
                      </div>
                      <h4>의료비 지원 혜택</h4>
                      <p>협력 병원을 통해 정기검진 및 예방접종 비용을 일부를 지원합니다.</p>
                   </div>
                   <div class="program-card">
                      <div class="icon-box blue">
                          <span class="material-symbols-outlined">groups</span>
                      </div>
                      <h4>입양자 커뮤니티</h4>
                      <p>입양 가족 간의 정보 공유와 소통을 위한 정기적인 모임을 지원합니다.</p>
                   </div>
                </div>
           </section>
           
           
            <%-- CTA Banner --%>
           <section class="cta-banner-section">
               <div class="layout-container">
                   <div class="cta-banner">
                       <div class="cta-content">
                           <h2>지금 당신의 작은 용기가<br>한 생명을 구할 수 있습니다.</h2>
                           <div class="cta-agree">
                               <label class="agree-label">
                                   <input type="checkbox" id="agreeCheck">
                                   <span>안내 및 약관을 확인하였습니다</span>
                               </label>
                           </div>
                           <div class="cta-buttons">
                               <c:choose>
                                   <c:when test="${not empty param.animalId}">
                                       <button id="adoptBtn" class="btn-adopt" disabled
                                           onclick="location.href='${ctp}/adoption/adoptionForm.do?animalId=${param.animalId}'">
                                           입양 신청하기
                                       </button>
                                   </c:when>
                                   <c:otherwise>
                                       <button id="adoptBtn" class="btn-adopt" disabled
                                           onclick="location.href='${ctp}/animal/animalList.do?fromGuide=true'">
                                           입양 신청하기
                                       </button>
                                   </c:otherwise>
                               </c:choose>
                               <a href="${ctp}/support/list.do" class="btn-inquiry">문의사항 남기기</a>
                           </div>
                       </div>
                   </div>
               </div>
           </section>
           
           <script>
               (function() {
                   var checkbox = document.getElementById('agreeCheck');
                   var adoptBtn = document.getElementById('adoptBtn');
                   checkbox.addEventListener('change', function() {
                       adoptBtn.disabled = !this.checked;
                   });
               })();
           </script>
          
       </main>
       <jsp:include page="/view/common/footer.jsp" />
    </div>
</div>

</body>
</html>