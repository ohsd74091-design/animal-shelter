package kr.or.ddit.member.controller;

import java.io.IOException;

import org.eclipse.jdt.internal.compiler.env.IModule.IService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.or.ddit.member.service.IMemberService;
import kr.or.ddit.member.service.MemberServiceImpl;
import kr.or.ddit.member.vo.MemberVO;

@WebServlet("/member/signup.do")
public class SignupController extends HttpServlet{
	private static final long serialVersionUID =1L;
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	
		
		req.setCharacterEncoding("UTF-8");
		
		IMemberService service = MemberServiceImpl.getinstance();
		
		
		
		//파라미터 얻기 
		String memberName = req.getParameter("memberName");
		String memberId = req.getParameter("memberId");
		String nickname = req.getParameter("nickname");
		String memberPw = req.getParameter("memberPw");
		String memberPwConfirm = req.getParameter("memberPwConfirm");
		String email = req.getParameter("email");
		String phone = req.getParameter("phone");
		
		//비밀번호 동일?
		if (memberPw == null || !memberPw.equals(memberPwConfirm)) {
			req.setAttribute("msg", "비밀번호가 일치하지 않습니다.");
			req.getRequestDispatcher("/membership.jsp").forward(req, resp);
			return;
		}
		
		if (service.checkNickname(nickname) > 0) {
			req.setAttribute("msg", "이미 사용 중인 닉네임입니다.");
			req.getRequestDispatcher("/membership.jsp").forward(req, resp);
			return;
		}
		// 필요하면 아이디, 이메일도 같이
		if (service.checkMemberId(memberId) > 0) {
		    req.setAttribute("msg", "이미 사용 중인 아이디입니다.");
		    req.getRequestDispatcher("/membership.jsp").forward(req, resp);
		    return;
		}
		
		//vo setter
		
		MemberVO vo =new MemberVO();
		vo.setMemberId(memberId);
		vo.setMemberName(memberName);
		vo.setNickname(nickname);
		vo.setMemberPw(memberPw);
		vo.setEmail(email);
		vo.setPhone(phone);
		
		
		int cnt = service.insertMember(vo);
		
		if (cnt > 0) {
			resp.sendRedirect(req.getContextPath() + "/login.do");
		} else {
			req.setAttribute("msg", "회원가입 실패....");
			req.getRequestDispatcher("/membership.jsp").forward(req, resp);
		}
		
		}
}
