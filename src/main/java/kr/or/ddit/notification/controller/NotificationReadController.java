package kr.or.ddit.notification.controller;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.or.ddit.member.vo.MemberVO;
import kr.or.ddit.notification.service.INotificationService;
import kr.or.ddit.notification.service.NotificationServiceImpl;

@WebServlet("/notification/read.do")
public class NotificationReadController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private INotificationService service = NotificationServiceImpl.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        MemberVO loginUser = (MemberVO) req.getSession().getAttribute("loginUser");
        if (loginUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login.do");
            return;
        }

        String notiIdStr = req.getParameter("notiId");
        String linkUrl = req.getParameter("linkUrl");

        if (notiIdStr != null && !notiIdStr.trim().isEmpty()) {
            int notiId = Integer.parseInt(notiIdStr);
            service.readNotification(notiId, loginUser.getMemberId());
        }

        if (linkUrl == null || linkUrl.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/mypage/main.do");
            return;
        }

        linkUrl = URLDecoder.decode(linkUrl, StandardCharsets.UTF_8);

        if (linkUrl.startsWith("http://") || linkUrl.startsWith("https://")) {
            resp.sendRedirect(linkUrl);
        } else {
            resp.sendRedirect(req.getContextPath() + linkUrl);
        }
    }
}