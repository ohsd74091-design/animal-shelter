<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="kr.or.ddit.member.vo.MemberVO" %>
<% MemberVO loginUser = (MemberVO) session.getAttribute("loginUser"); %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>너와 나의 연결고리</title>
<style>
    :root {
       --bg-color: #FDFCFB;
       --point-orange: #E67E22;
       --text-dark: #333333;
       --input-border: #E0E0E0;
       --card-white: #FFFFFF;
    }
    body {
       margin: 0;
       padding: 0;
       font-family: 'Noto Sans KR', sans-serif;
       background-color: var(--bg-color);
       display: flex;
       justify-content: center;
       align-items: center;
       height: 100vh;
    }
    .login-container {
       width: 100%;
       max-width: 400px;
       padding: 40px;
       background-color: var(--card-white);
       border-radius: 24px;
       box-shadow: 0 10px 25px rgba(0,0,0,0.05);
       text-align: center;
    }
    .logo{
       font-size: 24px;
       font-weight: bold;
       color: var(--point-orange);
       margin-bottom: 30px;
       display: flex;
       align-items: center;
       justify-content: center;
       gap: 8px;
       text-decoration: none;
    }
    h2 {
      font-size: 20px;
      color: var(--text-dark);
      margin-bottom: 25px;
    }
    .input-group {
       margin-bottom: 15px;
       text-align: left;
    }
    input {
        width: 100%;
        padding: 14px 15px;
        margin-top: 5px;
        border: 1px solid var(--input-border);
        border-radius: 12px;
        box-sizing: border-box;
        outline: none;
        transition: border-color 0.2s;
    }
    input:focus {
	      border-color: var(--point-orange);
	}
	.login-btn, .logout-btn {
	      width: 100%;
	      padding: 15px;
	      background-color: var(--point-orange);
	      color: white;
	      border: none;
	      border-radius: 30px;
	      font-size: 16px;
	      font-weight: bold;
	      cursor: pointer;
	      margin-top: 10px;
	      transition: background 0.3s;
	      display: inline-block;
	      text-decoration: none;
	      box-sizing: border-box;
	}
	.login-btn:hover { background-color: #D35400; }
	.logout-btn { background-color: #777; margin-top: 20px; }
	.logout-btn:hover { background-color: #555;}
	
	.extra-links {
	        margin-top: 25px;
	        font-size: 14px;
	        color: #777;
	}
	.extra-links a {
	       color: #777;
	       text-decoration: none;
	       margin: 0 10px;
	}
	.extra-links a:hover{ text-decoration: underline; }
	.welcome-box {
	       padding: 20px;
	       background: #fff9f4;
	       border-radius: 15px;
	       border: 1px dashed var(--point-orange);
	}
}
</style>
</head>
   <body>
   
      <div class="login-container">
          <a href="<%= request.getContextPath() %>/main.do" class="logo">너와 나의 연결고리</a>
          <% if (loginUser == null) { %>
               <h2>반갑습니다!</h2>
               
               <form action="<%= request.getContextPath() %>/login.do" method="post">
               		<input type="hidden" name="targetPath" value="${targetPath}">
               
                    <div class="input-group">
                         <input type="text" name="id" placeholder="아이디를 입력하세요" required>
                    </div>
                    
                    <div class="input-group">
                         <input type="password" name="pw" placeholder="비밀번호를 입력하세요" required>
                    </div>
                    
                    <button type="submit" class="login-btn">로그인</button>
               </form>
               
               <div class="extra-links">
                     아직 계정이 없으신가요?
                     <a href="./membership.jsp" style="color:var(--point-orange); font-weight:bold;">회원가입</a>           
               </div>
               
               <div class="extra-links" style="margin-top: 10px; font-size: 12px;">
	               <a href="<%=request.getContextPath()%>/findId.do">아이디 찾기</a>| 
	               <a href="<%=request.getContextPath()%>/findPw.do">비밀번호 찾기</a>
      		   </div>
      
      <% } else { %>
           <div class="welcome-box">
               <h2>환영합니다!</h2>
               <p><strong><%= loginUser.getNickname() %></strong>님,<br>오늘도 행복한 하루 되세요!</p>
               <a href="<%= request.getContextPath() %>/logout.do" class="logout-btn">로그아웃</a>   
        </div>
        <% } %>
        </div>
   </body>
</html>