package kr.or.ddit.admin.search.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.or.ddit.admin.search.service.AdminSearchServiceImpl;
import kr.or.ddit.admin.search.service.IAdminSearchService;

@WebServlet("/admin/animal/jump.do")
public class AdminAnimalJumpController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private IAdminSearchService service = AdminSearchServiceImpl.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String animalIdStr = req.getParameter("animalId");

        if (animalIdStr == null || animalIdStr.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/admin/animal/list.do");
            return;
        }

        int animalId = Integer.parseInt(animalIdStr);

        int countPerPage = 9;

        int page = service.getAnimalPageByAnimalId(animalId, countPerPage);
        if (page < 1) {
            page = 1;
        }

        resp.sendRedirect(req.getContextPath()
                + "/admin/animal/list.do?page=" + page + "&focusId=" + animalId);
    }
}