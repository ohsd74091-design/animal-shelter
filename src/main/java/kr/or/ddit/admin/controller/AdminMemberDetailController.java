package kr.or.ddit.admin.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.or.ddit.admin.service.AdminServiceImpl;
import kr.or.ddit.admin.service.IadminService;
import kr.or.ddit.member.vo.MemberVO;

@WebServlet("/admin/member/detail.do")
public class AdminMemberDetailController extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String memberId =req.getParameter("memberId");
		
		IadminService service =AdminServiceImpl.getinsetance();
		
		MemberVO member=service.selectMemberDetail(memberId);
		
		req.setAttribute("member", member);
		req.getRequestDispatcher("/view/admin/member-detail.jsp").forward(req, resp);
	}

}
