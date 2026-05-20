package kr.or.ddit.admin.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import kr.or.ddit.adoption.vo.AdoptionVO;
import kr.or.ddit.animal.dto.AnimalDetailDto;
import kr.or.ddit.animal.vo.AnimalImageVO;
import kr.or.ddit.animal.vo.AnimalMedicalVO;
import kr.or.ddit.animal.vo.AnimalRescueVO;
import kr.or.ddit.animal.vo.AnimalVO;
import kr.or.ddit.board.vo.BoardReportVO;
import kr.or.ddit.board.vo.BoardVO;
import kr.or.ddit.common.vo.PageVO;
import kr.or.ddit.member.vo.MemberVO;
import kr.or.ddit.volunteer_recruit.vo.Volunteer_ApplyVO;
import kr.or.ddit.volunteer_recruit.vo.Volunteer_RecruitVO;

public interface IadminService {
	public Map<String,Object> selectMemberListData(PageVO pageVO);
	
	public int selectMemberCount(PageVO pageVO);
	
	public MemberVO selectMemberDetail(String memberId);
	
	public int updateMemberRole(String memberId, String role);
	
	public int updateMemberStatus(String memberId, String status);
	
	public int deleteMember(String memberId);

	List<Volunteer_RecruitVO> selectRecruitList();

	Volunteer_RecruitVO selectRecruitDetail(int recruitId);

	List<Volunteer_ApplyVO> selectApplyListByRecruitId(int recruitId);

	int updateApplyStatus(Map<String, Object> param);
	/**
	 * 관리자 동물 목록 조회
	 * - 컨트롤러에서 PageVO(필터+페이징 정보)를 담아서 호출
	 * - 결과를 JSP의 animalList로 넘겨줌
	 */
	List<AnimalVO> selectAdminAnimalList(PageVO pageVO);
	

	
	/**
	 * 관리자 동물 목록 전체 건수
	 * - 컨트롤러에서 totalPage 계산하기 전에 먼저 호출
	 * - 필터 조건이 반영된 건수를 반환
	 */
	int selectAdminAnimalCount(PageVO pageVO);
	
	/**
	 * 동물 입양 상태 변경
	 * - AdminAnimalActionController에서 AJAX 요청 처리 시 호출
	 * - animalId: 변경할 동물 PK
	 * - newStatus: '입양가능' / '입양검토중' / '입양완료'
	 */
	
	int updateAnimalStatus(int animalId, String newStatus);

	/**
	 * 동물 삭제
	 * - AdminAnimalActionController에서 AJAX 요청 처리 시 호출
	 * - animalId: 삭제할 동물 PK
	 * - CASCADE로 하위 테이블 자동 삭제됨
	 */
	int deleteAnimal(int animalId);
	
	/**
	 * 동물 수정 (기본정보 + 의료정보 + 구조정보 + 이미지)
	 * - 4개 테이블을 하나의 트랜잭션으로 처리
	 * - 이미지는 기존 전체 삭제 후 새로 INSERT
	 * - 새 이미지가 없으면 기존 이미지 유지 (imageList가 비어있으면 삭제 안 함)
	 */
	boolean updateAnimal (AnimalVO animal,
						  AnimalMedicalVO medical,
						  AnimalRescueVO rescue,
                          List<AnimalImageVO> imageList,
                          boolean hasNewMainImage);
	
	/**
	 * 수정 페이지 진입 시 기존 데이터 조회
	 * - 기존 AnimalServiceImpl의 getAnimalDetail() 재사용
	 * - AnimalDetailDto에 animal, medical, rescue, imageList 모두 담겨있음
	 */
	AnimalDetailDto getAnimalDetailForEdit(int animalId);
	
	
	// ========================= 게시판 관리 =========================

	/** 전체 게시글 목록 (페이징 + 필터) */
	List<BoardVO> selectAdminBoardList(Map<String, Object> paramMap);

	/** 전체 게시글 건수 */
	int selectAdminBoardCount(Map<String, Object> paramMap);

	/** 숨김 처리된 게시글 건수 */
	int selectHiddenBoardCount();

	/** 신고된 게시글 목록 (페이징) */
	List<BoardVO> selectReportedBoardList(Map<String, Object> paramMap);

	/** 신고된 게시글 건수 */
	int selectReportedBoardCount();

	/** 게시글 숨김/복구 */
	boolean updateBoardHidden(int boardId, String isHidden);

	/** 게시글 강제 삭제 */
	boolean deleteAdminBoard(int boardId);

	/** 신고 사유 목록 조회 */
	List<BoardReportVO> selectBoardReportList(int boardId);

	/** 신고 기각 (STATUS = 'Y') */
	boolean dismissBoardReport(int boardId);
	
	//=======================================================
	public Map<String, Object> selectAnimalDashboardStats();
	public List<Map<String, Object>> selectTodayCombinedSchedule();
	public List<Map<String, Object>> selectMonthCombinedSchedule();
	List<Map<String, Object>> selectMonthCombinedSchedule(String year, String month);
	List<Map<String, Object>> selectRecentActivityFeed();
	// 이번 주 전체 일정 조회 (대시보드 위젯용)
	List<Map<String, Object>> selectWeekCombinedSchedule();
	/**
	 * 이달의 주목 동물 정보 가져오기
	 * @return AnimalVO
	 */
	public AnimalVO selectAnimalOfTheMonth();
	
	/**
	 * 입양 신청 전체 목록 조회
	 * - AdminAdoptionController.doGet() 에서 호출
	 * - 반환값을 req.setAttribute("adoptionList") 로 JSP에 전달
	 */
	List<AdoptionVO> selectAdoptionList();
	 
	/**
	 * 전체 입양 신청 건수
	 * - 통계 카드 "전체 신청" 숫자 표시용
	 */
	int selectAdoptionTotalCount();
	 
	/**
	 * 상태별 입양 신청 건수
	 * - 통계 카드 심사중/승인/반려 각각 호출
	 * - @param status "심사중" / "승인" / "반려"
	 */
	int selectAdoptionCountByStatus(String status);
	 
	/**
	 * 입양 신청 상태 업데이트 (승인 / 반려)
	 * - AdminAdoptionStatusController.doPost() 에서 호출
	 * - 트랜잭션 처리 (성공 시 commit, 실패 시 rollback)
	 * - @param adoptionId  처리할 신청 번호
	 * - @param status      "승인" 또는 "반려"
	 * - @param rejectReason 반려 사유 (승인 시 null 또는 빈 문자열)
	 * - @return 처리 성공 여부
	 */
	boolean updateAdoptionStatus(int adoptionId, String status, String rejectReason);
	
	List<AdoptionVO> selectAdoptionListPaged(Map<String, Object> paramMap);
	
	int selectAnimalCount();
	int selectVolunteerApprovedCount();
	List<AnimalVO> selectPopularAnimals(int limit);
	List<Volunteer_RecruitVO> selectPopularVolunteers(int limit);
	List<BoardVO> selectPopularBoards(int limit);
	List<Map<String, Object>> selectDonorRanking(int limit);
	
	public List<AdoptionVO> selectAdoptionHistoryListPaged(Map<String, Object> paramMap);

	public int selectAdoptionHistoryCount(String statusFilter);

	public AdoptionVO selectAdoptionHistoryDetail(int adoptionId);
	
}
