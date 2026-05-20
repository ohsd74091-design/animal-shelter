package kr.or.ddit.admin.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.or.ddit.admin.service.AdminServiceImpl;
import kr.or.ddit.admin.service.IadminService;
import kr.or.ddit.board.vo.BoardReportVO;
import kr.or.ddit.member.vo.MemberVO;

@WebServlet("/admin/board/reportList.do")
public class AdminBoardReportListController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json; charset=UTF-8");
        PrintWriter out = resp.getWriter();

        MemberVO loginUser = (MemberVO) req.getSession().getAttribute("loginUser");
        if (loginUser == null || !"ADMIN".equals(loginUser.getRole())) {
            out.print("[]");
            return;
        }

        try {
            int boardId = Integer.parseInt(req.getParameter("boardId"));

            IadminService service = AdminServiceImpl.getinsetance();
            List<BoardReportVO> list = service.selectBoardReportList(boardId);

            // Gson으로 List → JSON 변환
            Gson gson = new Gson();
            out.print(gson.toJson(list != null ? list : new java.util.ArrayList<>()));

        } catch (Exception e) {
            e.printStackTrace();
            out.print("[]");
        }
    }
}