package kr.or.ddit.report.controller;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.or.ddit.member.vo.MemberVO;
import kr.or.ddit.report.service.IReportService;
import kr.or.ddit.report.service.ReportServiceImpl;
import kr.or.ddit.report.vo.MemberReportVO;

@WebServlet("/report/member.do")
public class MemberReportInsertController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private IReportService service = ReportServiceImpl.getservice();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession();
        MemberVO loginUser = (MemberVO) session.getAttribute("loginUser");

        if (loginUser == null) {
            out.print("{\"success\":false,\"message\":\"회원 신고는 로그인 후 이용 가능합니다.\"}");
            out.flush();
            return;
        }

        String reporterId = loginUser.getMemberId();
        String targetId = request.getParameter("targetId");
        String reason = request.getParameter("reason");
        String content = request.getParameter("content");
        String replyEmail = request.getParameter("replyEmail");

        if (targetId == null || targetId.trim().isEmpty()) {
            out.print("{\"success\":false,\"message\":\"신고 대상 회원 아이디가 없습니다.\"}");
            out.flush();
            return;
        }

        if (reason == null || reason.trim().isEmpty()) {
            out.print("{\"success\":false,\"message\":\"신고 사유를 선택해주세요.\"}");
            out.flush();
            return;
        }

        if (replyEmail == null || replyEmail.trim().isEmpty()) {
            out.print("{\"success\":false,\"message\":\"처리 결과를 받을 이메일을 입력해주세요.\"}");
            out.flush();
            return;
        }

        MemberReportVO vo = new MemberReportVO();
        vo.setReporterId(reporterId);
        vo.setTargetId(targetId);
        vo.setReason(reason);
        vo.setContent(content);
        vo.setReplyEmail(replyEmail);

        int cnt = service.insertMemberRepor(vo);

        if (cnt > 0) {
            out.print("{\"success\":true}");
        } else {
            out.print("{\"success\":false,\"message\":\"회원 신고 접수에 실패했습니다.\"}");
        }

        out.flush();
    }
}