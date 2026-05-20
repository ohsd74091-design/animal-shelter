package kr.or.ddit.board.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.or.ddit.board.service.BoardService;
import kr.or.ddit.board.service.BoardServiceImpl;
import kr.or.ddit.board.vo.BoardVO;
import kr.or.ddit.common.vo.PageVO;

@WebServlet({ "/board/list.do", "/board/free.do", "/board/review.do", "/board/adoptReview.do",
		"/board/volunteerReview.do" })
public class BoardListController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private BoardService boardService = BoardServiceImpl.getInstance();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String uri = req.getRequestURI();
		// URL에 따라 boardType 자동 설정
		String boardType = req.getParameter("boardType");
		
		System.out.println("uri = " + uri);
		System.out.println("boardType = " + boardType);

		// 메인/헤더에서 커뮤니티 눌렀을 때 list.do로 들어오면 전체로 보이게
		if (uri.endsWith("/list.do")) {
		    if (boardType == null
		            || boardType.trim().isEmpty()
		            || "자유게시판".equals(boardType)
		            || "자유".equals(boardType)) {
		        boardType = "전체";
		    }
		} else if (uri.endsWith("/free.do")) {
		    boardType = "전체";
		} else if (uri.endsWith("/review.do")) {
		    boardType = "입양후기";
		} else if (uri.endsWith("/adoptReview.do")) {
		    boardType = "입양후기";
		} else if (uri.endsWith("/volunteerReview.do")) {
		    boardType = "자원봉사후기";
		}

		String keyword = req.getParameter("keyword");

		// 페이징 계산
		PageVO pageVO = new PageVO();

		int currentPage = req.getParameter("page") == null ? 1 : Integer.parseInt(req.getParameter("page"));

		pageVO.setCurrentPage(currentPage);
		pageVO.setCountPerPage(10);
		pageVO.setPageCount(5);

		// 필터 조건 Map 구성
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("boardType", boardType);
		paramMap.put("keyword", keyword);

		// 전체 건수 → 페이지 계산
		int totalCount = boardService.getBoardCount(paramMap);
		pageVO.setTotalCount(totalCount);

		int totalPage = (int) Math.ceil((double) totalCount / pageVO.getCountPerPage());
		if (totalPage < 1)
			totalPage = 1;
		pageVO.setTotalPage(totalPage);

		int startRow = (currentPage - 1) * pageVO.getCountPerPage() + 1;
		int endRow = startRow + pageVO.getCountPerPage() - 1;
		pageVO.setStartRow(startRow);
		pageVO.setEndRow(endRow);

		int startPage = ((currentPage - 1) / pageVO.getPageCount()) * pageVO.getPageCount() + 1;
		int endPage = startPage + pageVO.getPageCount() - 1;
		if (endPage > totalPage)
			endPage = totalPage;
		pageVO.setStartPage(startPage);
		pageVO.setEndPage(endPage);

		// 페이징 정보 전달
		paramMap.put("startRow", startRow);
		paramMap.put("endRow", endRow);

		// 목록 조회
		List<BoardVO> boardList = boardService.getBoardList(paramMap);

		// 조회순 인기글
		List<BoardVO> popularList = boardService.getPopularBoardList();

		// 추천순 인기글
		List<BoardVO> popularListByLike = boardService.getPopularBoardListByLike();

		// JSP로 전달
		req.setAttribute("boardList", boardList);
		req.setAttribute("boardType", boardType);
		req.setAttribute("keyword", keyword);
		req.setAttribute("pageVO", pageVO);
		req.setAttribute("popularList", popularList);
		req.setAttribute("popularListByLike", popularListByLike);

		req.getRequestDispatcher("/view/boardList.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}