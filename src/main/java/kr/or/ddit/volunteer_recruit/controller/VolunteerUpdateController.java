package kr.or.ddit.volunteer_recruit.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.List;
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

@WebServlet("/volunteer/update.do")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024,
    maxFileSize = 1024 * 1024 * 5,
    maxRequestSize = 1024 * 1024 * 20
)
public class VolunteerUpdateController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        MemberVO loginUser = (MemberVO) req.getSession().getAttribute("loginUser");

        if (loginUser == null || !"ADMIN".equals(loginUser.getRole())) {
            resp.sendRedirect(req.getContextPath() + "/volunteer/list.do");
            return;
        }

        int recruitId = Integer.parseInt(req.getParameter("recruitId"));

        IVolunteer_RecruitService service = Volunteer_RecruitServiceImpl.getservice();

        Volunteer_RecruitVO recruit = service.getVolunteerRecruitDetail(recruitId);
        List<Volunteer_RecruitDetailVO> detailList = service.getRecruitDetailList(recruitId);

        req.setAttribute("recruit", recruit);
        req.setAttribute("detailList", detailList);

        req.getRequestDispatcher("/view/volunteer-update.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        MemberVO loginUser = (MemberVO) req.getSession().getAttribute("loginUser");

        if (loginUser == null || !"ADMIN".equals(loginUser.getRole())) {
            resp.sendRedirect(req.getContextPath() + "/volunteer/list.do");
            return;
        }

        int recruitId = Integer.parseInt(req.getParameter("recruitId"));
        String title = req.getParameter("title");
        String content = req.getParameter("content");
        String location = req.getParameter("location");
        Date volunteerDate = Date.valueOf(req.getParameter("volunteerDate"));
        Date applyDeadline = Date.valueOf(req.getParameter("applyDeadline"));
        int cleaningCapacity = Integer.parseInt(req.getParameter("cleaningCapacity"));
        int walkingCapacity = Integer.parseInt(req.getParameter("walkingCapacity"));
        String oldThumbnailImg = req.getParameter("oldThumbnailImg");

        Part thumbnailPart = req.getPart("thumbnailFile");
        String saveFileName = oldThumbnailImg;

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

        if (saveFileName == null || saveFileName.trim().isEmpty()) {
            saveFileName = oldThumbnailImg;
        }

        Volunteer_RecruitVO recruitVO = new Volunteer_RecruitVO();
        recruitVO.setRecruitId(recruitId);
        recruitVO.setTitle(title);
        recruitVO.setContent(content);
        recruitVO.setLocation(location);
        recruitVO.setVolunteerDate(volunteerDate);
        recruitVO.setApplyDeadline(applyDeadline);
        recruitVO.setThumbnailImg(saveFileName);

        Volunteer_RecruitDetailVO cleaningVO = new Volunteer_RecruitDetailVO();
        cleaningVO.setRecruitId(recruitId);
        cleaningVO.setInterestType("청소");
        cleaningVO.setCapacity(cleaningCapacity);

        Volunteer_RecruitDetailVO walkingVO = new Volunteer_RecruitDetailVO();
        walkingVO.setRecruitId(recruitId);
        walkingVO.setInterestType("산책");
        walkingVO.setCapacity(walkingCapacity);

        IVolunteer_RecruitService service = Volunteer_RecruitServiceImpl.getservice();
        boolean result = service.updateVolunteerRecruit(recruitVO, cleaningVO, walkingVO);

        resp.setContentType("text/html; charset=UTF-8");
        PrintWriter out = resp.getWriter();
        out.println("<script>");

        if (result) {
            out.println("alert('봉사 모집글이 수정되었습니다.');");
            out.println("location.href='" + req.getContextPath() + "/volunteer/detail.do?recruitId=" + recruitId + "';");
        } else {
            out.println("alert('봉사 모집글 수정에 실패했습니다.');");
            out.println("history.back();");
        }

        out.println("</script>");
    }
}