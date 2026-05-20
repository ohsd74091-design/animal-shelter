package kr.or.ddit.board.controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.or.ddit.board.service.BoardService;
import kr.or.ddit.board.service.BoardServiceImpl;
import kr.or.ddit.board.vo.BoardCommentVO;
import kr.or.ddit.board.vo.BoardFileVO;
import kr.or.ddit.board.vo.BoardVO;
import kr.or.ddit.member.vo.MemberVO;

@WebServlet("/board/detail.do")
public class BoardDetailController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private BoardService boardService = BoardServiceImpl.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

    	int boardId = Integer.parseInt(req.getParameter("boardId"));

        boardService.increaseViewCount(boardId);

        BoardVO boardVo                  = boardService.getBoard(boardId);
        List<BoardCommentVO> commentList = boardService.getCommentList(boardId);
        List<BoardFileVO> fileList       = boardService.getFileList(boardId);

        MemberVO loginUser = (MemberVO) req.getSession().getAttribute("loginUser");
        boolean isBlocked  = false;
        boolean isLiked    = false; // 현재 로그인 유저가 추천했는지 여부

        if (loginUser != null) {
            String status = boardService.getMemberStatus(loginUser.getMemberId());
            isBlocked = !"Y".equals(status);
            // ✅ 추천 여부 확인 → JSP에서 추천 버튼 상태 표시용
            isLiked = boardService.isLiked(boardId, loginUser.getMemberId());
        }

        req.setAttribute("boardVo",     boardVo);
        req.setAttribute("commentList", commentList);
        req.setAttribute("fileList",    fileList);
        req.setAttribute("isBlocked",   isBlocked);
        req.setAttribute("isLiked",     isLiked); // JSP로 전달

        req.getRequestDispatcher("/view/boardDetail.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        // ✅ 추천: 세션에서 memberId 가져오기
        MemberVO loginUser = (MemberVO) req.getSession().getAttribute("loginUser");
        if (loginUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login.do");
            return;
        }

        int boardId    = Integer.parseInt(req.getParameter("boardId"));
        String memberId = loginUser.getMemberId();

        // toggleBoardLike: 1=추천완료, -1=추천취소, 0=실패
        int result = boardService.toggleBoardLike(boardId, memberId);

        if (result == 1) {
            req.getSession().setAttribute("likeMsg", "추천했습니다.");
        } else if (result == -1) {
            req.getSession().setAttribute("likeMsg", "추천을 취소했습니다.");
        }

        resp.sendRedirect(req.getContextPath() + "/board/detail.do?boardId=" + boardId);
    }
}