package kr.or.ddit.admin.controller;

import java.io.File;
import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/upload/*")
public class AdminSupportFile extends HttpServlet{
	private static final String UPLOAD_DIR = "d:/D_Other/upload_files";
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String fileName = req.getPathInfo().substring(1); // /upload/abc.jpg → abc.jpg
        File file = new File(UPLOAD_DIR + File.separator + fileName);

        if (!file.exists()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String mimeType = getServletContext().getMimeType(file.getName());
        if (mimeType == null) mimeType = "application/octet-stream";
        resp.setContentType(mimeType);

        java.nio.file.Files.copy(file.toPath(), resp.getOutputStream());
	}
	

}
