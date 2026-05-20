package kr.or.ddit.volunteer_recruit.service;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import kr.or.ddit.common.vo.PageVO;
import kr.or.ddit.member.vo.MemberVO;
import kr.or.ddit.util.MybatisUtil;
import kr.or.ddit.volunteer_recruit.dao.IVolunteer_RecruitDao;
import kr.or.ddit.volunteer_recruit.dao.Volunteer_RecruitDaoImpl;
import kr.or.ddit.volunteer_recruit.vo.Volunteer_ApplyVO;
import kr.or.ddit.volunteer_recruit.vo.Volunteer_RecruitDetailVO;
import kr.or.ddit.volunteer_recruit.vo.Volunteer_RecruitVO;

public class Volunteer_RecruitServiceImpl implements IVolunteer_RecruitService {
	private static IVolunteer_RecruitService service = new Volunteer_RecruitServiceImpl();
	private IVolunteer_RecruitDao dao;
	
	
	private  Volunteer_RecruitServiceImpl() {
		dao=Volunteer_RecruitDaoImpl.getinstance();
	}
	
	public static IVolunteer_RecruitService getservice() {
		return service;
		
	}

	@Override
	public int insert_Volunteer_Recruit(MemberVO memvo, Volunteer_RecruitVO vo) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Volunteer_RecruitVO> list_Volunteer_Recruit() {
		List<Volunteer_RecruitVO> list =null;
		SqlSession session =MybatisUtil.getsqlsession();
		
		try {
			list=dao.list_Volunteer_Recruit(session);
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			session.close();
		}
		return list;
	}

	@Override
	public int getTotalCount(PageVO pageVO) {
	    SqlSession session = MybatisUtil.getsqlsession();
	    int cnt = 0;

	    try {
	        cnt = dao.getTotalCount(session, pageVO);
	    } finally {
	        session.close();
	    }

	    return cnt;
	}

	@Override
	public PageVO getPageInfo(int currentPage, int totalCount) {
		PageVO pageVO =new PageVO();
		
		int countPerPage =5; //한페이지당 글 수
		int pageCount = 5; //페이지 번호수 
		
		int totalPage =(int)Math.ceil((double)totalCount/countPerPage);
		
		if(totalPage ==0) totalPage =1;
		if(currentPage <1) currentPage =1;
		if(currentPage > totalPage) currentPage = totalPage;

		int startPage = ((currentPage - 1) / pageCount) * pageCount + 1;
		int endPage = startPage + pageCount - 1;

		if(endPage > totalPage) endPage = totalPage;

		int startRow = (currentPage - 1) * countPerPage + 1;
		int endRow = currentPage * countPerPage;

		pageVO.setCurrentPage(currentPage);
		pageVO.setTotalCount(totalCount);
		pageVO.setCountPerPage(countPerPage);
		pageVO.setPageCount(pageCount);
		pageVO.setTotalPage(totalPage);
		pageVO.setStartPage(startPage);
		pageVO.setEndPage(endPage);
		pageVO.setStartRow(startRow);
		pageVO.setEndRow(endRow);
		
		return pageVO;
	}
	
	private String buildRecruitSummary(List<Volunteer_RecruitDetailVO> detailList) {
	    if(detailList == null || detailList.isEmpty()) {
	        return "모집 정보 없음";
	    }

	    StringBuilder sb = new StringBuilder();

	    for(int i = 0; i < detailList.size(); i++) {
	        Volunteer_RecruitDetailVO vo = detailList.get(i);

	        if(i > 0) {
	            sb.append(" ");
	        }

	        sb.append(vo.getInterestType())
	          .append(" ")
	          .append(vo.getApplyCount())
	          .append("/")
	          .append(vo.getCapacity());
	    }

	    return sb.toString();
	}

	@Override
	public List<Volunteer_RecruitVO> listByPage(PageVO pageVO) {
		SqlSession session = MybatisUtil.getsqlsession();
	    List<Volunteer_RecruitVO> list = null;

	    try {
	        list = dao.listByPage(session, pageVO);

	        if(list != null) {
	            for(Volunteer_RecruitVO vo : list) {
	                List<Volunteer_RecruitDetailVO> detailList = dao.getRecruitDetailList(session, vo.getRecruitId());
	                vo.setRecruitSummary(buildRecruitSummary(detailList));
	            }
	        }

	    } finally {
	        session.close();
	    }

	    return list;
	}

	@Override
	public Volunteer_RecruitVO getVolunteerRecruitDetail(int recruit_id) {
		SqlSession session =MybatisUtil.getsqlsession();
		Volunteer_RecruitVO vo=null;
		
		try {
			vo=dao.getVolunteerRecruitDetail(session, recruit_id);
			
		}finally {
			session.close();
		}
		return vo;
	}

	@Override
	public List<Volunteer_RecruitDetailVO> getRecruitDetailList(int recruitId) {
		 SqlSession session = MybatisUtil.getsqlsession();
		    List<Volunteer_RecruitDetailVO> list = null;

		    try {
		        list = dao.getRecruitDetailList(session, recruitId);
		    } finally {
		        session.close();
		    }

		    return list;
		}

	@Override
	public boolean applyVolunteer(Volunteer_ApplyVO vo) {
	    SqlSession session = MybatisUtil.getsqlsession(false);

	    	try {
	            System.out.println("memberId = " + vo.getMemberId());
	            System.out.println("recruitId = " + vo.getRecruitId());
	            System.out.println("interestType = " + vo.getInterestType());

	            // 0. 모집 상태 체크
	            String recruitStatus = dao.getRecruitStatus(session, vo.getRecruitId());
	            System.out.println("recruitStatus = " + recruitStatus);

	            if (!"모집중".equals(recruitStatus)) {
	                System.out.println("모집마감 상태라 신청 불가");
	                return false;
	            }

	            // 1. 중복신청 체크
	            int dup = dao.checkDuplicate(session, vo);
	            System.out.println("dup = " + dup);

	            if (dup > 0) {
	                System.out.println("중복 신청");
	                return false;
	            }

	            // 2. 현재 신청인원
	            int current = dao.getApplyCount(session, vo);
	            System.out.println("current = " + current);

	            // 3. 정원
	            int capacity = dao.getCapacity(session, vo);
	            System.out.println("capacity = " + capacity);

	            if (capacity <= 0) {
	                System.out.println("정원 조회 실패");
	                return false;
	            }

	            if (current >= capacity) {
	                System.out.println("정원 초과");
	                return false;
	            }

	            // 4. 봉사 날짜 세팅
	            Volunteer_RecruitVO recruit = dao.getVolunteerRecruitDetail(session, vo.getRecruitId());
	            vo.setVolunteerDate(recruit.getVolunteerDate());

	            // 5. 신청 insert
	            int cnt = dao.insertApply(session, vo);
	            System.out.println("insert cnt = " + cnt);

	            if (cnt > 0) {
	                session.commit();
	                return true;
	            }

	        } catch (Exception e) {
	            e.printStackTrace();
	            session.rollback();
	        } finally {
	            session.close();
	        }

	        return false;
	    }

	@Override
	public boolean insertVolunteerRecruit(Volunteer_RecruitVO recruitVO, Volunteer_RecruitDetailVO cleaningVO,
			Volunteer_RecruitDetailVO walkingVO) {
		SqlSession session = MybatisUtil.getsqlsession(false);

	    try {
	        int cnt = dao.insertVolunteerRecruit(session, recruitVO);

	        if (cnt <= 0) {
	            return false;
	        }

	        int recruitId = recruitVO.getRecruitId();
	        	
	        cleaningVO.setRecruitId(recruitId);
	        walkingVO.setRecruitId(recruitId);

	        int cnt1 = dao.insertVolunteerRecruitDetail(session, cleaningVO);
	        int cnt2 = dao.insertVolunteerRecruitDetail(session, walkingVO);
	        System.out.println("insert 후 recruitId = " + recruitVO.getRecruitId());
	        if (cnt1 <= 0 || cnt2 <= 0) {
	            session.rollback();
	            return false;
	        }

	        session.commit();
	        return true;

	    } catch (Exception e) {
	        e.printStackTrace();
	        session.rollback();
	        return false;
	    } finally {
	        session.close();
	    }
	}

	@Override
	public boolean updateVolunteerRecruit(Volunteer_RecruitVO recruitVO, Volunteer_RecruitDetailVO cleaningVO,
			Volunteer_RecruitDetailVO walkingVO) {
		SqlSession session = MybatisUtil.getsqlsession(false);

	    try {
	        int cnt1 = dao.updateVolunteerRecruit(session, recruitVO);
	        int cnt2 = dao.updateVolunteerRecruitDetail(session, cleaningVO);
	        int cnt3 = dao.updateVolunteerRecruitDetail(session, walkingVO);

	        if (cnt1 > 0 && cnt2 > 0 && cnt3 > 0) {
	            session.commit();
	            return true;
	        }

	        session.rollback();
	        return false;

	    } catch (Exception e) {
	        e.printStackTrace();
	        session.rollback();
	        return false;
	    } finally {
	        session.close();
	    }
	}

	@Override
	public boolean deleteVolunteerRecruit(int recruitId) {
		SqlSession session = MybatisUtil.getsqlsession(false);

	    try {
	        int cnt = dao.deleteVolunteerRecruit(session, recruitId);

	        if (cnt > 0) {
	            session.commit();
	            return true;
	        }

	        session.rollback();
	        return false;

	    } catch (Exception e) {
	        e.printStackTrace();
	        session.rollback();
	        return false;
	    } finally {
	        session.close();
	    }
	}

	@Override
	public void refreshRecruitStatus() {
		SqlSession session = MybatisUtil.getsqlsession(false);

	    try {
	        // 1. 마감일 지난 글 마감 처리
	        dao.closeExpiredRecruits(session);

	        // 2. 정원 다 찬 글 마감 처리
	        List<Integer> fullIds = dao.getFullRecruitIds(session);

	        if (fullIds != null) {
	            for (int recruitId : fullIds) {
	                dao.updateRecruitStatusToClosed(session, recruitId);
	            }
	        }

	        session.commit();

	    } catch (Exception e) {
	        e.printStackTrace();
	        session.rollback();
	    } finally {
	        session.close();
	    }
	}
}
	
	