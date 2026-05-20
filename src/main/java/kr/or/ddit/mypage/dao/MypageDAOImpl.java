package kr.or.ddit.mypage.dao;

import kr.or.ddit.animal.vo.AnimalVO;
import kr.or.ddit.board.vo.BoardVO;
import kr.or.ddit.donation.vo.DonationVO;
import kr.or.ddit.member.vo.MemberVO;
import kr.or.ddit.mypage.vo.ActivityVO;
import kr.or.ddit.mypage.vo.AdoptionHistoryVO;
import kr.or.ddit.mypage.vo.MypageStatsVO;
import kr.or.ddit.mypage.vo.VolunteerHistoryVO;
import kr.or.ddit.util.MybatisUtil;

import org.apache.ibatis.session.SqlSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 마이페이지 DAO 구현체 (싱글톤) - MyBatis SqlSession 사용 - namespace: kr.or.ddit.mypage
 * (mypage.xml 과 일치)
 */
public class MypageDAOImpl implements IMypageDAO
{

	// ── 싱글톤 ──────────────────────────────────────
	private static MypageDAOImpl instance;

	private MypageDAOImpl()
	{
	}

	public static MypageDAOImpl getInstance()
	{
		if (instance == null)
		{
			instance = new MypageDAOImpl();
		}
		return instance;
	}
	// ─────────────────────────────────────────────────

	/** mypage.xml namespace */
	private static final String NS = "mypage.";

	/* 통계 카드 조회 */
	/**
	 * 마이페이지 통계 카드 데이터 조회 (관심동물 수 / 입양신청 수 / 작성글 수)
	 *
	 * @param memberId 로그인한 회원 ID
	 * @return MypageStatsVO
	 */
	@Override
	public MypageStatsVO getMypageStats(String memberId)
	{
		try (SqlSession session = MybatisUtil.getsqlsession())
		{
			return session.selectOne(NS + "getMypageStats", memberId);
		}
	}

	/* 최근 활동 내역 조회 */
	/**
	 * 최근 활동 내역 조회 (최근 5건)
	 *
	 * @param memberId 로그인한 회원 ID
	 * @return List<ActivityVO>
	 */
	@Override
	public List<ActivityVO> getRecentActivities(String memberId)
	{
		try (SqlSession session = MybatisUtil.getsqlsession())
		{
			return session.selectList(NS + "getRecentActivities", memberId);
		}
	}

	/* 회원 정보 수정 */
	/**
	 * 회원 정보 수정
	 * 
	 * @return 수정된 행 수
	 */
	@Override
	public int updateMember(MemberVO updateVO)
	{
		try (SqlSession session = MybatisUtil.getsqlsession())
		{
			int result = session.update(NS + "updateMember", updateVO);
			session.commit();

			return result;
		}
	}

	/**
	 * 현재 비밀번호 일치 여부 확인
	 * 
	 * @return true: 일치, false: 불일치
	 */
	@Override
	public boolean checkCurrentPw(String memberId, String currentPw)
	{
		try (SqlSession session = MybatisUtil.getsqlsession())
		{
			Map<String, String> paramMap = new HashMap<>();
			paramMap.put("memberId", memberId);
			paramMap.put("currentPw", currentPw);
			int count = session.selectOne(NS + "checkCurrentPw", paramMap);
			return count > 0;
		}
	}

	/**
	 * 닉네임 중복 카운트
	 * 
	 * @return 0이면 사용 가능
	 */
	@Override
	public int countByNickname(String nickname)
	{
		try (SqlSession session = MybatisUtil.getsqlsession())
		{
			return session.selectOne(NS + "countByNickname", nickname);
		}
	}

	/**
	 * 회원 탈퇴 (소프트 삭제 - STATUS = 'N')
	 *
	 * @return 수정된 행 수
	 */
	@Override
	public int withdrawMember(String memberId)
	{
		try (SqlSession session = MybatisUtil.getsqlsession())
		{
			int result = session.update(NS + "withdrawMember", memberId);
			session.commit();
			return result;
		}
	}

	/* 관심 동물 */
	/**
	 * 관심동물 총 개수 조회
	 *
	 * @param memberId 로그인한 회원 ID
	 * @return 관심동물 수
	 */
	@Override
	public int getFavoriteAnimalCount(String memberId)
	{
		try (SqlSession session = MybatisUtil.getsqlsession())
		{
			return session.selectOne(NS + "getFavoriteAnimalCount", memberId);
		}
	}

	/**
	 * 관심동물 목록 조회 (페이징)
	 *
	 * @param paramMap memberId, start, end
	 * @return List<AnimalVO>
	 */
	@Override
	public List<AnimalVO> getFavoriteAnimalList(Map<String, Object> paramMap)
	{
		try (SqlSession session = MybatisUtil.getsqlsession())
		{
			return session.selectList(NS + "getFavoriteAnimalList", paramMap);
		}
	}

	/**
	 * 관심동물 찜 해제
	 *
	 * @param paramMap memberId, animalId
	 */
	@Override
	public void removeFavoriteAnimal(Map<String, Object> paramMap)
	{
		try (SqlSession session = MybatisUtil.getsqlsession())
		{
			session.delete(NS + "removeFavoriteAnimal", paramMap);
			session.commit();
		}
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
		try (SqlSession session = MybatisUtil.getsqlsession())
		{
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("memberId", memberId);
			paramMap.put("status",   status == null || status.isEmpty() ? null : status);
			return session.selectOne(NS + "getAdoptionHistoryCount", paramMap);
		}
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
		try (SqlSession session = MybatisUtil.getsqlsession())
		{
			return session.selectList(NS + "getAdoptionHistoryList", paramMap);
		}
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
		try (SqlSession session = MybatisUtil.getsqlsession())
		{
			session.delete(NS + "cancelAdoption", paramMap);
			session.commit();
		}
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
		try (SqlSession session = MybatisUtil.getsqlsession())
		{
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("memberId",  memberId);
			paramMap.put("boardType", boardType == null || boardType.isEmpty() ? null : boardType);
			paramMap.put("keyword",   keyword   == null || keyword.isEmpty()   ? null : keyword);
			return session.selectOne(NS + "getMyPostsCount", paramMap);
		}
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
		try (SqlSession session = MybatisUtil.getsqlsession())
		{
			return session.selectList(NS + "getMyPostsList", paramMap);
		}
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
		try (SqlSession session = MybatisUtil.getsqlsession())
		{
			session.delete(NS + "deleteMyPost", paramMap);
			session.commit();
		}
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
		try (SqlSession session = MybatisUtil.getsqlsession())
		{
			return session.selectOne(NS + "getDonationStats", memberId);
		}
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
		try (SqlSession session = MybatisUtil.getsqlsession())
		{
			return session.selectList(NS + "getDonationHistoryList", paramMap);
		}
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
		try (SqlSession session = MybatisUtil.getsqlsession())
		{
			return session.selectOne(NS + "getVolunteerStats", memberId);
		}
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
		try (SqlSession session = MybatisUtil.getsqlsession())
		{
			return session.selectOne(NS + "getVolunteerHistoryCount", memberId);
		}
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
		try (SqlSession session = MybatisUtil.getsqlsession())
		{
			return session.selectList(NS + "getVolunteerHistoryList", paramMap);
		}
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
		try (SqlSession session = MybatisUtil.getsqlsession())
		{
			return session.selectList(NS + "getVolunteerCalendarEvents", paramMap);
		}
	}
 
	/**
	 * 봉사 신청 취소 (STATUS → '반려')
	 *
	 * @param paramMap memberId, volunteerId
	 */
	@Override
	public void cancelVolunteer(Map<String, Object> paramMap)
	{
		try (SqlSession session = MybatisUtil.getsqlsession())
		{
			session.update(NS + "cancelVolunteer", paramMap);
			session.commit();
		}
	}
	
	
	/* 최근 활동 전체 조회 */
	@Override
	public int getActivityAllCount(String memberId)
	{
		try (SqlSession session = MybatisUtil.getsqlsession())
		{
			return session.selectOne(NS + "getActivityAllCount", memberId);
		}
	}

	@Override
	public int getActivityFavoriteCount(String memberId)
	{
		try (SqlSession session = MybatisUtil.getsqlsession())
		{
			return session.selectOne(NS + "getActivityFavoriteCount", memberId);
		}
	}

	@Override
	public int getActivityAdoptionCount(String memberId)
	{
		try (SqlSession session = MybatisUtil.getsqlsession())
		{
			return session.selectOne(NS + "getActivityAdoptionCount", memberId);
		}
	}

	@Override
	public int getActivityDonationCount(String memberId)
	{
		try (SqlSession session = MybatisUtil.getsqlsession())
		{
			return session.selectOne(NS + "getActivityDonationCount", memberId);
		}
	}

	@Override
	public int getActivityVolunteerCount(String memberId)
	{
		try (SqlSession session = MybatisUtil.getsqlsession())
		{
			return session.selectOne(NS + "getActivityVolunteerCount", memberId);
		}
	}

	@Override
	public List<ActivityVO> getActivityAllList(Map<String, Object> paramMap)
	{
		try (SqlSession session = MybatisUtil.getsqlsession())
		{
			return session.selectList(NS + "getActivityAllList", paramMap);
		}
	}

	@Override
	public List<ActivityVO> getActivityFavoriteList(Map<String, Object> paramMap)
	{
		try (SqlSession session = MybatisUtil.getsqlsession())
		{
			return session.selectList(NS + "getActivityFavoriteList", paramMap);
		}
	}

	@Override
	public List<ActivityVO> getActivityAdoptionList(Map<String, Object> paramMap)
	{
		try (SqlSession session = MybatisUtil.getsqlsession())
		{
			return session.selectList(NS + "getActivityAdoptionList", paramMap);
		}
	}

	@Override
	public List<ActivityVO> getActivityDonationList(Map<String, Object> paramMap)
	{
		try (SqlSession session = MybatisUtil.getsqlsession())
		{
			return session.selectList(NS + "getActivityDonationList", paramMap);
		}
	}

	@Override
	public List<ActivityVO> getActivityVolunteerList(Map<String, Object> paramMap)
	{
		try (SqlSession session = MybatisUtil.getsqlsession())
		{
			return session.selectList(NS + "getActivityVolunteerList", paramMap);
		}
	}
}
