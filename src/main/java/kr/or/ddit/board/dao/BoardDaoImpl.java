package kr.or.ddit.board.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import kr.or.ddit.board.vo.BoardCommentVO;
import kr.or.ddit.board.vo.BoardFileVO;
import kr.or.ddit.board.vo.BoardReportVO;
import kr.or.ddit.board.vo.BoardVO;
import kr.or.ddit.board.vo.CommentReportVO;

public class BoardDaoImpl implements IBoardDao {

    private static IBoardDao dao;

    private BoardDaoImpl() {
    }

    public static IBoardDao getInstance() {
        if (dao == null) {
            dao = new BoardDaoImpl();
        }
        return dao;
    }

    // ========================= 게시글 =========================

    @Override
    public List<BoardVO> selectBoardList(SqlSession session, Map<String, Object> paramMap) {
        return session.selectList("board.selectBoardList", paramMap);
    }

    @Override
    public int selectBoardCount(SqlSession session, Map<String, Object> paramMap) {
        return session.selectOne("board.selectBoardCount", paramMap);
    }

    @Override
    public BoardVO selectBoard(SqlSession session, int boardId) {
        return session.selectOne("board.selectBoard", boardId);
    }

    @Override
    public int updateViewCount(SqlSession session, int boardId) {
        return session.update("board.updateViewCount", boardId);
    }

    @Override
    public int insertBoard(SqlSession session, BoardVO boardVo) {
        return session.insert("board.insertBoard", boardVo);
    }

    @Override
    public int updateBoard(SqlSession session, BoardVO boardVo) {
        return session.update("board.updateBoard", boardVo);
    }

    @Override
    public int deleteBoard(SqlSession session, int boardId) {
        return session.delete("board.deleteBoard", boardId);
    }

    @Override
    public int hideBoard(SqlSession session, int boardId) {
        return session.update("board.hideBoard", boardId);
    }

    @Override
    public List<BoardVO> selectPopularBoardList(SqlSession session) {
        return session.selectList("board.selectPopularBoardList");
    }

    @Override
    public List<BoardVO> selectPopularBoardListByLike(SqlSession session) {
        return session.selectList("board.selectPopularBoardListByLike");
    }

    // ========================= 추천 =========================

    @Override
    public int checkBoardLike(SqlSession session, Map<String, Object> paramMap) {
        return session.selectOne("board.checkBoardLike", paramMap);
    }

    @Override
    public int insertBoardLike(SqlSession session, Map<String, Object> paramMap) {
        return session.insert("board.insertBoardLike", paramMap);
    }

    @Override
    public int deleteBoardLike(SqlSession session, Map<String, Object> paramMap) {
        return session.delete("board.deleteBoardLike", paramMap);
    }

    @Override
    public int updateLikeCount(SqlSession session, int boardId) {
        return session.update("board.updateLikeCount", boardId);
    }

    @Override
    public int decreaseLikeCount(SqlSession session, int boardId) {
        return session.update("board.decreaseLikeCount", boardId);
    }

    // ========================= 댓글 =========================

    @Override
    public List<BoardCommentVO> selectCommentList(SqlSession session, int boardId) {
        return session.selectList("board.selectCommentList", boardId);
    }

    @Override
    public int insertComment(SqlSession session, BoardCommentVO commentVo) {
        return session.insert("board.insertComment", commentVo);
    }

    @Override
    public int deleteComment(SqlSession session, int commentId) {
        return session.delete("board.deleteComment", commentId);
    }

    // ========================= 신고 =========================

    @Override
    public int checkBoardReport(SqlSession session, Map<String, Object> paramMap) {
        return session.selectOne("board.checkBoardReport", paramMap);
    }

    @Override
    public int insertBoardReport(SqlSession session, BoardReportVO reportVo) {
        return session.insert("board.insertBoardReport", reportVo);
    }

    @Override
    public int countBoardReport(SqlSession session, int boardId) {
        return session.selectOne("board.countBoardReport", boardId);
    }

    // ========================= 회원/파일 =========================

    @Override
    public String getMemberStatus(SqlSession session, String memberId) {
        return session.selectOne("board.getMemberStatus", memberId);
    }

    @Override
    public int insertBoardFile(SqlSession session, BoardFileVO fileVo) {
        return session.insert("board.insertBoardFile", fileVo);
    }

    @Override
    public List<BoardFileVO> selectFileList(SqlSession session, int boardId) {
        return session.selectList("board.selectFileList", boardId);
    }

    @Override
    public BoardFileVO selectFile(SqlSession session, int fileId) {
        return session.selectOne("board.selectFile", fileId);
    }

	@Override
	public int insertCommentReport(SqlSession session, CommentReportVO reportVo) {
		// TODO Auto-generated method stub
		return session.insert("board.insertCommentReport", reportVo);
	}

	@Override
	public int checkCommentReport(SqlSession session, Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		 return session.selectOne("board.checkCommentReport", paramMap);
	}

	@Override
	public int countCommentReport(SqlSession session, int commentId) {
		// TODO Auto-generated method stub
		return session.selectOne("board.countCommentReport", commentId);
	}

	@Override
	public int updateComment(SqlSession session, BoardCommentVO commentVo) {
		// TODO Auto-generated method stub
		 return session.update("board.updateComment", commentVo);
	}
}