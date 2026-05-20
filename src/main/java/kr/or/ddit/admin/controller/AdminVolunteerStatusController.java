package kr.or.ddit.admin.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.or.ddit.admin.service.AdminServiceImpl;
import kr.or.ddit.admin.service.IadminService;
import kr.or.ddit.member.vo.MemberVO;
import kr.or.ddit.notification.service.INotificationService;
import kr.or.ddit.notification.service.NotificationServiceImpl;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/admin/volunteer/status.do")
public class AdminVolunteerStatusController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private IadminService adminService = AdminServiceImpl.getinsetance();
    private INotificationService notificationService = NotificationServiceImpl.getInstance();

    // 승인 처리
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        MemberVO loginUser = (MemberVO) req.getSession().getAttribute("loginUser");
        if (loginUser == null || !"ADMIN".equals(loginUser.getRole())) {
            resp.sendRedirect(req.getContextPath() + "/main.do");
            return;
        }

        String volunteerIdStr = req.getParameter("volunteerId");
        String status = req.getParameter("status");

        if (volunteerIdStr == null || volunteerIdStr.isEmpty()
                || status == null || status.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/admin/volunteer/manage.do");
            return;
        }

        try {
            int volunteerId = Integer.parseInt(volunteerIdStr);

            Map<String, Object> param = new HashMap<>();
            param.put("volunteerId", volunteerId);
            param.put("status", status);
            param.put("rejectReason", null);

            int result = adminService.updateApplyStatus(param);

            if (result > 0) {
                notificationService.notifyVolunteerStatus(volunteerId, status);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        String referer = req.getHeader("referer");
        if (referer != null && !referer.isEmpty()) {
            resp.sendRedirect(referer);
        } else {
            resp.sendRedirect(req.getContextPath() + "/admin/volunteer/manage.do");
        }
    }

    // 반려 처리
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json; charset=UTF-8");

        PrintWriter out = resp.getWriter();

        MemberVO loginUser = (MemberVO) req.getSession().getAttribute("loginUser");
        if (loginUser == null || !"ADMIN".equals(loginUser.getRole())) {
            out.print("{\"success\": false, \"message\": \"권한이 없습니다.\"}");
            return;
        }

        String volunteerIdStr = req.getParameter("volunteerId");
        String status = req.getParameter("status");
        String rejectReason = req.getParameter("rejectReason");

        if (volunteerIdStr == null || status == null) {
            out.print("{\"success\": false, \"message\": \"파라미터 오류\"}");
            return;
        }

        try {
            int volunteerId = Integer.parseInt(volunteerIdStr);

            Map<String, Object> param = new HashMap<>();
            param.put("volunteerId", volunteerId);
            param.put("status", status);
            param.put("rejectReason", rejectReason);

            int result = adminService.updateApplyStatus(param);

            if (result > 0) {
                notificationService.notifyVolunteerStatus(volunteerId, status);
                out.print("{\"success\": true}");
            } else {
                out.print("{\"success\": false, \"message\": \"처리에 실패했습니다.\"}");
            }
        } catch (NumberFormatException e) {
            out.print("{\"success\": false, \"message\": \"volunteerId 형식 오류\"}");
        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"success\": false, \"message\": \"오류가 발생했습니다.\"}");
        }
    }
}