package kr.or.ddit.admin.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import kr.or.ddit.admin.dao.AdminDaoImpl;
import kr.or.ddit.admin.dao.IadminDao;
import kr.or.ddit.adoption.vo.AdoptionVO;
import kr.or.ddit.animal.dao.AnimalDaoImpl;
import kr.or.ddit.animal.dao.IAnimalDao;
import kr.or.ddit.animal.dto.AnimalDetailDto;
import kr.or.ddit.animal.service.AnimalServiceImpl;
import kr.or.ddit.animal.service.IAnimalService;
import kr.or.ddit.animal.vo.AnimalImageVO;
import kr.or.ddit.animal.vo.AnimalMedicalVO;
import kr.or.ddit.animal.vo.AnimalRescueVO;
import kr.or.ddit.animal.vo.AnimalVO;
import kr.or.ddit.board.vo.BoardReportVO;
import kr.or.ddit.board.vo.BoardVO;
import kr.or.ddit.common.vo.PageVO;
import kr.or.ddit.member.vo.MemberVO;
import kr.or.ddit.notification.service.INotificationService;
import kr.or.ddit.notification.service.NotificationServiceImpl;
import kr.or.ddit.util.MybatisUtil;
import kr.or.ddit.volunteer_recruit.vo.Volunteer_ApplyVO;
import kr.or.ddit.volunteer_recruit.vo.Volunteer_RecruitVO;

public class AdminServiceImpl implements IadminService {
	private IadminDao dao = null;
	private static IadminService service= new AdminServiceImpl();

 private AdminServiceImpl(){
	 dao =AdminDaoImpl.getinsetance();	 
 }
 
 public static IadminService getinsetance() {
	 return service;
 }
 
	@Override
	public Map<String, Object> selectMemberListData(PageVO pageVO) {
		SqlSession session = MybatisUtil.getsqlsession();
		Map<String, Object> resultMap =new HashMap<String, Object>();
		try {
			List<MemberVO> list=dao.selectMemberList(session,pageVO);
			int totalCount =dao.selectMemberCount(session,pageVO);
			int todayCount = dao.selectTodayJoinCount(session);
			
			resultMap.put("memberList", list);
			resultMap.put("totalCount", totalCount);
			resultMap.put("todayCount", todayCount);
			
		}finally {
			session.close();
		}
		return resultMap;
	}

	@Override
	public int selectMemberCount(PageVO pageVO) {
		SqlSession session = MybatisUtil.getsqlsession();
		int count =0;
		
		try {
			count=dao.selectMemberCount(session, pageVO);
		}finally {
			session.close();
		}
		return count;
	}

	@Override
	public MemberVO selectMemberDetail(String memberId) {
		SqlSession session =MybatisUtil.getsqlsession();
		MemberVO member=null;
		
		try {
		member=dao.selectMemberDetail(session, memberId);
		
		}finally {
			session.close();
		}
		return member;
	}

	@Override
	public int updateMemberRole(String memberId, String role) {
		SqlSession session =MybatisUtil.getsqlsession();
		
		Map<String, String> param = new HashMap<String, String>();
		param.put("memberId", memberId);
		param.put("role", role);
		
		int cnt =0;
		
		try {
			
			cnt=dao.updateMemberRole(session,param);
			
			if(cnt >0) {
				session.commit();
			}else {
				session.rollback();
			}
		}catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}
		finally {
			session.close();
		}
		return cnt;
	}

	@Override
	public int updateMemberStatus(String memberId, String status) {
		SqlSession session =MybatisUtil.getsqlsession();
		
		Map<String, String> param = new HashMap<String, String>();
		param.put("memberId", memberId);
		param.put("status", status);
		
		int cnt =0;
		
		try {
			
			cnt=dao.updateMemberStatus(session,param);
			
			if(cnt >0) {
				session.commit();
			}else {
				session.rollback();
			}
		}catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}
		finally {
			session.close();
		}
		return cnt;
	}
	

	@Override
	public int deleteMember(String memberId) {
SqlSession session =MybatisUtil.getsqlsession();

		int cnt =0;
		
		try {
			
			cnt=dao.deleteMember(session,memberId);
			
			if(cnt >0) {
				session.commit();
			}else {
				session.rollback();
			}
		}catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}
		finally {
			session.close();
		}
		return cnt;
	}

	@Override
	public List<Volunteer_RecruitVO> selectRecruitList() {
		SqlSession session =MybatisUtil.getsqlsession();
		List<Volunteer_RecruitVO> list =null;
		
		try {
			list =dao.selectRecruitList(session);
		}finally {
			session.close();
		}
		return list;
	}

	@Override
	public Volunteer_RecruitVO selectRecruitDetail(int recruitId) {
		SqlSession session = MybatisUtil.getsqlsession();
		Volunteer_RecruitVO vo = null;

		try {
			vo = dao.selectRecruitDetail(session, recruitId);
		} finally {
			session.close();
		}

		return vo;
	}

	@Override
	public List<Volunteer_ApplyVO> selectApplyListByRecruitId(int recruitId) {
		SqlSession session = MybatisUtil.getsqlsession();
		List<Volunteer_ApplyVO> list = null;

		try {
			list = dao.selectApplyListByRecruitId(session, recruitId);
		} finally {
			session.close();
		}

		return list;
	}

	@Override
	public int updateApplyStatus(Map<String, Object> param) {
		SqlSession session = MybatisUtil.getsqlsession();
		int result = 0;

		try {
			result = dao.updateApplyStatus(session, param);

			if (result > 0) {
				session.commit();
			} else {
				session.rollback();
			}
		} catch (Exception e) {
			session.rollback();
			throw e;
		} finally {
			session.close();
		}

		return result;
	}

	@Override
	public List<AnimalVO> selectAdminAnimalList(PageVO pageVO) {
		 SqlSession session = MybatisUtil.getsqlsession();
		    List<AnimalVO> list = null;
		    try {
		        list = dao.selectAdminAnimalList(session, pageVO);
		    } finally {
		        // 조회(SELECT)는 commit/rollback 불필요, finally에서 무조건 close
		        session.close();
		    }
		    return list;
	}

	@Override
	public int updateAnimalStatus(int animalId, String newStatus) {
		SqlSession session = MybatisUtil.getsqlsession();

	    // map에 두 값을 담아서 DAO로 전달 → xml의 #{animalId}, #{newStatus}로 매핑
	    Map<String, Object> param = new HashMap<>();
	    param.put("animalId", animalId);
	    param.put("newStatus", newStatus);

	    int cnt = 0;
	    try {
	        cnt = dao.updateAnimalStatus(session, param);
	        // DML은 반드시 commit/rollback 처리
	        if (cnt > 0) {
	            session.commit();

	            // ✅ 찜한 동물이 입양완료된 경우 알림 발송
	            // FAVORITE_ANIMAL 테이블에서 해당 동물을 찜한 회원 목록을 조회한 뒤
	            // NOTIFICATION 테이블에 알림을 INSERT 한다.
	            if ("입양완료".equals(newStatus)) {
	                INotificationService notificationService = NotificationServiceImpl.getInstance();
	                notificationService.notifyFavoriteAnimalAdopted(animalId);
	            }
	        } else {
	            session.rollback();
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        session.rollback();
	    } finally {
	        session.close();
	    }
	    return cnt;
	}

	@Override
	public int deleteAnimal(int animalId) {
	    SqlSession session = null;
	    int result = 0;

	    try {
	        session = MybatisUtil.getsqlsession();

	        // 1. 자식 테이블 먼저 삭제
	        session.delete("admin.deleteFavoriteByAnimalId", animalId);
	        session.delete("admin.deleteAdoptionByAnimalId", animalId);

	        // 필요하면 이것들도 추가
	        session.delete("admin.deleteAnimalImageByAnimalId", animalId);
	        session.delete("admin.deleteAnimalMedicalByAnimalId", animalId);
	        session.delete("admin.deleteAnimalRescueByAnimalId", animalId);

	        // 2. 부모 삭제
	        result = session.delete("admin.deleteAnimal", animalId);

	        session.commit();

	    } catch (Exception e) {
	        if(session != null) session.rollback();
	        e.printStackTrace();
	    } finally {
	        if(session != null) session.close();
	    }

	    return result;
	}

	@Override
	public int selectAdminAnimalCount(PageVO pageVO) {
		SqlSession session = MybatisUtil.getsqlsession();
	    int count = 0;
	    try {
	        count = dao.selectAdminAnimalCount(session, pageVO);
	    } finally {
	        session.close();
	    }
	    return count;
	}

	@Override
	public boolean updateAnimal(AnimalVO animal, AnimalMedicalVO medical, AnimalRescueVO rescue,
			List<AnimalImageVO> imageList, boolean hasNewMainImage) {
		SqlSession session =MybatisUtil.getsqlsession();
		 boolean isSuccess = false;
		try {
			//1. 동물 기본정보 수정
			int cnt = dao.updateAnimal(session, animal);
			
			if(cnt == 0 ) {
				session.rollback();
				return false;
			}
			//2. 의료정보수정
			if(medical !=null) {
				dao.updateAnimalMedical(session, medical);
			}
			
			//3. 동물 구조정보 수정
			 if (rescue != null) {
		            dao.updateAnimalRescue(session, rescue);
		        }
			 

		        // 4. 이미지 처리
		        // 새 이미지가 있을 때만 기존 이미지 삭제 후 새로 INSERT
		        // 새 이미지가 없으면 기존 이미지 그대로 유지
		        if (hasNewMainImage && imageList != null && !imageList.isEmpty()) {
		            // 기존 이미지 전체 삭제
		            dao.deleteAnimalImages(session, animal.getAnimalId());
		            
		            // 새 이미지 INSERT
		            // insertAnimalImage는 기존 IadminDao에 없으므로 AnimalDaoImpl 재사용
		            IAnimalDao<SqlSession> animalDao = AnimalDaoImpl.getInstance();
		            for (AnimalImageVO image : imageList) {
		                image.setAnimalId(animal.getAnimalId());
		                animalDao.insertAnimalImage(session, image);
		            	}
		        	}
		        session.commit();
		        isSuccess = true;
		
	}catch (Exception e) {
		e.printStackTrace();
        session.rollback();
        return false;
	}finally {
		session.close();
	}
		return isSuccess;
}

	@Override
	public AnimalDetailDto getAnimalDetailForEdit(int animalId) {
		// 기존 AnimalServiceImpl의 getAnimalDetail() 재사용
	    // 수정 페이지 진입 시 기존 데이터 조회용
	    IAnimalService animalService = AnimalServiceImpl.getInstance();
	    return animalService.getAnimalDetail(animalId);
	}

	// ========================= 게시판 관리 =========================

	@Override
	public List<BoardVO> selectAdminBoardList(Map<String, Object> paramMap) {
	    try (SqlSession session = MybatisUtil.getsqlsession()) {
	        return dao.selectAdminBoardList(session, paramMap);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}

	@Override
	public int selectAdminBoardCount(Map<String, Object> paramMap) {
	    try (SqlSession session = MybatisUtil.getsqlsession()) {
	        return dao.selectAdminBoardCount(session, paramMap);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return 0;
	    }
	}

	@Override
	public int selectHiddenBoardCount() {
	    try (SqlSession session = MybatisUtil.getsqlsession()) {
	        return dao.selectHiddenBoardCount(session);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return 0;
	    }
	}

	@Override
	public List<BoardVO> selectReportedBoardList(Map<String, Object> paramMap) {
	    try (SqlSession session = MybatisUtil.getsqlsession()) {
	        return dao.selectReportedBoardList(session, paramMap);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}

	@Override
	public int selectReportedBoardCount() {
	    try (SqlSession session = MybatisUtil.getsqlsession()) {
	        return dao.selectReportedBoardCount(session);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return 0;
	    }
	}

	@Override
	public boolean updateBoardHidden(int boardId, String isHidden) {
	    boolean isSuccess = false;
	    try (SqlSession session = MybatisUtil.getsqlsession(false)) {
	        Map<String, Object> param = new HashMap<>();
	        param.put("boardId",  boardId);
	        param.put("isHidden", isHidden);
	        int cnt = dao.updateBoardHidden(session, param);
	        if (cnt > 0) {
	            session.commit();
	            isSuccess = true;
	        } else {
	            session.rollback();
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return isSuccess;
	}

	@Override
	public boolean deleteAdminBoard(int boardId) {
	    boolean isSuccess = false;
	    try (SqlSession session = MybatisUtil.getsqlsession(false)) {
	        int cnt = dao.deleteAdminBoard(session, boardId);
	        if (cnt > 0) {
	            session.commit();
	            isSuccess = true;
	        } else {
	            session.rollback();
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return isSuccess;
	}

	@Override
	public List<BoardReportVO> selectBoardReportList(int boardId) {
	    try (SqlSession session = MybatisUtil.getsqlsession()) {
	        return dao.selectBoardReportList(session, boardId);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}

	@Override
	public boolean dismissBoardReport(int boardId) {
	    // 신고 기각: BOARD_REPORT STATUS = 'Y' 로 업데이트
	    boolean isSuccess = false;
	    try (SqlSession session = MybatisUtil.getsqlsession(false)) {
	        Map<String, Object> param = new HashMap<>();
	        param.put("boardId", boardId);
	        param.put("status",  "Y"); // Y = 처리완료(기각)
	        int cnt = dao.updateBoardReportStatus(session, param);
	        if (cnt > 0) {
	            session.commit();
	            isSuccess = true;
	        } else {
	            session.rollback();
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return isSuccess;
	}

	// 동물 통계
	@Override
	public Map<String, Object> selectAnimalDashboardStats() {
	    try (SqlSession session = MybatisUtil.getsqlsession()) {
	        return dao.selectAnimalDashboardStats(session);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return new HashMap<>();
	    }
	}

	// 오늘의 일정 목록
	@Override
	public List<Map<String, Object>> selectTodayCombinedSchedule() {
	    try (SqlSession session = MybatisUtil.getsqlsession()) {
	        return dao.selectTodayCombinedSchedule(session);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return new ArrayList<>();
	    }
	}

	// ── 이번 달 전체 일정 목록 (캘린더용) ── 기존 메서드 수정
	@Override
	public List<Map<String, Object>> selectMonthCombinedSchedule() {
	    try (SqlSession session = MybatisUtil.getsqlsession()) {

	        return dao.selectMonthCombinedSchedule(session, new HashMap<>());
	    } catch (Exception e) {
	        e.printStackTrace();
	        return new ArrayList<>();
	    }
	}
	
	// ── 특정 년/월 일정 목록 (헤더 캘린더 AJAX용) ──
	@Override
	public List<Map<String, Object>> selectMonthCombinedSchedule(String year, String month) {
	    try (SqlSession session = MybatisUtil.getsqlsession()) {
	        Map<String, Object> param = new HashMap<>();
	        param.put("year",  year);
	        param.put("month", month);
	        return dao.selectMonthCombinedSchedule(session, param);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return new ArrayList<>();
	    }
	}

	@Override
	public List<AdoptionVO> selectAdoptionList() {
	    // try-with-resources 로 SqlSession 자동 닫힘 보장
	    try (SqlSession session = MybatisUtil.getsqlsession()) {
	        return dao.selectAdoptionList(session);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}
	 
	@Override
	public int selectAdoptionTotalCount() {
	    try (SqlSession session = MybatisUtil.getsqlsession()) {
	        return dao.selectAdoptionTotalCount(session);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return 0;
	    }
	}
	 
	@Override
	public int selectAdoptionCountByStatus(String status) {
	    try (SqlSession session = MybatisUtil.getsqlsession()) {
	        return dao.selectAdoptionCountByStatus(session, status);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return 0;
	    }
	}
	 
	@Override
	public boolean updateAdoptionStatus(int adoptionId, String status, String rejectReason) {
	    boolean isSuccess = false;
	    // autoCommit=false 로 수동 트랜잭션 처리
	    try (SqlSession session = MybatisUtil.getsqlsession(false)) {
	        Map<String, Object> param = new HashMap<>();
	        param.put("adoptionId",   adoptionId);
	        param.put("status",       status);
	        param.put("rejectReason", rejectReason); // 승인 시 null → jdbcType=VARCHAR 로 null 처리
	        int cnt = dao.updateAdoptionStatus(session, param);
	        if (cnt > 0) {
	            session.commit();   // 성공 시 커밋
	            isSuccess = true;
	        } else {
	            session.rollback(); // 실패 시 롤백
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return isSuccess;
	}
	@Override
	public List<AdoptionVO> selectAdoptionListPaged(Map<String, Object> paramMap) {
	    try (SqlSession session = MybatisUtil.getsqlsession()) {
	        return dao.selectAdoptionListPaged(session, paramMap);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}

	@Override
	public List<Map<String, Object>> selectRecentActivityFeed() {
		try (SqlSession session = MybatisUtil.getsqlsession()) {
	        return dao.selectRecentActivityFeed(session);
	    }
	}

	@Override
	public AnimalVO selectAnimalOfTheMonth() {
		try (SqlSession session = MybatisUtil.getsqlsession()) {
	        return dao.selectAnimalOfTheMonth(session);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}
	
	
	public int selectAnimalCount() {
	    try (SqlSession session = MybatisUtil.getsqlsession()) {
	        return dao.selectAnimalCount(session);
	    } catch (Exception e) { e.printStackTrace(); return 0; }
	}
	 
	@Override
	public int selectVolunteerApprovedCount() {
	    try (SqlSession session = MybatisUtil.getsqlsession()) {
	        return dao.selectVolunteerApprovedCount(session);
	    } catch (Exception e) { e.printStackTrace(); return 0; }
	}
	 
	@Override
	public List<AnimalVO> selectPopularAnimals(int limit) {
	    try (SqlSession session = MybatisUtil.getsqlsession()) {
	        return dao.selectPopularAnimals(session, limit);
	    } catch (Exception e) { e.printStackTrace(); return null; }
	}
	 
	@Override
	public List<Volunteer_RecruitVO> selectPopularVolunteers(int limit) {
	    try (SqlSession session = MybatisUtil.getsqlsession()) {
	        return dao.selectPopularVolunteers(session, limit);
	    } catch (Exception e) { e.printStackTrace(); return null; }
	}
	 
	@Override
	public List<BoardVO> selectPopularBoards(int limit) {
	    try (SqlSession session = MybatisUtil.getsqlsession()) {
	        return dao.selectPopularBoards(session, limit);
	    } catch (Exception e) { e.printStackTrace(); return null; }
	}
	 
	@Override
	public List<Map<String, Object>> selectDonorRanking(int limit) {
	    try (SqlSession session = MybatisUtil.getsqlsession()) {
	        return dao.selectDonorRanking(session, limit);
	    } catch (Exception e) { e.printStackTrace(); return null; }
	}

	// 이번 주 전체 일정 조회
	@Override
	public List<Map<String, Object>> selectWeekCombinedSchedule() {
	    try (SqlSession session = MybatisUtil.getsqlsession()) {
	        return dao.selectWeekCombinedSchedule(session);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return new ArrayList<>();
	    }
	}
	 
	@Override
	public List<AdoptionVO> selectAdoptionHistoryListPaged(Map<String, Object> paramMap) {
		try (SqlSession session = MybatisUtil.getsqlsession()) {
			return dao.selectAdoptionHistoryListPaged(session, paramMap);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public int selectAdoptionHistoryCount(String statusFilter) {
		try (SqlSession session = MybatisUtil.getsqlsession()) {
			return dao.selectAdoptionHistoryCount(session, statusFilter);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public AdoptionVO selectAdoptionHistoryDetail(int adoptionId) {
		try (SqlSession session = MybatisUtil.getsqlsession()) {
			return dao.selectAdoptionHistoryDetail(session, adoptionId);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
