package kr.or.ddit.mypage.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.or.ddit.member.vo.MemberVO;
import kr.or.ddit.mypage.service.IMypageService;
import kr.or.ddit.mypage.service.MypageServiceImpl;

import java.io.IOException;

/**
 * 회원 탈퇴 컨트롤러 GET /mypage/withdraw.do → 탈퇴 처리 후 메인으로 리다이렉트
 */
@WebServlet("/mypage/withdraw.do")
public class MypageWithdrawController extends HttpServlet
{

	private static final long serialVersionUID = 1L;
	private IMypageService mypageService = MypageServiceImpl.getInstance();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{

		HttpSession session = request.getSession(false);

		// 로그인 체크
		if (session == null || session.getAttribute("loginUser") == null)
		{
			response.sendRedirect(request.getContextPath() + "/member/login.do");
			return;
		}

		MemberVO loginUser = (MemberVO) session.getAttribute("loginUser");
		String memberId = loginUser.getMemberId();

		// 탈퇴 처리 (MEMBER 테이블 STATUS → 'N' 으로 변경)
		int result = mypageService.withdrawMember(memberId);

		if (result > 0)
		{
			// 세션 무효화
			session.invalidate();
			// 메인 페이지로 리다이렉트
			response.sendRedirect(request.getContextPath() + "/main.do?withdraw=true");
		}
		else
		{
			// 실패 시 수정 페이지로 돌아가서 오류 메시지 출력
			request.setAttribute("errorMsg", "회원 탈퇴 처리 중 오류가 발생했습니다. 다시 시도해주세요.");
			request.getRequestDispatcher("/WEB-INF/view/mypage/mypageEditForm.jsp").forward(request, response);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doGet(request, response);
	}
}
