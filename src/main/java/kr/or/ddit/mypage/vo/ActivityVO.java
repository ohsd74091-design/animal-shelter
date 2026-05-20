package kr.or.ddit.mypage.vo;

import lombok.Data;

/**
 * 최근 활동 내역 VO
 *
 * badgeType 값 규칙 (mypage.css 클래스명과 일치):
 *   "primary" → 관심등록	(주황)
 *   "green"   → 후원완료	(초록)
 *   "blue"    → 심사중	(파랑)
 *   "red"     → 반려/취소	(빨강)
 *   "emerald" → 봉사		(주황)
 *   "gray"    → 기타		(회색)
 *
 * iconType 값 규칙 (아이콘 배경색 고정용):
 *   "pink"    → 관심
 *   "green"   → 후원
 *   "blue"    → 입양
 *   "orange"  → 봉사
 */
@Data
public class ActivityVO
{

	private int activityId; 	// 활동 고유 ID
	private String icon; 		// Material Symbol 아이콘명 (favorite, assignment 등)
	private String iconType; 	// 아이콘 배경색 고정용 (pink / green / blue / orange)
	private String description; // 활동 설명 문구
	private String actDate; 	// 활동 날짜 (YYYY.MM.DD)
	private String badgeType; 	// 뱃지 색상 접미사
	private String badgeLabel; 	// 뱃지 표시 텍스트

}
