package kr.or.ddit.support.controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.or.ddit.common.vo.PageVO;
import kr.or.ddit.member.vo.MemberVO;
import kr.or.ddit.support.service.ISupportService;
import kr.or.ddit.support.service.SupportServiceImpl;

import kr.or.ddit.support.vo.SupportVO;
import kr.or.ddit.util.PageUtil;

@WebServlet("/support/list.do")
public class SupportList extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		ISupportService service = SupportServiceImpl.getInstance();
		// 세션에서 로그인한 사용자 정보 가져오기
		HttpSession session = req.getSession();
		MemberVO loginUser = (MemberVO) session.getAttribute("loginUser"); 
		
		if(loginUser == null) {
			
			 // 현재 요청한 URL과 파라미터
            String uri = req.getRequestURI(); // 
            String query = req.getQueryString(); 
            String targetPath = (query == null) ? uri : uri + "?" + query;

            // 세션에 이 목적지 주소를 저장
            session.setAttribute("targetPath", targetPath);
			
			resp.sendRedirect(req.getContextPath() + "/login.do");
			return;
		}
		
		String memberId = loginUser.getMemberId();
		
		int currentPage = 1;
		String pageParam = req.getParameter("page");
		String status = req.getParameter("status");

		if(pageParam != null && !pageParam.isEmpty()) {
			currentPage = Integer.parseInt(pageParam);
		}
		String dbStatus = "all";
		if("대기".equals(status) || "N".equals(status)) {
		    dbStatus = "N";
		} else if("완료".equals(status) || "Y".equals(status)) {
		    dbStatus = "Y"; 
		}
		// 서비스에게 페이지 계산 요청 (calculatePageInfo)
	    // 이 안에서 전체 글 수(totalCount) 조회와 모든 수학적 계산이 일어남
		PageVO pageVO = service.calculatePageInfo(currentPage, memberId, dbStatus);
		// 계산된 범위를 바탕으로 실제 게시글 목록 가져오기
		List<SupportVO> sList = service.selectSupportListWithPaging(pageVO);
		
		String pager = PageUtil.pageList(
				pageVO.getStartPage(), pageVO.getEndPage(), pageVO.getTotalPage(), pageVO.getCurrentPage());
		
		req.setAttribute("sList", sList);
		req.setAttribute("pageVO", pageVO);
		req.setAttribute("pager", pager);
		
		req.getRequestDispatcher("/view/support_list.jsp").forward(req, resp);
		
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	
		doGet(req, resp);
	}
	
}
