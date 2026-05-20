package kr.or.ddit.admin.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.or.ddit.admin.service.AdminServiceImpl;
import kr.or.ddit.admin.service.IadminService;
import kr.or.ddit.member.vo.MemberVO;
import kr.or.ddit.volunteer_recruit.vo.Volunteer_ApplyVO;
import kr.or.ddit.volunteer_recruit.vo.Volunteer_RecruitVO;

import java.io.IOException;
import java.util.List;

/**
 * Servlet implementation class AdminVolunteerManageController
 */
@WebServlet("/admin/volunteer/manage.do")
public class AdminVolunteerManageController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminVolunteerManageController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	
		// 관리자 체크
		MemberVO loginUser = (MemberVO) req.getSession().getAttribute("loginUser");
		if (loginUser == null || !"ADMIN".equals(loginUser.getRole())) {
			resp.sendRedirect(req.getContextPath() + "/main.do");
			return;
		}

		IadminService service = AdminServiceImpl.getinsetance();

		// 왼쪽 모집글 목록
		List<Volunteer_RecruitVO> recruitList = service.selectRecruitList();
		req.setAttribute("recruitList", recruitList);

		// 오른쪽 선택한 모집글
		String recruitIdStr = req.getParameter("recruitId");

		if (recruitIdStr != null && !recruitIdStr.isEmpty()) {
			int recruitId = Integer.parseInt(recruitIdStr);

			Volunteer_RecruitVO recruit = service.selectRecruitDetail(recruitId);
			List<Volunteer_ApplyVO> applyList = service.selectApplyListByRecruitId(recruitId);

			req.setAttribute("recruit", recruit);
			req.setAttribute("applyList", applyList);
			req.setAttribute("applyCount", applyList.size());
		}

		req.getRequestDispatcher("/view/admin/volunteer-manage.jsp")
		   .forward(req, resp);
		
		
		
	}

}
