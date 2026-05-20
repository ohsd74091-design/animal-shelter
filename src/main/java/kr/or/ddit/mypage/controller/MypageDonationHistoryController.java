package kr.or.ddit.mypage.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.or.ddit.member.vo.MemberVO;
import kr.or.ddit.mypage.service.IMypageService;
import kr.or.ddit.mypage.service.MypageServiceImpl;
import kr.or.ddit.donation.vo.DonationVO;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MypageDonationHistoryController.java
 * 후원 내역 페이지 컨트롤러
 *
 *  GET /mypage/donationHistory.do → 후원 내역 목록 & 통계 조회 & JSP 포워드
 */
@WebServlet("/mypage/donationHistory.do")
public class MypageDonationHistoryController extends HttpServlet
{

	private static final long serialVersionUID = 1L;

	/** 한 페이지에 보여줄 항목 수 */
	private static final int PAGE_SIZE = 5;

	/** 페이지네이션 블록 크기 */
	private static final int BLOCK_SIZE = 5;

	private final IMypageService mypageService = MypageServiceImpl.getInstance();

    /* ====================================================
       GET - 후원 내역 목록 + 통계 조회
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
		paramMap.put("start", start);
		paramMap.put("end", end);

		// 데이터 조회
		Map<String, Object> donationStats = mypageService.getDonationStats(memberId);
		List<DonationVO> donationList = mypageService.getDonationHistoryList(paramMap);

		// 페이지네이션 계산
		int totalCount = ((Number) donationStats.get("totalCount")).intValue();
		int totalPages = Math.max(1, (int) Math.ceil((double) totalCount / PAGE_SIZE));
		currentPage = Math.min(currentPage, totalPages);
		int startPage = ((currentPage - 1) / BLOCK_SIZE) * BLOCK_SIZE + 1;
		int endPage = Math.min(startPage + BLOCK_SIZE - 1, totalPages);

		// 표시 범위
		int startRow = (currentPage - 1) * PAGE_SIZE + 1;
		int endRow = Math.min(currentPage * PAGE_SIZE, totalCount);

		// 뷰에 전달
		request.setAttribute("donationStats", donationStats);
		request.setAttribute("donationList", donationList);
		request.setAttribute("currentPage", currentPage);
		request.setAttribute("totalPages", totalPages);
		request.setAttribute("startPage", startPage);
		request.setAttribute("endPage", endPage);
		request.setAttribute("startRow", startRow);
		request.setAttribute("endRow", endRow);

		request.getRequestDispatcher("/WEB-INF/view/mypage/mypageDonationHistory.jsp").forward(request, response);
	}

    @Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doGet(request, response);
	}
}
