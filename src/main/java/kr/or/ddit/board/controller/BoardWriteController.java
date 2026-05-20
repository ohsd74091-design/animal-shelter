package kr.or.ddit.board.controller;

import java.net.URLEncoder;
import java.io.IOException;
import java.io.File;
import java.util.Collection;
import java.util.UUID;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.Part;
import kr.or.ddit.board.service.BoardService;
import kr.or.ddit.board.service.BoardServiceImpl;
import kr.or.ddit.board.vo.BoardVO;
import kr.or.ddit.member.vo.MemberVO;
import kr.or.ddit.board.vo.BoardFileVO;

@WebServlet("/board/write.do")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 10, maxRequestSize = 1024 * 1024 * 50)
public class BoardWriteController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private BoardService boardService = BoardServiceImpl.getInstance();

	/** 글쓰기 화면 */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // ✅ 로그인 체크 → 비로그인이면 로그인 페이지로
        MemberVO loginUser = (MemberVO) req.getSession().getAttribute("loginUser");
        if (loginUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login.do");
            return;
        }

        req.getRequestDispatcher("/view/boardWrite.jsp").forward(req, resp);
    }

    /** 글 등록 처리 */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        // ✅ 세션에서 로그인 유저 가져오기
        MemberVO loginUser = (MemberVO) req.getSession().getAttribute("loginUser");

        // 비로그인 → 로그인 페이지로
        if (loginUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login.do");
            return;
        }

        // ✅ memberId를 세션에서 가져옴 (기존: 폼 파라미터로 받아서 보안 취약)
        String memberId  = loginUser.getMemberId();
        String userRole  = loginUser.getRole();
        String boardType = req.getParameter("boardType");
        String title     = req.getParameter("title");
        String content   = req.getParameter("content");

        // 일반회원은 공지 작성 불가
        if ("USER".equals(userRole) && "공지".equals(boardType)) {
            resp.sendRedirect(req.getContextPath() + "/board/write.do");
            return;
        }

        // 정지회원 글쓰기 제한 (STATUS = 'N'이면 정지)
        String status = boardService.getMemberStatus(memberId);
        if (!"Y".equals(status)) {
            resp.sendRedirect(req.getContextPath() + "/board/write.do");
            return;
        }

        // 빈값 체크
        if (title == null || title.trim().isEmpty()
                || content == null || content.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/board/write.do");
            return;
        }

        // ✅ BoardVO 구성 (memberId는 세션값 사용)
        BoardVO boardVo = new BoardVO();
        boardVo.setMemberId(memberId);
        boardVo.setBoardType(boardType);
        boardVo.setTitle(title);
        boardVo.setContent(content);

        int cnt = boardService.registBoard(boardVo);

        // 게시글 등록 성공 시 첨부파일 처리
        if (cnt > 0) {
            String uploadPath = req.getServletContext().getRealPath("/upload");
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) uploadDir.mkdirs();

            Collection<Part> parts = req.getParts();
            for (Part part : parts) {

                // 파일 파트만 처리 (name="uploadFile", 크기 > 0)
                if ("uploadFile".equals(part.getName()) && part.getSize() > 0) {

                    String originName = part.getSubmittedFileName();
                    String saveName   = UUID.randomUUID().toString() + "_" + originName;

                    // 확장자 추출
                    String fileExt = "";
                    if (originName != null && originName.contains(".")) {
                        fileExt = originName
                            .substring(originName.lastIndexOf(".") + 1)
                            .toLowerCase();
                    }

                    // 이미지 여부 판단
                    String isImage = "N";
                    if ("jpg".equals(fileExt) || "jpeg".equals(fileExt)
                            || "png".equals(fileExt) || "gif".equals(fileExt)
                            || "webp".equals(fileExt)) {
                        isImage = "Y";
                    }

                    part.write(uploadPath + File.separator + saveName);

                    // 파일 정보 DB 저장
                    BoardFileVO fileVo = new BoardFileVO();
                    fileVo.setBoardId(boardVo.getBoardId());
                    fileVo.setOriginFileName(originName);
                    fileVo.setSaveFileName(saveName);
                    fileVo.setFilePath(uploadPath);
                    fileVo.setFileSize(part.getSize());
                    fileVo.setFileExt(fileExt);
                    fileVo.setIsImage(isImage);

                    boardService.insertBoardFile(fileVo);
                }
            }
        }

        // 등록 완료 후 목록으로 이동
        resp.sendRedirect(req.getContextPath() + "/board/list.do?boardType="
                + URLEncoder.encode(boardType, "UTF-8"));
    }
}
