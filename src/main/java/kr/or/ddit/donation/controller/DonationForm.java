package kr.or.ddit.donation.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.or.ddit.member.vo.MemberVO;

@WebServlet("/donation/form.do")
public class DonationForm extends HttpServlet{
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		
		// 로그인 체크(회원만 후원하게??)
		HttpSession session = req.getSession();
		MemberVO loginUser = (MemberVO) session.getAttribute("loginUser");
		
		if(loginUser == null) {
			resp.sendRedirect(req.getContextPath() + "/login.do");
			return;
		}
		req.getRequestDispatcher("/view/donation.jsp").forward(req, resp);
	}

}
