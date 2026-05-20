package kr.or.ddit.admin.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.or.ddit.common.vo.PageVO;
import kr.or.ddit.donation.service.DonationServiceImpl;
import kr.or.ddit.donation.service.IDonationService;
import kr.or.ddit.donation.vo.AESUtil;
import kr.or.ddit.donation.vo.DonationVO;
import kr.or.ddit.member.vo.MemberVO;
import kr.or.ddit.util.PageUtil;

/**
 * 관리자 기부 관리 현황 페이지 — 최초 진입 (GET)
 * URL: /admin/donation.do
 *
 * 서버에서 JSP로 넘기는 데이터:
 *   - donationList  : List<DonationVO>  현재 페이지 목록
 *   - totalCount    : int               전체 건수 (페이징)
 *   - stats         : Map               stat 카드 4개 (totalAmount 등)
 *   - topDonorList  : List<Map>         이달의 TOP 5
 *   - receiptList   : List<DonationVO>  영수증 신청 목록
 *   - currentPage   : int               현재 페이지 번호
 *   - pageSize      : int               페이지당 건수
 *   - param         : Map               현재 검색 조건 (화면 유지용)
 */

@WebServlet("/admin/donation.do")
public class AdminDonationForm extends HttpServlet{
	
	private static final int PAGE_SIZE = 5; // 페이지당 표시 건수
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		req.setCharacterEncoding("UTF-8");
		// ── 관리자 권한 체크 ──
		HttpSession session = req.getSession();
		MemberVO loginUser = (MemberVO) session.getAttribute("loginUser");
		
		if(loginUser == null) {
			resp.sendRedirect(req.getContextPath() + "/login.do");
			return;
		}
		
		if(!"ADMIN".equals(loginUser.getRole())) {
			resp.sendError(403, "접근 권한이 업습니다.");
			return;
		}
		// ──검색 파라미터 수집 ───
		String keyword = req.getParameter("keyword");
		String method = req.getParameter("method");
		String paymentType = req.getParameter("paymentType");
		String startDate = req.getParameter("startDate");
		String endDate = req.getParameter("endDate");
		 // 페이지 번호 (기본 1)
        int currentPage = 1;
        try {
            String pageParam = req.getParameter("page");
            if (pageParam != null) currentPage = Integer.parseInt(pageParam);
        } catch (NumberFormatException e) { /* 기본값 유지 */ }
        if (currentPage < 1) currentPage = 1;

        PageVO pvo = new PageVO();
        pvo.setKeyword(keyword);
        pvo.setMethod(method);
        pvo.setPaymentType(paymentType);
        pvo.setStartDate(startDate);
        pvo.setEndDate(endDate);
        pvo.setCurrentPage(currentPage);
        pvo.setCountPerPage(PAGE_SIZE);
      
        // 서비스 호출 
        IDonationService donationService = DonationServiceImpl.getInstance();
        pvo = donationService.selectAdminDonationData(pvo);
        //(추가 데이터 화면에 필요한 다른 통계 정보들도 가져옴
        Map<String, Object> stats = donationService.selectDonationStats();
        List<PageVO> topDonorList = donationService.selectTopDonorList();
        List<DonationVO> receiptList = donationService.selectReceiptList();
        
        for (DonationVO vo : receiptList) {
            try {
                // regNo1 복호화해서 다시 세팅
                String encryptedNo = vo.getRegNo1();
                String decryptedNo = AESUtil.decrypt(encryptedNo); 
                vo.setRegNo1(decryptedNo);
            } catch (Exception ex) {
                ex.printStackTrace();
                System.err.println("복호화 실패 (ID: " + vo.getDonationId() + ") : " + ex.getMessage());
            }
        }
        
     // pvo에 필요한 정보가 다 계산되어 있다고 가정할 때 (PageVO 내부 로직 확인 필요)
        int totalPage = (int) Math.ceil((double) pvo.getTotalCount() / PAGE_SIZE);
        int startPage = ((currentPage - 1) / 5) * 5 + 1; // 5개씩 보여줄 때
        int endPage = startPage + 2;
        if (endPage > totalPage) endPage = totalPage;

        // ★ PageUtil을 사용하여 페이징 HTML 생성
		/*
		 * String pageBar = PageUtil.pageList(startPage, endPage, totalPage,
		 * currentPage); req.setAttribute("pageBar", pageBar);
		 */
        
     // [확인 지점 1] 페이징 및 목록 데이터 확인
//        System.out.println("=== [DEBUG] 기부 목록 정보 ===");
//        System.out.println("현재 페이지: " + pvo.getCurrentPage());
//        System.out.println("전체 건수: " + pvo.getTotalCount());
//        if(pvo.getDonationList() != null) {
//            System.out.println("가져온 목록 개수: " + pvo.getDonationList().size());
//        }

     // [확인 지점 2] 통계 데이터 확인 (중요!)
//        System.out.println("=== [DEBUG] 통계 데이터 ===");
//        System.out.println("stats 데이터: " + stats); 
        // stats가 Map이므로 {totalAmount=50000, ...} 형태로 출력됩니다.

     // [확인 지점 3] 우수 기부자 및 영수증 목록
//        System.out.println("TOP 5 리스트 크기: " + (topDonorList != null ? topDonorList.size() : "null"));
//        System.out.println("영수증 리스트 크기: " + (receiptList != null ? receiptList.size() : "null"));
        
        // 데이터 전달
        req.setAttribute("pvo", pvo);
        req.setAttribute("stats", stats);
        req.setAttribute("topDonorList", topDonorList);
        req.setAttribute("receiptList", receiptList);
        req.setAttribute("currentPage", currentPage);
        req.setAttribute("totalCount", pvo.getTotalCount());
        req.setAttribute("pageSize", PAGE_SIZE);

        req.getRequestDispatcher("/view/admin/donationAdmin.jsp").forward(req, resp);
	}

}
