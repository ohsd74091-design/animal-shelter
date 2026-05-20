package kr.or.ddit.adoption.service;

import java.util.List;
import java.util.Map;

import kr.or.ddit.adoption.vo.AdoptionVO;

public interface IAdoptionService {

    /** 중복 신청 여부 확인 (심사중 or 승인 상태) */
    int checkDuplicate(Map<String, Object> param);

    /** 반려 이력 확인 */
    int checkRejected(Map<String, Object> param);

    /** 반려 사유 조회 (가장 최근) */
    String getRejectReason(Map<String, Object> param);

    /**
     * 입양 신청 처리
     * @return 1: 성공 / -1: 중복 신청 / -2: DB 오류
     */
    int applyAdoption(AdoptionVO adoptionVO);

    /** 단건 상세 조회 */
    AdoptionVO getAdoption(int adoptionId);

    /** 회원 본인 신청 목록 */
    List<AdoptionVO> getAdoptionListByMember(String memberId);
}