<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>너와 나의 연결고리 - 내 문의 및 답변</title>
     <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/common.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/support_list.css">
	 <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com/" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Public+Sans:wght@400;500;600;700;900&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:wght,FILL@100..700,0..1&display=swap" rel="stylesheet">
</head>
<body>

<jsp:include page="/view/common/header.jsp" />
<div class="wrapper">
<main class="layout-container">
	<div class="support-container">
	    <h2>내 문의 및 답변</h2>
	    <p class="subtitle">반려동물에 대해 궁금하신 점과 보호소의 답변을 확인하세요.</p>
	
	    <div class="tab-menu">
	        <button class="tab-btn ${param.status eq 'all' or empty param.status ? 'active' : ''}" 
            onclick="location.href='${pageContext.request.contextPath}/support/list.do?page=1&status=all'">전체 문의</button>
    		<button class="tab-btn ${param.status eq 'N' ? 'active' : ''}" 
            onclick="location.href='${pageContext.request.contextPath}/support/list.do?page=1&status=N'">답변 대기</button>
    		<button class="tab-btn ${param.status eq 'Y' ? 'active' : ''}" 
            onclick="location.href='${pageContext.request.contextPath}/support/list.do?page=1&status=Y'">답변 완료</button>
	       	<button type="button" class="write-btn" onclick="goToWritePage()">
			    <span class="material-symbols-outlined">edit</span>
			    1:1 문의하기
			</button>
	    </div>
	
	    <table class="support-table">
	        <thead>
	            <tr>
	                <th>문의 번호</th>
	                <th>유형</th>
	                <th>제목</th>
	                <th>날짜</th>
	                <th>상태</th>
	                <th>작업</th>
	            </tr>
	        </thead>
	        	<tbody id="supportBody">
	        	<!-- if문 test="${empty sList} 참이면 문의내역 없음 출력 -->
	        		<c:if test="${empty sList}">
	        			<tr><td colspan="6" style="text-align: center;">문의 내역이 없습니다.</td></tr>
	        		</c:if>
	        		<!-- items="${sList}": 반복할 대상 데이터 바구니 -->
	        		<c:forEach var="svo" items="${sList}">
	        			<tr class="support-row" data-status="${svo.status == 'Y' ? '완료' : '대기'}">
	        				<td>${svo.supportId}</td> 
	        				<td>${svo.supportType}</td>
	        				<td>${svo.title}</td>
	        				<td><fmt:formatDate value="${svo.createDate}" pattern="yyyy.MM.dd"/></td>
	           				 <td>
	              			  <c:choose>
	              			  	<c:when test="${svo.status == 'Y'}">
			                        <span class="status-badge done">답변 완료</span>
			                    </c:when>
			                    <c:otherwise>
			                        <span class="status-badge wait">답변 대기</span>
			                    </c:otherwise>
			                </c:choose>
			                </td>
			            	<td><button class="view-btn" onclick="toggleDetail(this)">상세 보기</button></td>
	      				</tr>
	      				<!-- 상세보기 클릭시 활성화 -->
	        			<tr class="detail-content" style="display: none;">
	        				
				            <td colspan="6">
				               <div class="qna-box">
								    <div class="question"><strong>Q.</strong> ${svo.content}</div>
								    
								    <c:if test="${svo.status == 'N'}">
								        <div class="detail-actions" style="text-align: right; margin-top: 10px;">
								            <button type="button" class="edit-btn" 
											    onclick="openEditForm('${svo.supportId}', '${svo.supportType}', '${fn:escapeXml(svo.title)}', '${fn:escapeXml(svo.content)}')">
											    수정하기
											</button>
								            <button type="button" class="delete-btn" 
								                onclick="deleteInquiry('${svo.supportId}')">삭제하기</button>
								        </div>
								    </c:if>
								
								    <c:forEach var="rvo" items="${svo.replyList}">
								        <div class="answer"><strong>A.</strong> ${rvo.replyContent}</div>
								    </c:forEach>
								    
								    <c:if test="${empty svo.replyList}">
								        <div class="empty-answer">관리자가 확인 중입니다.</div>
								    </c:if>
								</div>
				            </td>
				        </tr>
	        		</c:forEach>
	        	</tbody>
	    </table>
	    <div id="pagination-area">
        ${pager}
    	</div>
	</div>
	<!-- js(editModal), -->
	<div id="editModal" class="modal" style="display: none;">
    <div class="modal-content">
        <form id="editForm"> <input type="hidden" name="support_id" id="editSupportId">
            
            <div class="input-group">
                <label>문의 유형</label>
                <select name="supportType" id="editSupportType" class="form-control">
                    <option value="입양문의">입양 문의</option>
                    <option value="후원문의">후원 문의</option>
                    <option value="봉사문의">봉사 문의</option>
                    <option value="일반문의">일반 문의</option>
                </select>
            </div>
            
            <div class="form-group">
                <label>제목</label>
                <input type="text" name="edit_title" id="editTitle" required>
            </div> 
            
            <div class="form-group">
                <label>내용</label>
                <textarea name="edit_content" id="editContent" rows="10" required></textarea>
            </div>

            <div class="modal-btns">
                <button type="button" class="cancel-btn" onclick="closeEditModal()">취소</button>
                <button type="button" class="submit-btns" onclick="submitEdit()">수정 완료</button>
            </div>
        </form>
    </div>
</div>
</main>
</div>
<jsp:include page="/view/common/footer.jsp" />

<script>
function goToWritePage() {
    // 세션에 저장된 loginUser가 있는지 체크 (JSP 태그 활용)
    const isLogin = "${not empty sessionScope.loginUser}";

    if (isLogin === "true") {
        // 로그인 상태면 작성 페이지로 이동
        location.href = "${pageContext.request.contextPath}/view/support.jsp";
    } else {
        // 비로그인 상태면 알림 후 로그인 페이지로 유도
        alert("로그인 된 사용자만 이용 가능합니다.");
        location.href = "${pageContext.request.contextPath}/login.do";
    }
}

function changePage(pageNo) {
    // 현재 주소창에서 status 값을 가져와서 (없으면 all)
    const urlParams = new URLSearchParams(window.location.search);
    let status = urlParams.get('status') || 'all';
    
    // 페이지 번호와 상태값을 함께 서버로 던짐
    location.href = "${pageContext.request.contextPath}/support/list.do?page=" + pageNo + "&status=" + status;
}
</script>

<script src="../js/support_list.js"></script>
</body>
</html>