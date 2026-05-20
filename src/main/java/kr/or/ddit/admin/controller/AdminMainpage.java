package kr.or.ddit.admin.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.or.ddit.admin.service.AdminServiceImpl;
import kr.or.ddit.admin.service.IadminService;
import kr.or.ddit.animal.vo.AnimalVO;
import kr.or.ddit.member.vo.MemberVO;

@WebServlet("/admin/main.do")
public class AdminMainpage extends HttpServlet{
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	
		MemberVO loginUser =(MemberVO) req.getSession().getAttribute("loginUser");
		
		//로그인 안했으면 차단
		if(loginUser ==null) {
			resp.sendRedirect(req.getContextPath() +"/login.do");
			return;
		}
		
		//관리자 아닌경우 차단 
		if(!"ADMIN".equals(loginUser.getRole())) {
			resp.sendRedirect(req.getContextPath()+"/main.do");
			return;
		}
		
		IadminService service = AdminServiceImpl.getinsetance();

        // ── 1. 전체 보호 동물 통계 (ANIMAL 테이블) ──
        //       결과 Map 키: TOTAL_COUNT, DOG_COUNT, CAT_COUNT, WEEK_NEW_COUNT
        //                   AVAILABLE_COUNT, PROGRESS_COUNT, MONTH_DONE_COUNT, LAST_MONTH_DONE_COUNT
        Map<String, Object> animalStats = service.selectAnimalDashboardStats();
       
        // ── 2. 입양 통계 (ANIMAL 테이블에서 STATUS 별 카운트) ──
        //       animalStats 에 입양 관련 수치가 모두 포함되어 있으므로
        //       별도 adoptionStats 없이 animalStats 재활용
        //       (JSP에서 ${animalStats.AVAILABLE_COUNT} 등으로 접근)

        // ── 3. 이주의 일정 (ADOPTION + VOLUNTEER_APPLY, 이번 주 월~일) ──
        List<Map<String, Object>> scheduleList = service.selectWeekCombinedSchedule();

        // ── 4. 이번 달 전체 일정 (캘린더 모달용) ──
        //       selectMonthCombinedSchedule 쿼리 호출
        //       JSP에서 window.ALL_SCHEDULE_LIST 로 주입됨
        List<Map<String, Object>> allScheduleList = service.selectMonthCombinedSchedule();
        String ctp = req.getContextPath();
        for (Map<String, Object> s : allScheduleList) {
            String type    = String.valueOf(s.get("TYPE"));
            String id      = String.valueOf(s.get("ID"));
            String moveUrl = "#";

            switch (type) {
                case "ADOPTION":
                    // 입양 상세: adoptionId로 카드 자동 선택
                    moveUrl = ctp + "/admin/adoption/list.do?adoptionId=" + id;
                    break;
                case "VOLUNTEER":
                    // 봉사 신청 상세: id = "recruitId_volunteerId" 형식
                    if (id.contains("_")) {
                        String[] parts = id.split("_");
                        moveUrl = ctp + "/admin/volunteer/manage.do?recruitId=" + parts[0]
                                      + "&volunteerId=" + parts[1];
                    } else {
                        moveUrl = ctp + "/admin/volunteer/manage.do?volunteerId=" + id;
                    }
                    break;
                case "RECRUIT":
                    // 봉사 모집글 상세: recruitId로 이동
                    moveUrl = ctp + "/volunteer/detail.do?recruitId=" + id;
                    break;
                default:
                    moveUrl = "#";
                    break;
            }
            s.put("MOVE_URL", moveUrl); // Map에 MOVE_URL 키로 추가
        }
        List<Map<String, Object>> activityFeed = service.selectRecentActivityFeed();
       
        AnimalVO featuredAnimal = service.selectAnimalOfTheMonth();
        
        // ── 5. JSP로 데이터 전달 ──
        req.setAttribute("animalStats",    animalStats);
        req.setAttribute("adoptionStats",  animalStats);  // 재활용 (입양 현황 카드용)
        req.setAttribute("stats",          animalStats);  // 재활용 (성공 스토리 카드용)
        req.setAttribute("scheduleList",   scheduleList);
        req.setAttribute("allScheduleList", allScheduleList);
        req.setAttribute("activityFeed", activityFeed);
        req.setAttribute("featuredAnimal",  featuredAnimal);
        
        
        // 관리자 대시보드 이동
        req.getRequestDispatcher("/view/admin/dashboard.jsp").forward(req, resp);

		
	}

}
