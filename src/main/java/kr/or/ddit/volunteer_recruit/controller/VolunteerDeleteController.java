package kr.or.ddit.volunteer_recruit.controller;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.or.ddit.member.vo.MemberVO;
import kr.or.ddit.volunteer_recruit.service.IVolunteer_RecruitService;
import kr.or.ddit.volunteer_recruit.service.Volunteer_RecruitServiceImpl;

@WebServlet("/volunteer/delete.do")
public class VolunteerDeleteController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		MemberVO loginUser = (MemberVO) req.getSession().getAttribute("loginUser");

        if (loginUser == null || !"ADMIN".equals(loginUser.getRole())) {
            resp.sendRedirect(req.getContextPath() + "/volunteer/list.do");
            return;
        }

        int recruitId = Integer.parseInt(req.getParameter("recruitId"));

        IVolunteer_RecruitService service = Volunteer_RecruitServiceImpl.getservice();
        boolean result = service.deleteVolunteerRecruit(recruitId);

        resp.setContentType("text/html; charset=UTF-8");
        PrintWriter out = resp.getWriter();

        out.println("<script>");
        if (result) {
            out.println("alert('봉사 모집글이 삭제되었습니다.');");
            out.println("location.href='" + req.getContextPath() + "/volunteer/list.do';");
        } else {
            out.println("alert('삭제에 실패했습니다.');");
            out.println("history.back();");
        }
        out.println("</script>");
	}
}
