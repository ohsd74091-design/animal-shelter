package kr.or.ddit.admin.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import kr.or.ddit.admin.service.AdminServiceImpl;
import kr.or.ddit.admin.service.IadminService;
import kr.or.ddit.member.vo.MemberVO;

@WebServlet("/admin/getMonthlySchedule.do")
public class AdminScheduleController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // ── 관리자 권한 체크 ──
        MemberVO loginUser = (MemberVO) req.getSession().getAttribute("loginUser");
        if (loginUser == null || !"ADMIN".equals(loginUser.getRole())) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            resp.getWriter().write("{\"error\":\"unauthorized\"}");
            return;
        }

        // ── year, month 파라미터 수신 (없으면 null → 서비스에서 SYSDATE 기준) ──
        String year  = req.getParameter("year");   // ex) "2026"
        String month = req.getParameter("month");  // ex) "4" (LPAD로 처리)

        IadminService service = AdminServiceImpl.getinsetance();

        List<Map<String, Object>> scheduleList;

        if (year != null && month != null) {
            // 특정 년/월 일정 조회
            scheduleList = service.selectMonthCombinedSchedule(year, month);
        } else {
            // 기본: 이번 달 조회
            scheduleList = service.selectMonthCombinedSchedule();
        }

        // ── JSON 응답 ──
        resp.setContentType("application/json; charset=UTF-8");
        Gson gson = new Gson();
        resp.getWriter().write(gson.toJson(scheduleList));
    }
}