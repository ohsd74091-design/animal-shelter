package kr.or.ddit.member.controller;

import java.io.IOException;
import java.net.Authenticator.RequestorType;
import java.net.ResponseCache;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.or.ddit.member.service.IMemberService;
import kr.or.ddit.member.service.MemberServiceImpl;
import kr.or.ddit.member.vo.MemberVO;

@WebServlet("/login.do")
public class LoginController extends HttpServlet
{

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		// URL에 targetPath가 있다면 forward할 때 같이 넘겨줌 (login.jsp의 hidden 필드용)
		String targetPath = req.getParameter("targetPath");
		req.setAttribute("targetPath", targetPath);

		req.getRequestDispatcher("/WEB-INF/view/login.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		req.setCharacterEncoding("UTF-8");
		String id = req.getParameter("id");
		String pw = req.getParameter("pw");

		MemberVO vo = new MemberVO();
		vo.setMemberId(id);
		vo.setMemberPw(pw);

		IMemberService service = MemberServiceImpl.getinstance();

		MemberVO loginMember = service.loginMember(vo);

		if (loginMember != null)
		{
			HttpSession session = req.getSession();
			session.setAttribute("loginUser", loginMember);

			// URL 파라미터(또는 jsp에서 넘어온 값) 확인
			String targetPath = req.getParameter("targetPath");

			// 파라미터에 없다면 세션 확인
			if (targetPath == null || targetPath.isEmpty())
			{
				targetPath = (String) session.getAttribute("targetPath");
				session.removeAttribute("targetPath");
			}

			// 이동할 곳이 있다면 이동, 없으면 메인으로
			if (targetPath != null && !targetPath.isEmpty())
				resp.sendRedirect(targetPath);
			else
				resp.sendRedirect(req.getContextPath() + "/main.do");
		}
		else
		{
			resp.setContentType("text/html; charset=UTF-8");
			resp.getWriter().println("<script>alert('아이디 또는 비밀번호가 틀립니다'); history.back();</script>");
		}
	}

}
