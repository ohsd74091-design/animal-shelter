package kr.or.ddit.support.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import kr.or.ddit.common.vo.PageVO;
import kr.or.ddit.support.vo.SupportReplyVO;
import kr.or.ddit.support.vo.SupportVO;

public interface ISupportDao {
	
	/**
	 * 새문의글 저장
	 * @param session
	 * @param svo
	 * @return
	 */
	public int insertSupport(SqlSession session, SupportVO svo);
	
	// 전체 문의글 목록
	public List<SupportVO>  selectSupportList(SqlSession session, String memberId);
	// 특정 문의글 1개의 상세 정보(파일, 답변 포함)
	public SupportVO selectSupportDetail(SqlSession session, int supportId);
	
	public int updateSupport(SqlSession session, SupportVO svo);
	
	public int deleteSupportFiles(SqlSession session, int sId);
	
	public int deleteSupport(SqlSession session, int sId);
	
	public int getSupportCount(SqlSession session, PageVO pageVO);
	
	public List<SupportVO> selectSupportListWithPaging(SqlSession session, PageVO pageVo);
	
	public List<SupportVO> selectSupportListForAdmin(SqlSession session);
	
	public int insertSupportReply(SqlSession session, SupportReplyVO replyVO);
	
	public int updateSupportStatus(SqlSession session, int supportId);
	
	public int getAdminSupportCount(SqlSession session, PageVO pageVO);
	public List<SupportVO> selectAdminSupportListWithPaging(SqlSession session, PageVO pageVO);

}
