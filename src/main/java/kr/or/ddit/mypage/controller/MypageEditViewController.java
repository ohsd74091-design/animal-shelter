package kr.or.ddit.mypage.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import kr.or.ddit.member.vo.MemberVO;
import kr.or.ddit.mypage.service.IMypageService;
import kr.or.ddit.mypage.service.MypageServiceImpl;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * 회원 정보 수정 페이지 컨트롤러
 */
@WebServlet("/mypage/edit.do")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2,   // 2MB
    maxFileSize       = 1024 * 1024 * 10,   // 10MB
    maxRequestSize    = 1024 * 1024 * 50    // 50MB
)
public class MypageEditViewController extends HttpServlet
{

	private static final long serialVersionUID = 1L;
	private static final String UPLOAD_PATH = "D:/upload/profile";
	private IMypageService mypageService = MypageServiceImpl.getInstance();

	/* 수정 폼 출력(폼 화면 열기) */
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

		// 폼 출력 (loginUser 는 세션에서 JSP가 직접 사용)
		request.getRequestDispatcher("/WEB-INF/view/mypage/mypageEditForm.jsp").forward(request, response);
	}

	/* 수정 처리 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{

		request.setCharacterEncoding("UTF-8");

		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("loginUser") == null)
		{
			response.sendRedirect(request.getContextPath() + "/member/login.do");
			return;
		}

		MemberVO loginUser = (MemberVO) session.getAttribute("loginUser");

		// 파라미터 수집
		String nickname   = request.getParameter("nickname");
		String email      = request.getParameter("email");
		String phone      = request.getParameter("phone");
		String currentPw  = request.getParameter("currentPw");
		String newPw      = request.getParameter("newPw");

		// VO에 변경값 반영
		MemberVO updateVO = new MemberVO();
		updateVO.setMemberId(loginUser.getMemberId());
		updateVO.setNickname(nickname);
		updateVO.setEmail(email);
		updateVO.setPhone(phone);

		// 비밀번호 변경 요청이 있는 경우
		if (newPw != null && !newPw.isEmpty())
		{
			updateVO.setMemberPw(newPw);
		}

		// 프로필 이미지 처리
		try
		{
			Part filePart = request.getPart("profileImg");
			System.out.println("[프로필이미지] filePart: " + filePart);
			System.out.println("[프로필이미지] fileSize: " + (filePart != null ? filePart.getSize() : "null"));
			if (filePart != null && filePart.getSize() > 0)
			{
				// 저장 폴더 생성
				File uploadDir = new File(UPLOAD_PATH);
				if (!uploadDir.exists()) uploadDir.mkdirs();

				// 원본 파일명 추출
				String originName = filePart.getSubmittedFileName();
				String ext        = originName.substring(originName.lastIndexOf("."));
				String saveName   = UUID.randomUUID().toString() + ext;

				System.out.println("[프로필이미지] originName: " + originName);
				System.out.println("[프로필이미지] saveName: " + saveName);

				// 파일 저장
				filePart.write(UPLOAD_PATH + File.separator + saveName);

				// DB에 파일명만 저장
				updateVO.setProfileImg(saveName);
				System.out.println("[프로필이미지] profileImg set: " + saveName);
			}
		}
		catch (Exception e)
		{
			System.out.println("[프로필이미지] 오류 발생: " + e.getMessage());
			e.printStackTrace();
		}

		// 서비스 호출
		int result = mypageService.updateMember(updateVO, currentPw);

		if (result > 0)
		{
			// 세션 정보 갱신
			loginUser.setNickname(nickname);
			loginUser.setEmail(email);
			loginUser.setPhone(phone);
			if (updateVO.getProfileImg() != null)
			{
				loginUser.setProfileImg(updateVO.getProfileImg());
			}
			session.setAttribute("loginUser", loginUser);

			response.sendRedirect(request.getContextPath() + "/mypage/edit.do?success=true");
		}
		else
		{
			// 실패 시 폼으로 돌아가서 오류 메시지 출력
			request.setAttribute("errorMsg", "정보 수정에 실패했습니다. 현재 비밀번호를 확인해주세요.");
			request.getRequestDispatcher("/WEB-INF/view/mypage/mypageEditForm.jsp").forward(request, response);
		}
	}
}