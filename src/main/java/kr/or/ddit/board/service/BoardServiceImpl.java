package kr.or.ddit.board.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import kr.or.ddit.board.dao.BoardDaoImpl;
import kr.or.ddit.board.dao.IBoardDao;
import kr.or.ddit.board.vo.BoardCommentVO;
import kr.or.ddit.board.vo.BoardFileVO;
import kr.or.ddit.board.vo.BoardReportVO;
import kr.or.ddit.board.vo.BoardVO;
import kr.or.ddit.board.vo.CommentReportVO;
import kr.or.ddit.util.MybatisUtil;

public class BoardServiceImpl implements BoardService {
    
	private static BoardService service;
	private IBoardDao boardDao;
	
	private BoardServiceImpl() {
		boardDao = BoardDaoImpl.getInstance();
	}
	
	public static BoardService getInstance() {
		if (service == null) {
			service = new BoardServiceImpl();
		}
		return service;
	}
	
	// ========================= 게시글 =========================

    /** 게시글 목록 조회 (페이징 + 필터) */
    @Override
    public List<BoardVO> getBoardList(Map<String, Object> paramMap) {
        try (SqlSession session = MybatisUtil.getsqlsession()) {
            return boardDao.selectBoardList(session, paramMap);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /** 게시글 전체 건수 (페이징용) */
    @Override
    public int getBoardCount(Map<String, Object> paramMap) {
        try (SqlSession session = MybatisUtil.getsqlsession()) {
            return boardDao.selectBoardCount(session, paramMap);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /** 게시글 단건 조회 */
    @Override
    public BoardVO getBoard(int boardId) {
        try (SqlSession session = MybatisUtil.getsqlsession()) {
            return boardDao.selectBoard(session, boardId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /** 조회수 증가 */
    @Override
    public int increaseViewCount(int boardId) {
        try (SqlSession session = MybatisUtil.getsqlsession(false)) {
            int cnt = boardDao.updateViewCount(session, boardId);
            if (cnt > 0) session.commit();
            return cnt;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /** 게시글 등록 */
    @Override
    public int registBoard(BoardVO boardVo) {
        try (SqlSession session = MybatisUtil.getsqlsession(false)) {
            int cnt = boardDao.insertBoard(session, boardVo);
            if (cnt > 0) session.commit();
            else         session.rollback();
            return cnt;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /** 게시글 수정 */
    @Override
    public int modifyBoard(BoardVO boardVo) {
        try (SqlSession session = MybatisUtil.getsqlsession(false)) {
            int cnt = boardDao.updateBoard(session, boardVo);
            if (cnt > 0) session.commit();
            else         session.rollback();
            return cnt;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /** 게시글 삭제 */
    @Override
    public int removeBoard(int boardId) {
        try (SqlSession session = MybatisUtil.getsqlsession(false)) {
            int cnt = boardDao.deleteBoard(session, boardId);
            if (cnt > 0) session.commit();
            else         session.rollback();
            return cnt;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /** 게시글 숨김 처리 */
    @Override
    public int hideBoard(int boardId) {
        try (SqlSession session = MybatisUtil.getsqlsession(false)) {
            int cnt = boardDao.hideBoard(session, boardId);
            if (cnt > 0) session.commit();
            else         session.rollback();
            return cnt;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    public List<BoardVO> getPopularBoardList() {
        try (SqlSession session = MybatisUtil.getsqlsession()) {
            return boardDao.selectPopularBoardList(session);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // ========================= 추천 =========================

    /**
     * 추천 토글
     * - 이미 추천했으면 → 추천 취소 + likeCount -1 → -1 반환
     * - 추천 안 했으면  → 추천 등록 + likeCount +1 → 1 반환
     */
    @Override
    public int toggleBoardLike(int boardId, String memberId) {
        try (SqlSession session = MybatisUtil.getsqlsession(false)) {
            Map<String, Object> param = new HashMap<>();
            param.put("boardId",  boardId);
            param.put("memberId", memberId);

            int alreadyLiked = boardDao.checkBoardLike(session, param);

            if (alreadyLiked > 0) {
                // 이미 추천 → 취소
                boardDao.deleteBoardLike(session, param);
                boardDao.decreaseLikeCount(session, boardId);
                session.commit();
                return -1; // 추천 취소
            } else {
                // 추천 안 함 → 추천 등록
                boardDao.insertBoardLike(session, param);
                boardDao.updateLikeCount(session, boardId);
                session.commit();
                return 1; // 추천 완료
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /** 추천 여부 확인 */
    @Override
    public boolean isLiked(int boardId, String memberId) {
        try (SqlSession session = MybatisUtil.getsqlsession()) {
            Map<String, Object> param = new HashMap<>();
            param.put("boardId",  boardId);
            param.put("memberId", memberId);
            return boardDao.checkBoardLike(session, param) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ========================= 댓글 =========================

    /** 댓글 목록 조회 */
    @Override
    public List<BoardCommentVO> getCommentList(int boardId) {
        try (SqlSession session = MybatisUtil.getsqlsession()) {
            return boardDao.selectCommentList(session, boardId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /** 댓글 등록 */
    @Override
    public int registComment(BoardCommentVO commentVo) {
        try (SqlSession session = MybatisUtil.getsqlsession(false)) {
            int cnt = boardDao.insertComment(session, commentVo);
            if (cnt > 0) session.commit();
            else         session.rollback();
            return cnt;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /** 댓글 삭제 */
    @Override
    public int deleteComment(int commentId) {
        try (SqlSession session = MybatisUtil.getsqlsession(false)) {
            int cnt = boardDao.deleteComment(session, commentId);
            if (cnt > 0) session.commit();
            else         session.rollback();
            return cnt;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // ========================= 신고 =========================

    /**
     * 게시글 신고
     * - 중복 신고 방지
     * - 신고 5회 이상 시 자동 숨김 처리
     * @return 1: 신고 완료, -1: 이미 신고함, 0: 실패
     */
    @Override
    public int registBoardReport(BoardReportVO reportVo) {
        try (SqlSession session = MybatisUtil.getsqlsession(false)) {
            Map<String, Object> param = new HashMap<>();
            param.put("boardId",  reportVo.getBoardId());
            param.put("memberId", reportVo.getMemberId());

            // 중복 신고 체크
            int alreadyReported = boardDao.checkBoardReport(session, param);
            if (alreadyReported > 0) {
                return -1; // 이미 신고한 게시글
            }

            // 신고 등록
            int cnt = boardDao.insertBoardReport(session, reportVo);
            if (cnt > 0) {
                session.commit();

                // 신고 횟수 확인 → 5회 이상이면 자동 숨김
                int reportCount = boardDao.countBoardReport(session, reportVo.getBoardId());
                if (reportCount >= 5) {
                    boardDao.hideBoard(session, reportVo.getBoardId());
                    session.commit();
                }
                return 1; // 신고 완료
            } else {
                session.rollback();
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /** 신고 횟수 조회 */
    @Override
    public int getBoardReportCount(int boardId) {
        try (SqlSession session = MybatisUtil.getsqlsession()) {
            return boardDao.countBoardReport(session, boardId);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // ========================= 회원/파일 =========================

    /** 회원 상태 조회 */
    @Override
    public String getMemberStatus(String memberId) {
        try (SqlSession session = MybatisUtil.getsqlsession()) {
            return boardDao.getMemberStatus(session, memberId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /** 첨부파일 등록 */
    @Override
    public int insertBoardFile(BoardFileVO fileVo) {
        try (SqlSession session = MybatisUtil.getsqlsession(false)) {
            int cnt = boardDao.insertBoardFile(session, fileVo);
            if (cnt > 0) session.commit();
            return cnt;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /** 첨부파일 목록 조회 */
    @Override
    public List<BoardFileVO> getFileList(int boardId) {
        try (SqlSession session = MybatisUtil.getsqlsession()) {
            return boardDao.selectFileList(session, boardId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /** 첨부파일 단건 조회 */
    @Override
    public BoardFileVO getFile(int fileId) {
        try (SqlSession session = MybatisUtil.getsqlsession()) {
            return boardDao.selectFile(session, fileId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<BoardVO> getPopularBoardListByLike() {
        try (SqlSession session = MybatisUtil.getsqlsession()) {
            return boardDao.selectPopularBoardListByLike(session);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

	@Override
	public int registCommentReport(CommentReportVO reportVo) {
		try (SqlSession session = MybatisUtil.getsqlsession(false)) {
	        Map<String, Object> param = new HashMap<>();
	        param.put("commentId", reportVo.getCommentId());
	        param.put("memberId", reportVo.getMemberId());

	        int alreadyReported = boardDao.checkCommentReport(session, param);
	        if (alreadyReported > 0) {
	            return -1;
	        }

	        int cnt = boardDao.insertCommentReport(session, reportVo);
	        if (cnt > 0) {
	            session.commit();
	            return 1;
	        } else {
	            session.rollback();
	            return 0;
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        return 0;
	}
	}

	@Override
	public int getCommentReportCount(int commentId) {
		 try (SqlSession session = MybatisUtil.getsqlsession()) {
		        return boardDao.countCommentReport(session, commentId);
		    } catch (Exception e) {
		        e.printStackTrace();
		        return 0;
		    }
		}

	@Override
	public int modifyComment(BoardCommentVO commentVo) {
		try (SqlSession session = MybatisUtil.getsqlsession(false)) {
	        int cnt = boardDao.updateComment(session, commentVo);
	        if (cnt > 0) {
	            session.commit();
	        } else {
	            session.rollback();
	        }
	        return cnt;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return 0;
	    }
	}
}