<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>너와 나의 연결고리 - 회원 정보 수정</title>

<%-- 공통 CSS --%>
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined" rel="stylesheet">
<link href="https://cdn.jsdelivr.net/gh/orioncactus/pretendard/dist/web/static/pretendard.css" rel="stylesheet">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/common.css">

<%-- 마이페이지 공통 CSS (사이드바 등 공유) --%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/mypage/mypage.css">

<%-- 회원정보수정 전용 CSS / JS --%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/mypage/mypageEdit.css">
<script src="${pageContext.request.contextPath}/js/mypage/mypageEdit.js" defer></script>

</head>
<body>

    <%-- header --%>
    <jsp:include page="/view/common/header.jsp" />

    <div class="mypage-container">

        <%-- ===== 사이드바 ===== --%>
        <jsp:include page="/view/common/sidebarNav.jsp">
		    <jsp:param name="activeMenu" value="edit"/>
		</jsp:include>

        <%-- ===== 메인 콘텐츠 ===== --%>
        <section class="mypage-content">
            <div class="edit-card">
                <h3 class="edit-title">회원 정보 수정</h3>

                <%-- 서버 메시지 출력 (성공/실패) --%>
                <c:if test="${not empty successMsg}">
                    <div class="alert alert-success">
                        <span class="material-symbols-outlined">check_circle</span>
                        <c:out value="${successMsg}"/>
                    </div>
                </c:if>
                <c:if test="${not empty errorMsg}">
                    <div class="alert alert-error">
                        <span class="material-symbols-outlined">error</span>
                        <c:out value="${errorMsg}"/>
                    </div>
                </c:if>

                <%-- ── 폼 시작 ── --%>
                <form id="editForm" action="${pageContext.request.contextPath}/mypage/edit.do" 
                	method="post" enctype="multipart/form-data">

                    <%-- ── 프로필 사진 ── --%>
                    <div class="profile-section">
                        <div class="profile-img-wrap">
                            <img
                                id="previewImg"
                                src="${not empty loginUser.profileImg
                                        ? pageContext.request.contextPath.concat('/member/profileImage.do?fileName=').concat(loginUser.profileImg)
                                        : pageContext.request.contextPath.concat('/images/mypage/default.png')}"
                                alt="프로필 사진"
                            >
                            <label class="photo-edit-btn" for="profileImgInput" aria-label="사진 변경">
                                <span class="material-symbols-outlined">photo_camera</span>
                                <input type="file" id="profileImgInput" name="profileImg" accept="image/*" class="hidden-input">
                            </label>
                        </div>
                        <div class="profile-info-text">
                            <p class="profile-nickname"><c:out value="${loginUser.nickname}"/></p>
                            <p class="profile-email"><c:out value="${loginUser.email}"/></p>
                        </div>
                    </div>

                    <%-- 계정 정보 --%>
                    <div class="form-section">
                        <h4 class="form-section-title">계정 정보</h4>
                        <div class="form-grid">

                            <div class="form-group">
                                <label class="form-label">아이디</label>
                                <input
                                    type="text"
                                    class="form-input readonly"
                                    value="<c:out value="${loginUser.memberId}"/>"
                                    readonly
                                    aria-label="아이디 (변경 불가)"
                                >
                            </div>

                            <div class="form-group">
                                <label class="form-label" for="email">이메일</label>
                                <div class="input-btn-wrap">
                                    <input
                                        type="email"
                                        id="email"
                                        name="email"
                                        class="form-input"
                                        value="<c:out value="${loginUser.email}"/>"
                                        placeholder="이메일을 입력하세요"
                                    >
                                    <button type="button" class="btn-inline" id="btnEmailCheck">변경</button>
                                </div>
                                <p class="field-msg" id="emailMsg"></p>
                            </div>

                        </div>
                    </div>

                    <%-- 기본 정보 --%>
                    <div class="form-section">
                        <h4 class="form-section-title">기본 정보</h4>
                        <div class="form-grid">

                            <div class="form-group">
                                <label class="form-label" for="nickname">닉네임</label>
                                <div class="input-btn-wrap">
                                    <input
                                        type="text"
                                        id="nickname"
                                        name="nickname"
                                        class="form-input"
                                        value="<c:out value="${loginUser.nickname}"/>"
                                        placeholder="닉네임을 입력하세요"
                                        maxlength="20"
                                    >
                                    <button type="button" class="btn-inline" id="btnNicknameCheck">중복 확인</button>
                                </div>
                                <p class="field-msg" id="nicknameMsg"></p>
                            </div>

                            <div class="form-group">
                                <label class="form-label" for="phone">전화번호</label>
                                <input
                                    type="tel"
                                    id="phone"
                                    name="phone"
                                    class="form-input"
                                    value="<c:out value="${loginUser.phone}"/>"
                                    placeholder="010-0000-0000"
                                    maxlength="13"
                                >
                                <p class="field-msg" id="phoneMsg"></p>
                            </div>

                        </div>
                    </div>

                    <%-- 비밀번호 변경 --%>
                    <div class="form-section">
                        <h4 class="form-section-title">비밀번호 변경</h4>
                        <div class="form-col">

                            <div class="form-group">
                                <label class="form-label" for="currentPw">현재 비밀번호</label>
                                <input
                                    type="password"
                                    id="currentPw"
                                    name="currentPw"
                                    class="form-input"
                                    placeholder="현재 비밀번호를 입력하세요"
                                    autocomplete="current-password"
                                >
                                <p class="field-msg" id="currentPwMsg"></p>
                            </div>

                            <div class="form-group">
                                <label class="form-label" for="newPw">새 비밀번호</label>
                                <input
                                    type="password"
                                    id="newPw"
                                    name="newPw"
                                    class="form-input"
                                    placeholder="새 비밀번호를 입력하세요 (8자 이상)"
                                    autocomplete="new-password"
                                >
                                <p class="field-msg" id="newPwMsg"></p>
                            </div>

                            <div class="form-group">
                                <label class="form-label" for="newPwConfirm">새 비밀번호 확인</label>
                                <input
                                    type="password"
                                    id="newPwConfirm"
                                    name="newPwConfirm"
                                    class="form-input"
                                    placeholder="새 비밀번호를 다시 입력하세요"
                                    autocomplete="new-password"
                                >
                                <p class="field-msg" id="newPwConfirmMsg"></p>
                            </div>

                        </div>
                    </div>

                    <%-- 버튼 --%>
                    <div class="form-actions">
                        <button type="submit" class="btn-primary" id="btnSubmit">변경사항 저장</button>
                        <button type="button" class="btn-secondary"
                            onclick="location.href='${pageContext.request.contextPath}/mypage/main.do'">
                            취소
                        </button>
                    </div>

                </form><%-- /#editForm --%>

                <%-- 회원 탈퇴 --%>
                <div class="withdraw-area">
                    <button type="button" class="btn-withdraw" id="btnWithdraw">
                        <span class="material-symbols-outlined">no_accounts</span>
                        회원 탈퇴
                    </button>
                </div>

            </div><%-- /.edit-card --%>
        </section>

    </div><%-- /.mypage-container --%>

    <%-- footer --%>
    <jsp:include page="/view/common/footer.jsp" />

</body>
</html>
