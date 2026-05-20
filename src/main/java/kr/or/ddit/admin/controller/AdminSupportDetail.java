package kr.or.ddit.admin.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.or.ddit.support.service.AdminSupportServiceImpl;
import kr.or.ddit.support.service.IAdminSupportService;
import kr.or.ddit.support.vo.SupportVO;

@WebServlet("/admin/support/detail.do")
public class AdminSupportDetail extends HttpServlet{

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 파라미터로 넘어온 문의글 번호 받기
		String sIdParam = req.getParameter("supportId");
        int supportId = Integer.parseInt(sIdParam);
		// 서비스 호출하여 상세 데이터 가져오기
        IAdminSupportService adminService = AdminSupportServiceImpl.getInstance();
        SupportVO supportVO = adminService.selectSupportDetail(supportId);
        
//        if(supportVO.getFileList() != null) {
//            System.out.println("첨부파일 개수: " + supportVO.getFileList().size());
//        } else {
//            System.out.println("첨부파일 리스트가 비어있습니다.");
//        }
        
        // JSP에서 쓸 수 있게 request에 담기
        req.setAttribute("support", supportVO);

        // 상세 페이지(JSP)로 이동
        req.getRequestDispatcher("/view/admin/support_detail.jsp").forward(req, resp);
    
	}
	
}
