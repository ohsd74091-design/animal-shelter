<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>너와 나의 연결고리 - 1:1 문의하기</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/support.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/common.css">
    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;600;700&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined" rel="stylesheet" />
    
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com/" crossorigin>
</head>
<body>
<jsp:include page="/view/common/header.jsp" />

 <div class="main-content">
     <main class="inquiry-card">
         <header class="card-header">
             <h1>1:1 문의하기</h1>
             <p>궁금하신 점을 남겨주시면 정성껏 답변해 드리겠습니다.</p>
         </header>

         <form id="inquiryForm" action="${pageContext.request.contextPath}/insertSupport.do" 
      				data-context-path="${pageContext.request.contextPath}" enctype="multipart/form-data">
            <div class="form-row">
			    <div class="input-group type-selector">
			        <label>문의 유형</label>
			        <select name="supportType" id="supportType" class="form-control" required>
			            <option value="">문의 유형을 선택해주세요</option>
			            <option value="입양문의">입양 문의</option>
			            <option value="후원문의">후원 문의</option>
			            <option value="봉사문의">봉사 문의</option>
			            <option value="일반문의">일반 문의</option>
			        </select>
			        <span id="typeMsg" class="msg"></span>
			    </div>
	
				    <!-- <div class="checkbox-group">
				        <input type="checkbox" id="isPrivate" name="isPrivate">
				        <label for="isPrivate">비공개 문의로 설정</label>
				    </div> -->
			</div>
                         
              <div class="input-group">
                 <label>제목</label>
                 <input type="text" name="title" id="title" class="form-control" placeholder="제목을 입력해주세요" required>
                 <span id="titleMsg" class="msg"></span> 
             	</div>
             	
             	
             <div class="input-group">
                 <label>내용</label>
                 <textarea name="content" class="form-control" rows="6" placeholder="상세 내용을 입력해주세요. (입양/봉사 신청 관련 문의 시 신청자 성함과 연락처를 남겨주시면 원활한 상담이 가능합니다.)" required></textarea>
             	  <span id="contentMsg" class="msg"></span>
             </div>
           

             <div class="image-section">
                 <label>이미지 첨부 (최대 3장)</label>
                 <div class="image-upload-wrapper">
                     <label class="upload-btn">
                         <span class="material-symbols-outlined">add_a_photo</span>
                         <span>이미지 추가</span>
                         <input type="file" id="imageInput" name="atchFile" accept="image/*" multiple hidden>
                  	</label>
                     <div id="previewContainer" class="preview-container"></div>
                 </div>
             </div>

             <button type="button" id="submitBtn" class="submit-btn">
                 <span class="material-symbols-outlined">send</span>
                 문의 등록하기
             </button>
         </form>

         <!-- <div id="successBox" class="success-box" style="display: none;">
             <span class="material-symbols-outlined">check_circle</span>
             <div>
                 <strong>전송 완료</strong>
                 <p>문의가 정상적으로 등록되었습니다. 담당자가 확인 후 빠른 시일 내에 답변해 드리겠습니다.</p> -->
     </main>
 </div>
<jsp:include page="/view/common/footer.jsp" />    
<script src="${pageContext.request.contextPath}/js/support.js"></script>
</body>
</html>