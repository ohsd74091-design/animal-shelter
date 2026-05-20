package kr.or.ddit.admin.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.or.ddit.donation.service.DonationServiceImpl;
import kr.or.ddit.donation.service.IDonationService;
import kr.or.ddit.member.vo.MemberVO;
/**
 * 관리자 후원 내역 삭제 (POST → JSON 응답)
 * URL: /admin/donation/delete.do
 *
 * 요청 파라미터:
 *   donationId  int  삭제할 후원 ID
 *
 * 응답 JSON:
 *   {"success": true}
 *   {"success": false, "message": "..."}
 */
@WebServlet("/admin/donation/delete.do")
public class AdminDonationDelete extends HttpServlet{
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		req.setCharacterEncoding("UTF-8");
		resp.setContentType("application/json; charset=UTF-8");
		
		HttpSession session = req.getSession();
		MemberVO loginUser = (MemberVO) session.getAttribute("loginUser");
		
		if(loginUser == null || !"ADMIN".equals(loginUser.getRole())) {
			resp.getWriter().print("{\"success\":false,\"message\":\"권한이 없습니다.\"}");
			return;
		}
		
		String idStr = req.getParameter("donationId");
		if(idStr == null || idStr.trim().isEmpty()) {
			resp.getWriter().print("{\"success\":false,\"message\":\"삭제할 후원 ID가 없습니다.\"}");
            return;
		}
		
		 int donationId;
        try {
            donationId = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            resp.getWriter().print("{\"success\":false,\"message\":\"올바르지 않은 ID입니다.\"}");
            return;
        }
        
        IDonationService donationService = DonationServiceImpl.getInstance();
        int result = donationService.deleteDonation(donationId);
        
        //JSON 응답 ────────────────────────────────
        if (result > 0) {
            resp.getWriter().print("{\"success\":true}");
        } else {
            resp.getWriter().print("{\"success\":false,\"message\":\"삭제 중 오류가 발생했습니다.\"}");
        }
	}

}
