package kr.or.ddit.support.controller;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.or.ddit.support.service.ISupportService;
import kr.or.ddit.support.service.SupportServiceImpl;
import kr.or.ddit.support.vo.SupportVO;

@WebServlet("/support/updateSupport.do")
public class SupportUpdate extends HttpServlet{
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		
		String sId = req.getParameter("supportId");
		String type = req.getParameter("supportType");
        String title = req.getParameter("title");
        String content = req.getParameter("content");
		/* System.out.println("sid = " + sId); */
        if(sId == null || sId.trim().isEmpty()) {
            // ID가 안 넘어왔을 때 서버가 안 죽게 처리
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print("{\"success\": false, \"message\": \"ID값이 누락되었습니다.\"}");
            return; // 여기서 멈춤
        }
        
        SupportVO svo = new SupportVO();
        svo.setSupportId(Integer.parseInt(sId));
        svo.setSupportType(type);
        svo.setTitle(title);
        svo.setContent(content);
        
       
        ISupportService service = SupportServiceImpl.getInstance();
        
        int cnt = service.updateSupport(svo, null);
        
        resp.setContentType("application/json; charset=utf-8");
        PrintWriter out = resp.getWriter();
        
        if(cnt > 0) {
            out.print("{\"success\": true, \"message\": \"수정이 완료되었습니다.\"}");
        } else {
            out.print("{\"success\": false, \"message\": \"수정에 실패했습니다.\"}");
        }
	}

}
