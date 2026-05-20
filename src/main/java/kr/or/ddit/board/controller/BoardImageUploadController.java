package kr.or.ddit.board.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import kr.or.ddit.member.vo.MemberVO;

@WebServlet("/board/imageUpload.do")
@MultipartConfig(
fileSizeThreshold = 1024 * 1024,        // 1MB
maxFileSize       = 1024 * 1024 * 10,   // 10MB
maxRequestSize    = 1024 * 1024 * 50    // 50MB
		)

public class BoardImageUploadController extends HttpServlet{
	 private static final String UPLOAD_PATH = "D:/upload/board";
@Override
protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	
	resp.setContentType("application/json; charset=UTF-8");
    PrintWriter out = resp.getWriter();

    // 로그인 체크
    MemberVO loginUser = (MemberVO) req.getSession().getAttribute("loginUser");
    if (loginUser == null) {
        out.print("{\"error\":{\"message\":\"로그인이 필요합니다.\"}}");
        return;
    }

    try {
        // CKEditor는 파트 이름을 "upload"로 전송
        Part filePart = req.getPart("upload");

        if (filePart == null || filePart.getSize() == 0) {
            out.print("{\"error\":{\"message\":\"파일이 없습니다.\"}}");
            return;
        }

        String originName = filePart.getSubmittedFileName();

        // 확장자 추출
        String ext = "";
        if (originName != null && originName.contains(".")) {
            ext = originName.substring(originName.lastIndexOf(".")).toLowerCase();
        }

        // 이미지 파일만 허용
        if (!ext.equals(".jpg") && !ext.equals(".jpeg")
                && !ext.equals(".png") && !ext.equals(".gif")
                && !ext.equals(".webp")) {
            out.print("{\"error\":{\"message\":\"이미지 파일만 업로드 가능합니다.\"}}");
            return;
        }

        String saveName = UUID.randomUUID().toString() + ext;

        // D:/upload/board/ 에 저장
        File uploadDir = new File(UPLOAD_PATH);
        if (!uploadDir.exists()) uploadDir.mkdirs();
        filePart.write(UPLOAD_PATH + File.separator + saveName);

        // BoardImageViewController가 읽어서 뿌려주는 URL로 응답
        // CKEditor5는 {"default": "이미지URL"} 형식을 요구
        String imageUrl = req.getContextPath() + "/board/image?fileName=" + saveName;
        out.print("{\"default\":\"" + imageUrl + "\"}");

    } catch (Exception e) {
        e.printStackTrace();
        out.print("{\"error\":{\"message\":\"업로드 중 오류가 발생했습니다.\"}}");
    }
}
}


