package kr.or.ddit.member.controller;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.security.auth.message.callback.PrivateKeyCallback.Request;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.or.ddit.member.service.IMemberService;
import kr.or.ddit.member.service.MemberServiceImpl;

@WebServlet("/member/idCheck.do")
public class idCheck extends HttpServlet{
	private static final long serialVersionUID =1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		
		req.setCharacterEncoding("UTF-8");
		resp.setContentType("application/json; charset=UTF-8");
		
		String memberId = req.getParameter("memberId");
		
		IMemberService service = MemberServiceImpl.getinstance();
		
		int ex =service.checkMemberId(memberId);
		
		PrintWriter out =resp.getWriter();
		
		if(ex >= 1) {
			out.print("{\"flag\":\"fail\"}");
		}else {
			out.print("{\"flag\":\"ok\"}");
		}
		
		out.flush();
		
	}
	
}
