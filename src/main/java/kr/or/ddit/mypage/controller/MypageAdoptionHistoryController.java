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
import kr.or.ddit.mypage.vo.AdoptionHistoryVO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MypageAdoptionHistoryController.java
 * 입양 신청 내역 페이지 컨트롤러
 *
 *  GET  /mypage/adoptionHistory.do        → 목록 조회 & JSP 포워드
 *  POST /mypage/adoptionHistory/cancel.do → 신청 취소 AJAX (JSON 응답)
 */
@WebServlet(urlPatterns = { "/mypage/adoptionHistory.do", "/mypage/adoptionHistory/cancel.do" })
public class MypageAdoptionHistoryController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final int PAGE_SIZE = 5; // 한 페이지에 보여줄 항목 수
    private static final int BLOCK_SIZE = 5; // 페이지네이션 블록 크기

    /** 탭 목록 - label: 화면 표시, value: DB status 값
     *  DB 제약조건: STATUS IN ('심사중', '승인', '반려')
     */
    private static final List<Map<String, String>> TABS = new ArrayList<>();

    static {
        for (String[] tab : new String[][]{
            {"전체",   ""},
            {"심사중", "심사중"},
            {"승인",   "승인"},
            {"반려",   "반려"}
        }) {
            Map<String, String> m = new HashMap<>();
            m.put("label", tab[0]);
            m.put("value", tab[1]);
            TABS.add(m);
        }
    }
 
	private final IMypageService mypageService = MypageServiceImpl.getInstance();
	private final Gson gson = new Gson();
 
    /* ====================================================
       GET - 입양 신청 내역 목록 조회
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

		// 탭 필터 상태값 (기본값: 전체 = "")
		String currentStatus = request.getParameter("status");
		if (currentStatus == null)
			currentStatus = "";

		// 현재 페이지
		int currentPage = 1;
		String pageParam = request.getParameter("page");
		
		if (pageParam != null && pageParam.matches("\\d+"))
			currentPage = Math.max(1, Integer.parseInt(pageParam));

		// 페이징 범위 계산
		int start = (currentPage - 1) * PAGE_SIZE + 1;
		int end = currentPage * PAGE_SIZE;

		// 서비스 파라미터
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("memberId", memberId);
		paramMap.put("status", currentStatus.isEmpty() ? null : currentStatus);
		paramMap.put("start", start);
		paramMap.put("end", end);

		// 데이터 조회
		int totalCount = mypageService.getAdoptionHistoryCount(memberId, currentStatus);
		List<AdoptionHistoryVO> list = mypageService.getAdoptionHistoryList(paramMap);

		// 페이지네이션 계산
		int totalPages = (int) Math.ceil((double) totalCount / PAGE_SIZE);
		totalPages = Math.max(totalPages, 1);
		currentPage = Math.min(currentPage, totalPages);

		int startPage = ((currentPage - 1) / BLOCK_SIZE) * BLOCK_SIZE + 1;
		int endPage = Math.min(startPage + BLOCK_SIZE - 1, totalPages);

		// 뷰에 전달
		request.setAttribute("tabs", TABS);
		request.setAttribute("currentStatus", currentStatus);
		request.setAttribute("adoptionList", list);
		request.setAttribute("totalCount", totalCount);
		request.setAttribute("currentPage", currentPage);
		request.setAttribute("totalPages", totalPages);
		request.setAttribute("startPage", startPage);
		request.setAttribute("endPage", endPage);

		request.getRequestDispatcher("/WEB-INF/view/mypage/mypageAdoptionHistory.jsp").forward(request, response);
	}
 
    /* ====================================================
       POST - 신청 취소 AJAX / 공통 처리
    ==================================================== */
    @Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{

		String uri = request.getRequestURI();

		if (uri.endsWith("/cancel.do"))
		{
			handleCancelAdoption(request, response);
			return;
		}

		doGet(request, response);
	}
 
    /* ====================================================
       신청 취소 AJAX 처리
    ==================================================== */
	private void handleCancelAdoption(HttpServletRequest request, HttpServletResponse response) throws IOException
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
		String adoptionIdStr = request.getParameter("adoptionId");

		Map<String, Object> result = new HashMap<>();

		try
		{
			int adoptionId = Integer.parseInt(adoptionIdStr);

			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("memberId", memberId);
			paramMap.put("adoptionId", adoptionId);

			mypageService.cancelAdoption(paramMap);

			result.put("result", "success");
			result.put("message", "입양 신청이 취소되었습니다.");

		}
		catch (NumberFormatException e)
		{
			result.put("result", "fail");
			result.put("message", "잘못된 신청 ID입니다.");
		}
		catch (Exception e)
		{
			result.put("result", "fail");
			result.put("message", "처리 중 오류가 발생했습니다.");
		}

		response.getWriter().write(gson.toJson(result));
	}
}
