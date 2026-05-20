package kr.or.ddit.admin.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.or.ddit.admin.service.AdminServiceImpl;
import kr.or.ddit.admin.service.IadminService;
import kr.or.ddit.animal.vo.AnimalVO;
import kr.or.ddit.common.vo.PageVO;

import java.io.IOException;
import java.util.List;

/**
 * Servlet implementation class AdminAnimalListController
 */
@WebServlet("/admin/animal/list.do")
public class AdminAnimalListController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminAnimalListController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		IadminService service =AdminServiceImpl.getinsetance();
		
		//1.파라미터 수집 
		PageVO pageVO =new PageVO();
		
		//2.페이지번호: 기본값 1
		int currentPage =req.getParameter("page") == null ? 1:Integer.parseInt(req.getParameter("page"));
		
		pageVO.setCurrentPage(currentPage);
		pageVO.setCountPerPage(9); // 카드 3열 기준 9개 ...
		pageVO.setPageCount(5); //페이지 네이션에 보여줄 번호 개수 (1~5, 6-10 ...)
		
		//JSP 필터 버튼에서 넘어오는 값 => PAGEVO에 담아서 XML필터조건 사용 
		pageVO.setAnimalType(req.getParameter("animalType"));
		pageVO.setAdoptionStatus(req.getParameter("adoptionStatus"));
		pageVO.setSort(req.getParameter("sort"));
		
		//전체 건수 조회 => 페이징 계산 ...
		//필터 조건이 반영된 건수를 먼저 구해야 totalpage 계산 
		int totalCount =service.selectAdminAnimalCount(pageVO);
		pageVO.setTotalCount(totalCount);
		
		int totalPage =(int) Math.ceil((double)totalCount/pageVO.getCountPerPage());
		
		if (totalPage < 1) totalPage =1;// 데이터없을떄 0페이지방지
	
		pageVO.setTotalPage(totalPage);
		
		//rownum 페이징용 시작/끝행 번호 계산 
		//ex) 2페이지,9개씩 =>startRow=10 endRow=18
		int startRow = (currentPage - 1) * pageVO.getCountPerPage() + 1;
        int endRow   = startRow + pageVO.getCountPerPage() - 1;
        pageVO.setStartRow(startRow);
        pageVO.setEndRow(endRow);
        
        // 페이지네이션 번호 범위 계산
        // ex) pageCount=5, currentPage=7 → startPage=6, endPage=10
        int startPage = ((currentPage - 1) / pageVO.getPageCount()) * pageVO.getPageCount() + 1;
        int endPage   = startPage + pageVO.getPageCount() - 1;
        if (endPage > totalPage) endPage = totalPage; // 마지막 그룹 초과 방지
        pageVO.setStartPage(startPage);
        pageVO.setEndPage(endPage);
        
        //목록조회 ... 
        List<AnimalVO> animalList =service.selectAdminAnimalList(pageVO);
        
        //jsp전달 
        
        req.setAttribute("animalList", animalList);
        req.setAttribute("totalCount", totalCount);
        req.setAttribute("pageVO", pageVO);  //페이지네이션 + 필터유지\
        
        req.getRequestDispatcher("/view/admin/animal-list.jsp").forward(req, resp);
	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
