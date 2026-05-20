package kr.or.ddit.admin.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import kr.or.ddit.adoption.vo.AdoptionVO;
import kr.or.ddit.animal.vo.AnimalMedicalVO;
import kr.or.ddit.animal.vo.AnimalRescueVO;
import kr.or.ddit.animal.vo.AnimalVO;
import kr.or.ddit.board.vo.BoardReportVO;
import kr.or.ddit.board.vo.BoardVO;
import kr.or.ddit.common.vo.PageVO;
import kr.or.ddit.member.vo.MemberVO;
import kr.or.ddit.volunteer_recruit.vo.Volunteer_ApplyVO;
import kr.or.ddit.volunteer_recruit.vo.Volunteer_RecruitVO;

public interface IadminDao {
	public List<MemberVO> selectMemberList(SqlSession session, PageVO pageVO);
	
	public int selectMemberCount(SqlSession session, PageVO pageVO);
	
	public int selectTodayJoinCount(SqlSession session);
	
	public MemberVO selectMemberDetail(SqlSession session, String memberId);
	
	int updateMemberRole(SqlSession session, Map<String, String> param);
	
	int updateMemberStatus(SqlSession session, Map<String, String> param);
	
	int deleteMember(SqlSession session, String memberId);
	
	List<Volunteer_RecruitVO> selectRecruitList(SqlSession session);
	
	Volunteer_RecruitVO selectRecruitDetail(SqlSession session, int recruitId);
	
	List<Volunteer_ApplyVO> selectApplyListByRecruitId(SqlSession session, int recruitId);
	
	int updateApplyStatus(SqlSession session, Map<String, Object> param);
	/**
	 * 관리자 동물 목록 조회
	 * - PageVO에 담긴 필터(animalType, adoptionStatus)와 페이징(startRow, endRow) 조건으로 조회
	 * - admin.xml의 selectAdminAnimalList 호출
	 */
	List<AnimalVO> selectAdminAnimalList(SqlSession session, PageVO pageVO);
	/**
	 * 관리자 동물 목록 전체 건수
	 * - 필터 조건 반영한 건수 → 컨트롤러에서 totalPage 계산에 사용
	 * - admin.xml의 selectAdminAnimalCount 호출
	 */
	int selectAdminAnimalCount(SqlSession session,PageVO pageVO);
	
	/**
	 * 동물 입양 상태 변경
	 * - map에 animalId(int)와 newStatus(String) 담아서 전달
	 * - admin.xml의 updateAnimalStatus 호출
	 */
	int updateAnimalStatus(SqlSession session,Map<String,Object> param);
	
	/**
	 * 동물 삭제
	 * - ANIMAL 삭제 시 CASCADE로 하위 테이블 자동 삭제
	 * - admin.xml의 deleteAnimal 호출
	 */
	int deleteAnimal(SqlSession session, int animalId);
	
	/**
	 * 동물 기본정보 수정
	 * - AnimalVO에 animalId 포함해서 전달
	 */
	int updateAnimal(SqlSession session, AnimalVO animal);

	/**
	 * 동물 의료정보 수정
	 * - AnimalMedicalVO에 animalId 포함해서 전달
	 */
	int updateAnimalMedical(SqlSession session, AnimalMedicalVO medical);

	/**
	 * 동물 구조정보 수정
	 * - AnimalRescueVO에 animalId 포함해서 전달
	 */
	int updateAnimalRescue(SqlSession session, AnimalRescueVO rescue);

	/**
	 * 동물 이미지 전체 삭제
	 * - 수정 시 기존 이미지 전부 삭제 후 새로 INSERT하는 전략
	 * - animalId에 해당하는 ANIMAL_IMAGE 행 전체 삭제
	 */
	int deleteAnimalImages(SqlSession session, int animalId);
	
	
	// ========================= 게시판 관리 =========================

	/** 전체 게시글 목록 (페이징 + 필터) */
	List<BoardVO> selectAdminBoardList(SqlSession session, Map<String, Object> paramMap);

	/** 전체 게시글 건수 */
	int selectAdminBoardCount(SqlSession session, Map<String, Object> paramMap);

	/** 숨김 처리된 게시글 건수 */
	int selectHiddenBoardCount(SqlSession session);

	/** 신고된 게시글 목록 (페이징) */
	List<BoardVO> selectReportedBoardList(SqlSession session, Map<String, Object> paramMap);

	/** 신고된 게시글 건수 */
	int selectReportedBoardCount(SqlSession session);

	/** 게시글 숨김/복구 */
	int updateBoardHidden(SqlSession session, Map<String, Object> paramMap);

	/** 게시글 강제 삭제 */
	int deleteAdminBoard(SqlSession session, int boardId);

	/** 신고 사유 목록 조회 */
	List<BoardReportVO> selectBoardReportList(SqlSession session, int boardId);

	/** 신고 기각 */
	int updateBoardReportStatus(SqlSession session, Map<String, Object> paramMap);
	
	//===================================================================
	public List<Map<String, Object>> selectTodayCombinedSchedule(SqlSession session);
	
	public List<Map<String, Object>> selectMonthCombinedSchedule(SqlSession session, Map<String, Object> param);
	
	public Map<String, Object> selectAnimalDashboardStats(SqlSession session);
	
	List<Map<String, Object>> selectRecentActivityFeed(SqlSession session);
	
	List<Map<String, Object>> selectWeekCombinedSchedule(SqlSession session);
	
	/**
	 * 이달의 주목 동물 (조회수 가장 높은 동물 1마리) 조회
	 * @param session SqlSession 객체
	 * @return AnimalVO 객체
	 */
	public AnimalVO selectAnimalOfTheMonth(SqlSession session);
	
	
	
	/**
	 * 입양 신청 전체 목록 조회
	 * - admin.xml: selectAdoptionList
	 * - ADOPTION + ADOPTION_DETAIL + ANIMAL + ANIMAL_IMAGE + MEMBER JOIN
	 * - 관리자 목록 페이지 진입 시 호출
	 */
	List<AdoptionVO> selectAdoptionList(SqlSession session);
	 
	/**
	 * 전체 입양 신청 건수
	 * - admin.xml: selectAdoptionTotalCount
	 * - 통계 카드 "전체 신청" 숫자에 사용
	 */
	int selectAdoptionTotalCount(SqlSession session);
	 
	/**
	 * 상태별 입양 신청 건수
	 * - admin.xml: selectAdoptionCountByStatus
	 * - 통계 카드 심사중/승인/반려 숫자에 사용
	 * - @param status "심사중" / "승인" / "반려"
	 */
	int selectAdoptionCountByStatus(SqlSession session, String status);
	 
	/**
	 * 입양 신청 상태 업데이트 (승인 / 반려)
	 * - admin.xml: updateAdoptionStatus
	 * - AdminAdoptionStatusController에서 승인/반려 버튼 클릭 시 호출
	 * - @param param { adoptionId, status, rejectReason }
	 */
	int updateAdoptionStatus(SqlSession session, Map<String, Object> param);
	 
	/**
	 * 페이징 + 상태 필터 적용 입양 신청 목록
	 * param key: startRow, endRow, statusFilter
	 * admin.xml > selectAdoptionListPaged
	 */
	List<AdoptionVO> selectAdoptionListPaged(SqlSession session, Map<String, Object> paramMap);
	
	int selectAnimalCount(SqlSession session);
	int selectVolunteerApprovedCount(SqlSession session);
	List<AnimalVO> selectPopularAnimals(SqlSession session, int limit);
	List<Volunteer_RecruitVO> selectPopularVolunteers(SqlSession session, int limit);
	List<BoardVO> selectPopularBoards(SqlSession session, int limit);
	List<Map<String, Object>> selectDonorRanking(SqlSession session, int limit);

	
	public List<AdoptionVO> selectAdoptionHistoryListPaged(SqlSession session,Map<String, Object> paramMap);

	public int selectAdoptionHistoryCount(SqlSession session,String statusFilter);

	public AdoptionVO selectAdoptionHistoryDetail(SqlSession session,int adoptionId);
	
}
