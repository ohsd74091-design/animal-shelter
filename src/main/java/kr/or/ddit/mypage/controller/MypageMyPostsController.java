package kr.or.ddit.mypage.controller;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.or.ddit.member.vo.MemberVO;
import kr.or.ddit.mypage.service.IMypageService;
import kr.or.ddit.mypage.service.MypageServiceImpl;
import kr.or.ddit.board.vo.BoardVO;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MypageMyPostsController.java
 * 내 작성글 페이지 컨트롤러
 *
 *  GET  /mypage/myPosts.do            → 목록 조회 & JSP 포워드
 *  POST /mypage/myPosts/delete.do     → 게시글 삭제 AJAX (JSON 응답)
 */
@WebServlet(urlPatterns = { "/mypage/myPosts.do", "/mypage/myPosts/delete.do" })
public class MypageMyPostsController extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	/** 한 페이지에 보여줄 게시글 수 */
	private static final int PAGE_SIZE = 10;

	/** 페이지네이션 블록 크기 */
	private static final int BLOCK_SIZE = 5;

	private final IMypageService mypageService = MypageServiceImpl.getInstance();
	private final Gson gson = new Gson();

    /* ====================================================
       GET - 내 작성글 목록 조회
    ==================================================== */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{

		// 로그인 체크
		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("loginUser") == null)
		{
			response.sendRedirect(request.getContextPath() + "/member/login.do");
			return;
		}

		MemberVO loginUser = (MemberVO) session.getAttribute("loginUser");
		String memberId = loginUser.getMemberId();

		// 파라미터
		String boardType = request.getParameter("boardType");
		if (boardType == null)
			boardType = "";

		String keyword = request.getParameter("keyword");
		if (keyword == null)
			keyword = "";

		// 현재 페이지
		int currentPage = 1;
		String pageParam = request.getParameter("page");
		if (pageParam != null && pageParam.matches("\\d+"))
		{
			currentPage = Math.max(1, Integer.parseInt(pageParam));
		}

		// 페이징 범위 계산
		int start = (currentPage - 1) * PAGE_SIZE + 1;
		int end = currentPage * PAGE_SIZE;

		// 서비스 파라미터
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("memberId", memberId);
		paramMap.put("boardType", boardType.isEmpty() ? null : boardType);
		paramMap.put("keyword", keyword.isEmpty() ? null : keyword);
		paramMap.put("start", start);
		paramMap.put("end", end);

		// 데이터 조회
		int totalCount = mypageService.getMyPostsCount(memberId, boardType, keyword);
		List<BoardVO> postList = mypageService.getMyPostsList(paramMap);

		// 페이지네이션 계산
		int totalPages = Math.max(1, (int) Math.ceil((double) totalCount / PAGE_SIZE));
		currentPage = Math.min(currentPage, totalPages);
		int startPage = ((currentPage - 1) / BLOCK_SIZE) * BLOCK_SIZE + 1;
		int endPage = Math.min(startPage + BLOCK_SIZE - 1, totalPages);

		// 표시 범위 (예: 1-10)
		int startRow = (currentPage - 1) * PAGE_SIZE + 1;
		int endRow = Math.min(currentPage * PAGE_SIZE, totalCount);

		// 뷰에 전달
		request.setAttribute("postList", postList);
		request.setAttribute("totalCount", totalCount);
		request.setAttribute("currentPage", currentPage);
		request.setAttribute("totalPages", totalPages);
		request.setAttribute("startPage", startPage);
		request.setAttribute("endPage", endPage);
		request.setAttribute("startRow", startRow);
		request.setAttribute("endRow", endRow);
		request.setAttribute("currentBoardType", boardType);
		request.setAttribute("keyword", keyword);

		request.getRequestDispatcher("/WEB-INF/view/mypage/mypageMyPosts.jsp").forward(request, response);
	}

    /* ====================================================
       POST - 삭제 AJAX / 공통 처리
    ==================================================== */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{

		String uri = request.getRequestURI();

		if (uri.endsWith("/delete.do"))
		{
			handleDeletePost(request, response);
			return;
		}

		doGet(request, response);
	}

    /* ====================================================
       게시글 삭제 AJAX 처리
    ==================================================== */
	private void handleDeletePost(HttpServletRequest request, HttpServletResponse response) throws IOException
	{

		response.setContentType("application/json;charset=UTF-8");

		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("loginUser") == null)
		{
			Map<String, String> fail = new HashMap<>();
			fail.put("result", "fail");
			fail.put("message", "로그인이 필요합니다.");
			response.getWriter().write(gson.toJson(fail));
			return;
		}

		MemberVO loginUser = (MemberVO) session.getAttribute("loginUser");
		String memberId = loginUser.getMemberId();
		String boardIdStr = request.getParameter("boardId");

		Map<String, Object> result = new HashMap<>();

		try
		{
			int boardId = Integer.parseInt(boardIdStr);

			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("memberId", memberId);
			paramMap.put("boardId", boardId);

			mypageService.deleteMyPost(paramMap);

			result.put("result", "success");
			result.put("message", "게시글이 삭제되었습니다.");

		}
		catch (NumberFormatException e)
		{
			result.put("result", "fail");
			result.put("message", "잘못된 게시글 ID입니다.");
		}
		catch (Exception e)
		{
			result.put("result", "fail");
			result.put("message", "처리 중 오류가 발생했습니다.");
		}

		response.getWriter().write(gson.toJson(result));
	}
}
