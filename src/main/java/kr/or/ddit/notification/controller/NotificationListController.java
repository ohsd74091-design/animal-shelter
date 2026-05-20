package kr.or.ddit.notification.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.or.ddit.member.vo.MemberVO;
import kr.or.ddit.notification.service.INotificationService;
import kr.or.ddit.notification.service.NotificationServiceImpl;
import kr.or.ddit.notification.vo.NotificationVO;

@WebServlet("/notification/list.do")
public class NotificationListController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private INotificationService service = NotificationServiceImpl.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json; charset=UTF-8");
        PrintWriter out = resp.getWriter();

        MemberVO loginUser = (MemberVO) req.getSession().getAttribute("loginUser");
        if (loginUser == null) {
            out.print("{\"success\":false,\"message\":\"login required\"}");
            return;
        }

        List<NotificationVO> list = service.getNotificationList(loginUser.getMemberId());
        int unreadCount = service.getUnreadCount(loginUser.getMemberId());

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("unreadCount", unreadCount);
        result.put("items", list);

        out.print(new Gson().toJson(result));
        out.flush();
    }
}