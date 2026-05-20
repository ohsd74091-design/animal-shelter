<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>아이디 찾기</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/login.css">
</head>
<body>
    <div class="login-container">
        <a href="<%=request.getContextPath()%>/main.do" class="logo">너와 나의 연결고리</a>

        <h2>아이디 찾기</h2>

        <form action="<%=request.getContextPath()%>/findId.do" method="post">
            <div class="input-group">
                <input type="text" name="mem_name" placeholder="이름을 입력하세요" required>
            </div>

            <div class="input-group">
                <input type="email" name="mem_mail" placeholder="이메일을 입력하세요" required>
            </div>

            <button type="submit" class="login-btn">아이디 찾기</button>
        </form>

        <div class="extra-links">
            <a href="<%=request.getContextPath()%>/login.do">로그인</a>
            <a href="<%=request.getContextPath()%>/findPw.do">비밀번호 찾기</a>
        </div>
    </div>
</body>
</html>