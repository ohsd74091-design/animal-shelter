package kr.or.ddit.report.controller;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.or.ddit.member.vo.MemberVO;
import kr.or.ddit.report.service.IReportService;
import kr.or.ddit.report.service.ReportServiceImpl;

@WebServlet("/admin/report/member.do")
public class AdminMemberReportProcessController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private IReportService service = ReportServiceImpl.getservice();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json; charset=UTF-8");
        PrintWriter out = resp.getWriter();

        try {
            int reportId = Integer.parseInt(req.getParameter("reportId"));
            String action = req.getParameter("action");
            String replyEmail = req.getParameter("replyEmail");
            String processContent = req.getParameter("processContent");

            System.out.println("reportId = " + reportId);
            System.out.println("action = " + action);
            System.out.println("replyEmail = " + replyEmail);
            System.out.println("processContent = " + processContent);
            
            
            
            boolean result = service.updateMemberReportStatus(reportId, action, replyEmail, processContent);

            if (result) {
                out.print("{\"success\":true}");
            } else {
                out.print("{\"success\":false,\"message\":\"처리 또는 메일 발송에 실패했습니다.\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"success\":false,\"message\":\"서버 오류가 발생했습니다.\"}");
        }
        out.flush();
    }
}