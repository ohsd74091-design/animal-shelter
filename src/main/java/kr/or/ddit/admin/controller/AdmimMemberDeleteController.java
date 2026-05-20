package kr.or.ddit.admin.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.or.ddit.admin.service.AdminServiceImpl;
import kr.or.ddit.admin.service.IadminService;

@WebServlet("/admin/member/delete.do")
public class AdmimMemberDeleteController extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String memberId =req.getParameter("memberId");
		
		IadminService service =AdminServiceImpl.getinsetance();
		service.deleteMember(memberId);
		
		resp.sendRedirect(req.getContextPath() + "/admin/member/list.do");
		
	}
}
