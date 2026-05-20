package kr.or.ddit.admin.controller;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.or.ddit.admin.service.AdminServiceImpl;
import kr.or.ddit.admin.service.IadminService;
import kr.or.ddit.member.vo.MemberVO;

@WebServlet("/admin/board/dismissReport.do")
public class AdminBoardDismissController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json; charset=UTF-8");
        PrintWriter out = resp.getWriter();

        MemberVO loginUser = (MemberVO) req.getSession().getAttribute("loginUser");
        if (loginUser == null || !"ADMIN".equals(loginUser.getRole())) {
            out.print("{\"success\":false,\"message\":\"권한이 없습니다.\"}");
            return;
        }

        try {
            int boardId = Integer.parseInt(req.getParameter("boardId"));

            IadminService service = AdminServiceImpl.getinsetance();
            boolean result = service.dismissBoardReport(boardId);

            if (result) {
                out.print("{\"success\":true}");
            } else {
                out.print("{\"success\":false,\"message\":\"처리에 실패했습니다.\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"success\":false,\"message\":\"오류가 발생했습니다.\"}");
        }
    }
}