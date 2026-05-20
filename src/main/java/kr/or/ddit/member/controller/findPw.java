package kr.or.ddit.member.controller;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.or.ddit.member.service.IMemberService;
import kr.or.ddit.member.service.MemberServiceImpl;
import kr.or.ddit.member.vo.MemberVO;

@WebServlet("/findPw.do")
public class findPw extends HttpServlet{
	private static final long serialVersionUID =1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/view/findpw.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		
		String memId = req.getParameter("mem_id");
		String memMail= req.getParameter("mem_mail");
		
		MemberVO vo = new MemberVO();
		
		vo.setMemberId(memId);
		vo.setEmail(memMail);
		
		IMemberService service = MemberServiceImpl.getinstance();
		boolean res = service.resetPasswordAndSendMail(vo);
		
		resp.setContentType("text/html; charset =UTF-8");
		PrintWriter out = resp.getWriter();
		
		out.println("<script>");
		
		if(res) {
			 out.println("alert('임시 비밀번호를 이메일로 전송했습니다.');");
	            out.println("location.href='" + req.getContextPath() + "/login.do';");
		}else {
			 out.println("alert('일치하는 회원 정보가 없습니다.');");
	            out.println("history.back();");
		}
		  out.println("</script>");
	}
}
