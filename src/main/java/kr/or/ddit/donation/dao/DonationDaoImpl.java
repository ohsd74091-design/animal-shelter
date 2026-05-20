package kr.or.ddit.donation.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;

import kr.or.ddit.common.vo.PageVO;
import kr.or.ddit.donation.vo.DonationVO;

public class DonationDaoImpl implements IDonationDao{
	
	 private static IDonationDao donationDao = new DonationDaoImpl();
     private DonationDaoImpl() {}
     public static IDonationDao getInstance() {
    	 return donationDao; 
     }


	@Override
	public int insertDonation(SqlSession session, DonationVO dvo) {
		 try {
            return session.insert("donation.insertDonation", dvo);
        } catch (PersistenceException ex) {
            ex.printStackTrace();
            throw new RuntimeException("후원 등록 중 예외 발생!", ex);
        }

	}

	@Override
	public List<DonationVO> selectMyDonationList(SqlSession session, String memberId) {
		 try {
            return session.selectList("donation.selectMyDonationList", memberId);
        } catch (PersistenceException ex) {
            ex.printStackTrace();
            throw new RuntimeException("후원 목록 조회 중 예외 발생!", ex);
        }

	}

	@Override
	public DonationVO selectDonationForReceipt(SqlSession session, int donationId, String memberId) {
		 try {
	        Map<String, Object> param = new HashMap<>();
            param.put("donationId", donationId);
            param.put("memberId", memberId);
            return session.selectOne("donation.selectDonationForReceipt", param);
        } catch (PersistenceException ex) {
            ex.printStackTrace();
            throw new RuntimeException("영수증 조회 중 예외 발생!", ex);
        }
	}

	
	@Override
	public int updateMemberDonorYn(SqlSession session, String memberId) {
		
		return session.update("donation.updateMemberDonorYn",memberId);
	}


    /** 관리자 후원 목록 — 페이징 + 필터 */
    @Override
    public List<DonationVO> selectAdminDonationList(SqlSession session, PageVO pageVO) {
        try {
        	List<DonationVO> list = session.selectList("donation.selectAdminDonationList", pageVO);
        	
            return list;
        } catch (PersistenceException ex) {
            ex.printStackTrace();
            throw new RuntimeException("관리자 후원 목록 조회 중 예외 발생!", ex);
        }
    }

    /** 관리자 후원 건수 — 페이징 계산 */
    @Override
    public int selectAdminDonationCount(SqlSession session, PageVO pageVO) {
        try {
            return session.selectOne("donation.selectAdminDonationCount", pageVO);
        } catch (PersistenceException ex) {
            ex.printStackTrace();
            throw new RuntimeException("관리자 후원 건수 조회 중 예외 발생!", ex);
        }
    }

    /** 통계 요약 (stat 카드 4개) */
    @Override
    public Map<String, Object> selectDonationStats(SqlSession session) {
        try {
            return session.selectOne("donation.selectDonationStats");
        } catch (PersistenceException ex) {
            ex.printStackTrace();
            throw new RuntimeException("후원 통계 조회 중 예외 발생!", ex);
        }
    }

    /** 영수증 신청 목록 */
    @Override
    public List<DonationVO> selectReceiptList(SqlSession session) {
        try {
            return session.selectList("donation.selectReceiptList");
        } catch (PersistenceException ex) {
            ex.printStackTrace();
            throw new RuntimeException("영수증 목록 조회 중 예외 발생!", ex);
        }
    }

    /** 엑셀 다운로드용 — 전체 조회 */
    @Override
    public List<DonationVO> selectAdminDonationListAll(SqlSession session, PageVO pageVO) {
        try {
            return session.selectList("donation.selectAdminDonationListAll", pageVO);
        } catch (PersistenceException ex) {
            ex.printStackTrace();
            throw new RuntimeException("엑셀용 후원 목록 조회 중 예외 발생!", ex);
        }
    }

    /** 이달의 우수 기부자 TOP 5 */
    @Override
    public List<PageVO> selectTopDonorList(SqlSession session) {
        try {
            return session.selectList("donation.selectTopDonorList");
        } catch (PersistenceException ex) {
            ex.printStackTrace();
            throw new RuntimeException("TOP 기부자 조회 중 예외 발생!", ex);
        }
    }

    /** 후원 내역 수정 (관리자) */
    @Override
    public int updateDonationByAdmin(SqlSession session, DonationVO dvo) {
        try {
            return session.update("donation.updateDonationByAdmin", dvo);
        } catch (PersistenceException ex) {
            ex.printStackTrace();
            throw new RuntimeException("후원 수정 중 예외 발생!", ex);
        }
    }

    /** 후원 내역 삭제 (관리자) */
    @Override
    public int deleteDonation(SqlSession session, int donationId) {
        try {
            return session.delete("donation.deleteDonation", donationId);
        } catch (PersistenceException ex) {
            ex.printStackTrace();
            throw new RuntimeException("후원 삭제 중 예외 발생!", ex);
        }
    }
	@Override
	public int updateReceiptStatus(SqlSession session, DonationVO dvo) {
		 try {
            return session.update("donation.updateReceiptStatus", dvo);
        } catch (PersistenceException ex) {
            ex.printStackTrace();
            throw new RuntimeException("후원 영수증 발급 등록 중 예외 발생!", ex);
        }
	}
}
