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
import kr.or.ddit.member.vo.MemberVO;
import kr.or.ddit.notification.service.INotificationService;
import kr.or.ddit.notification.service.NotificationServiceImpl;



@WebServlet({ "/board/comment.do", "/board/commentDelete.do", "/board/commentUpdate.do" })
public class BoardCommentController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private BoardService boardService = BoardServiceImpl.getInstance();
	private INotificationService notificationService = NotificationServiceImpl.getInstance();
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");

		String uri = req.getRequestURI();

		// ================= 삭제 =================
		if (uri.endsWith("/commentDelete.do")) {
			int commentId = Integer.parseInt(req.getParameter("commentId"));
			int boardId = Integer.parseInt(req.getParameter("boardId"));

			boardService.deleteComment(commentId);

			resp.sendRedirect(req.getContextPath() + "/board/detail.do?boardId=" + boardId);
			return;
		}

		// ================= 로그인 체크 =================
		MemberVO loginUser = (MemberVO) req.getSession().getAttribute("loginUser");
		if (loginUser == null) {
			if (uri.endsWith("/commentUpdate.do")) {
				resp.getWriter().write("login");
			} else {
				resp.sendRedirect(req.getContextPath() + "/login.do");
			}
			return;
		}

		String memberId = loginUser.getMemberId();

		// ================= 정지 체크 =================
		String status = boardService.getMemberStatus(memberId);
		if (!"Y".equals(status)) {
			if (uri.endsWith("/commentUpdate.do")) {
				resp.getWriter().write("blocked");
			} else {
				int boardId = Integer.parseInt(req.getParameter("boardId"));
				resp.sendRedirect(req.getContextPath() + "/board/detail.do?boardId=" + boardId);
			}
			return;
		}

		// ================= 수정 =================
		if (uri.endsWith("/commentUpdate.do")) {
			resp.setContentType("text/plain;charset=UTF-8");

			int commentId = Integer.parseInt(req.getParameter("commentId"));
			int boardId = Integer.parseInt(req.getParameter("boardId"));
			String content = req.getParameter("content");

			if (content == null || content.trim().isEmpty()) {
				resp.getWriter().write("empty");
				return;
			}

			// 작성자 검증
			List<BoardCommentVO> list = boardService.getCommentList(boardId);
			BoardCommentVO target = null;

			for (BoardCommentVO vo : list) {
				if (vo.getCommentId() == commentId) {
					target = vo;
					break;
				}
			}

			if (target == null) {
				resp.getWriter().write("not_found");
				return;
			}

			if (!memberId.equals(target.getMemberId()) && !"ADMIN".equals(loginUser.getRole())) {
				resp.getWriter().write("unauthorized");
				return;
			}

			BoardCommentVO vo = new BoardCommentVO();
			vo.setCommentId(commentId);
			vo.setContent(content.trim());

			int cnt = boardService.modifyComment(vo);

			resp.getWriter().write(cnt > 0 ? "success" : "fail");
			return;
		}

		// ================= 등록 =================
		// ================= 등록 =================
		int boardId = Integer.parseInt(req.getParameter("boardId"));
		String content = req.getParameter("content");
		String parentCommentIdStr = req.getParameter("parentCommentId");

		if (content == null || content.trim().isEmpty()) {
		    resp.sendRedirect(req.getContextPath() + "/board/detail.do?boardId=" + boardId);
		    return;
		}

		BoardCommentVO vo = new BoardCommentVO();
		vo.setBoardId(boardId);
		vo.setMemberId(memberId);
		vo.setContent(content.trim());

		// 부모 댓글 ID 세팅 (재댓글 처리)
		Integer parentCommentId = null;
		if (parentCommentIdStr != null && !parentCommentIdStr.trim().equals("")) {
		    parentCommentId = Integer.parseInt(parentCommentIdStr);
		    vo.setParentCommentId(parentCommentId);
		}

		int cnt = boardService.registComment(vo);

		if (cnt > 0) {
		    notificationService.notifyBoardComment(boardId, vo.getCommentId(), parentCommentId, memberId);
		}

		resp.sendRedirect(req.getContextPath() + "/board/detail.do?boardId=" + boardId);
	}
}