package kr.or.ddit.adoption.dao;

import java.util.List;
import java.util.Map;

import kr.or.ddit.adoption.vo.AdoptionDetailVO;
import kr.or.ddit.adoption.vo.AdoptionVO;

public interface IAdoptionDao
{

	/** 중복 신청 확인 (같은 동물·같은 회원·심사중) */
	public int checkDuplicateAdoption(Map<String, Object> param);
	
	/** 반려 이력 확인 (같은 동물·같은 회원·반려) */
	int checkRejectedAdoption(Map<String, Object> param);
 
	/** 반려 사유 조회 (가장 최근) */
	String getRejectReason(Map<String, Object> param);
 
	/**
	 * ADOPTION 기본정보 INSERT ※ selectKey 로 adoptionId 가 vo 에 자동 세팅됨
	 */
	public int insertAdoption(AdoptionVO adoptionVO);
 
	/** ADOPTION_DETAIL 상세정보 INSERT */
	public int insertAdoptionDetail(AdoptionDetailVO detailVO);
 
	/** 단건 상세 조회 (동물정보 + 상세 JOIN) */
	public AdoptionVO selectAdoption(int adoptionId);
 
	/** 회원 본인 신청 목록 */
	public List<AdoptionVO> selectAdoptionListByMember(String memberId);
}
