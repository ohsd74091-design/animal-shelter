package kr.or.ddit.common.vo;

import java.util.List;

import kr.or.ddit.donation.vo.DonationVO;
import lombok.Data;

@Data
public class PageVO {
	private int currentPage;   // 현재 페이지
	private int startPage;     // 페이지네이션 시작 번호
	private int endPage;       // 페이지네이션 끝 번호
	private int totalPage;     // 전체 페이지 수
	private int totalCount;    // 전체 글 수

	private int countPerPage;  // 한 페이지당 글 수
	private int pageCount;     // 한 번에 보여줄 페이지 번호 수

	private int startRow;      // DB 조회 시작 행
	private int endRow;        // DB 조회 끝 행
	
	private String memberId;
	private String supportType;
	private String status;
	private String keyword;
	private String sort;
	private String searchWord;  // 검색
	
	private String role; //관리자단 권한역할에따른 검색조건
	

	private String method;
    private String paymentType;
    private String startDate;
    private String endDate;
    private String donatorName;
    private long totalAmount;
    private List<DonationVO> donationList;

	private String animalType; // 개/고양이 나누기 
	private String adoptionStatus; // 입양가능? 입양완료? 검토중?
	


}
