<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>비밀번호찾기</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/login.css">
</head>
<body>
 <div class="login-container">
        <a href="<%=request.getContextPath()%>/main.do" class="logo">너와 나의 연결고리</a>

        <h2>비밀번호 찾기</h2>


<form action="<%=request.getContextPath()%>/findPw.do" method="post">
    <div class="input-group">
        <input type="text" name="mem_id" placeholder="아이디를 입력하세요" required>
    </div>

    <div class="input-group">
        <input type="email" name="mem_mail" placeholder="이메일을 입력하세요" required>
    </div>

    <button type="submit" class="login-btn">임시 비밀번호 받기</button>
</form>

</body>
</html>