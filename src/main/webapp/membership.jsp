<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>너와 나의 연결고리 - 회원가입</title>
<style>
     body {
         margin: 0; padding: 0;
         font-family: 'Pretendard', sans-serif;
         background-color: #F9F7F2;
         display: flex; justify-content: center; align-items: center;
         min-height: 100vh;
     }
     .signup-card {
         background: #ffffff;
         width: 100%; max-width: 480px;
         padding: 40px;
         border-radius: 24px;
         box-shadow: 0 10px 30px rgba(0, 0, 0, 0.05);
     }
     .logo-area { text-align: center; margin-bottom: 25px; }
     .logo-text { color: #E67E22; font-size: 22px; font-weight: bold; text-decoration: none; }
     h2 { font-size: 18px; color: #444; text-align: center; margin-bottom: 30px;}
     
     .form-group { margin-bottom: 18px; }
     .label-row { display: block; font-size: 14px; font-weight: 600; color: #555; margin-bottom: 8px; }
     .input-wrapper { display: flex; gap: 8px;}
     
     input {
          width: 100%; padding: 12px 15px;
          border: 1px solid #E0E0E0; border-radius: 10px;
          font-size: 14px; outline: none; box-sizing: border-box;
     }
     input:focus { border-color: #E67E22; }
     
     .btn-check { 
          white-space: nowrap; padding: 0 15px;
          background-color: #f4f4f4; border: 1px solid #ddd;
          border-radius: 10px; font-size: 12px; cursor: pointer;
     }
     .btn-submit {
          width: 100%; padding: 15px;
          background-color: #E67E22; color: white;
          border: none; border-radius: 10px;
          font-size: 16px; font-weight: bold; cursor: pointer;
          margin-top: 20px;
     }
     .btn-submit:disabled { background-color: #ccc; cursor: not-allowed; }
     
     .msg{ font-size: 12px; margin-top: 5px; display: block; }
     .error { color : #e74c3c; }
     .success { color : #2ecc71 }     
</style>
</head>
<body>

<div class="signup-card">
   <div class="logo-area">
       <a href="${pageContext.request.contextPath}/main.do" class="logo-text"> 너와 나의 연결고리</a>
   </div>
   <h2>새로운 가족이 되어주세요</h2>
     <form id="signupForm" action="${pageContext.request.contextPath}/member/signup.do" method="post">
    
    <div class="form-group">
        <label class="label-row">이름</label>
        <input type="text" id="memberName" name="memberName" placeholder="이름을 입력해주세요" required>
    </div>

    <div class="form-group">
        <label class="label-row">아이디</label>
        <div class="input-wrapper">
            <input type="text" id="memberId" name="memberId" placeholder="아이디를 입력해주세요" required>
            <button type="button" id="idCheckBtn" class="btn-check">중복확인</button>
        </div>
        <span id="idMsg" class="msg"></span>
        <input type="hidden" id="idCheck" value="N">
    </div>

    <div class="form-group">
    <label class="label-row">닉네임</label>
    <div class="input-wrapper">
        <input type="text" id="nickname" name="nickname" placeholder="사용하실 별명을 입력해주세요" required>
        <button type="button" id="nickCheckBtn" class="btn-check">중복확인</button>
    </div>
    <span id="nickMsg" class="msg"></span>
    <input type="hidden" id="nickCheck" value="N">
</div>

    <div class="form-group">
        <label class="label-row">비밀번호</label>
        <input type="password" id="memberPw" name="memberPw" placeholder="8자 이상 입력해주세요" required>
    </div>

    <div class="form-group">
        <label class="label-row">비밀번호 확인</label>
        <input type="password" id="memberPwConfirm" name="memberPwConfirm" placeholder="비밀번호를 다시 입력해주세요" required>
        <span id="pwMsg" class="msg"></span>
    </div>

    <div class="form-group">
        <label class="label-row">이메일</label>
        <div class="input-wrapper">
            <input type="email" id="email" name="email" placeholder="example@mail.com" required>
            <button type="button" id="emailCheckBtn" class="btn-check">중복확인</button>
        </div>
        <span id="emailMsg" class="msg"></span>
        <input type="hidden" id="emailCheck" value="N">
    </div>

    <div class="form-group">
        <label class="label-row">전화번호</label>
        <input type="tel" id="phone" name="phone" placeholder="010-0000-0000" maxlength="13" required>
    </div>

    <button type="submit" id="submitBtn" class="btn-submit">가입 완료</button>
</form>
      </div>
      
  <script>
    window.contextPath = "${pageContext.request.contextPath}";
</script>
<script src="${pageContext.request.contextPath}/js/signup.js?v=3"></script>
</body>
</html>