package kr.or.ddit.mypage.service;

import kr.or.ddit.animal.vo.AnimalVO;
import kr.or.ddit.board.vo.BoardVO;
import kr.or.ddit.donation.vo.DonationVO;
import kr.or.ddit.member.vo.MemberVO;
import kr.or.ddit.mypage.dao.IMypageDAO;
import kr.or.ddit.mypage.dao.MypageDAOImpl;
import kr.or.ddit.mypage.vo.ActivityVO;
import kr.or.ddit.mypage.vo.AdoptionHistoryVO;
import kr.or.ddit.mypage.vo.MypageStatsVO;
import kr.or.ddit.mypage.vo.VolunteerHistoryVO;

import java.util.List;
import java.util.Map;

/**
 * 마이페이지 서비스 구현체 (싱글톤)
 */
public class MypageServiceImpl implements IMypageService
{

	// ── 싱글톤 ──────────────────────────────────────
	private static MypageServiceImpl instance;

	private MypageServiceImpl()
	{
	}

	public static MypageServiceImpl getInstance()
	{
		if (instance == null)
		{
			instance = new MypageServiceImpl();
		}
		return instance;
	}
	// ─────────────────────────────────────────────────

	private IMypageDAO mypageDAO = MypageDAOImpl.getInstance();
	
	/* 통계 카드 조회 */
	/**
	 * 마이페이지 통계 카드 데이터 조회
	 */
	@Override
	public MypageStatsVO getMypageStats(String memberId)
	{
		return mypageDAO.getMypageStats(memberId);
	}

	/* 최근 활동 내역 조회 */
	/**
	 * 최근 활동 내역 조회 (최근 5건)
	 */
	@Override
	public List<ActivityVO> getRecentActivities(String memberId)
	{
		return mypageDAO.getRecentActivities(memberId);
	}

	/* 회원 정보 수정 */
	/**
	 * 회원 정보 수정 - 비밀번호 변경 요청이 있는 경우: 현재 비밀번호 일치 여부 먼저 확인 - 일치하지 않으면 0 반환 (실패)
	 */
	@Override
	public int updateMember(MemberVO updateVO, String currentPw)
	{

		// 비밀번호 변경 요청이 있는 경우 현재 비밀번호 검증
		if (updateVO.getMemberPw() != null && !updateVO.getMemberPw().isEmpty())
		{
			boolean pwMatch = mypageDAO.checkCurrentPw(updateVO.getMemberId(), currentPw);
			if (!pwMatch)
				return 0;
		}

		return mypageDAO.updateMember(updateVO);
	}

	/**
	 * 닉네임 중복 확인 - 해당 닉네임을 가진 회원이 없으면 사용 가능(true)
	 */
	@Override
	public boolean isNicknameAvailable(String nickname)
	{
		return mypageDAO.countByNickname(nickname) == 0;
	}

	/**
	 * 회원 탈퇴 - 물리 삭제 대신 STATUS = 'N' 으로 소프트 삭제 처리
	 */
	@Override
	public int withdrawMember(String memberId)
	{
		return mypageDAO.withdrawMember(memberId);
	}

	
	/* 관심 동물 */
	/**
	 * 관심동물 총 개수 조회
	 */
	@Override
	public int getFavoriteAnimalCount(String memberId)
	{
		return mypageDAO.getFavoriteAnimalCount(memberId);
	}

	/**
	 * 관심동물 목록 조회 (페이징)
	 */
	@Override
	public List<AnimalVO> getFavoriteAnimalList(Map<String, Object> paramMap)
	{
		return mypageDAO.getFavoriteAnimalList(paramMap);
	}

	/**
	 * 관심동물 찜 해제
	 */
	@Override
	public void removeFavoriteAnimal(Map<String, Object> paramMap)
	{
		mypageDAO.removeFavoriteAnimal(paramMap);
	}
	
	
	/* 입양 신청 목록 */
	/**
	 * 입양 신청 총 개수 조회
	 *
	 * @param memberId 로그인한 회원 ID
	 * @param status   상태 필터 (빈 문자열이면 전체)
	 * @return 신청 건수
	 */
	@Override
	public int getAdoptionHistoryCount(String memberId, String status)
	{
		return mypageDAO.getAdoptionHistoryCount(memberId, status);
	}
 
	/**
	 * 입양 신청 목록 조회 (페이징)
	 *
	 * @param paramMap memberId, status, start, end
	 * @return List<AdoptionHistoryVO>
	 */
	@Override
	public List<AdoptionHistoryVO> getAdoptionHistoryList(Map<String, Object> paramMap)
	{
		return mypageDAO.getAdoptionHistoryList(paramMap);
	}
 
	/**
	 * 입양 신청 취소
	 * - '대기' 상태인 본인 신청만 삭제
	 *
	 * @param paramMap memberId, adoptionId
	 */
	@Override
	public void cancelAdoption(Map<String, Object> paramMap)
	{
		mypageDAO.cancelAdoption(paramMap);
	}
	
	
	/* 내 작성 글 */
	/**
	 * 내 작성글 총 개수 조회
	 *
	 * @param memberId  로그인한 회원 ID
	 * @param boardType 게시판 유형 필터 (빈 문자열이면 전체)
	 * @param keyword   제목 검색어 (빈 문자열이면 전체)
	 * @return 게시글 수
	 */
	@Override
	public int getMyPostsCount(String memberId, String boardType, String keyword)
	{
		return mypageDAO.getMyPostsCount(memberId, boardType, keyword);
	}
 
	/**
	 * 내 작성글 목록 조회 (페이징)
	 *
	 * @param paramMap memberId, boardType, keyword, start, end
	 * @return List<BoardVO>
	 */
	@Override
	public List<BoardVO> getMyPostsList(Map<String, Object> paramMap)
	{
		return mypageDAO.getMyPostsList(paramMap);
	}
 
	/**
	 * 내 작성글 삭제
	 * - 본인 게시글만 삭제
	 *
	 * @param paramMap memberId, boardId
	 */
	@Override
	public void deleteMyPost(Map<String, Object> paramMap)
	{
		mypageDAO.deleteMyPost(paramMap);
	}
	
	
	/* 후원 내역 */
	/**
	 * 후원 통계 조회 (총 횟수 + 누적 금액)
	 *
	 * @param memberId 로그인한 회원 ID
	 * @return map (totalCount, totalAmount)
	 */
	@Override
	public Map<String, Object> getDonationStats(String memberId)
	{
		return mypageDAO.getDonationStats(memberId);
	}
 
	/**
	 * 후원 내역 목록 조회 (페이징)
	 *
	 * @param paramMap memberId, start, end
	 * @return List<DonationVO>
	 */
	@Override
	public List<DonationVO> getDonationHistoryList(Map<String, Object> paramMap)
	{
		return mypageDAO.getDonationHistoryList(paramMap);
	}
	
	/* 봉사 신청 내역*/
	/**
	 * 올해 총 봉사 횟수 조회
	 *
	 * @param memberId 로그인한 회원 ID
	 * @return 봉사 횟수
	 */
	@Override
	public int getVolunteerStats(String memberId)
	{
		return mypageDAO.getVolunteerStats(memberId);
	}
 
	/**
	 * 봉사 신청 총 개수 조회
	 *
	 * @param memberId 로그인한 회원 ID
	 * @return 신청 건수
	 */
	@Override
	public int getVolunteerHistoryCount(String memberId)
	{
		return mypageDAO.getVolunteerHistoryCount(memberId);
	}
 
	/**
	 * 봉사 신청 목록 조회 (페이징)
	 *
	 * @param paramMap memberId, start, end
	 * @return List<VolunteerHistoryVO>
	 */
	@Override
	public List<VolunteerHistoryVO> getVolunteerHistoryList(Map<String, Object> paramMap)
	{
		return mypageDAO.getVolunteerHistoryList(paramMap);
	}
 
	/**
	 * 달력용 봉사 일정 조회 (해당 월)
	 *
	 * @param paramMap memberId, year, month
	 * @return List<Map> (dateStr, title, isCompleted)
	 */
	@Override
	public List<Map<String, Object>> getVolunteerCalendarEvents(Map<String, Object> paramMap)
	{
		return mypageDAO.getVolunteerCalendarEvents(paramMap);
	}
 
	/**
	 * 봉사 신청 취소 (STATUS → '반려')
	 *
	 * @param paramMap memberId, volunteerId
	 */
	@Override
	public void cancelVolunteer(Map<String, Object> paramMap)
	{
		mypageDAO.cancelVolunteer(paramMap);
	}
	
	
	/* 최근 활동 전체 조회 */
	@Override
	public int getActivityAllCount(String memberId)
	{
		return mypageDAO.getActivityAllCount(memberId);
	}

	@Override
	public int getActivityFavoriteCount(String memberId)
	{
		return mypageDAO.getActivityFavoriteCount(memberId);
	}

	@Override
	public int getActivityAdoptionCount(String memberId)
	{
		return mypageDAO.getActivityAdoptionCount(memberId);
	}

	@Override
	public int getActivityDonationCount(String memberId)
	{
		return mypageDAO.getActivityDonationCount(memberId);
	}

	@Override
	public int getActivityVolunteerCount(String memberId)
	{
		return mypageDAO.getActivityVolunteerCount(memberId);
	}

	@Override
	public List<ActivityVO> getActivityAllList(Map<String, Object> paramMap)
	{
		return mypageDAO.getActivityAllList(paramMap);
	}

	@Override
	public List<ActivityVO> getActivityFavoriteList(Map<String, Object> paramMap)
	{
		return mypageDAO.getActivityFavoriteList(paramMap);
	}

	@Override
	public List<ActivityVO> getActivityAdoptionList(Map<String, Object> paramMap)
	{
		return mypageDAO.getActivityAdoptionList(paramMap);
	}

	@Override
	public List<ActivityVO> getActivityDonationList(Map<String, Object> paramMap)
	{
		return mypageDAO.getActivityDonationList(paramMap);
	}

	@Override
	public List<ActivityVO> getActivityVolunteerList(Map<String, Object> paramMap)
	{
		return mypageDAO.getActivityVolunteerList(paramMap);
	}
}
