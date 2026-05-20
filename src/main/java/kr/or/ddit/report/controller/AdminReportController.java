package kr.or.ddit.report.controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.or.ddit.member.vo.MemberVO;
import kr.or.ddit.report.service.IReportService;
import kr.or.ddit.report.service.ReportServiceImpl;
import kr.or.ddit.report.vo.AnimalReportVO;
import kr.or.ddit.report.vo.MemberReportVO;

@WebServlet("/admin/report/list.do")
public class AdminReportController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // 관리자 권한 체크
        MemberVO loginUser = (MemberVO) req.getSession().getAttribute("loginUser");
        if (loginUser == null || !"ADMIN".equals(loginUser.getRole())) {
            resp.sendRedirect(req.getContextPath() + "/main.do");
            return;
        }

        IReportService service = ReportServiceImpl.getservice();

        // 탭 파라미터 (기본값: animal)
        String tab = req.getParameter("tab");
        if (tab == null) tab = "animal";

        // 목록 조회
        List<AnimalReportVO> animalList = service.selectAnimalReportList();
        List<MemberReportVO> memberList = service.selectMemberReportList();

        // 통계 카드용 건수
        int animalTotal  = service.selectAnimalReportCount();
        int memberTotal  = service.selectMemberReportCount();
        int animalPending = service.selectAnimalPendingCount();
        int memberPending = service.selectMemberPendingCount();
        int pendingTotal = animalPending + memberPending;

        req.setAttribute("tab",           tab);
        req.setAttribute("animalList",    animalList);
        req.setAttribute("memberList",    memberList);
        req.setAttribute("animalTotal",   animalTotal);
        req.setAttribute("memberTotal",   memberTotal);
        req.setAttribute("animalPending", animalPending);
        req.setAttribute("memberPending", memberPending);
        req.setAttribute("pendingTotal",  pendingTotal);

        req.getRequestDispatcher("/view/admin/Adminreportlist.jsp")
           .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doGet(req, resp);
    }
}