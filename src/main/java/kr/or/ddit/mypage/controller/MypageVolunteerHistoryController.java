package kr.or.ddit.mypage.controller;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.or.ddit.member.vo.MemberVO;
import kr.or.ddit.mypage.service.IMypageService;
import kr.or.ddit.mypage.service.MypageServiceImpl;
import kr.or.ddit.mypage.vo.VolunteerHistoryVO;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MypageVolunteerHistoryController.java
 * 봉사 신청 내역 페이지 컨트롤러
 *
 *  GET  /mypage/volunteerHistory.do              → 목록 & 통계 조회 & JSP 포워드
 *  GET  /mypage/volunteerHistory/calendar.do     → 달력 이벤트 AJAX (JSON)
 *  POST /mypage/volunteerHistory/cancel.do       → 봉사 신청 취소 AJAX (JSON)
 */
@WebServlet(urlPatterns = {
    "/mypage/volunteerHistory.do",
    "/mypage/volunteerHistory/calendar.do",
    "/mypage/volunteerHistory/cancel.do"
})
public class MypageVolunteerHistoryController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final int PAGE_SIZE  = 5;
    private static final int BLOCK_SIZE = 5;

    private final IMypageService mypageService = MypageServiceImpl.getInstance();
    private final Gson gson = new Gson();

    /* ====================================================
       GET 라우팅
    ==================================================== */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String uri = request.getRequestURI();

        if (uri.endsWith("/calendar.do")) {
            handleCalendar(request, response);
            return;
        }

        handleList(request, response);
    }

    /* ====================================================
       POST 라우팅
    ==================================================== */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String uri = request.getRequestURI();

        if (uri.endsWith("/cancel.do")) {
            handleCancel(request, response);
            return;
        }

        doGet(request, response);
    }

    /* ====================================================
       목록 + 통계 조회
    ==================================================== */
    private void handleList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loginUser") == null) {
            response.sendRedirect(request.getContextPath() + "/member/login.do");
            return;
        }

        MemberVO loginUser = (MemberVO) session.getAttribute("loginUser");
        String memberId = loginUser.getMemberId();

        // 현재 페이지
        int currentPage = 1;
        String pageParam = request.getParameter("page");
        if (pageParam != null && pageParam.matches("\\d+")) {
            currentPage = Math.max(1, Integer.parseInt(pageParam));
        }

        int start = (currentPage - 1) * PAGE_SIZE + 1;
        int end   = currentPage * PAGE_SIZE;

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("memberId", memberId);
        paramMap.put("start",    start);
        paramMap.put("end",      end);

        // 조회
        int volunteerStats              = mypageService.getVolunteerStats(memberId);
        int totalCount                  = mypageService.getVolunteerHistoryCount(memberId);
        List<VolunteerHistoryVO> list   = mypageService.getVolunteerHistoryList(paramMap);

        // 페이지네이션
        int totalPages  = Math.max(1, (int) Math.ceil((double) totalCount / PAGE_SIZE));
        currentPage     = Math.min(currentPage, totalPages);
        int startPage   = ((currentPage - 1) / BLOCK_SIZE) * BLOCK_SIZE + 1;
        int endPage     = Math.min(startPage + BLOCK_SIZE - 1, totalPages);

        // 오늘 날짜 (봉사완료 판단용)
        java.sql.Date today = new java.sql.Date(System.currentTimeMillis());

        request.setAttribute("volunteerStats", volunteerStats);
        request.setAttribute("volunteerList",  list);
        request.setAttribute("totalCount",     totalCount);
        request.setAttribute("currentPage",    currentPage);
        request.setAttribute("totalPages",     totalPages);
        request.setAttribute("startPage",      startPage);
        request.setAttribute("endPage",        endPage);
        request.setAttribute("today",          today);

        request.getRequestDispatcher("/WEB-INF/view/mypage/mypageVolunteerHistory.jsp")
               .forward(request, response);
    }

    /* ====================================================
       달력 이벤트 AJAX
       - 해당 월의 봉사 일정 반환
    ==================================================== */
    private void handleCalendar(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("application/json;charset=UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loginUser") == null) {
            response.getWriter().write("[]");
            return;
        }

        MemberVO loginUser = (MemberVO) session.getAttribute("loginUser");
        String memberId = loginUser.getMemberId();

        int year  = Integer.parseInt(request.getParameter("year"));
        int month = Integer.parseInt(request.getParameter("month")); // 1-based

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("memberId", memberId);
        paramMap.put("year",     year);
        paramMap.put("month",    month);

        List<Map<String, Object>> events = mypageService.getVolunteerCalendarEvents(paramMap);
        response.getWriter().write(gson.toJson(events));
    }

    /* ====================================================
       봉사 신청 취소 AJAX
    ==================================================== */
    private void handleCancel(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("application/json;charset=UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loginUser") == null) {
            Map<String, String> fail = new HashMap<>();
            fail.put("result",  "fail");
            fail.put("message", "로그인이 필요합니다.");
            response.getWriter().write(gson.toJson(fail));
            return;
        }

        MemberVO loginUser     = (MemberVO) session.getAttribute("loginUser");
        String memberId        = loginUser.getMemberId();
        String volunteerIdStr  = request.getParameter("volunteerId");

        Map<String, Object> result = new HashMap<>();

        try {
            int volunteerId = Integer.parseInt(volunteerIdStr);

            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("memberId",    memberId);
            paramMap.put("volunteerId", volunteerId);

            mypageService.cancelVolunteer(paramMap);

            result.put("result",  "success");
            result.put("message", "봉사 신청이 취소되었습니다.");

        } catch (NumberFormatException e) {
            result.put("result",  "fail");
            result.put("message", "잘못된 신청 ID입니다.");
        } catch (Exception e) {
            result.put("result",  "fail");
            result.put("message", "처리 중 오류가 발생했습니다.");
        }

        response.getWriter().write(gson.toJson(result));
    }
}
