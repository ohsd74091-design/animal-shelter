package kr.or.ddit.member.controller;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.or.ddit.member.service.IMemberService;
import kr.or.ddit.member.service.MemberServiceImpl;

@WebServlet("/member/nickCheck.do")
public class NickCheckController extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		 req.setCharacterEncoding("UTF-8");
	        resp.setContentType("application/json; charset=UTF-8");

	        String nickname = req.getParameter("nickname");

	        IMemberService service = MemberServiceImpl.getinstance();
	        int cnt = service.checkNickname(nickname);

	        PrintWriter out = resp.getWriter();

	        if (cnt > 0) {
	            out.print("{\"flag\":\"fail\"}");
	        } else {
	            out.print("{\"flag\":\"ok\"}");
	        }

	        out.flush();
	}
}

