package kr.or.ddit.support.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/imageView.do")
public class ImageViewer extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. 파라미터로 파일명 받기
        String fileName = request.getParameter("fileName");
        
        // 2. 실제 파일이 저장된 경로 (본인의 설정에 맞게 수정하세요)
        String uploadPath = "d:/D_Other/upload_files"; 
        
        File file = new File(uploadPath, fileName);

        if (file.exists()) {
            // 3. 파일의 확장자를 보고 MimeType 결정 (jpg, png 등)
            String mimeType = getServletContext().getMimeType(file.getName());
            if (mimeType == null) mimeType = "application/octet-stream";
            response.setContentType(mimeType);

            // 4. 파일을 읽어서 브라우저로 내보내기 (Stream)
            try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
                 BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream())) {
                byte[] buffer = new byte[8192];
                int readBytes;
                while ((readBytes = bis.read(buffer)) != -1) {
                    bos.write(buffer, 0, readBytes);
                }
            }
        }
    }
}