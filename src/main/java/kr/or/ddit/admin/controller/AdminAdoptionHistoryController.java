package kr.or.ddit.admin.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.or.ddit.admin.service.AdminServiceImpl;
import kr.or.ddit.admin.service.IadminService;
import kr.or.ddit.adoption.vo.AdoptionVO;
import kr.or.ddit.common.vo.PageVO;
import kr.or.ddit.member.vo.MemberVO;

@WebServlet("/admin/adoption/history.do")
public class AdminAdoptionHistoryController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// 한 페이지당 행 수
	private static final int COUNT_PER_PAGE = 10;
	// 페이지 블록 수
	private static final int PAGE_COUNT = 5;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		// 1. 관리자 권한 체크
		MemberVO loginUser = (MemberVO) req.getSession().getAttribute("loginUser");
		if (loginUser == null || !"ADMIN".equals(loginUser.getRole())) {
			resp.sendRedirect(req.getContextPath() + "/main.do");
			return;
		}

		IadminService service = AdminServiceImpl.getinsetance();

		// 2. 현재 페이지
		int currentPage = 1;
		String pageParam = req.getParameter("page");
		if (pageParam != null && !pageParam.trim().isEmpty()) {
			try {
				currentPage = Integer.parseInt(pageParam);
			} catch (NumberFormatException e) {
				currentPage = 1;
			}
		}

		// 3. 상태 필터
		String statusFilter = req.getParameter("statusFilter");
		if (statusFilter != null) {
			statusFilter = statusFilter.trim();
		}

		// 4. 통계 카드용 건수
		int totalCount = service.selectAdoptionHistoryCount(null);
		int pendingCount = service.selectAdoptionHistoryCount("심사중");
		int approvedCount = service.selectAdoptionHistoryCount("승인");
		int rejectedCount = service.selectAdoptionHistoryCount("반려");

		// 5. 현재 필터 기준 건수
		int filteredCount = service.selectAdoptionHistoryCount(statusFilter);

		// 6. 페이지 계산
		PageVO pageVO = buildPageVO(currentPage, filteredCount, COUNT_PER_PAGE, PAGE_COUNT);

		// 7. 목록 조회용 파라미터
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("startRow", pageVO.getStartRow());
		paramMap.put("endRow", pageVO.getEndRow());
		paramMap.put("statusFilter", statusFilter);

		List<AdoptionVO> adoptionHistoryList = service.selectAdoptionHistoryListPaged(paramMap);

		// 8. JSP 전달
		req.setAttribute("adoptionHistoryList", adoptionHistoryList);
		req.setAttribute("totalCount", totalCount);
		req.setAttribute("pendingCount", pendingCount);
		req.setAttribute("approvedCount", approvedCount);
		req.setAttribute("rejectedCount", rejectedCount);
		req.setAttribute("filteredCount", filteredCount);
		req.setAttribute("pageVO", pageVO);
		req.setAttribute("statusFilter", statusFilter);

		req.getRequestDispatcher("/view/admin/AdminAdoptionHistory.jsp")
		   .forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}

	/**
	 * PageVO 계산 헬퍼
	 */
	private PageVO buildPageVO(int currentPage, int totalCount, int countPerPage, int pageCount) {
		PageVO pageVO = new PageVO();

		pageVO.setCurrentPage(currentPage);
		pageVO.setCountPerPage(countPerPage);
		pageVO.setPageCount(pageCount);
		pageVO.setTotalCount(totalCount);

		int totalPage = (int) Math.ceil((double) totalCount / countPerPage);
		if (totalPage < 1) totalPage = 1;
		pageVO.setTotalPage(totalPage);

		int startRow = (currentPage - 1) * countPerPage + 1;
		int endRow = startRow + countPerPage - 1;
		pageVO.setStartRow(startRow);
		pageVO.setEndRow(endRow);

		int startPage = ((currentPage - 1) / pageCount) * pageCount + 1;
		int endPage = startPage + pageCount - 1;
		if (endPage > totalPage) endPage = totalPage;

		pageVO.setStartPage(startPage);
		pageVO.setEndPage(endPage);

		return pageVO;
	}
}