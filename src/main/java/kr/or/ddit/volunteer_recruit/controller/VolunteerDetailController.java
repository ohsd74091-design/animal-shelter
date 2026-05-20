package kr.or.ddit.volunteer_recruit.controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.or.ddit.volunteer_recruit.service.IVolunteer_RecruitService;
import kr.or.ddit.volunteer_recruit.service.Volunteer_RecruitServiceImpl;
import kr.or.ddit.volunteer_recruit.vo.Volunteer_RecruitDetailVO;
import kr.or.ddit.volunteer_recruit.vo.Volunteer_RecruitVO;

@WebServlet("/volunteer/detail.do")
public class VolunteerDetailController extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int recruitId = Integer.parseInt(req.getParameter("recruitId"));

        IVolunteer_RecruitService service = Volunteer_RecruitServiceImpl.getservice();

        service.refreshRecruitStatus();
        
        Volunteer_RecruitVO recruit = service.getVolunteerRecruitDetail(recruitId);
        List<Volunteer_RecruitDetailVO> detailList = service.getRecruitDetailList(recruitId);

        req.setAttribute("recruit", recruit);
        req.setAttribute("detailList", detailList);

        req.getRequestDispatcher("/view/volunteer-detail.jsp").forward(req, resp);
	}

}
