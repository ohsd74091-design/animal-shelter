package kr.or.ddit.animal.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.or.ddit.adoption.service.AdoptionServiceImpl;
import kr.or.ddit.adoption.service.IAdoptionService;
import kr.or.ddit.animal.dto.AnimalDetailDto;
import kr.or.ddit.animal.service.AnimalServiceImpl;
import kr.or.ddit.animal.service.IAnimalService;
import kr.or.ddit.member.vo.MemberVO;

@WebServlet("/animal/animalDetail.do")
public class AnimalDetail extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		// 세션에서 로그인한 사용자 정보 가져오기
		HttpSession session = req.getSession();
		MemberVO loginUser = (MemberVO) session.getAttribute("loginUser");

		if (loginUser == null)
		{
			// 현재 요청한 URL과 파라미터
			String uri = req.getRequestURI(); // /project/animal/animalDetail.do
			String query = req.getQueryString(); // animalId=10
			String targetPath = (query == null) ? uri : uri + "?" + query;

			// 세션에 이 목적지 주소를 저장
			session.setAttribute("targetPath", targetPath);
			
			// 로그인 페이지로 이동
			resp.sendRedirect(req.getContextPath() + "/login.do");
			return;
			
			
		}
		
		
		//해당 동물 id값 가져옴 
		String idParam =req.getParameter("animalId");
		if (idParam ==null) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "animalId required");
			return;
		}
		
		int animalId =Integer.parseInt(idParam);
		
		
		// DTO 값 SERVIEC로 지정후 가져오기 
		AnimalDetailDto dto;
		
		IAnimalService service = AnimalServiceImpl.getInstance();
		IAdoptionService adoptionService = AdoptionServiceImpl.getInstance();
		
		dto=service.getAnimalDetail(animalId);
		
		//찜 여부 조회 ... 
		boolean isFavorited =false;
		isFavorited= service.isFavorited(animalId, loginUser.getMemberId());
		
		req.setAttribute("animalDetail", dto);
		req.setAttribute("isFavorited", isFavorited);

		// 이미 신청한 동물인지 체크 (심사중 or 승인 상태)
		Map<String, Object> dupParam = new HashMap<>();
		dupParam.put("animalId", animalId);
		dupParam.put("memberId", loginUser.getMemberId());
		int dupCount = adoptionService.checkDuplicate(dupParam);
		req.setAttribute("alreadyApplied", dupCount > 0);

		// 반려 이력 체크 (입양완료 동물은 제외)
		boolean isAdoptionDone = dto.getAnimal() != null
				&& "입양완료".equals(dto.getAnimal().getAdoptionStatus());

		if (!isAdoptionDone) {
			int rejectedCount = adoptionService.checkRejected(dupParam);
			req.setAttribute("isRejected", rejectedCount > 0);

			// 반려 사유 조회
			if (rejectedCount > 0) {
				String rejectReason = adoptionService.getRejectReason(dupParam);
				req.setAttribute("rejectReason", rejectReason);
			}
		}
		req.getRequestDispatcher("/view/animal-detail.jsp").forward(req, resp);
		
		System.out.println("animalDetail: " + dto);
		
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		doGet(req, resp);
	}
}