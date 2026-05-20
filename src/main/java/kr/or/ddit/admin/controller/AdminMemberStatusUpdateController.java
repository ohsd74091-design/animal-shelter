package kr.or.ddit.admin.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.or.ddit.admin.service.AdminServiceImpl;
import kr.or.ddit.admin.service.IadminService;

@WebServlet("/admin/member/statusUpdate.do")
public class AdminMemberStatusUpdateController extends HttpServlet {
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String memberId =req.getParameter("memberId");
		String status =req.getParameter("status");
		
		IadminService service =AdminServiceImpl.getinsetance();
		service.updateMemberStatus(memberId, status);
		
		resp.sendRedirect(req.getContextPath()+"/admin/member/detail.do?memberId=" + memberId);
		
	}

}
