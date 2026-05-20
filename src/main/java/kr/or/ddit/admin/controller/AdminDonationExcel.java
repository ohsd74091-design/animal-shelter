package kr.or.ddit.admin.controller;

import java.io.IOException;
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
import kr.or.ddit.donation.vo.DonationVO;
import kr.or.ddit.member.vo.MemberVO;
/**
 * 관리자 엑셀 다운로드용 데이터 제공 (GET → JSON)
 * URL: /admin/donation/excel.do
 *
 * ※ 엑셀 생성은 프론트(SheetJS)에서 처리
 *    이 컨트롤러는 현재 필터 조건에 맞는 전체 데이터를
 *    JSON으로 내려주는 역할만 담당.
 *
 * 요청 파라미터 (선택):
 *   keyword, method, paymentType, startDate, endDate
 *
 * 응답 JSON:
 *   {
 *     "success": true,
 *     "data": [ { donationId, donatorName, donationAmount, ... }, ... ]
 *   }
 */
@WebServlet("/admin/donation/excel.do")
public class AdminDonationExcel extends HttpServlet{
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		// 응답 형식을 JSON으로 지정
		resp.setContentType("application/json; charset=UTF-8");
		
		//관리자 권한 체크
		HttpSession session = req.getSession();
		MemberVO loginUser = (MemberVO) session.getAttribute("loginUser");
		if(loginUser == null|| ! "ADMIN".equals(loginUser.getRole())) {
			resp.getWriter().print("{\"success\":false,\"message\":\"권한이 없습니다.\"}");
            return;
		}
		
		 //파라미터 수집 ─
		PageVO pvo = new PageVO();
		pvo.setKeyword(req.getParameter("keyword")); // 검색
		pvo.setMethod(req.getParameter("method")); // 결제수단
		pvo.setPaymentType(req.getParameter("paymentType"));  // 결제 방식
		pvo.setStartDate(req.getParameter("startDate"));  // 시작일
		pvo.setEndDate(req.getParameter("endDate"));  // 종료일
		
		IDonationService donationService = DonationServiceImpl.getInstance();
		List<DonationVO> list = donationService.selectAdminDonationListAll(pvo);
		// JSON 변환 및 응답
		try {
			/* * [ Gson 라이브러리 설명 ]
             * - 역할: Java 객체(List, Map, VO 등)를 JSON이라는 '문자열' 포맷으로 자동 변환해줌.
             * - toJson(): 자바 데이터를 {"key":"value"} 형태의 텍스트로 만들어주는 핵심 메서드.
             */
			Gson gson = new Gson();
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("success", true);
			result.put("data", list); // List<DonationVO>를 통째로 넣음
			// gson.toJson(result) 호출 시:
            // 1. Map이 JSON 객체 { } 로 변환됨
            // 2. "data" 안의 List는 JSON 배열 [ ] 로 변환됨
            // 3. List 안의 DonationVO들은 각각 JSON 객체 { } 로 변환됨 (필드명 -> 키값)
			resp.getWriter().print(gson.toJson(result));
		}catch(Exception ex) {
			ex.printStackTrace();
			resp.getWriter().print("{\"success\":false,\"message\":\"데이터 변환 중 오류 발생\"}");
		}
		
	}

}
