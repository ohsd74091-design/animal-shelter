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

@WebServlet("/support/deleteSupport.do")
public class SupportDelete extends HttpServlet{
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String id = req.getParameter("id");
		int sId = 0;
		try {
			sId = Integer.parseInt(id);
		}catch(NumberFormatException ex) {
			ex.printStackTrace();
		}
        ISupportService service = SupportServiceImpl.getInstance();
        
        // DB에서 해당 ID 삭제 실행
        int cnt = service.deleteSupport(sId);
        
        resp.setContentType("application/json; charset=utf-8");
        PrintWriter out = resp.getWriter();
        if(cnt > 0) out.print("{\"success\": true}");
        else out.print("{\"success\": false, \"message\": \"삭제 권한이 없거나 실패했습니다.\"}");
	}

}
