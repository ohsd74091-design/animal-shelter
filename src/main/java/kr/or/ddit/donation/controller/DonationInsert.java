package kr.or.ddit.donation.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.or.ddit.donation.service.DonationServiceImpl;
import kr.or.ddit.donation.service.IDonationService;
import kr.or.ddit.donation.vo.AESUtil;
import kr.or.ddit.donation.vo.DonationVO;
import kr.or.ddit.member.vo.MemberVO;

@WebServlet("/donation/insert.do")
public class DonationInsert extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json; charset=UTF-8");

        // ── 1. 세션 로그인 확인 ──
        HttpSession session = req.getSession();
        MemberVO loginUser = (MemberVO) session.getAttribute("loginUser");
        if (loginUser == null) {
            resp.getWriter().print("{\"success\":false,\"message\":\"로그인이 필요합니다.\"}");
            return;
        }

        // ── 2. 파라미터 수집 ──
        String amountStr  = req.getParameter("donationAmount");
        String type       = req.getParameter("donationType");
        String method     = req.getParameter("donationMethod");
        String name       = req.getParameter("donatorName");
        String tel        = req.getParameter("donatorTel");
        String email      = req.getParameter("donatorEmail");
        String receiptYn  = req.getParameter("receiptYn");   // Y or null
        String regNo1     = req.getParameter("regNo1");
        String regNo2     = req.getParameter("regNo2");

        // ── 3. 유효성 검사 ──
        if (amountStr == null || name == null || name.trim().isEmpty()) {
            resp.getWriter().print("{\"success\":false,\"message\":\"필수 항목을 입력해주세요.\"}");
            return;
        }

        long amount;
        try {
            amount = Long.parseLong(amountStr);
            if (amount < 1000) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            resp.getWriter().print("{\"success\":false,\"message\":\"금액은 1,000원 이상이어야 합니다.\"}");
            return;
        }

        // ── 4. VO 구성 ──
        DonationVO dvo = new DonationVO();
        dvo.setMemberId(loginUser.getMemberId());
        dvo.setDonatorName(name.trim());
        dvo.setDonatorTel(tel);
        dvo.setDonatorEmail(email);
        dvo.setDonationAmount(amount);
        dvo.setDonationType(type);
        dvo.setDonationMethod(method);
        dvo.setReceiptYn("Y".equals(receiptYn) ? "Y" : "N");
        // 영수증 신청 시 주민번호 암호화 후 저장
        if ("Y".equals(receiptYn)) {
            try {
                dvo.setRegNo1(AESUtil.encrypt(regNo1));
                dvo.setRegNo2(AESUtil.encrypt(regNo2));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // ── 5. 서비스 호출 ──
        IDonationService service = DonationServiceImpl.getInstance();
        int result = service.insertDonation(dvo);
        
        

        // ── 6. JSON 응답 ──
        if (result > 0) {
        	
        	//후원성공시 member테이블 is_donor =y로 업데이트 
        	service.updateMemberDonorYn(loginUser.getMemberId());
        	
        	//세션 login 갱신 -> 갱신안하면 로그아웃하기전까지 isdoner n유지 
        	loginUser.setIsDonor("Y");
        	session.setAttribute("loginUser", loginUser);
        	
            resp.getWriter().print("{\"success\":true,\"donationId\":" + dvo.getDonationId() + "}");
        } else {
            resp.getWriter().print("{\"success\":false,\"message\":\"후원 처리 중 오류가 발생했습니다.\"}");
        }
    }
}
