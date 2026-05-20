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
import kr.or.ddit.util.MybatisUtil;
import kr.or.ddit.volunteer_recruit.vo.Volunteer_ApplyVO;
import kr.or.ddit.volunteer_recruit.vo.Volunteer_RecruitVO;

public class AdminDaoImpl implements IadminDao {
	private static IadminDao dao= new AdminDaoImpl();
	
	
	
	private AdminDaoImpl() {}
	
	public static IadminDao getinsetance() {
		return dao;
	}


	@Override
	public int selectTodayJoinCount(SqlSession session) {
		return session.selectOne("admin.selectTodayJoinCount");
	}

	@Override
	public List<MemberVO> selectMemberList(SqlSession session, PageVO pageVO) {
		
		return session.selectList("admin.selectMemberList", pageVO);
	}

	@Override
	public int selectMemberCount(SqlSession session, PageVO pageVO) {
		
		return session.selectOne("admin.selectMemberCount",pageVO);
	}

	@Override
	public MemberVO selectMemberDetail(SqlSession session, String memberId) {
		
		return session.selectOne("admin.selectMemberDetail",memberId);
	}

	@Override
	public int updateMemberRole(SqlSession session, Map<String, String> param) {
			
		return session.update("admin.updateMemberRole",param);
	}

	@Override
	public int updateMemberStatus(SqlSession session, Map<String, String> param) {
		
		return session.update("admin.updateMemberStatus",param);
	}

	@Override
	public int deleteMember(SqlSession session, String memberId) {
		
		return session.delete("admin.deleteMember",memberId);
	}

	@Override
	public List<Volunteer_RecruitVO> selectRecruitList(SqlSession session) {
		
		return session.selectList("admin.selectRecruitList");
	}

	@Override
	public Volunteer_RecruitVO selectRecruitDetail(SqlSession session, int recruitId) {
		
		return session.selectOne("admin.selectRecruitDetail", recruitId);
	}

	@Override
	public List<Volunteer_ApplyVO> selectApplyListByRecruitId(SqlSession session, int recruitId) {
	
		return session.selectList("admin.selectApplyListByRecruitId", recruitId);
	}

	@Override
	public int updateApplyStatus(SqlSession session, Map<String, Object> param) {
	
		return session.update("admin.updateApplyStatus", param);
	}

	@Override
	public List<AnimalVO> selectAdminAnimalList(SqlSession session, PageVO pageVO) {
	    // PageVO 통째로 넘기면 xml에서 #{animalType}, #{adoptionStatus}, #{startRow}, #{endRow} 등 꺼내씀
		return session.selectList("admin.selectAdminAnimalList", pageVO);
	}

	@Override
	public int selectAdminAnimalCount(SqlSession session, PageVO pageVO) {
		// 필터 조건 반영된 전체 건수 반환
		return session.selectOne("admin.selectAdminAnimalCount", pageVO);
	}

	@Override
	public int updateAnimalStatus(SqlSession session, Map<String, Object> param) {
		// param에 animalId, newStatus 담겨있음 → xml의 #{animalId}, #{newStatus}로 매핑
		return session.update("admin.updateAnimalStatus", param);
	}

	@Override
	public int deleteAnimal(SqlSession session, int animalId) {
		
		return session.delete("admin.deleteAnimal",animalId);
	}

	@Override
	public int updateAnimal(SqlSession session, AnimalVO animal) {
		
		return session.update("animal.updateAnimal",animal);
	}

	@Override
	public int updateAnimalMedical(SqlSession session, AnimalMedicalVO medical) {
		
		return session.update("animal.updateAnimalMedical", medical);
	}

	@Override
	public int updateAnimalRescue(SqlSession session, AnimalRescueVO rescue) {
		return session.update("animal.updateAnimalRescue", rescue);
	}

	@Override
	public int deleteAnimalImages(SqlSession session, int animalId) {
		
		return session.delete("animal.deleteAnimalImages", animalId);
	}
	
	// ========================= 게시판 관리 =========================

	@Override
	public List<BoardVO> selectAdminBoardList(SqlSession session, Map<String, Object> paramMap) {
	    return session.selectList("admin.selectAdminBoardList", paramMap);
	}

	@Override
	public int selectAdminBoardCount(SqlSession session, Map<String, Object> paramMap) {
	    return session.selectOne("admin.selectAdminBoardCount", paramMap);
	}

	@Override
	public int selectHiddenBoardCount(SqlSession session) {
	    return session.selectOne("admin.selectHiddenBoardCount");
	}

	@Override
	public List<BoardVO> selectReportedBoardList(SqlSession session, Map<String, Object> paramMap) {
	    return session.selectList("admin.selectReportedBoardList", paramMap);
	}

	@Override
	public int selectReportedBoardCount(SqlSession session) {
	    return session.selectOne("admin.selectReportedBoardCount");
	}

	@Override
	public int updateBoardHidden(SqlSession session, Map<String, Object> paramMap) {
	    return session.update("admin.updateBoardHidden", paramMap);
	}

	@Override
	public int deleteAdminBoard(SqlSession session, int boardId) {
	    return session.delete("admin.deleteAdminBoard", boardId);
	}

	@Override
	public List<BoardReportVO> selectBoardReportList(SqlSession session, int boardId) {
	    return session.selectList("admin.selectBoardReportList", boardId);
	}

	@Override
	public int updateBoardReportStatus(SqlSession session, Map<String, Object> paramMap) {
	    return session.update("admin.updateBoardReportStatus", paramMap);
	}

	// 오늘의 일정
	@Override
	public List<Map<String, Object>> selectTodayCombinedSchedule(SqlSession session) {
	    
	    return session.selectList("admin.selectTodayCombinedSchedule");
	}

	// 이번 달 전체 일정
	@Override
	public List<Map<String, Object>> selectMonthCombinedSchedule(SqlSession session, Map<String, Object> param) {
	    
	    return session.selectList("admin.selectMonthCombinedSchedule", param );
	}

	// 동물 대시보드 통계 
	@Override
	public Map<String, Object> selectAnimalDashboardStats(SqlSession session) {
	   
	    return session.selectOne("admin.selectAnimalDashboardStats");
	}


	@Override
	public List<AdoptionVO> selectAdoptionList(SqlSession session) {
	    // admin.xml → selectAdoptionList 실행
	    // resultMap="adoptionResultMap" 으로 AdoptionVO + detail 중첩 매핑
	    return session.selectList("admin.selectAdoptionList");
	}
	 
	@Override
	public int selectAdoptionTotalCount(SqlSession session) {
	    // admin.xml → selectAdoptionTotalCount 실행
	    return session.selectOne("admin.selectAdoptionTotalCount");
	}
	 
	@Override
	public int selectAdoptionCountByStatus(SqlSession session, String status) {
	    // admin.xml → selectAdoptionCountByStatus 실행
	    // status 파라미터를 WHERE STATUS = #{status} 에 바인딩
	    return session.selectOne("admin.selectAdoptionCountByStatus", status);
	}
	 
	@Override
	public int updateAdoptionStatus(SqlSession session, Map<String, Object> param) {
	    // admin.xml → updateAdoptionStatus 실행
	    // param: { adoptionId, status, rejectReason } 을 쿼리에 바인딩
	    return session.update("admin.updateAdoptionStatus", param);
	}
	@Override
	public List<AdoptionVO> selectAdoptionListPaged(SqlSession session, Map<String, Object> paramMap) {
	    // admin.xml > selectAdoptionListPaged
	    return session.selectList("admin.selectAdoptionListPaged", paramMap);
	}

	@Override
	public List<Map<String, Object>> selectRecentActivityFeed(SqlSession session) {
	        return session.selectList("admin.selectRecentActivityFeed");
	}

	@Override
	public AnimalVO selectAnimalOfTheMonth(SqlSession session) {
		return session.selectOne("admin.selectAnimalOfTheMonth");
	}

	@Override
	public int selectAnimalCount(SqlSession session) {
	    return session.selectOne("admin.selectAnimalCount");
	}
	 
	@Override
	public int selectVolunteerApprovedCount(SqlSession session) {
	    return session.selectOne("admin.selectVolunteerApprovedCount");
	}
	 
	@Override
	public List<AnimalVO> selectPopularAnimals(SqlSession session, int limit) {
	    return session.selectList("admin.selectPopularAnimals", limit);
	}
	 
	@Override
	public List<Volunteer_RecruitVO> selectPopularVolunteers(SqlSession session, int limit) {
	    return session.selectList("admin.selectPopularVolunteers", limit);
	}
	 
	@Override
	public List<BoardVO> selectPopularBoards(SqlSession session, int limit) {
	    return session.selectList("admin.selectPopularBoards", limit);
	}
	 
	@Override
	public List<Map<String, Object>> selectDonorRanking(SqlSession session, int limit) {
	    return session.selectList("admin.selectDonorRanking", limit);
	}

	@Override
	public List<Map<String, Object>> selectWeekCombinedSchedule(SqlSession session) {
		return session.selectList("admin.selectWeekCombinedSchedule");
	}
	
	public List<AdoptionVO> selectAdoptionHistoryListPaged(SqlSession session, Map<String, Object> paramMap) {
		return session.selectList("admin.selectAdoptionHistoryListPaged", paramMap);
	}

	@Override
	public int selectAdoptionHistoryCount(SqlSession session, String statusFilter) {
		return session.selectOne("admin.selectAdoptionHistoryCount", statusFilter);
	}

	@Override
	public AdoptionVO selectAdoptionHistoryDetail(SqlSession session, int adoptionId) {
		return session.selectOne("admin.selectAdoptionHistoryDetail", adoptionId);
	}

}
