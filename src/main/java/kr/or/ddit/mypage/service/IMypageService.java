package kr.or.ddit.mypage.service;

import kr.or.ddit.animal.vo.AnimalVO;
import kr.or.ddit.board.vo.BoardVO;
import kr.or.ddit.donation.vo.DonationVO;
import kr.or.ddit.member.vo.MemberVO;
import kr.or.ddit.mypage.vo.ActivityVO;
import kr.or.ddit.mypage.vo.AdoptionHistoryVO;
import kr.or.ddit.mypage.vo.MypageStatsVO;
import kr.or.ddit.mypage.vo.VolunteerHistoryVO;

import java.util.List;
import java.util.Map;

/**
 * 마이페이지 서비스 인터페이스
 */
public interface IMypageService
{
	/* 통계 카드 조회 */
	/**
	 * 마이페이지 통계 카드 데이터 조회
	 *
	 * @param memberId 로그인한 회원 ID
	 * @return MypageStatsVO
	 */
	MypageStatsVO getMypageStats(String memberId);
	
	/* 최근 활동 내역 조회 */
	/**
	 * 최근 활동 내역 조회 (최근 5건)
	 *
	 * @param memberId 로그인한 회원 ID
	 * @return List<ActivityVO>
	 */
	List<ActivityVO> getRecentActivities(String memberId);

	/* 회원 정보 수정 */
	/**
	 * 회원 정보 수정
	 *
	 * @param updateVO  변경할 회원 정보 VO
	 * @param currentPw 현재 비밀번호 (비밀번호 변경 시 검증용, 변경 없으면 null)
	 * @return 수정된 행 수 (1: 성공, 0: 실패)
	 */
	int updateMember(MemberVO updateVO, String currentPw);

	/**
	 * 닉네임 중복 확인
	 *
	 * @param nickname 확인할 닉네임
	 * @return true: 사용 가능, false: 중복
	 */
	boolean isNicknameAvailable(String nickname);

	/**
	 * 회원 탈퇴 - MEMBER 테이블 STATUS 를 'N' 으로 변경 (물리 삭제 X)
	 *
	 * @param memberId 탈퇴할 회원 ID
	 * @return 수정된 행 수 (1: 성공, 0: 실패)
	 */
	int withdrawMember(String memberId);

	
	/* 관심 동물 */
	/**
	 * 관심동물 총 개수 조회
	 */
	int getFavoriteAnimalCount(String memberId);

	/**
	 * 관심동물 목록 조회 (페이징)
	 */
	List<AnimalVO> getFavoriteAnimalList(Map<String, Object> paramMap);

	/**
	 * 관심동물 찜 해제
	 */
	void removeFavoriteAnimal(Map<String, Object> paramMap);
	
	
	/* 입양 신청 목록 */
	/**
	 * 입양 신청 총 개수 조회
	 *
	 * @param memberId 로그인한 회원 ID
	 * @param status   상태 필터 (빈 문자열이면 전체)
	 * @return 신청 건수
	 */
	int getAdoptionHistoryCount(String memberId, String status);
	
	/**
	 * 입양 신청 목록 조회 (페이징)
	 *
	 * @param paramMap memberId, status, start, end
	 * @return List<AdoptionHistoryVO>
	 */
	List<AdoptionHistoryVO> getAdoptionHistoryList(Map<String, Object> paramMap);
	
	/**
	 * 입양 신청 취소
	 * - '대기' 상태인 본인 신청만 삭제
	 *
	 * @param paramMap memberId, adoptionId
	 */
	void cancelAdoption(Map<String, Object> paramMap);
	
	
	/* 내 작성 글 */
	/**
	 * 내 작성글 총 개수 조회
	 *
	 * @param memberId  로그인한 회원 ID
	 * @param boardType 게시판 유형 필터 (빈 문자열이면 전체)
	 * @param keyword   제목 검색어 (빈 문자열이면 전체)
	 * @return 게시글 수
	 */
	int getMyPostsCount(String memberId, String boardType, String keyword);
	
	/**
	 * 내 작성글 목록 조회 (페이징)
	 *
	 * @param paramMap memberId, boardType, keyword, start, end
	 * @return List<BoardVO>
	 */
	List<BoardVO> getMyPostsList(Map<String, Object> paramMap);
	
	/**
	 * 내 작성글 삭제
	 * - 본인 게시글만 삭제
	 *
	 * @param paramMap memberId, boardId
	 */
	void deleteMyPost(Map<String, Object> paramMap);
	
	
	/* 후원 내역 */
	/**
	 * 후원 통계 조회 (총 횟수 + 누적 금액)
	 *
	 * @param memberId 로그인한 회원 ID
	 * @return map (totalCount, totalAmount)
	 */
	Map<String, Object> getDonationStats(String memberId);

	/**
	 * 후원 내역 목록 조회 (페이징)
	 *
	 * @param paramMap memberId, start, end
	 * @return List<DonationVO>
	 */
	List<DonationVO> getDonationHistoryList(Map<String, Object> paramMap);
	
	
	/* 봉사 신청 내역*/
	/**
	 * 올해 총 봉사 횟수 조회
	 *
	 * @param memberId 로그인한 회원 ID
	 * @return 봉사 횟수
	 */
	int getVolunteerStats(String memberId);
	
	/**
	 * 봉사 신청 총 개수 조회
	 *
	 * @param memberId 로그인한 회원 ID
	 * @return 신청 건수
	 */
	int getVolunteerHistoryCount(String memberId);
	
	/**
	 * 봉사 신청 목록 조회 (페이징)
	 *
	 * @param paramMap memberId, start, end
	 * @return List<VolunteerHistoryVO>
	 */
	List<VolunteerHistoryVO> getVolunteerHistoryList(Map<String, Object> paramMap);
	
	/**
	 * 달력용 봉사 일정 조회 (해당 월)
	 *
	 * @param paramMap memberId, year, month
	 * @return List<Map> (dateStr, title, isCompleted)
	 */
	List<Map<String, Object>> getVolunteerCalendarEvents(Map<String, Object> paramMap);
	
	/**
	 * 봉사 신청 취소 (STATUS → '반려')
	 *
	 * @param paramMap memberId, volunteerId
	 */
	void cancelVolunteer(Map<String, Object> paramMap);
	
	
	/* 최근 활동 전체 조회*/
	/**
	 * 활동 내역 총 개수 조회
	 *
	 * @param memberId    로그인한 회원 ID
	 * @return 활동 건수
	 */
	int getActivityAllCount(String memberId);
	int getActivityFavoriteCount(String memberId);
	int getActivityAdoptionCount(String memberId);
	int getActivityDonationCount(String memberId);
	int getActivityVolunteerCount(String memberId);

	/**
	 * 활동 내역 목록 조회 (페이징 + 타입 필터)
	 *
	 * @param paramMap memberId, start, end
	 * @return List<ActivityVO>
	 */
	List<ActivityVO> getActivityAllList(Map<String, Object> paramMap);
	List<ActivityVO> getActivityFavoriteList(Map<String, Object> paramMap);
	List<ActivityVO> getActivityAdoptionList(Map<String, Object> paramMap);
	List<ActivityVO> getActivityDonationList(Map<String, Object> paramMap);
	List<ActivityVO> getActivityVolunteerList(Map<String, Object> paramMap);
}
