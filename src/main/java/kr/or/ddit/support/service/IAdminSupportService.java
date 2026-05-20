package kr.or.ddit.support.service;

import java.util.List;

import kr.or.ddit.common.vo.PageVO;
import kr.or.ddit.support.vo.SupportReplyVO;
import kr.or.ddit.support.vo.SupportVO;

public interface IAdminSupportService {
	
	public List<SupportVO> getAllSupportList();
	
	public int registerReply(SupportReplyVO replyVO);
	
	public SupportVO selectSupportDetail(int supportId);
	
	public PageVO calculateAdminPageInfo(PageVO pageVO, int currentPage);
	public List<SupportVO> selectAdminSupportListWithPaging(PageVO pageVO);

}
