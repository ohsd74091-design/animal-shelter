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
import kr.or.ddit.volunteer_recruit.vo.Volunteer_ApplyVO;

@WebServlet("/volunteer/apply.do")
public class VolunteerApplyController extends HttpServlet{
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		req.setCharacterEncoding("UTF-8");

        MemberVO loginUser = (MemberVO) req.getSession().getAttribute("loginUser");

        if (loginUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login.do");
            return;
        }

        String recruitIdParam = req.getParameter("recruitId");
        String interestType = req.getParameter("interestType");
        String applyReason = req.getParameter("applyReason");

        int recruitId = 0;

        try {
            recruitId = Integer.parseInt(recruitIdParam);
        } catch (Exception e) {
            resp.sendRedirect(req.getContextPath() + "/volunteer/list.do");
            return;
        }

        Volunteer_ApplyVO vo = new Volunteer_ApplyVO();
        vo.setMemberId(loginUser.getMemberId());
        vo.setRecruitId(recruitId);
        vo.setInterestType(interestType);
        vo.setApplyReason(applyReason);
        vo.setStatus("대기");
        
        IVolunteer_RecruitService service = Volunteer_RecruitServiceImpl.getservice();
        boolean result =service.applyVolunteer(vo);
        
        resp.setContentType("text/html; charset=UTF-8");
        PrintWriter out =resp.getWriter();
        
        out.println("<script>");
        
        if (result) {
            out.println("alert('봉사 신청이 완료되었습니다.');");
            out.println("location.href='" + req.getContextPath() + "/volunteer/detail.do?recruitId=" + recruitId + "';");
        } else {
            out.println("alert('신청에 실패했습니다. 중복 신청 또는 정원 초과일 수 있습니다.');");
            out.println("history.back();");
        }

        out.println("</script>");
	    }
}
