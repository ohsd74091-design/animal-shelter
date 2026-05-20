package kr.or.ddit.donation.service;

import java.util.List;
import java.util.Map;

import kr.or.ddit.common.vo.PageVO;
import kr.or.ddit.donation.vo.DonationVO;

public interface IDonationService {
	
	int insertDonation(DonationVO donationVO);
	
	public List<DonationVO> selectMyDonationList(String memberId);
	
	public DonationVO selectDonationForReceipt(int donationId, String memberId);

	public PageVO selectAdminDonationData(PageVO pageVO);

	int updateMemberDonorYn(String memberId);

	public Map<String, Object> selectDonationStats();

    /**
     * 영수증 신청 목록
     */
	public List<DonationVO> selectReceiptList();

	public List<DonationVO> selectAdminDonationListAll(PageVO pageVO);

    /**
     * 이달의 우수 기부자 TOP 5
     */
	public List<PageVO> selectTopDonorList();

    /**
     * 후원 내역 수정 (관리자)
     */
	public int updateDonationByAdmin(DonationVO dvo);

    /**
     * 후원 내역 삭제 (관리자)
     */
	public int deleteDonation(int donationId);
	
	public int updateReceiptStatus(DonationVO dvo);
}

