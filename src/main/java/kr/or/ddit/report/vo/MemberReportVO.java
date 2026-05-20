package kr.or.ddit.report.vo;

import java.util.Date;

import lombok.Data;

@Data
public class MemberReportVO {
	    private int reportId;
	    private String reporterId;
	    private String targetId;
	    private String reason;
	    private Date reportDate;
	    private String status;
	    private String content;
	    private String replyEmail;
}
