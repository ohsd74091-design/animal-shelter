package kr.or.ddit.member.controller;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/support/faq.do")
public class FAQcontroller extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public FAQcontroller() {
		super();
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		
	     req.setAttribute("title", "자주 묻는 질문 - 너와 나의 연결고리");
		
	     String viewPath = "/view/FAQ.jsp";
	     
	     RequestDispatcher dispatcher = req.getRequestDispatcher(viewPath);
	     dispatcher.forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		doGet(req, resp);
	}

	
	
	

}
