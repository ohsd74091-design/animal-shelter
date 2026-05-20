package kr.or.ddit.board.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.or.ddit.board.service.BoardService;
import kr.or.ddit.board.service.BoardServiceImpl;
import kr.or.ddit.board.vo.BoardReportVO;
import kr.or.ddit.board.vo.BoardVO;
import kr.or.ddit.member.vo.MemberVO;

@WebServlet("/board/report.do")
public class BoardReportController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private BoardService boardService = BoardServiceImpl.getInstance();

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        MemberVO loginUser = (MemberVO) req.getSession().getAttribute("loginUser");
        if (loginUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login.do");
            return;
        }

        int boardId = Integer.parseInt(req.getParameter("boardId"));
        String memberId = loginUser.getMemberId();
        String reason = req.getParameter("reason");

        if (reason == null || reason.trim().isEmpty()) {
            reason = "기타";
        }

        // 본인 글 신고 방지
        BoardVO boardVo = boardService.getBoard(boardId);
        if (boardVo != null && memberId.equals(boardVo.getMemberId())) {
            req.getSession().setAttribute("reportMsg", "본인 게시글은 신고할 수 없습니다.");
            resp.sendRedirect(req.getContextPath() + "/board/detail.do?boardId=" + boardId);
            return;
        }

        BoardReportVO reportVo = new BoardReportVO();
        reportVo.setBoardId(boardId);
        reportVo.setMemberId(memberId);
        reportVo.setReason(reason);

        // 1=신고완료, -1=중복신고, 0=실패
        int result = boardService.registBoardReport(reportVo);

        String msg;
        if (result == 1) {
            msg = "신고가 접수되었습니다.";
        } else if (result == -1) {
            msg = "이미 신고한 게시글입니다.";
        } else {
            msg = "신고 처리 중 오류가 발생했습니다.";
        }

        req.getSession().setAttribute("reportMsg", msg);
        resp.sendRedirect(req.getContextPath() + "/board/detail.do?boardId=" + boardId);
    }

	    }
	
