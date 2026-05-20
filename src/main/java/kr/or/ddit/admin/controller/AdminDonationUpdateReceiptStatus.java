package kr.or.ddit.admin.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.or.ddit.donation.service.DonationServiceImpl;
import kr.or.ddit.donation.service.IDonationService;
import kr.or.ddit.donation.vo.DonationVO;

@WebServlet("/admin/updateReceiptStatus.do")
public class AdminDonationUpdateReceiptStatus extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        String donationIdStr = req.getParameter("donationId");
        String receiptYn     = req.getParameter("receiptYn");

        Map<String, Object> resultMsg = new HashMap<>();

        if (donationIdStr == null || donationIdStr.isEmpty()) {
            resultMsg.put("result", "fail");
            resultMsg.put("msg", "donationId 누락");
            resp.setContentType("application/json; charset=utf-8");
            resp.getWriter().print(new Gson().toJson(resultMsg));
            return;
        }

        DonationVO dvo = new DonationVO();
        dvo.setDonationId(Integer.parseInt(donationIdStr));
        dvo.setReceiptYn(receiptYn);

        IDonationService service = DonationServiceImpl.getInstance();
        int cnt = service.updateReceiptStatus(dvo);

        if (cnt > 0) {
            resultMsg.put("result", "success");
            resultMsg.put("msg", "처리가 완료되었습니다.");
        } else {
            resultMsg.put("result", "fail");
            resultMsg.put("msg", "업데이트 실패");
        }

        resp.setContentType("application/json; charset=utf-8");
        resp.getWriter().print(new Gson().toJson(resultMsg));
    }
}
