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
import kr.or.ddit.board.vo.BoardVO;
import kr.or.ddit.common.vo.PageVO;
import kr.or.ddit.member.vo.MemberVO;

@WebServlet("/admin/board/list.do")
public class AdminBoardController extends HttpServlet {

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

        String tab       = req.getParameter("tab");
        String boardType = req.getParameter("boardType");
        String keyword   = req.getParameter("keyword");
        if (tab == null) tab = "all";

        // ── 통계 카드 ──────────────────────────────────
        int totalCount    = service.selectAdminBoardCount(new HashMap<>());
        int reportedCount = service.selectReportedBoardCount();
        int hiddenCount   = service.selectHiddenBoardCount();

        req.setAttribute("totalCount",    totalCount);
        req.setAttribute("reportedCount", reportedCount);
        req.setAttribute("hiddenCount",   hiddenCount);
        req.setAttribute("tab",           tab);
        req.setAttribute("boardType",     boardType);
        req.setAttribute("keyword",       keyword);

        // ── 전체 게시글 탭 ─────────────────────────────
        int currentPage = req.getParameter("page") == null ? 1
                : Integer.parseInt(req.getParameter("page"));

        PageVO pageVO = buildPageVO(currentPage, totalCount, 10, 5);

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("boardType", boardType);
        paramMap.put("keyword",   keyword);
        paramMap.put("startRow",  pageVO.getStartRow());
        paramMap.put("endRow",    pageVO.getEndRow());

        // boardType이 빈 문자열이면 null로 처리 (전체 조회)
        if (boardType != null && boardType.isEmpty()) paramMap.put("boardType", null);

        List<BoardVO> boardList = service.selectAdminBoardList(paramMap);

        req.setAttribute("boardList", boardList);
        req.setAttribute("pageVO",    pageVO);

        // ── 신고 게시글 탭 ─────────────────────────────
        int reportPage = "report".equals(tab) && req.getParameter("page") != null
                ? Integer.parseInt(req.getParameter("page")) : 1;

        PageVO reportPageVO = buildPageVO(reportPage, reportedCount, 10, 5);

        Map<String, Object> reportParamMap = new HashMap<>();
        reportParamMap.put("startRow", reportPageVO.getStartRow());
        reportParamMap.put("endRow",   reportPageVO.getEndRow());

        List<BoardVO> reportedList = service.selectReportedBoardList(reportParamMap);

        req.setAttribute("reportedList",  reportedList);
        req.setAttribute("reportPageVO",  reportPageVO);

        req.getRequestDispatcher("/view/admin/adminBoardList.jsp")
           .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doGet(req, resp);
    }

    /** 페이징 VO 빌더 */
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