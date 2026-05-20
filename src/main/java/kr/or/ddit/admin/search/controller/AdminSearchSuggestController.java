package kr.or.ddit.admin.search.controller;

import java.io.IOException;
import java.io.PrintWriter;

import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.or.ddit.admin.search.service.AdminSearchServiceImpl;
import kr.or.ddit.admin.search.service.IAdminSearchService;
import kr.or.ddit.admin.search.vo.AdminSearchResponseVO;

@WebServlet("/admin/search/suggest.do")
public class AdminSearchSuggestController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private IAdminSearchService service = AdminSearchServiceImpl.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String keyword = req.getParameter("keyword");
        if (keyword == null) keyword = "";
        keyword = keyword.trim();

        AdminSearchResponseVO result = service.getSearchSuggest(keyword, req.getContextPath());

        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        out.print(new Gson().toJson(result));
        out.flush();
    }
}