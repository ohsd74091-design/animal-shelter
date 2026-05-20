package kr.or.ddit.support.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import kr.or.ddit.member.vo.MemberVO;
import kr.or.ddit.support.service.ISupportService;
import kr.or.ddit.support.service.SupportServiceImpl;
import kr.or.ddit.support.vo.SupportVO;

@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2, // 2MB
    maxFileSize = 1024 * 1024 * 10,      // 10MB
    maxRequestSize = 1024 * 1024 * 50    // 50MB
)
@WebServlet("/insertSupport.do")
public class SupportInsert extends HttpServlet{
	
	public SupportInsert() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		// 한글 깨짐 방지 설정
		req.setCharacterEncoding("UTF-8");
		// 로그인 체크 세션에서 로그인한 사용자 정보를 가져옴
		HttpSession session = req.getSession();
		MemberVO loginUser = (MemberVO) session.getAttribute("loginUser");
		if(loginUser == null) {
			// 로그인 안 되어 있으면 메인페이지로 이동(리다이렉트)
			resp.sendRedirect(req.getContextPath() + "/view/mainpage.jsp");
			return;
		}
		/*
		if (loginUser == null) {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED); // 401
            return;
        }*/
		// 데이터 파싱 사용자가 입력한 제목, 내용 등을 변수에 담음
		String memberId = loginUser.getMemberId();
		String type = req.getParameter("supportType");
		String title = req.getParameter("title");
		String content = req.getParameter("content");
		
		// VO 객체 생성 및 세팅
		SupportVO svo = new SupportVO();
		svo.setMemberId(memberId);
		svo.setSupportType(type);
		svo.setTitle(title);
		svo.setContent(content);
		
		// 서비스 호출 (문의글 + 파일 리스트를 통째로 넘김)
		ISupportService service = SupportServiceImpl.getInstance();
		// 파일 포함된 Parts 리스트 전달
		List<Part> parts = req.getParts().stream().collect(Collectors.toList());
		int cnt = service.registerSupport(svo, parts);
		// JSON 형태로 응답
		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");

		PrintWriter out = resp.getWriter();

		if(cnt > 0) {
		    // 성공 시: 자바스크립트가 인식할 수 있게 JSON 문자열 출력
		    out.print("{\"success\": true, \"message\": \"문의가 등록되었습니다.\"}");
		} else {
		    // 실패 시
		    out.print("{\"success\": false, \"message\": \"등록에 실패했습니다.\"}");
		}
		out.flush();
		
	}

}
