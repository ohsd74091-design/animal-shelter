package kr.or.ddit.admin.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.or.ddit.admin.service.AdminServiceImpl;
import kr.or.ddit.admin.service.IadminService;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Servlet implementation class AdminAnimalActionController
 */

//두 개의 URL을 하나의 서블릿에서 처리
//uri.contains()로 어떤 요청인지 구분
@WebServlet({ "/admin/animal/status.do", "/admin/animal/delete.do" })
public class AdminAnimalActionController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminAnimalActionController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		// JSON 형태로 응답 -> JSP FETCH()에서 RES.JSON으로 파싱
		resp.setContentType("application/json; charset=UTF-8");
		
		IadminService service =AdminServiceImpl.getinsetance();
		PrintWriter out = resp.getWriter();
		
		//어떤 url로 들어왔는지 확인/분기
		String url =req.getRequestURI();
		
		//--상태변경(status.do)
		
		if(url.contains("status.do")) {
			String animalStringIdStr =req.getParameter("animalId");
			String newStatus = req.getParameter("newStatus");
			
			if(animalStringIdStr == null || newStatus == null || newStatus.trim().isEmpty()) {
				out.print("{\"success\": false, \"message\": \"파라미터 오류\"}");
                return;
			}
			
			
			//DB 제약조건과 일치하는값인지 서버에서 한번더 검증 => 클라이언트에서 임의 변조가능성 
			
			if(!newStatus.equals("입양가능") && !newStatus.equals("입양완료") && !newStatus.equals("입양검토중")) {
				out.print("{\"success\": false, \"message\": \"잘못된 상태값\"}");
                return;
			}
			
			try {
				int animalId =Integer.parseInt(animalStringIdStr);
				int result =service.updateAnimalStatus(animalId, newStatus);
				
				if(result > 0) {
					out.print("{\"success\": true, \"message\": \"상태가 변경되었습니다.\"}");
				}else {
					out.print("{\"success\": false, \"message\": \"상태 변경에 실패했습니다.\"}");
				}
			}catch (NumberFormatException e) {
				out.print("{\"success\": false, \"message\": \"animalId 형식 오류\"}");
			}
			//삭제 (delete.do)
		}else if(url.contains("delete.do")) {
		    String animalIdStr = req.getParameter("animalId");

		    if(animalIdStr == null || animalIdStr.trim().isEmpty()) {
		        out.print("{\"success\": false, \"message\": \"파라미터 오류\"}");
		        return;
		    }

		    try {
		        int animalId = Integer.parseInt(animalIdStr);

		        int result = service.deleteAnimal(animalId);

		        if (result > 0) {
		            out.print("{\"success\": true, \"message\": \"삭제되었습니다.\"}");
		        } else {
		            out.print("{\"success\": false, \"message\": \"삭제에 실패했습니다.\"}");
		        }

		    } catch (NumberFormatException e) {
		        out.print("{\"success\": false, \"message\": \"animalId 형식 오류\"}");
		    } catch (Exception e) {
		        e.printStackTrace();
		        out.print("{\"success\": false, \"message\": \"삭제 중 오류가 발생했습니다.\"}");
		    }
		}
		
		
		
		
	}

}
