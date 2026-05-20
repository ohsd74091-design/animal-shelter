package kr.or.ddit.admin.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.or.ddit.member.vo.MemberVO;
import kr.or.ddit.support.service.AdminSupportServiceImpl;
import kr.or.ddit.support.service.IAdminSupportService;
import kr.or.ddit.support.vo.SupportReplyVO;

@WebServlet("/admin/support/reply.do")
public class AdminSupportReply extends HttpServlet{

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		req.setCharacterEncoding("UTF-8");
		// JSP에서 보낸 파라미터 가져오기
		int supportId = Integer.parseInt(req.getParameter("supportId"));
		String content = req.getParameter("replyContent");
		
		// 세션에서 로그인한 관리자의 ID를 가져와야
		HttpSession session = req.getSession();
		MemberVO admin = (MemberVO) session.getAttribute("loginUser");
		String adminId = admin.getMemberId();
		
		// 데이터를 바구니(VO)에 담기
		SupportReplyVO replyVO = new SupportReplyVO();
		replyVO.setSupportId(supportId);
		replyVO.setReplyContent(content);
		replyVO.setMemberId(adminId);
		
		// 서비스 호출하여 DB 저장
		IAdminSupportService adminService = AdminSupportServiceImpl.getInstance();
		int result = adminService.registerReply(replyVO);
		
		resp.setContentType("application/json; charset=utf-8");
		if(result > 0) {
		    resp.getWriter().print("{\"status\":\"success\"}");
		} else {
		    resp.getWriter().print("{\"status\":\"fail\"}");
		}
	}
	
}
