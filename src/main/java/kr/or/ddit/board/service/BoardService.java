package kr.or.ddit.board.service;

import java.util.List;
import java.util.Map;

import kr.or.ddit.board.vo.BoardCommentVO;
import kr.or.ddit.board.vo.BoardFileVO;
import kr.or.ddit.board.vo.BoardReportVO;
import kr.or.ddit.board.vo.BoardVO;
import kr.or.ddit.board.vo.CommentReportVO;

public interface BoardService {

    // ========================= 게시글 =========================

    List<BoardVO> getBoardList(Map<String, Object> paramMap);

    int getBoardCount(Map<String, Object> paramMap);

    BoardVO getBoard(int boardId);

    int increaseViewCount(int boardId);

    int registBoard(BoardVO boardVo);

    int modifyBoard(BoardVO boardVo);

    int removeBoard(int boardId);

    int hideBoard(int boardId);

    List<BoardVO> getPopularBoardList();

    List<BoardVO> getPopularBoardListByLike();

    // ========================= 추천 =========================

    int toggleBoardLike(int boardId, String memberId);

    boolean isLiked(int boardId, String memberId);

    // ========================= 댓글 =========================

    List<BoardCommentVO> getCommentList(int boardId);

    int registComment(BoardCommentVO commentVo);

    int deleteComment(int commentId);

    int modifyComment(BoardCommentVO commentVo);
    // ========================= 신고 =========================

    int registBoardReport(BoardReportVO reportVo);

    int getBoardReportCount(int boardId);
    
    int registCommentReport(CommentReportVO reportVo);

    /** 댓글 신고 횟수 조회 */
    int getCommentReportCount(int commentId);

    // ========================= 회원/파일 =========================

    String getMemberStatus(String memberId);

    int insertBoardFile(BoardFileVO fileVo);

    List<BoardFileVO> getFileList(int boardId);

    BoardFileVO getFile(int fileId);
}