package kr.or.ddit.volunteer_recruit.service;

import java.util.List;

import kr.or.ddit.common.vo.PageVO;
import kr.or.ddit.member.vo.MemberVO;
import kr.or.ddit.volunteer_recruit.vo.Volunteer_ApplyVO;
import kr.or.ddit.volunteer_recruit.vo.Volunteer_RecruitDetailVO;
import kr.or.ddit.volunteer_recruit.vo.Volunteer_RecruitVO;

public interface IVolunteer_RecruitService {
	
	public int insert_Volunteer_Recruit(MemberVO memvo, Volunteer_RecruitVO vo);
	
	 public List<Volunteer_RecruitVO> list_Volunteer_Recruit();
	 
	 public int getTotalCount(PageVO pageVO);
	 
	 public PageVO getPageInfo(int currentPage, int totalCount);
	 
	 public List<Volunteer_RecruitVO> listByPage(PageVO pageVO);
	 
	 public Volunteer_RecruitVO getVolunteerRecruitDetail(int recruit_id);
	 
	 public List<Volunteer_RecruitDetailVO> getRecruitDetailList(int recruitId);
	 
	 public boolean applyVolunteer(Volunteer_ApplyVO vo);
	 
	 public boolean insertVolunteerRecruit(Volunteer_RecruitVO recruitVO,
			 						Volunteer_RecruitDetailVO cleaningVO,
			 						Volunteer_RecruitDetailVO walkingVO);
	 
	 public boolean updateVolunteerRecruit(Volunteer_RecruitVO recruitVO,
             Volunteer_RecruitDetailVO cleaningVO,
             Volunteer_RecruitDetailVO walkingVO);

public boolean deleteVolunteerRecruit(int recruitId);
	 
public void refreshRecruitStatus();
	 
}
