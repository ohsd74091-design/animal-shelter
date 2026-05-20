package kr.or.ddit.certificate.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import kr.or.ddit.certificate.service.CertificateServiceImpl;
import kr.or.ddit.certificate.service.ICertificateService;
import kr.or.ddit.certificate.vo.AdoptionCertificateVO;
import kr.or.ddit.member.vo.MemberVO;

@WebServlet("/certificate/adoption.do")
public class AdoptionCertificateController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private ICertificateService service = CertificateServiceImpl.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession();
        MemberVO loginUser = (MemberVO) session.getAttribute("loginUser");

        if (loginUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login.do");
            return;
        }

        String adoptionIdStr = req.getParameter("adoptionId");

        if (adoptionIdStr == null) {
            resp.sendRedirect(req.getContextPath() + "/mypage/main.do");
            return;
        }

        int adoptionId = Integer.parseInt(adoptionIdStr);

        AdoptionCertificateVO cert =
                service.getCertificate(adoptionId, loginUser.getMemberId());

        if (cert == null) {
            session.setAttribute("msg", "승인된 입양 건이 아니거나 접근 권한이 없습니다.");
            resp.sendRedirect(req.getContextPath() + "/mypage/main.do");
            return;
        }

        req.setAttribute("cert", cert);

        req.getRequestDispatcher("/view/certificate/adoptionCertificate.jsp")
           .forward(req, resp);
    }
}