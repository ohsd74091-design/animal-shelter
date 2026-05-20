package kr.or.ddit.mypage.vo;

import lombok.Data;

/**
 * 마이페이지 통계 카드용 VO
 */
@Data
public class MypageStatsVO
{

	private int favoriteCount; // 관심동물 수
	private int adoptionCount; // 입양신청 수
	private int postCount; // 작성글 수
}
