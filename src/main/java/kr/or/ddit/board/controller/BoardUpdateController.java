package kr.or.ddit.board.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.or.ddit.board.service.BoardService;
import kr.or.ddit.board.service.BoardServiceImpl;
import kr.or.ddit.board.vo.BoardVO;

@WebServlet("/board/update.do")
public class BoardUpdateController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private BoardService boardService = BoardServiceImpl.getInstance();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		int BoardId = Integer.parseInt(req.getParameter("boardId"));
		
		BoardVO boardVo = boardService.getBoard(BoardId);
		
		req.setAttribute("boardVo", boardVo);
		req.getRequestDispatcher("/view/boardUpdate.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		
		int boardId = Integer.parseInt(req.getParameter("boardId"));
		String boardType = req.getParameter("boardType");
		String title = req.getParameter("title");
		String content = req.getParameter("content");
		
		BoardVO boardVo = new BoardVO();
		boardVo.setBoardId(boardId);
		boardVo.setBoardType(boardType);
		boardVo.setTitle(title);
		boardVo.setContent(content);
		
		boardService.modifyBoard(boardVo);
		
		resp.sendRedirect(req.getContextPath() + "/board/detail.do?boardId=" + boardId);
	}
    
    
}
