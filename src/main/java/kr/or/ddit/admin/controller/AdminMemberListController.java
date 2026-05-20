package kr.or.ddit.admin.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.or.ddit.admin.service.AdminServiceImpl;
import kr.or.ddit.admin.service.IadminService;
import kr.or.ddit.common.vo.PageVO;
import kr.or.ddit.member.service.IMemberService;
import kr.or.ddit.member.service.MemberServiceImpl;
import kr.or.ddit.member.vo.MemberVO;

@WebServlet("/admin/member/list.do")
public class AdminMemberListController extends HttpServlet{
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		IadminService service = AdminServiceImpl.getinsetance();

		PageVO pageVO = new PageVO();

		int currentPage = req.getParameter("page") == null ? 1
				: Integer.parseInt(req.getParameter("page"));

		pageVO.setCurrentPage(currentPage);
		pageVO.setCountPerPage(10);
		pageVO.setPageCount(5);

		pageVO.setKeyword(req.getParameter("keyword"));
		pageVO.setStatus(req.getParameter("status"));
		pageVO.setRole(req.getParameter("role"));

		int totalCount = service.selectMemberCount(pageVO);
		pageVO.setTotalCount(totalCount);

		int totalPage = (int) Math.ceil((double) totalCount / pageVO.getCountPerPage());
		pageVO.setTotalPage(totalPage);

		int startRow = (currentPage - 1) * pageVO.getCountPerPage() + 1;
		int endRow = startRow + pageVO.getCountPerPage() - 1;

		pageVO.setStartRow(startRow);
		pageVO.setEndRow(endRow);

		int startPage = ((currentPage - 1) / pageVO.getPageCount()) * pageVO.getPageCount() + 1;
		int endPage = startPage + pageVO.getPageCount() - 1;

		if (endPage > totalPage) {
			endPage = totalPage;
		}

		pageVO.setStartPage(startPage);
		pageVO.setEndPage(endPage);

		Map<String, Object> data = service.selectMemberListData(pageVO);

		List<MemberVO> list = (List<MemberVO>) data.get("memberList");
		int todayCount = (int) data.get("todayCount");

		req.setAttribute("memberList", list);
		req.setAttribute("totalCount", totalCount);
		req.setAttribute("todayCount", todayCount);
		req.setAttribute("pageVO", pageVO);

		req.getRequestDispatcher("/view/admin/member-list.jsp")
		   .forward(req, resp);
	}
}
