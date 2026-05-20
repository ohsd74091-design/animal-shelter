package kr.or.ddit.volunteer_recruit.controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.or.ddit.member.vo.MemberVO;
import kr.or.ddit.volunteer_recruit.service.IVolunteer_RecruitService;
import kr.or.ddit.volunteer_recruit.service.Volunteer_RecruitServiceImpl;
import kr.or.ddit.volunteer_recruit.vo.Volunteer_RecruitDetailVO;
import kr.or.ddit.volunteer_recruit.vo.Volunteer_RecruitVO;

@WebServlet("/volunteer/applyView.do")
public class VolunteerApplyViewController extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		MemberVO loginUser =(MemberVO)req.getSession().getAttribute("loginUser");
		
		if(loginUser ==null) {
			resp.sendRedirect(req.getContextPath() + "/login.do");
			return;
		}
		String recruitIdParam =req.getParameter("recruitId");
		int recruitId= 0;
		
		try {
			recruitId =Integer.parseInt(recruitIdParam);
		}catch (Exception e) {
			resp.sendRedirect(req.getContextPath() + "/volunteer/list.do");
			return;
		}
		
		IVolunteer_RecruitService service =Volunteer_RecruitServiceImpl.getservice();
		
		service.refreshRecruitStatus();
		
		//모집글 기본정보...
		Volunteer_RecruitVO recruit =service.getVolunteerRecruitDetail(recruitId);
		
		//청소 산책 상세유형 + 신청현황....
		List<Volunteer_RecruitDetailVO>detailList =service.getRecruitDetailList(recruitId);
		
		if(recruit ==null) {
			resp.sendRedirect(req.getContextPath() + "/volunteer/list.do");
			return;
		}
		req.setAttribute("recruit", recruit);
		req.setAttribute("detailList", detailList);
		
		req.getRequestDispatcher("/view/volunteer-write.jsp").forward(req, resp);
		
	}

}
