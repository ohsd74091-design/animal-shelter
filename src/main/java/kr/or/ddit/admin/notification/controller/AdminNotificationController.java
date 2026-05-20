package kr.or.ddit.admin.notification.controller;

import java.io.IOException;
import java.io.PrintWriter;

import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.or.ddit.admin.notification.service.AdminNotificationServiceImpl;
import kr.or.ddit.admin.notification.service.IAdminNotificationService;
import kr.or.ddit.admin.notification.vo.AdminNotificationResponseVO;

@WebServlet("/admin/notification/list.do")
public class AdminNotificationController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private IAdminNotificationService service = AdminNotificationServiceImpl.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        AdminNotificationResponseVO result = service.getNotificationList(req.getContextPath());

        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        out.print(new Gson().toJson(result));
        out.flush();
    }
}