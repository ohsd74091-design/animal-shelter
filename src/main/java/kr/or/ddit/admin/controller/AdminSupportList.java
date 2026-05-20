package kr.or.ddit.admin.controller;

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
import kr.or.ddit.support.service.AdminSupportServiceImpl;
import kr.or.ddit.support.service.IAdminSupportService;

import kr.or.ddit.support.vo.SupportVO;
import kr.or.ddit.util.PageUtil;

@WebServlet("/admin/support/list.do")
public class AdminSupportList extends HttpServlet{
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		// 서비스 객체 가져오기 
		IAdminSupportService adminService = AdminSupportServiceImpl.getInstance();
		
		//  관리자 권한 체크 (세션 확인)
		HttpSession session = req.getSession();
		MemberVO loginUser = (MemberVO) session.getAttribute("loginUser");
		
		// 로그인이 안 되어 있거나, 관리자가 아닌 경우 처리 (예: admin이 관리자 아이디인 경우)
		
		if(loginUser == null || !"ADMIN".equals(loginUser.getRole())) {
			String uri = req.getRequestURI();
            String query = req.getQueryString();
            String targetPath = (query == null) ? uri : uri + "?" + query;
            session.setAttribute("targetPath", targetPath);

            resp.sendRedirect(req.getContextPath() + "/login.do");
            return;
		}
		// 1. 파라미터 받기 (현재 페이지, 상태, 유형)
		String pageStr = req.getParameter("page");
		int currentPage = (pageStr == null || pageStr.isEmpty()) ? 1 : Integer.parseInt(pageStr);
		String status = req.getParameter("status"); // Y, N 등
		String supportType = req.getParameter("supportType"); // 입양문의, 일반문의 등

		// 2. PageVO에 데이터 담기 (PageVO에 필드가 있다고 가정)
		PageVO pageVO = new PageVO();
		pageVO.setCurrentPage(currentPage);
		pageVO.setStatus(status);
		pageVO.setSupportType(supportType);

		// 3. 서비스 호출하여 페이징 정보 및 리스트 가져오기
		// (서비스의 calculateAdminPageInfo 메서드도 pageVO를 받도록 수정이 필요합니다!)
		PageVO pagingInfo = adminService.calculateAdminPageInfo(pageVO, currentPage);
		List<SupportVO> adminList = adminService.selectAdminSupportListWithPaging(pagingInfo);

		// 4. PageUtil을 이용해 하단 페이지 버튼 HTML 생성
		String pageHtml = PageUtil.pageList(pagingInfo.getStartPage(), pagingInfo.getEndPage(), pagingInfo.getTotalPage(), currentPage);

		// 5. JSP로 데이터 전달
		req.setAttribute("adminList", adminList);
		req.setAttribute("pageHtml", pageHtml); // 페이징 버튼
		req.setAttribute("pagingInfo", pagingInfo); 
		req.setAttribute("currentStatus", status); // 상태 유지용
		req.setAttribute("currentType", supportType); // 상태 유지용

		req.getRequestDispatcher("/view/admin/support_admin.jsp").forward(req, resp);
	}

}
