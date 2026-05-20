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
import kr.or.ddit.donation.vo.DonationVO;
import kr.or.ddit.member.vo.MemberVO;

@WebServlet("/admin/donation/update.do")
public class AdminDonationUpdate extends HttpServlet{
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		 req.setCharacterEncoding("UTF-8");
	     resp.setContentType("application/json; charset=UTF-8");
	     
	     HttpSession session = req.getSession();
         MemberVO loginUser  = (MemberVO) session.getAttribute("loginUser");

         if (loginUser == null || !"ADMIN".equals(loginUser.getRole())) {
            resp.getWriter().print("{\"success\":false,\"message\":\"권한이 없습니다.\"}");
            return;
         }
         
         //  파라미터 수집 
         String idStr = req.getParameter("donationId");
         String name = req.getParameter("donatorName");
         String amountStr = req.getParameter("donationAmount");
         String type = req.getParameter("donationType");
         String method = req.getParameter("donationMethod");
         
         // 유효성 검사 ──────────────────────
         if (idStr == null || name == null || name.trim().isEmpty() || amountStr == null) {
             resp.getWriter().print("{\"success\":false,\"message\":\"필수 항목을 입력해주세요.\"}");
             return;
         }
         
         int  donationId;
         long amount;
         try {
             donationId = Integer.parseInt(idStr);
             amount     = Long.parseLong(amountStr);
             if (amount < 1000) throw new NumberFormatException();
         } catch (NumberFormatException e) {
             resp.getWriter().print("{\"success\":false,\"message\":\"올바른 값을 입력해주세요.\"}");
             return;
         }
         
         //VO 구성 ─── 서비스 호출
         DonationVO dvo = new DonationVO();
         dvo.setDonationId(donationId);
         dvo.setDonatorName(name.trim());
         dvo.setDonationAmount(amount);
         dvo.setDonationType(type);
         dvo.setDonationMethod(method);
         
         IDonationService donationService = DonationServiceImpl.getInstance();
         int result = donationService.updateDonationByAdmin(dvo);
         
         //JSON 응답 ──
         if (result > 0) {
             resp.getWriter().print("{\"success\":true}");
         } else {
             resp.getWriter().print("{\"success\":false,\"message\":\"수정 중 오류가 발생했습니다.\"}");
         }
	}

}
