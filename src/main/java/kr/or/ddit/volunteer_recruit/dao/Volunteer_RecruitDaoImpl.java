package kr.or.ddit.volunteer_recruit.dao;

import java.util.List;

import javax.management.RuntimeErrorException;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;

import kr.or.ddit.common.vo.PageVO;
import kr.or.ddit.volunteer_recruit.vo.Volunteer_ApplyVO;
import kr.or.ddit.volunteer_recruit.vo.Volunteer_RecruitDetailVO;
import kr.or.ddit.volunteer_recruit.vo.Volunteer_RecruitVO;

public class Volunteer_RecruitDaoImpl implements IVolunteer_RecruitDao {
	private static IVolunteer_RecruitDao volunteerDao = new Volunteer_RecruitDaoImpl();
	
	private Volunteer_RecruitDaoImpl() {
		
	}
	
	public static IVolunteer_RecruitDao getinstance() {
		return volunteerDao;
		
	}
	
	@Override
	public int insert_Volunteer_Recruit(SqlSession session, Volunteer_RecruitVO vo) {
		int cnt =0;
		try {
		cnt = session.insert("Volunteer_Recruit.insert_Volunteer_Recruit",vo);
		
		}catch(PersistenceException e){
			e.printStackTrace();
			throw new RuntimeException("봉사활동 모집 글 등록중 오류가 발생하였습니다..",e); 
		}
		return cnt;
	}

	@Override
	public List<Volunteer_RecruitVO> list_Volunteer_Recruit(SqlSession session) {
		List<Volunteer_RecruitVO> list = null;
		
		try {
			list=session.selectList("Volunteer_Recruit.list_Volunteer_Recruit");
		}catch (PersistenceException e) {
			e.printStackTrace();
			throw new RuntimeException("해당 페이지 이동중 오류가 발생하였습니다..",e);
		}
		return list;
	}

	@Override
	public int getTotalCount(SqlSession session,  PageVO pageVO) {
		int cnt = 0;

	    try {
	        cnt = session.selectOne("Volunteer_Recruit.getTotalCount", pageVO);
	    } catch (Exception e) {
	        e.printStackTrace();
	        throw new RuntimeException("봉사 모집글 총 개수 조회 오류", e);
	    }

	    return cnt;
	}

	@Override
	public List<Volunteer_RecruitVO> listByPage(SqlSession session, PageVO pageVO) {
		List<Volunteer_RecruitVO> list = null;

	    try {
	        list = session.selectList("Volunteer_Recruit.listByPage", pageVO);
	    } catch (Exception e) {
	        e.printStackTrace();
	        throw new RuntimeException("봉사 모집글 페이징 조회 오류", e);
	    }

	    return list;
	}

	@Override
	public Volunteer_RecruitVO getVolunteerRecruitDetail(SqlSession session, int recruit_id) {
		Volunteer_RecruitVO vo=null;
		
		try {
			vo=session.selectOne("Volunteer_Recruit.getVolunteerRecruitDetail",recruit_id);
		}catch(PersistenceException e) {
			e.printStackTrace();
			throw new RuntimeException("상세 페이지 이동중 오류가 발생하였습니다..",e);
		}
		return vo;
	}

	@Override
	public List<Volunteer_RecruitDetailVO> getRecruitDetailList(SqlSession session, int recruitId) {
		 List<Volunteer_RecruitDetailVO> list = null;

		    try {
		        list = session.selectList("Volunteer_Recruit.getRecruitDetailList", recruitId);
		    } catch (Exception e) {
		        e.printStackTrace();
		        throw new RuntimeException("모집 상세유형 조회 오류", e);
		    }

		    return list;
		}

	@Override
	public int checkDuplicate(SqlSession session, Volunteer_ApplyVO vo) {
		int cnt =0;
		
		try {
		cnt=session.selectOne("Volunteer_Recruit.checkDuplicate",vo);
		
	}catch(PersistenceException e){
		e.printStackTrace();
		
	}
		return cnt;
	}

	@Override
	public int getApplyCount(SqlSession session, Volunteer_ApplyVO vo) {
		int cnt =0;
		
		cnt=session.selectOne("Volunteer_Recruit.getApplyCount",vo);
		return cnt;
	}

	@Override
	public int getCapacity(SqlSession session, Volunteer_ApplyVO vo) {
		int cnt =0;
		
		cnt=session.selectOne("Volunteer_Recruit.getCapacity",vo);
		return cnt;
	}

	@Override
	public int insertApply(SqlSession session, Volunteer_ApplyVO vo) {
		int cnt=0;
		cnt=session.insert("Volunteer_Recruit.insertApply",vo);
		return cnt;
	}

	@Override
	public int insertVolunteerRecruit(SqlSession session, Volunteer_RecruitVO recruitVO) {
		System.out.println("insert 전 recruitId = " + recruitVO.getRecruitId());
		int cnt =0;
		
		try {
			cnt =session.insert("Volunteer_Recruit.insertVolunteerRecruit", recruitVO);
		}catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException("봉사 모집글 등록 오류",e);
		}
		return cnt;
	}

	@Override
	public int insertVolunteerRecruitDetail(SqlSession session, Volunteer_RecruitDetailVO detailVO) {
		int cnt =0;
		try {
	        cnt = session.insert("Volunteer_Recruit.insertVolunteerRecruitDetail", detailVO);
	    } catch (Exception e) {
	        e.printStackTrace();
	        throw new RuntimeException("봉사 모집 상세유형 등록 오류", e);
	    }

	    return cnt;
	}

	@Override
	public int updateVolunteerRecruit(SqlSession session, Volunteer_RecruitVO recruitVO) {
		return session.update("Volunteer_Recruit.updateVolunteerRecruit", recruitVO);
	}

	@Override
	public int updateVolunteerRecruitDetail(SqlSession session, Volunteer_RecruitDetailVO detailVO) {
		 return session.update("Volunteer_Recruit.updateVolunteerRecruitDetail", detailVO);
	}

	@Override
	public int deleteVolunteerRecruit(SqlSession session, int recruitId) {
		 return session.delete("Volunteer_Recruit.deleteVolunteerRecruit", recruitId);
	}

	@Override
	public int closeExpiredRecruits(SqlSession session) {
		int cnt = 0;

	    try {
	        cnt = session.update("Volunteer_Recruit.closeExpiredRecruits");
	    } catch (Exception e) {
	        e.printStackTrace();
	        throw new RuntimeException("마감일 지난 모집글 상태 변경 오류", e);
	    }

	    return cnt;
	}

	@Override
	public List<Integer> getFullRecruitIds(SqlSession session) {
		List<Integer> list = null;

	    try {
	        list = session.selectList("Volunteer_Recruit.getFullRecruitIds");
	    } catch (Exception e) {
	        e.printStackTrace();
	        throw new RuntimeException("정원 마감 모집글 조회 오류", e);
	    }

	    return list;
	}

	@Override
	public int updateRecruitStatusToClosed(SqlSession session, int recruitId) {
		 int cnt = 0;

		    try {
		        cnt = session.update("Volunteer_Recruit.updateRecruitStatusToClosed", recruitId);
		    } catch (Exception e) {
		        e.printStackTrace();
		        throw new RuntimeException("모집글 상태 마감 처리 오류", e);
		    }

		    return cnt;
	}

	@Override
	public String getRecruitStatus(SqlSession session, int recruitId) {
		String status = null;

	    try {
	        status = session.selectOne("Volunteer_Recruit.getRecruitStatus", recruitId);
	    } catch (Exception e) {
	        e.printStackTrace();
	        throw new RuntimeException("모집글 상태 조회 오류", e);
	    }

	    return status;
	}


}
