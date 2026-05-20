package kr.or.ddit.volunteer_recruit.controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.or.ddit.common.vo.PageVO;
import kr.or.ddit.volunteer_recruit.service.IVolunteer_RecruitService;
import kr.or.ddit.volunteer_recruit.service.Volunteer_RecruitServiceImpl;
import kr.or.ddit.volunteer_recruit.vo.Volunteer_RecruitVO;
@WebServlet("/volunteer/list.do")
public class VolunteerListController extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		  IVolunteer_RecruitService service = Volunteer_RecruitServiceImpl.getservice();

	        // 상태 자동 갱신
	        service.refreshRecruitStatus();

	        String pageParam = req.getParameter("page");
	        String status = req.getParameter("status");
	        String keyword = req.getParameter("keyword");
	        String sort = req.getParameter("sort");

	        int currentPage = 1;

	        if (pageParam != null && !pageParam.trim().isEmpty()) {
	            currentPage = Integer.parseInt(pageParam);
	        }

	        if (sort == null || sort.trim().isEmpty()) {
	            sort = "latest";
	        }

	        PageVO pageVO = new PageVO();
	        pageVO.setCurrentPage(currentPage);
	        pageVO.setStatus(status);
	        pageVO.setKeyword(keyword);
	        pageVO.setSort(sort);

	        int totalCount = service.getTotalCount(pageVO);

	        pageVO = service.getPageInfo(currentPage, totalCount);
	        pageVO.setStatus(status);
	        pageVO.setKeyword(keyword);
	        pageVO.setSort(sort);

	        List<Volunteer_RecruitVO> recruitList = service.listByPage(pageVO);

	        req.setAttribute("recruitList", recruitList);
	        req.setAttribute("pageVO", pageVO);
		req.getRequestDispatcher("/view/vlounteer-list.jsp").forward(req, resp);
	}
	
	
	
	
}
