package kr.or.ddit.adoption.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.or.ddit.adoption.service.AdoptionServiceImpl;
import kr.or.ddit.adoption.service.IAdoptionService;
import kr.or.ddit.adoption.vo.AdoptionDetailVO;
import kr.or.ddit.adoption.vo.AdoptionVO;
import kr.or.ddit.animal.dto.AnimalDetailDto;
import kr.or.ddit.animal.service.AnimalServiceImpl;
import kr.or.ddit.animal.service.IAnimalService;
import kr.or.ddit.member.vo.MemberVO;

/**
 * 입양 신청 컨트롤러
 *
 * GET  /animal/adoptionForm.do?animalId=123  → 신청 폼 페이지
 * POST /animal/adoptionApply.do              → 신청 처리 (AJAX, text 응답)
 */
@WebServlet(urlPatterns = { "/adoption/adoptionForm.do", "/adoption/adoptionApply.do" })
public class AdoptionController extends HttpServlet
{

	private static final long serialVersionUID = 1L;

	private final IAdoptionService adoptionService = AdoptionServiceImpl.getInstance();
	private final IAnimalService   animalService   = AnimalServiceImpl.getInstance();

    /* ====================================================
       GET - 신청 폼 페이지
    ==================================================== */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{

		request.setCharacterEncoding("UTF-8");

		HttpSession session = request.getSession(false);

		// 로그인 체크
		if (session == null || session.getAttribute("loginUser") == null)
		{
			response.sendRedirect(request.getContextPath() + "/login.do");
			return;
		}

		MemberVO loginUser = (MemberVO) session.getAttribute("loginUser");

		// animalId 파라미터 (동물상세에서 넘어온 경우)
		String animalIdStr = request.getParameter("animalId");
		int animalId = 0;
		try
		{
			if (animalIdStr != null)
				animalId = Integer.parseInt(animalIdStr);
		}
		catch (NumberFormatException e)
		{
			e.printStackTrace();
		}

		// 동물 정보 조회 (animalId 가 있는 경우)
		if (animalId > 0)
		{
			AnimalDetailDto dto = animalService.getAnimalDetail(animalId);
			if (dto != null && dto.getAnimal() != null)
			{
				request.setAttribute("animal", dto.getAnimal()); // AnimalVO
				request.setAttribute("mainImage", dto.getMainImage()); // AnimalImageVO (대표이미지)
			}
		}

		// 로그인 회원 정보를 JSP 에서 세션으로 바로 참조하므로 별도 setAttribute 불필요
		// JSP 에서 ${sessionScope.loginUser.memberName}, ${sessionScope.loginUser.phone}
		// 으로 사용

		request.getRequestDispatcher("/WEB-INF/view/adoption/adoptionForm.jsp").forward(request, response);
	}

    /* ====================================================
       POST - 신청 처리 (AJAX)
    ==================================================== */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{

		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/plain;charset=UTF-8");

		PrintWriter out = response.getWriter();

		HttpSession session = request.getSession(false);

		// 로그인 체크
		if (session == null || session.getAttribute("loginUser") == null)
		{
			out.print("NOLOGIN");
			return;
		}

		MemberVO loginUser = (MemberVO) session.getAttribute("loginUser");

		try
		{
			// ---- AdoptionVO 구성 ----
			AdoptionVO adoptionVO = new AdoptionVO();
			adoptionVO.setMemberId(loginUser.getMemberId());
			adoptionVO.setAnimalId(Integer.parseInt(request.getParameter("animalId")));

			// 방문 희망일
			String visitDateStr = request.getParameter("visitDate");
			if (visitDateStr != null && !visitDateStr.isEmpty())
			{
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				adoptionVO.setVisitDate(new java.sql.Date(sdf.parse(visitDateStr).getTime()));
			}

			// ---- AdoptionDetailVO 구성 ----
			AdoptionDetailVO detailVO = new AdoptionDetailVO();
			detailVO.setJob(request.getParameter("job"));
			detailVO.setHousingType(request.getParameter("housingType"));
			detailVO.setPetExperience(request.getParameter("petExperience"));
			detailVO.setAddress(request.getParameter("address"));
			detailVO.setAdoptionReason(request.getParameter("adoptionReason"));

			adoptionVO.setDetail(detailVO);

			// ---- 서비스 호출 ----
			int result = adoptionService.applyAdoption(adoptionVO);

			if (result == 1)
				out.print("SUCCESS");
			else if (result == -1)
				out.print("DUPLICATE");
			else
				out.print("FAIL");

		}
		catch (Exception e)
		{
			e.printStackTrace();
			out.print("FAIL");
		}
	}
}