package kr.or.ddit.member.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * ProfileImageController.java
 * 프로필 이미지 서빙 서블릿
 * - D:/upload/profile 에 저장된 파일을 응답
 * - URL: /member/profileImage.do?fileName=UUID.jpg
 */
@WebServlet("/member/profileImage.do")
public class ProfileImageController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String UPLOAD_PATH = "D:/upload/profile";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

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

        String mimeType = getServletContext().getMimeType(file.getName());
        if (mimeType == null) {
            mimeType = "application/octet-stream";
        }

        resp.setContentType(mimeType);
        resp.setContentLengthLong(file.length());

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
