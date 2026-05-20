package kr.or.ddit.volunteer_recruit.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import kr.or.ddit.common.vo.PageVO;
import kr.or.ddit.volunteer_recruit.vo.Volunteer_ApplyVO;
import kr.or.ddit.volunteer_recruit.vo.Volunteer_RecruitDetailVO;
import kr.or.ddit.volunteer_recruit.vo.Volunteer_RecruitVO;

public interface IVolunteer_RecruitDao {
	
	public int insert_Volunteer_Recruit(SqlSession session, Volunteer_RecruitVO vo);

	public List<Volunteer_RecruitVO> list_Volunteer_Recruit (SqlSession session);
	
	public int getTotalCount(SqlSession session, PageVO pageVO);
	
	public List<Volunteer_RecruitVO> listByPage(SqlSession session, PageVO pageVO);
	
	public Volunteer_RecruitVO getVolunteerRecruitDetail(SqlSession session, int recruit_id);
	
	public List<Volunteer_RecruitDetailVO> getRecruitDetailList(SqlSession session, int recruitId);
	
	public int checkDuplicate(SqlSession session, Volunteer_ApplyVO vo);
	
	public int getApplyCount(SqlSession session, Volunteer_ApplyVO vo);
	
	public int getCapacity(SqlSession session, Volunteer_ApplyVO vo);
	
	public int insertApply(SqlSession session, Volunteer_ApplyVO vo);
	
	public int insertVolunteerRecruit(SqlSession session, Volunteer_RecruitVO recruitVO);
	
	public int insertVolunteerRecruitDetail(SqlSession session, Volunteer_RecruitDetailVO detailVO);
	
	 public int updateVolunteerRecruit(SqlSession session, Volunteer_RecruitVO recruitVO);
	 
	 public int updateVolunteerRecruitDetail(SqlSession session, Volunteer_RecruitDetailVO detailVO);
	 
	 public int deleteVolunteerRecruit(SqlSession session, int recruitId);
	 
	 public int closeExpiredRecruits(SqlSession session);
	 
	 
	 public List<Integer> getFullRecruitIds(SqlSession session);
	 
	 
	 public int updateRecruitStatusToClosed(SqlSession session, int recruitId);
	 
	 
	 public String getRecruitStatus(SqlSession session, int recruitId);
	
	
	
	
	
	
	
	
	
	
	
	
}
