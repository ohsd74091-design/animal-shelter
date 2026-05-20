package kr.or.ddit.donation.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import kr.or.ddit.common.vo.PageVO;
import kr.or.ddit.donation.dao.DonationDaoImpl;
import kr.or.ddit.donation.dao.IDonationDao;
import kr.or.ddit.donation.vo.DonationVO;
import kr.or.ddit.util.MybatisUtil;

public class DonationServiceImpl implements IDonationService{
	
	private static IDonationService donationService = new DonationServiceImpl();
	private IDonationDao donationDao;
	private DonationServiceImpl() {
		donationDao = DonationDaoImpl.getInstance();
	}

	public static IDonationService getInstance() {
		return donationService;
		
	}
	
	@Override
	public int insertDonation(DonationVO donationVO) {
		 int cnt = 0;
        try (SqlSession session = MybatisUtil.getsqlsession(false)) {
            cnt = donationDao.insertDonation(session, donationVO);
            if (cnt > 0) session.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            cnt = 0;
        }
        return cnt;

	}

	@Override
	public List<DonationVO> selectMyDonationList(String memberId) {
		List<DonationVO> list = new ArrayList<>();
        try (SqlSession session = MybatisUtil.getsqlsession()) {
            list = donationDao.selectMyDonationList(session, memberId);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;

	}

	@Override
	public DonationVO selectDonationForReceipt(int donationId, String memberId) {
		DonationVO dvo = null;
        try (SqlSession session = MybatisUtil.getsqlsession()) {
            dvo = donationDao.selectDonationForReceipt(session, donationId, memberId);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return dvo;

	}


	@Override
	public int updateMemberDonorYn(String memberId) {
		int cnt =0;
		SqlSession session = MybatisUtil.getsqlsession();
		try {
		cnt=donationDao.updateMemberDonorYn(session, memberId);
		if(cnt >0) session.commit();
		
		}catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}
		
		return cnt;
	}

    @Override
    public PageVO selectAdminDonationData(PageVO pageVO) {
       
        try (SqlSession session = MybatisUtil.getsqlsession()) {
            
            int totalCount  = donationDao.selectAdminDonationCount(session, pageVO);
            pageVO.setTotalCount(totalCount);
            
            if(pageVO.getCountPerPage() == 0) {
            	pageVO.setCountPerPage(totalCount);
            }
            
            if(pageVO.getCountPerPage() == 0) {
            	pageVO.setCountPerPage(1);
            }
            
            int startRow = (pageVO.getCurrentPage() - 1) * pageVO.getCountPerPage() + 1;
            int endRow = pageVO.getCurrentPage() * pageVO.getCountPerPage();
            
            pageVO.setStartRow(startRow);
            pageVO.setEndRow(endRow);
            
            List<DonationVO> list = donationDao.selectAdminDonationList(session, pageVO);
            pageVO.setDonationList(list);
            
        } catch (Exception ex) {
            ex.printStackTrace();
            pageVO.setDonationList(new ArrayList<>());
            pageVO.setTotalCount(0);
        }
        return pageVO;
    }

    @Override
    public Map<String, Object> selectDonationStats() {
        Map<String, Object> stats = new HashMap<>();
        try (SqlSession session = MybatisUtil.getsqlsession()) {
            stats = donationDao.selectDonationStats(session);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return stats;
    }

    /** 영수증 신청 목록 */
    @Override
    public List<DonationVO> selectReceiptList() {
        List<DonationVO> list = new ArrayList<>();
        try (SqlSession session = MybatisUtil.getsqlsession()) {
            list = donationDao.selectReceiptList(session);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    /** 엑셀 다운로드용 — 페이징 없이 전체 */
    @Override
    public List<DonationVO> selectAdminDonationListAll(PageVO pageVO) {
        List<DonationVO> list = new ArrayList<>();
        try (SqlSession session = MybatisUtil.getsqlsession()) {
            list = donationDao.selectAdminDonationListAll(session, pageVO);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    /** 이달의 우수 기부자 TOP 5 */
    @Override
    public List<PageVO> selectTopDonorList() {
        List<PageVO> list = new ArrayList<>();
        try (SqlSession session = MybatisUtil.getsqlsession()) {
            list = donationDao.selectTopDonorList(session);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    /** 후원 내역 수정 (관리자) */
    @Override
    public int updateDonationByAdmin(DonationVO dvo) {
        int cnt = 0;
        try (SqlSession session = MybatisUtil.getsqlsession(false)) {
            cnt = donationDao.updateDonationByAdmin(session, dvo);
            if (cnt > 0) session.commit();
            else         session.rollback();
        } catch (Exception ex) {
            ex.printStackTrace();
            cnt = 0;
        }
        return cnt;
    }

    /** 후원 내역 삭제 (관리자) */
    @Override
    public int deleteDonation(int donationId) {
        int cnt = 0;
        try (SqlSession session = MybatisUtil.getsqlsession(false)) {
            cnt = donationDao.deleteDonation(session, donationId);
            if (cnt > 0) session.commit();
            else         session.rollback();
        } catch (Exception ex) {
            ex.printStackTrace();
            cnt = 0;
        }
        return cnt;
    }

	@Override
	public int updateReceiptStatus(DonationVO dvo) {
		try(SqlSession session = MybatisUtil.getsqlsession(false)) {
			
	        int cnt = donationDao.updateReceiptStatus(session, dvo);
	        if (cnt > 0) session.commit();
	        return cnt;
		}catch(Exception ex) {
			ex.printStackTrace();
			return 0;
		}
	}
}
