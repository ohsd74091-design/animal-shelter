package kr.or.ddit.board.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.or.ddit.board.service.BoardService;
import kr.or.ddit.board.service.BoardServiceImpl;
import kr.or.ddit.board.vo.CommentReportVO;
import kr.or.ddit.member.vo.MemberVO;

@WebServlet("/board/commentReport.do")
public class CommentReportController extends HttpServlet{
	private BoardService service = BoardServiceImpl.getInstance();
	
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		MemberVO loginUser = (MemberVO) req.getSession().getAttribute("loginUser");

        int commentId = Integer.parseInt(req.getParameter("commentId"));
        String reason = req.getParameter("reason");

        CommentReportVO vo = new CommentReportVO();
        vo.setCommentId(commentId);
        vo.setMemberId(loginUser.getMemberId());
        vo.setReason(reason);

        service.registCommentReport(vo);

        resp.sendRedirect(req.getHeader("Referer"));
    }

}
