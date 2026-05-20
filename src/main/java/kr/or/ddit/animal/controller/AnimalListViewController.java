package kr.or.ddit.animal.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.or.ddit.animal.service.AnimalServiceImpl;
import kr.or.ddit.animal.service.IAnimalService;
import kr.or.ddit.animal.vo.AnimalVO;
import kr.or.ddit.member.vo.MemberVO;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/animal/animalList.do")
public class AnimalListViewController extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	private IAnimalService animalService = AnimalServiceImpl.getInstance();

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		
		String action = request.getParameter("action");
		String keyword = request.getParameter("keyword");
		String breed   = request.getParameter("breed");
		
		// 페이징 파라미터 수집
        String pageParam = request.getParameter("page");
        int currentPage = (pageParam == null || pageParam.isEmpty()) ? 1 : Integer.parseInt(pageParam);
        int pageSize = 8; // 한 페이지에 8마리씩 출력
        
        
		
		// 파라미터 수집 (단일 값)
		String animalType = request.getParameter("animalType"); // 전체/강아지/고양이
		String gender = request.getParameter("gender"); // 성별 (M/F)
		String sort = request.getParameter("sort"); // 정렬 (views/latest)

		// 다중 선택 파라미터 수집 (체크박스 - getParameterValues 사용)
		String[] personalitys = request.getParameterValues("personalityList"); // 성격
		String[] sizes = request.getParameterValues("sizeList"); // 크기

		// MyBatis(animal.xml) 전달을 위한 Map 구성
		Map<String, Object> paramMap = new HashMap<>();

		// 추가: 세션에서 로그인한 사용자 아이디 가져오기
	    HttpSession session = request.getSession();
	    MemberVO loginUser = (MemberVO) session.getAttribute("loginUser");
	    
	    // loginMemberId라는 이름으로 Map에 저장 (xml의 변수명과 일치해야 함)
	    if (loginUser != null) {
	        paramMap.put("loginMemberId", loginUser.getMemberId());
	    } else {
	        paramMap.put("loginMemberId", null); // 로그인 안 했으면 null
	    }
		
		// 정렬 초기값 설정
		if (sort == null || sort.isEmpty())
			sort = "latest";
		
		// [추가] 검색어가 있으면 Map에 저장 (DB 쿼리의 LIKE 조건으로 사용됨)
		if (keyword != null && !keyword.trim().isEmpty()) {
			paramMap.put("keyword", keyword.trim());
		}
		
		if (breed != null && !breed.trim().isEmpty()) {
		    paramMap.put("breed", breed.trim());
		}

		// 값이 있을 때만 Map에 담기 (MyBatis의 <if> 태그 조건과 일치)
		if (animalType != null && !animalType.isEmpty())
			paramMap.put("animalType", animalType);
		if (gender != null && !gender.isEmpty())
			paramMap.put("gender", gender);
		if (sort != null && !sort.isEmpty())
			paramMap.put("sort", sort);

		// 체크박스 배열을 List로 변환하여 Map에 추가
		// * <foreach collection> 사용 시 배열보다 List 일때 처리가 더 유연
		if (personalitys != null && personalitys.length > 0)
			paramMap.put("personalityList", Arrays.asList(personalitys));
		if (sizes != null && sizes.length > 0)
			paramMap.put("sizeList", Arrays.asList(sizes));

		// 페이징 계산
        // - 필터링된 조건에 맞는 전체 글 개수 조회
        int totalCount = animalService.getAnimalCount(paramMap); 
        int totalPage = (int) Math.ceil((double) totalCount / pageSize);
        
        // - Oracle ROWNUM 기준 범위 계산
        int start = (currentPage - 1) * pageSize + 1;
        int end = currentPage * pageSize;
        System.out.println("=== breed 파라미터 확인 ===");
        System.out.println("breed = " + breed);
        System.out.println("paramMap = " + paramMap);
        paramMap.put("start", start);
        paramMap.put("end", end);
		
		// 서비스 호출 및 결과 받기
		List<AnimalVO> animalList = animalService.displayAnimalList(paramMap);

		// JSP로 결과 전달
		request.setAttribute("animalList", animalList);
		request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPage", totalPage);
        request.setAttribute("keyword", keyword);
        request.setAttribute("breed",   breed);
		request.setAttribute("currentFilters", paramMap); // JSP input value 유지용
		

		request.getRequestDispatcher("/WEB-INF/view/animal/animalListForm.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doGet(request, response);
	}

}
