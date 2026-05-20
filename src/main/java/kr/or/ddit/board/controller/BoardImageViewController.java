package kr.or.ddit.board.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/board/image")
public class BoardImageViewController extends HttpServlet {
	private static final String UPLOAD_PATH = "D:/upload/board";
	
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		 String fileName = req.getParameter("fileName");

	        if (fileName == null || fileName.trim().isEmpty()) {
	            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
	            return;
	        }

	        File file = new File(UPLOAD_PATH, fileName);

	        if (!file.exists()) {
	            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
	            return;
	        }

	        // MIME 타입 자동 감지 (jpg → image/jpeg 등)
	        String mimeType = getServletContext().getMimeType(file.getName());
	        if (mimeType == null) mimeType = "application/octet-stream";

	        resp.setContentType(mimeType);
	        resp.setContentLengthLong(file.length());

	        // 파일 읽어서 응답으로 전송
	        try (FileInputStream fis = new FileInputStream(file);
	             OutputStream os = resp.getOutputStream()) {
	            byte[] buffer = new byte[8192];
	            int len;
	            while ((len = fis.read(buffer)) != -1) {
	                os.write(buffer, 0, len);
	            }
	        }
	}

}
