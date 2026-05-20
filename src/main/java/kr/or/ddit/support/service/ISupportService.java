package kr.or.ddit.support.service;

import java.util.List;

import jakarta.servlet.http.Part;
import kr.or.ddit.common.vo.PageVO;
import kr.or.ddit.support.vo.SupportVO;

public interface ISupportService {
	
	public int registerSupport(SupportVO svo, List<Part> parts);
	
	public List<SupportVO> selectSupportList(String memberId);
	
	//다오의 selectSupportDetail 메서드를 호출
	public SupportVO selectSupportDetail(int supportId);
	
	/**
	 * 문의글 수정 기능
	 * @param svo   : 수정할 제목, 내용 등이 담긴 바구니
	 * @param parts : 새로 추가하거나 교체할 파일 조각들의 리스트
	 */
	public int updateSupport(SupportVO svo, List<Part> parts);
	
	public int deleteSupport(int sId);
	
	public PageVO calculatePageInfo(int currentPage, String memberId, String status);
	
	public List<SupportVO> selectSupportListWithPaging(PageVO pageVO);

}
