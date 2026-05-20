package kr.or.ddit.support.vo;

import java.sql.Date;

import lombok.Data;

@Data
public class SupportFileVO {
	
    private long fileId;
 // 문의글 번호 (어떤 문의글의 파일인지 알려주는 외래키)
	private int supportId;
	private String originFileName; // 사용자가 올린 원래 이름
	private String saveFileName;  //서버에 저장된 중복 방지용 이름
	private String filePath; // 파일이 저장된 서버 내 경로
	private long fileSize;
	private String fileExt;
	private Date createDate;
    private String isImage;

}
