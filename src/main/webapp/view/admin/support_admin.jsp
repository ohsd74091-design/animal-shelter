<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctp" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
    <title>관리자 문의 목록</title>
    <link rel="stylesheet" href="${ctp}/view/admin/css/admin_support.css">
    <link rel="stylesheet" href="${ctp}/view/admin/css/common/admin-layout.css">
    <link href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined" rel="stylesheet"/>
    <script defer src="${ctp}/view/admin/js/admin-layout.js"></script>
    <script defer src="${ctp}/view/admin/js/admin_support.js"></script>
</head>
<body>
    <div class="admin-layout">
        <jsp:include page="/view/admin/common/admin-sidebar.jsp" />

        <div class="admin-main-wrap">
            <jsp:include page="/view/admin/common/admin-header.jsp" />

            <main class="inquiry-wrapper">
                <div class="card">
               
                    <div class="card-header admin-header-flex">
                    <div class="header-title">
					        문의 관리 (Support Management)
					    </div>
					    <div class="filter-area">
					        <form id="searchForm" action="${ctp}/admin/support/list.do" method="get">
					            <input type="hidden" name="page" id="page" value="${pagingInfo.currentPage}">
					            <select name="supportType" onchange="submitForm()" class="admin-select">
					                <option value="">유형 전체</option>
					                <option value="입양문의" ${currentType == '입양문의' ? 'selected' : ''}>입양문의</option>
					                <option value="후원문의" ${currentType == '후원문의' ? 'selected' : ''}>후원문의</option>
					                <option value="봉사문의" ${currentType == '봉사문의' ? 'selected' : ''}>봉사문의</option>
					                <option value="일반문의" ${currentType == '일반문의' ? 'selected' : ''}>일반문의</option>
					            </select>
					
					            <select name="status" onchange="submitForm()" class="admin-select">
					                <option value="">상태 전체</option>
					                <option value="N" ${currentStatus == 'N' ? 'selected' : ''}>답변 대기</option>
					                <option value="Y" ${currentStatus == 'Y' ? 'selected' : ''}>답변 완료</option>
					            </select>
					        </form>
					    </div>
					</div>
                    
                    <table class="inquiry-table">
                        <thead>
                            <tr>
                                <th style="width: 10%;">문의 번호</th>
                                <th style="width: 12%;">작성자</th>
                                <th style="width: 12%;">문의 유형</th>
                                <th style="width: 40%;">제목</th>
                                <th style="width: 12%;">날짜</th>
                                <th style="width: 12%;">상태</th>
                            </tr>
                        </thead>
                        <tbody>
                           <c:forEach var="support" items="${adminList}">
                           		<tr class="main-row" onclick="toggleContent('content-${support.supportId}')">
                           			<td>#${support.supportId }</td>
                           			<td>${support.memberId }</td>
                           			<td>${support.supportType }</td>
                           			<td class="font-medium">${support.title }</td>
                           			<td>${support.createDate }</td>
                           			<td>
                           				<span class="badge ${support.status eq 'Y' ? 'status-complete' : 'status-waiting'}">
                           					${support.status eq 'Y' ? '답변 완료' : '답변 대기'}
                           				</span>
                           			</td>
                           		</tr>
                           		
                           		<tr id="content-${support.supportId}" class="detail-row" style="display: none;">
						            <td colspan="6">
						                <div class="detail-inner">
						                    <p><strong>문의 내용:</strong></p>
						                    <div class="content-text">${support.content}</div>
						                    
						                    <hr> <p><strong>[관리자 답변]</strong></p>
											   <div class="reply-text">
											       <c:choose>
											           <c:when test="${not empty support.replyList}">
											               <c:forEach var="reply" items="${support.replyList}">
											                   <div class="reply-item">
											                       <p>${reply.replyContent}</p>
											                       <small>(작성일: ${reply.createDate})</small>
											                   </div>
											               </c:forEach>
											           </c:when>
											           <c:otherwise>
											               <p style="color: #999;">아직 등록된 답변이 없습니다.</p>
											           </c:otherwise>
											       </c:choose>
											   </div>
						                    
						                    <div class="detail-btns" style="margin-top: 20px;">
						                        <button class="btn-go" onclick="location.href='${ctp}/admin/support/detail.do?supportId=${support.supportId}'">
						                            답변 등록/수정(이미지 확인)하러 가기
						                        </button>
						                    </div>
						                </div>
						            </td>
						        </tr>
                           </c:forEach>
                        </tbody>
                    </table>
                   <div class="paging-area" style="text-align: center; margin-top: 20px; margin-bottom: 20px;">
                        ${pageHtml}
                    </div>
                   
                </div>
            </main>
        </div>
    </div>
</body>
</html>