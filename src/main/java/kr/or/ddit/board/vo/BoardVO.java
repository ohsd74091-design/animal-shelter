package kr.or.ddit.board.vo;

import lombok.Data;

@Data
public class BoardVO
{

	private int boardId; // 게시글 번호
	private String memberId; // 작성자 아이디
	private String boardType; // 게시판 유형
	private String title; // 제목
	private String content; // 내용
	private int viewCount; // 조회수
	private int likeCount; // 좋아요 수
	private String createDate; // 작성일
	private int commentCount; // 댓글 수
	private String profileImg; // 작성자 프로필 이미지
	private String isHidden;   // 숨김 여부
	private int reportCount; // 신고 건수 (관리자 목록 조회 시 사용)
	private String role; // 작성자의 권한 (USER / ADMIN)

}
