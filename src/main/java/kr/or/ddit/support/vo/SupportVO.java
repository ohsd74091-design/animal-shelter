package kr.or.ddit.support.vo;

import java.sql.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data   // [기능] Getter, Setter, toString 등을 자동으로 생성
@NoArgsConstructor  // 기본생성자
@AllArgsConstructor // 필드를 담는 생성자
public class SupportVO {
	
	// 문의번호 (Primary Key)
    // 각 문의를 식별하는 고유 번호
    private int supportId; 
    // 작성자 아이디 (Foreign Key)
    // MEMBER 테이블의 MEMBER_ID
    private String memberId;
    private String content;
    // STATUS  'Y'(답변완료) 또는 'N'(대기중) 등을 저장
    private String status;
    private Date createDate;
    private String title;
    private String supportType;  // 문의 유형 
    // SupportVO에 추가될 예시 코드
    private List<SupportFileVO> fileList;
    private List<SupportReplyVO> replyList;
	
	

}
