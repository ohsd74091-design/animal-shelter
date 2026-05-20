package kr.or.ddit.mypage.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.or.ddit.donation.service.DonationServiceImpl;
import kr.or.ddit.donation.service.IDonationService;
import kr.or.ddit.donation.vo.DonationVO;
import kr.or.ddit.member.vo.MemberVO;
import kr.or.ddit.mypage.service.IMypageService;
import kr.or.ddit.mypage.service.MypageServiceImpl;
import kr.or.ddit.mypage.vo.ActivityVO;
import kr.or.ddit.mypage.vo.MypageStatsVO;

import java.io.IOException;
import java.util.List;

@WebServlet("/mypage/main.do")
public class MypageViewController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private IMypageService mypageService = MypageServiceImpl.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        // 로그인 체크 - 비로그인 시 로그인 페이지로 리다이렉트
        if (session == null || session.getAttribute("loginUser") == null) {
            response.sendRedirect(request.getContextPath() + "/member/login.do");
            return;
        }

        MemberVO loginUser = (MemberVO) session.getAttribute("loginUser");
        String memberId = loginUser.getMemberId();

        // 통계 카드 데이터 조회 (관심동물 수 / 입양신청 수 / 작성글 수)
        MypageStatsVO mypageStats = mypageService.getMypageStats(memberId);

        // 최근 활동 내역 조회 (최근 5건)
        List<ActivityVO> recentActivities = mypageService.getRecentActivities(memberId);

        // JSP로 전달
        request.setAttribute("mypageStats", mypageStats);
        request.setAttribute("recentActivities", recentActivities);

        request.getRequestDispatcher("/WEB-INF/view/mypage/mypage.jsp")
               .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
