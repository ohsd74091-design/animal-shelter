package kr.or.ddit.board.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import kr.or.ddit.board.vo.BoardCommentVO;
import kr.or.ddit.board.vo.BoardFileVO;
import kr.or.ddit.board.vo.BoardReportVO;
import kr.or.ddit.board.vo.BoardVO;
import kr.or.ddit.board.vo.CommentReportVO;

public interface IBoardDao {

    // ========================= 게시글 =========================

    /** 게시글 목록 조회 (페이징 + 필터) */
    List<BoardVO> selectBoardList(SqlSession session, Map<String, Object> paramMap);

    /** 게시글 전체 건수 (페이징용) */
    int selectBoardCount(SqlSession session, Map<String, Object> paramMap);

    /** 게시글 단건 조회 */
    BoardVO selectBoard(SqlSession session, int boardId);

    /** 조회수 증가 */
    int updateViewCount(SqlSession session, int boardId);

    /** 게시글 등록 */
    int insertBoard(SqlSession session, BoardVO boardVo);

    /** 게시글 수정 */
    int updateBoard(SqlSession session, BoardVO boardVo);

    /** 게시글 삭제 */
    int deleteBoard(SqlSession session, int boardId);

    /** 게시글 숨김 처리 (신고 5회 이상 자동) */
    int hideBoard(SqlSession session, int boardId);

    /** 조회순 인기글 */
    List<BoardVO> selectPopularBoardList(SqlSession session);

    /** 추천순 인기글 */
    List<BoardVO> selectPopularBoardListByLike(SqlSession session);

    // ========================= 추천 =========================

    /** 추천 여부 확인 (중복 방지) */
    int checkBoardLike(SqlSession session, Map<String, Object> paramMap);

    /** 추천 등록 */
    int insertBoardLike(SqlSession session, Map<String, Object> paramMap);

    /** 추천 취소 */
    int deleteBoardLike(SqlSession session, Map<String, Object> paramMap);

    /** 추천수 +1 */
    int updateLikeCount(SqlSession session, int boardId);

    /** 추천수 -1 */
    int decreaseLikeCount(SqlSession session, int boardId);

    // ========================= 댓글 =========================

    /** 댓글 목록 조회 */
    List<BoardCommentVO> selectCommentList(SqlSession session, int boardId);

    /** 댓글 등록 */
    int insertComment(SqlSession session, BoardCommentVO commentVo);

    /** 댓글 삭제 */
    int deleteComment(SqlSession session, int commentId);
    /** 댓글 신고 등록 */
    int insertCommentReport(SqlSession session, CommentReportVO reportVo);

    /** 댓글 중복 신고 여부 확인 */
    int checkCommentReport(SqlSession session, Map<String, Object> paramMap);

    /** 댓글 신고 횟수 조회 */
    int countCommentReport(SqlSession session, int commentId);
    
    /** 댓글 수정 */
    int updateComment(SqlSession session, BoardCommentVO commentVo);

    // ========================= 신고 =========================

    /** 중복 신고 여부 확인 */
    int checkBoardReport(SqlSession session, Map<String, Object> paramMap);

    /** 신고 등록 */
    int insertBoardReport(SqlSession session, BoardReportVO reportVo);

    /** 신고 횟수 조회 */
    int countBoardReport(SqlSession session, int boardId);

    // ========================= 회원/파일 =========================

    /** 회원 상태 조회 */
    String getMemberStatus(SqlSession session, String memberId);

    /** 첨부파일 등록 */
    int insertBoardFile(SqlSession session, BoardFileVO fileVo);

    /** 첨부파일 목록 조회 */
    List<BoardFileVO> selectFileList(SqlSession session, int boardId);

    /** 첨부파일 단건 조회 */
    BoardFileVO selectFile(SqlSession session, int fileId);
}