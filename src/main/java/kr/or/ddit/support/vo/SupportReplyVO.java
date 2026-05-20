package kr.or.ddit.support.vo;

import java.sql.Date;

import lombok.Data;

@Data
public class SupportReplyVO {
	
    
    private int supportId;  //연결된 문의글 번호
    private String memberId; // 답변 작성자(관리자) ID
    private String replyContent;
    private Date createDate;
    private int replyId;

}
