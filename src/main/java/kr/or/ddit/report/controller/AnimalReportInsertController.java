package kr.or.ddit.report.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.UUID;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import kr.or.ddit.member.vo.MemberVO;
import kr.or.ddit.report.service.IReportService;
import kr.or.ddit.report.service.ReportServiceImpl;
import kr.or.ddit.report.vo.AnimalReportVO;

@WebServlet("/report/animal.do")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024,
    maxFileSize = 1024 * 1024 * 10,
    maxRequestSize = 1024 * 1024 * 50
)
public class AnimalReportInsertController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private IReportService service = ReportServiceImpl.getservice();
    
    // 업로드 고정 경로
    private static final String UPLOAD_DIR = "D:/upload/report";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json; charset=UTF-8");
        PrintWriter out = resp.getWriter();

        HttpSession session = req.getSession();
        MemberVO loginUser = (MemberVO) session.getAttribute("loginUser");

        String memberId = null;
        if (loginUser != null) {
            memberId = loginUser.getMemberId();
        }

        String animalType = req.getParameter("animalType");
        String location = req.getParameter("location");
        String status = req.getParameter("status");
        String content = req.getParameter("content");
        String replyEmail = req.getParameter("replyEmail");

        if (animalType == null || animalType.trim().isEmpty()) {
            out.print("{\"success\":false,\"message\":\"동물 종류를 선택해주세요.\"}");
            out.flush();
            return;
        }

        if (location == null || location.trim().isEmpty()) {
            out.print("{\"success\":false,\"message\":\"발견 위치를 입력해주세요.\"}");
            out.flush();
            return;
        }

        if (status == null || status.trim().isEmpty()) {
            out.print("{\"success\":false,\"message\":\"동물 상태를 선택해주세요.\"}");
            out.flush();
            return;
        }

        if (replyEmail == null || replyEmail.trim().isEmpty()) {
            out.print("{\"success\":false,\"message\":\"처리 결과를 받을 이메일을 입력해주세요.\"}");
            out.flush();
            return;
        }

        String imageName = null;
        Part filePart = req.getPart("imageFile");

        if (filePart != null && filePart.getSize() > 0) {
            String originalFileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();

            String ext = "";
            int dotIndex = originalFileName.lastIndexOf(".");
            if (dotIndex > -1) {
                ext = originalFileName.substring(dotIndex);
            }

            String saveFileName = UUID.randomUUID().toString().replace("-", "") + ext;

            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            File saveFile = new File(uploadDir, saveFileName);
            filePart.write(saveFile.getAbsolutePath());

            imageName = saveFileName;
        }

        AnimalReportVO vo = new AnimalReportVO();
        vo.setMemberId(memberId);
        vo.setAnimalType(animalType);
        vo.setLocation(location);
        vo.setStatus(status);
        vo.setImageName(imageName);
        vo.setContent(content);
        vo.setReplyEmail(replyEmail);

        int cnt = service.insertAnimalReport(vo);

        if (cnt > 0) {
            out.print("{\"success\":true}");
        } else {
            out.print("{\"success\":false,\"message\":\"유기동물 제보 접수에 실패했습니다.\"}");
        }

        out.flush();
    }
}