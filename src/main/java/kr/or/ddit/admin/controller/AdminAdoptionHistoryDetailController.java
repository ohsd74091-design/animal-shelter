package kr.or.ddit.admin.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.or.ddit.admin.service.AdminServiceImpl;
import kr.or.ddit.admin.service.IadminService;
import kr.or.ddit.adoption.vo.AdoptionVO;
import kr.or.ddit.member.vo.MemberVO;

@WebServlet("/admin/adoption/historyDetail.do")
public class AdminAdoptionHistoryDetailController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		// 1. 관리자 권한 체크
		MemberVO loginUser = (MemberVO) req.getSession().getAttribute("loginUser");
		if (loginUser == null || !"ADMIN".equals(loginUser.getRole())) {
			resp.sendRedirect(req.getContextPath() + "/main.do");
			return;
		}

		// 2. 파라미터 체크
		String adoptionIdParam = req.getParameter("adoptionId");
		if (adoptionIdParam == null || adoptionIdParam.trim().isEmpty()) {
			resp.sendRedirect(req.getContextPath() + "/admin/adoption/history.do");
			return;
		}

		int adoptionId;
		try {
			adoptionId = Integer.parseInt(adoptionIdParam);
		} catch (NumberFormatException e) {
			resp.sendRedirect(req.getContextPath() + "/admin/adoption/history.do");
			return;
		}

		// 3. 상세 조회
		IadminService service = AdminServiceImpl.getinsetance();
		AdoptionVO adoption = service.selectAdoptionHistoryDetail(adoptionId);

		// 4. 조회 결과 없으면 목록으로
		if (adoption == null) {
			resp.sendRedirect(req.getContextPath() + "/admin/adoption/history.do");
			return;
		}

		// 5. JSP 전달
		req.setAttribute("adoption", adoption);
		req.getRequestDispatcher("/view/admin/AdminAdoptionHistoryDetail.jsp")
		   .forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}
}