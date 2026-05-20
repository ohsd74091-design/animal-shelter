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

@WebServlet("/findId.do")
public class FindIdView extends HttpServlet{
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/view/findId.jsp").forward(req, resp);
		
	
	}


@Override
protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	req.setCharacterEncoding("UTF-8");
	
	String mem_name =req.getParameter("mem_name");
	String mem_mail = req.getParameter("mem_mail");
	
	System.out.println("이름 : " + mem_name);
	System.out.println("이메일 : " + mem_mail);
	
	MemberVO vo = new MemberVO();
	vo.setMemberName(mem_name);
	vo.setEmail(mem_mail);
	
	IMemberService service = MemberServiceImpl.getinstance();
	
	boolean res =service.sendIdByEmail(vo);
	resp.setContentType("text/html; charset=UTF-8");
    PrintWriter out = resp.getWriter();

    out.println("<script>");

    if(res) {
        out.println("alert('아이디를 이메일로 전송했습니다.');");
        out.println("location.href='" + req.getContextPath() + "/login.do';");
    } else {
        out.println("alert('일치하는 회원 정보가 없습니다.');");
        out.println("history.back();");
    }

    out.println("</script>");

}
}



	
	
	