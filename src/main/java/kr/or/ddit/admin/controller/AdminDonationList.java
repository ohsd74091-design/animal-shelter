package kr.or.ddit.admin.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

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

@WebServlet("/admin/donationList.do")
public class AdminDonationList extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private static final int PAGE_SIZE = 5; // 페이지당 표시 건수
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("application/json;charset=UTF-8");
		// ── 관리자 권한 체크 ──
		HttpSession session = req.getSession();
		MemberVO loginUser = (MemberVO) session.getAttribute("loginUser");
		if(loginUser == null || !"ADMIN".equals(loginUser.getRole())) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            resp.getWriter().print("{\"result\":\"fail\", \"message\":\"Access Denied\"}");
            return;
        }
		// ──검색 파라미터 수집 ───
		String keyword = req.getParameter("keyword");
		String method = req.getParameter("method");
		String paymentType = req.getParameter("paymentType");
		String startDate = req.getParameter("startDate");
		String endDate = req.getParameter("endDate");
		 
		int currentPage = 1;
        try {
            String pageParam = req.getParameter("page");
            if (pageParam != null && !pageParam.isEmpty()) {
                currentPage = Integer.parseInt(pageParam);
            }
        } catch (NumberFormatException e) {
            currentPage = 1;
        }

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
        try {
        	 pvo = donationService.selectAdminDonationData(pvo);
             
             // 데이터가 없을 경우 빈 리스트 세팅
             if(pvo.getDonationList() == null) {
                 pvo.setDonationList(new ArrayList<>());
             }

             // totalPage를 서버에서 직접 계산 (PageVO.getTotalPage()는 자동계산 안 됨)
             int totalPage = (pvo.getTotalCount() > 0 && PAGE_SIZE > 0)
                             ? (int) Math.ceil((double) pvo.getTotalCount() / PAGE_SIZE)
                             : 1;

             // JSON 응답 구성
             Map<String, Object> resultMap = new HashMap<>();
             resultMap.put("donationList", pvo.getDonationList()); // 현재 페이지 목록
             resultMap.put("currentPage",  pvo.getCurrentPage());  // 현재 페이지 번호
             resultMap.put("totalCount",   pvo.getTotalCount());   // 전체 건수
             resultMap.put("totalPage",    totalPage);              // 전체 페이지 수 (직접 계산)
             resultMap.put("pageSize",     PAGE_SIZE);              // 페이지당 건수

             // 6. JSON 출력
             Gson gson = new Gson();
             resp.getWriter().print(gson.toJson(resultMap));
             
         } catch (Exception e) {
             e.printStackTrace();
             resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
             resp.getWriter().print("{\"result\":\"error\", \"message\":\"Server Error\"}");
        }
	}
	
}
