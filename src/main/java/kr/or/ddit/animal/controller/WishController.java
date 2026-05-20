package kr.or.ddit.animal.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.or.ddit.animal.service.AnimalServiceImpl;
import kr.or.ddit.animal.service.IAnimalService;
import kr.or.ddit.member.vo.MemberVO;

@WebServlet("/animal/toggleWish.do")
public class WishController extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	private IAnimalService animalService = AnimalServiceImpl.getInstance();

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		resp.setContentType("application/json; charset=UTF-8");
		PrintWriter out = resp.getWriter();

		// 세션에서 로그인 정보 확인
		HttpSession session = req.getSession();
		MemberVO loginUser = (MemberVO) session.getAttribute("loginUser");

		// 로그인 안된 경우
		if (loginUser == null)
		{
			// 1. 현재 요청한 페이지의 정보를 가져옴 (보통 Referer 헤더에 이전 페이지 주소가 담겨있음)
			String targetPath = req.getHeader("Referer");

			// 2. 만약 Referer가 없다면 파라미터로 받은 animalId를 이용해 강제로 주소를 만듭니다.
			if (targetPath == null || targetPath.isEmpty())
			{
				String animalId = req.getParameter("animalId");
				targetPath = req.getContextPath() + "/animal/animalDetail.do?id=" + animalId;
			}

			out.print("{\"success\": false, \"message\": \"LOGIN_REQUIRED\", \"targetPath\": \"" + targetPath + "\"}");
			return;
		}

		// 파라미터 수집
		String animalId = req.getParameter("animalId");
		String memberId = loginUser.getMemberId();

		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("animalId", animalId);
		paramMap.put("memberId", memberId);

		// 서비스 호출 (기존 AnimalServiceImpl의 updateFavorite 활용)
		try
		{
			int result = animalService.updateFavorite(paramMap);

			if (result > 0)
				out.print("{\"success\": true}");
			else
				out.print("{\"success\": false, \"message\": \"FAIL\"}");
		}
		catch (Exception e)
		{
			e.printStackTrace();
			out.print("{\"success\": false, \"message\": \"ERROR\"}");
		}

	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		doPost(req, resp);
	}

}
