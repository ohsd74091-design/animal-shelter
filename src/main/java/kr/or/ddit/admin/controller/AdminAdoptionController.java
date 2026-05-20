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

@WebServlet("/admin/adoption/list.do")
public class AdminAdoptionController extends HttpServlet {

    // 한 페이지에 보여줄 카드 수
    private static final int COUNT_PER_PAGE = 8;
    // 페이지 버튼 수
    private static final int PAGE_COUNT = 5;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // 관리자 권한 체크
        MemberVO loginUser = (MemberVO) req.getSession().getAttribute("loginUser");
        if (loginUser == null || !"ADMIN".equals(loginUser.getRole())) {
            resp.sendRedirect(req.getContextPath() + "/main.do");
            return;
        }

        IadminService service = AdminServiceImpl.getinsetance();

        // 현재 페이지 (기본값 1)
        int currentPage = 1;
        String pageParam = req.getParameter("page");
        if (pageParam != null && !pageParam.isEmpty()) {
            currentPage = Integer.parseInt(pageParam);
        }

        // 상태 필터 파라미터 (전체/심사중/승인/반려)
        String statusFilter = req.getParameter("statusFilter");

        // 통계 카드용 건수 (필터 관계없이 전체 건수)
        int totalCount    = service.selectAdoptionTotalCount();
        int pendingCount  = service.selectAdoptionCountByStatus("심사중");
        int approvedCount = service.selectAdoptionCountByStatus("승인");
        int rejectedCount = service.selectAdoptionCountByStatus("반려");

        // 필터 적용된 건수로 페이징 계산
        int filteredCount = (statusFilter != null && !statusFilter.isEmpty())
                ? service.selectAdoptionCountByStatus(statusFilter)
                : totalCount;

        // PageVO 생성
        PageVO pageVO = buildPageVO(currentPage, filteredCount, COUNT_PER_PAGE, PAGE_COUNT);

        // 페이징 + 필터 적용된 목록 조회
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("startRow",     pageVO.getStartRow());
        paramMap.put("endRow",       pageVO.getEndRow());
        paramMap.put("statusFilter", statusFilter);

        List<AdoptionVO> adoptionList = service.selectAdoptionListPaged(paramMap);

        req.setAttribute("adoptionList",   adoptionList);
        req.setAttribute("totalCount",     totalCount);
        req.setAttribute("pendingCount",   pendingCount);
        req.setAttribute("approvedCount",  approvedCount);
        req.setAttribute("rejectedCount",  rejectedCount);
        req.setAttribute("pageVO",         pageVO);
        req.setAttribute("statusFilter",   statusFilter);

        req.getRequestDispatcher("/view/admin/AdminAdoptionList.jsp")
           .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doGet(req, resp);
    }

    /** PageVO 생성 헬퍼 */
    private PageVO buildPageVO(int currentPage, int totalCount,
                               int countPerPage, int pageCount) {
        PageVO pageVO = new PageVO();
        pageVO.setCurrentPage(currentPage);
        pageVO.setCountPerPage(countPerPage);
        pageVO.setPageCount(pageCount);
        pageVO.setTotalCount(totalCount);

        int totalPage = (int) Math.ceil((double) totalCount / countPerPage);
        if (totalPage < 1) totalPage = 1;
        pageVO.setTotalPage(totalPage);

        int startRow  = (currentPage - 1) * countPerPage + 1;
        int endRow    = startRow + countPerPage - 1;
        pageVO.setStartRow(startRow);
        pageVO.setEndRow(endRow);

        int startPage = ((currentPage - 1) / pageCount) * pageCount + 1;
        int endPage   = startPage + pageCount - 1;
        if (endPage > totalPage) endPage = totalPage;
        pageVO.setStartPage(startPage);
        pageVO.setEndPage(endPage);

        return pageVO;
    }
}