package kr.or.ddit.admin.controller;

import java.io.IOException;
import java.io.PrintWriter;

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

@WebServlet("/admin/adoption/status.do")
public class AdminAdoptionStatusController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private IadminService adminService = AdminServiceImpl.getinsetance();
    private INotificationService notificationService = NotificationServiceImpl.getInstance();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json; charset=UTF-8");
        PrintWriter out = resp.getWriter();

        // 관리자 권한 체크
        MemberVO loginUser = (MemberVO) req.getSession().getAttribute("loginUser");
        if (loginUser == null || !"ADMIN".equals(loginUser.getRole())) {
            out.print("{\"success\":false,\"message\":\"권한이 없습니다.\"}");
            return;
        }

        try {
            int    adoptionId   = Integer.parseInt(req.getParameter("adoptionId"));
            String status       = req.getParameter("status");
            String rejectReason = req.getParameter("rejectReason");

            // 반려인데 사유가 없으면 오류 반환
            if ("반려".equals(status)
                    && (rejectReason == null || rejectReason.trim().isEmpty())) {
                out.print("{\"success\":false,\"message\":\"반려 사유를 입력해주세요.\"}");
                return;
            }

            boolean result = adminService.updateAdoptionStatus(adoptionId, status, rejectReason);

            if (result) {
                // ✅ 승인 시 동물 상태 '입양완료'로 변경
                if ("승인".equals(status)) {
                    int animalId = Integer.parseInt(req.getParameter("animalId"));
                    adminService.updateAnimalStatus(animalId, "입양완료");
                }

                // 알림 발송
                // notificationService.notifyAdoptionStatus(adoptionId, status);

                out.print("{\"success\":true}");
            } else {
                out.print("{\"success\":false,\"message\":\"처리에 실패했습니다.\"}");
            }

        } catch (NumberFormatException e) {
            out.print("{\"success\":false,\"message\":\"ID 형식 오류\"}");
        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"success\":false,\"message\":\"오류가 발생했습니다.\"}");
        }
    }
}
