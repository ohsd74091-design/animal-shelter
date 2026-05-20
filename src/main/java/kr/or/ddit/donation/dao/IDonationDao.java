package kr.or.ddit.donation.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import kr.or.ddit.common.vo.PageVO;
import kr.or.ddit.donation.vo.DonationVO;

public interface IDonationDao {
	// 후원 등록
	 int insertDonation(SqlSession session, DonationVO dvo);
	 
	 // 내 후원 목록(마이페이지)
	 public List<DonationVO> selectMyDonationList(SqlSession session, String memberId);
	 // 영수증
	 public DonationVO selectDonationForReceipt(SqlSession session, int donationId, String memberId);

	 public List<DonationVO> selectAdminDonationList(SqlSession session, PageVO pageVO);

	 int updateMemberDonorYn(SqlSession session, String memberId);

	 public int selectAdminDonationCount(SqlSession session, PageVO pageVO);

	 public Map<String, Object> selectDonationStats(SqlSession session);

    /**
     * 영수증 신청 목록 (관리자 영수증 관리 탭)
     */
	 public List<DonationVO> selectReceiptList(SqlSession session);
   
	 public List<DonationVO> selectAdminDonationListAll(SqlSession session, PageVO pageVO);

	 public List<PageVO> selectTopDonorList(SqlSession session);

    /**
     * 후원 내역 수정 (관리자)
     */
	 public int updateDonationByAdmin(SqlSession session, DonationVO dvo);

    /**
     * 후원 내역 삭제 (관리자)
     */
	 public int deleteDonation(SqlSession session, int donationId);
	 
	 
	 public int updateReceiptStatus(SqlSession session, DonationVO dvo);



}
