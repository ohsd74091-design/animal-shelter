package kr.or.ddit.member.controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.or.ddit.admin.service.AdminServiceImpl;
import kr.or.ddit.admin.service.IadminService;
import kr.or.ddit.animal.vo.AnimalVO;
import kr.or.ddit.board.vo.BoardVO;
import kr.or.ddit.volunteer_recruit.vo.Volunteer_RecruitVO;

@WebServlet("/main.do")
public class MainController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        IadminService service = AdminServiceImpl.getinsetance();

        // ── 히어로 통계 수치 ──────────────────────────
        req.setAttribute("totalAnimalCount",    service.selectAnimalCount());
        req.setAttribute("totalAdoptionCount",  service.selectAdoptionCountByStatus("승인"));
        req.setAttribute("totalVolunteerCount", service.selectVolunteerApprovedCount());

        // ── 이번주 인기 동물 (상위 8마리) ─────────────
        List<AnimalVO> popularAnimals = service.selectPopularAnimals(8);
        req.setAttribute("popularAnimals", popularAnimals);

        // ── 봉사활동 모집 중 (최신 3개) ───────────────
        List<Volunteer_RecruitVO> popularVolunteers = service.selectPopularVolunteers(3);
        req.setAttribute("popularVolunteers", popularVolunteers);

        // ── 이번주 인기글 (상위 4개) ─────────────────
        List<BoardVO> popularBoards = service.selectPopularBoards(4);
        req.setAttribute("popularBoards", popularBoards);

       
        req.getRequestDispatcher("/view/mainpage.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doGet(req, resp);
    }
}
