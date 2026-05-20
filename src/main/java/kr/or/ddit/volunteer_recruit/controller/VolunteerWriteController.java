package kr.or.ddit.volunteer_recruit.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.UUID;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import kr.or.ddit.member.vo.MemberVO;
import kr.or.ddit.volunteer_recruit.service.IVolunteer_RecruitService;
import kr.or.ddit.volunteer_recruit.service.Volunteer_RecruitServiceImpl;
import kr.or.ddit.volunteer_recruit.vo.Volunteer_RecruitDetailVO;
import kr.or.ddit.volunteer_recruit.vo.Volunteer_RecruitVO;

@WebServlet("/volunteer/writer.do")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024,
    maxFileSize = 1024 * 1024 * 5,
    maxRequestSize = 1024 * 1024 * 20
)
public class VolunteerWriteController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        MemberVO loginUser = (MemberVO) req.getSession().getAttribute("loginUser");

        if (loginUser == null || !"ADMIN".equals(loginUser.getRole())) {
            resp.setContentType("text/html; charset=UTF-8");
            PrintWriter out = resp.getWriter();
            out.println("<script>");
            out.println("alert('관리자만 접근할 수 있습니다.');");
            out.println("location.href='" + req.getContextPath() + "/volunteer/list.do';");
            out.println("</script>");
            return;
        }

        req.getRequestDispatcher("/view/volunteer-admin.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        MemberVO loginUser = (MemberVO) req.getSession().getAttribute("loginUser");

        if (loginUser == null || !"ADMIN".equals(loginUser.getRole())) {
            resp.sendRedirect(req.getContextPath() + "/login.do");
            return;
        }

        String title = req.getParameter("title");
        String content = req.getParameter("content");
        String volunteerDateStr = req.getParameter("volunteerDate");
        String applyDeadlineStr = req.getParameter("applyDeadline");
        String location = req.getParameter("location");
        String cleaningCapacityStr = req.getParameter("cleaningCapacity");
        String walkingCapacityStr = req.getParameter("walkingCapacity");

        Date volunteerDate = Date.valueOf(volunteerDateStr);
        Date applyDeadline = Date.valueOf(applyDeadlineStr);
        int cleaningCapacity = Integer.parseInt(cleaningCapacityStr);
        int walkingCapacity = Integer.parseInt(walkingCapacityStr);

        Part thumbnailPart = req.getPart("thumbnailFile");
        String saveFileName = null;

        if (thumbnailPart != null && thumbnailPart.getSize() > 0) {
            String originalFileName = Paths.get(thumbnailPart.getSubmittedFileName()).getFileName().toString();

            String ext = "";
            int dotIdx = originalFileName.lastIndexOf(".");
            if (dotIdx != -1) {
                ext = originalFileName.substring(dotIdx);
            }

            saveFileName = UUID.randomUUID().toString().replace("-", "") + ext;

            String uploadPath = "D:/upload/volunteer";
            File dir = new File(uploadPath);

            if (!dir.exists()) {
                dir.mkdirs();
            }

            thumbnailPart.write(uploadPath + File.separator + saveFileName);
        }

        Volunteer_RecruitVO recruitVO = new Volunteer_RecruitVO();
        recruitVO.setTitle(title);
        recruitVO.setContent(content);
        recruitVO.setVolunteerDate(volunteerDate);
        recruitVO.setApplyDeadline(applyDeadline);
        recruitVO.setLocation(location);
        recruitVO.setStatus("모집중");
        recruitVO.setMemberId(loginUser.getMemberId());
        recruitVO.setThumbnailImg(saveFileName);

        Volunteer_RecruitDetailVO cleaningVO = new Volunteer_RecruitDetailVO();
        cleaningVO.setInterestType("청소");
        cleaningVO.setCapacity(cleaningCapacity);

        Volunteer_RecruitDetailVO walkingVO = new Volunteer_RecruitDetailVO();
        walkingVO.setInterestType("산책");
        walkingVO.setCapacity(walkingCapacity);

        IVolunteer_RecruitService service = Volunteer_RecruitServiceImpl.getservice();
        boolean result = service.insertVolunteerRecruit(recruitVO, cleaningVO, walkingVO);

        resp.setContentType("text/html; charset=UTF-8");
        PrintWriter out = resp.getWriter();
        out.println("<script>");

        if (result) {
            out.println("alert('봉사 모집글이 등록되었습니다.');");
            out.println("location.href='" + req.getContextPath() + "/volunteer/list.do';");
        } else {
            out.println("alert('봉사 모집글 등록에 실패했습니다.');");
            out.println("history.back();");
        }

        out.println("</script>");
    }
}