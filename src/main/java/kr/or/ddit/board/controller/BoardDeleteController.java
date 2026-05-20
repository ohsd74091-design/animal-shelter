package kr.or.ddit.board.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.or.ddit.board.service.BoardService;
import kr.or.ddit.board.service.BoardServiceImpl;

@WebServlet("/board/delete.do")
public class BoardDeleteController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private BoardService boardService = BoardServiceImpl.getInstance();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String boardIdStr = req.getParameter("boardId");

		if (boardIdStr == null || boardIdStr.trim().isEmpty()) {
			resp.sendRedirect(req.getContextPath() + "/board/list.do");
			return;
		}

		int boardId = Integer.parseInt(boardIdStr);

		// 게시글 삭제 실행
		boardService.removeBoard(boardId);

		// 삭제 후 목록으로 이동
		resp.sendRedirect(req.getContextPath() + "/board/list.do");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}