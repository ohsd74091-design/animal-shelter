package kr.or.ddit.mypage.controller;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.or.ddit.member.vo.MemberVO;
import kr.or.ddit.mypage.service.IMypageService;
import kr.or.ddit.mypage.service.MypageServiceImpl;
import kr.or.ddit.animal.vo.AnimalVO;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MypageFavAnimalController.java
 * 관심동물(찜) 목록 페이지 컨트롤러
 *
 *  GET  /mypage/favorite.do           → 관심동물 목록 조회 & JSP 포워드
 *  POST /mypage/favorite/remove.do    → 찜 해제 AJAX (JSON 응답)
 */
@WebServlet(urlPatterns = { "/mypage/favorite.do", "/mypage/favorite/remove.do" })
public class MypageFavAnimalController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /** 한 페이지에 보여줄 카드 수 */
    private static final int PAGE_SIZE = 6;

    /** 페이지네이션 블록 크기 (1 2 3 4 5) */
    private static final int BLOCK_SIZE = 5;

    private final IMypageService mypageService = MypageServiceImpl.getInstance();
    private final Gson gson = new Gson();

    /* ====================================================
       GET - 관심동물 목록 조회
    ==================================================== */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 로그인 체크
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loginUser") == null) {
            response.sendRedirect(request.getContextPath() + "/member/login.do");
            return;
        }

        MemberVO loginUser = (MemberVO) session.getAttribute("loginUser");
        String memberId = loginUser.getMemberId();

        // 현재 페이지 파라미터 (기본값 1)
        int currentPage = 1;
        String pageParam = request.getParameter("page");
        if (pageParam != null && pageParam.matches("\\d+")) {
            currentPage = Math.max(1, Integer.parseInt(pageParam));
        }

        // 페이징 범위 계산
        int start = (currentPage - 1) * PAGE_SIZE + 1;
        int end   = currentPage * PAGE_SIZE;

        // 서비스 파라미터 구성
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("memberId", memberId);
        paramMap.put("start", start);
        paramMap.put("end", end);

        // 데이터 조회
        int totalCount        = mypageService.getFavoriteAnimalCount(memberId);
        List<AnimalVO> list   = mypageService.getFavoriteAnimalList(paramMap);

        // 페이지네이션 계산
        int totalPages = (int) Math.ceil((double) totalCount / PAGE_SIZE);
        totalPages = Math.max(totalPages, 1);               // 최소 1페이지
        currentPage = Math.min(currentPage, totalPages);    // 범위 초과 보정

        int startPage = ((currentPage - 1) / BLOCK_SIZE) * BLOCK_SIZE + 1;
        int endPage   = Math.min(startPage + BLOCK_SIZE - 1, totalPages);

        // 뷰에 전달
        request.setAttribute("favoriteList", list);
        request.setAttribute("totalCount",   totalCount);
        request.setAttribute("currentPage",  currentPage);
        request.setAttribute("totalPages",   totalPages);
        request.setAttribute("startPage",    startPage);
        request.setAttribute("endPage",      endPage);

        request.getRequestDispatcher("/WEB-INF/view/mypage/mypageFavAnimal.jsp")
               .forward(request, response);
    }

    /* ====================================================
       POST - 찜 해제 AJAX / 목록 폼 서브밋 공통 처리
    ==================================================== */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String uri = request.getRequestURI();

        // /mypage/favorite/remove.do → 찜 해제 AJAX
        if (uri.endsWith("/remove.do")) {
            handleRemoveFavorite(request, response);
            return;
        }

        // 그 외 POST → GET 재사용
        doGet(request, response);
    }

    /* ====================================================
       찜 해제 AJAX 처리
    ==================================================== */
    private void handleRemoveFavorite(HttpServletRequest request,
                                      HttpServletResponse response) throws IOException {

        response.setContentType("application/json;charset=UTF-8");

        // 로그인 체크
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loginUser") == null) {
            Map<String, String> fail = new HashMap<>();
            fail.put("result",  "fail");
            fail.put("message", "로그인이 필요합니다.");
            response.getWriter().write(gson.toJson(fail));
            return;
        }

        MemberVO loginUser = (MemberVO) session.getAttribute("loginUser");
        String memberId  = loginUser.getMemberId();
        String animalIdStr = request.getParameter("animalId");

        Map<String, Object> result = new HashMap<>();

        try {
            int animalId = Integer.parseInt(animalIdStr);

            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("memberId", memberId);
            paramMap.put("animalId", animalId);

            // 찜 해제 서비스 호출 (animal.xml의 deleteFavorite 사용)
            mypageService.removeFavoriteAnimal(paramMap);

            result.put("result",  "success");
            result.put("message", "관심 목록에서 해제되었습니다.");

        } catch (NumberFormatException e) {
            result.put("result",  "fail");
            result.put("message", "잘못된 동물 ID입니다.");
        } catch (Exception e) {
            result.put("result",  "fail");
            result.put("message", "처리 중 오류가 발생했습니다.");
        }

        response.getWriter().write(gson.toJson(result));
    }
}
