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
import kr.or.ddit.mypage.vo.ActivityVO;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MypageActivityAllController.java
 * 최근 활동 내역 전체보기 컨트롤러
 *
 *  GET /mypage/activityAll.do → 활동 내역 목록 & JSP 포워드
 */
@WebServlet("/mypage/activityAll.do")
public class MypageActivityAllController extends HttpServlet
{

	private static final long serialVersionUID = 1L;

	private static final int PAGE_SIZE = 10;
	private static final int BLOCK_SIZE = 5;

	private final IMypageService mypageService = MypageServiceImpl.getInstance();

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

		// 필터 타입 (빈 문자열 = 전체)
		String currentType = request.getParameter("type");
		if (currentType == null)
			currentType = "";

		// 현재 페이지
		int currentPage = 1;
		String pageParam = request.getParameter("page");
		if (pageParam != null && pageParam.matches("\\d+"))
		{
			currentPage = Math.max(1, Integer.parseInt(pageParam));
		}

		int start = (currentPage - 1) * PAGE_SIZE + 1;
		int end = currentPage * PAGE_SIZE;

		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("memberId", memberId);
		paramMap.put("start", start);
		paramMap.put("end", end);

		// 데이터 조회 - 타입별 분기
		int totalCount;
		List<ActivityVO> list;

		switch (currentType)
		{
		case "favorite":
			totalCount = mypageService.getActivityFavoriteCount(memberId);
			list = mypageService.getActivityFavoriteList(paramMap);
			break;
		case "adoption":
			totalCount = mypageService.getActivityAdoptionCount(memberId);
			list = mypageService.getActivityAdoptionList(paramMap);
			break;
		case "donation":
			totalCount = mypageService.getActivityDonationCount(memberId);
			list = mypageService.getActivityDonationList(paramMap);
			break;
		case "volunteer":
			totalCount = mypageService.getActivityVolunteerCount(memberId);
			list = mypageService.getActivityVolunteerList(paramMap);
			break;
		default:
			totalCount = mypageService.getActivityAllCount(memberId);
			list = mypageService.getActivityAllList(paramMap);
			break;
		}

		// 페이지네이션
		int totalPages = Math.max(1, (int) Math.ceil((double) totalCount / PAGE_SIZE));
		currentPage = Math.min(currentPage, totalPages);
		int startPage = ((currentPage - 1) / BLOCK_SIZE) * BLOCK_SIZE + 1;
		int endPage = Math.min(startPage + BLOCK_SIZE - 1, totalPages);

		request.setAttribute("activityList", list);
		request.setAttribute("totalCount", totalCount);
		request.setAttribute("currentPage", currentPage);
		request.setAttribute("totalPages", totalPages);
		request.setAttribute("startPage", startPage);
		request.setAttribute("endPage", endPage);
		request.setAttribute("currentType", currentType);

		request.getRequestDispatcher("/WEB-INF/view/mypage/mypageActivityAll.jsp").forward(request, response);
	}

    @Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doGet(request, response);
	}
}
