package kr.or.ddit.report.vo;



import java.util.Date;

import lombok.Data;

@Data
public class AnimalReportVO {
	private int reportId   ;
	private String memberId   ;
	private String animalType ;
	private String location    ;
	private String status      ;
	private String imageName  ;
	private String content     ;
	private Date reportDate ;
	private String processYn  ;
	private String replyEmail;
}
