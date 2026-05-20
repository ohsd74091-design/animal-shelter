<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctp" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
    <title>문의 상세 및 답변</title>
    <link rel="stylesheet" href="${ctp}/view/admin/css/admin_support.css">
    <link rel="stylesheet" href="${ctp}/view/admin/css/common/admin-layout.css">
    <link href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined" rel="stylesheet"/>
    <style>
        /* 이미지 갤러리 스타일 추가 */
        .image-gallery { display: flex; gap: 15px; margin: 20px 0; flex-wrap: wrap; }
        .support-img { width: 200px; height: 150px; object-fit: cover; border-radius: 8px; border: 1px solid #eee; cursor: pointer; }
        .no-image { color: #888; font-size: 14px; padding: 10px; background: #f9f9f9; border-radius: 5px; }
    </style>
</head>
<body>
    <div class="admin-layout">
        <jsp:include page="/view/admin/common/admin-sidebar.jsp" />
        
        <div class="admin-main-wrap">
            <jsp:include page="/view/admin/common/admin-header.jsp" />

            <main class="detail-wrapper">
                <button onclick="location.href='${ctp}/admin/support/list.do'" class="btn-back">
                    <span class="material-symbols-outlined" style="font-size: 20px;">arrow_back</span>
                    목록으로 돌아가기
                </button>

                <section class="card" style="padding: 40px;">
                    <div class="detail-header">
                        <span class="badge status-waiting">문의 번호: #${support.supportId}</span>
                        <h2 style="font-size: 26px; font-weight: 800; margin: 15px 0;">${support.title}</h2>
                    </div>
                    
                    <div class="content-box">
                        <div class="author-info">
                            <span>작성자: ${support.memberId}</span>
                            <span style="font-weight: normal; color: #888;">${support.createDate}</span>
                        </div>
                        <hr style="margin: 15px 0; border: 0; border-top: 1px solid #ddd;">
                        <p class="content-text" style="white-space: pre-wrap;">${support.content}</p>
                    </div>

                    <div class="image-gallery">
                        <c:choose>
                            <c:when test="${not empty support.fileList}">
							    <c:forEach var="file" items="${support.fileList}">
							        <a href="${ctp}/upload/${file.saveFileName}" target="_blank">
							            <img src="/upload/${file.saveFileName}" class="support-img" alt="첨부이미지">
							        </a>
							    </c:forEach>
							</c:when>
                            <c:otherwise>
                                <p class="no-image">첨부된 이미지가 없습니다.</p>
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <div class="reply-section">
                        <h3 style="font-weight: bold; margin-bottom: 15px; color: #333;">관리자 답변 등록</h3>
                        <textarea id="content" class="reply-area" placeholder="사용자에게 전달할 답변 내용을 상세히 입력하세요...">
                        <c:if test="${not empty support.replyList}">${support.replyList[0].replyContent}</c:if></textarea>
                        
                        <button type="button" class="btn-submit" onclick="sendReply()">
                            답변 완료 등록
                        </button>
                    </div>
                </section>
            </main>
        </div>
    </div>

    <script>
        function sendReply() {
            const reply = document.getElementById('content').value;
            const sId = "${support.supportId}"; // EL 태그로 글번호 가져오기
            
            if(!reply.trim()) { 
                alert("답변 내용을 입력하세요.");
                return;
            }

            if(!confirm("이 내용으로 답변을 등록하시겠습니까?")) return;

            // 비동기 전송 시작
            fetch('${ctp}/admin/support/reply.do', {
                method: 'POST',
                headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                body: "supportId=" + sId + "&replyContent=" + encodeURIComponent(reply)
            })
            .then(response => {
                if(!response.ok) throw new Error("네트워크 응답 오류");
                return response.json();
            })
            .then(data => {
                if(data.status === "success") {
                    alert("답변이 성공적으로 등록되었습니다!");
                    location.href = "${ctp}/admin/support/list.do";
                } else {
                    alert("답변 등록에 실패했습니다. (관리자 세션 만료 등)");
                }
            })
            .catch(error => {
                console.error("Error:", error);
                alert("서버 통신 중 오류가 발생했습니다.");
            });
        }
    </script>
</body>
</html>