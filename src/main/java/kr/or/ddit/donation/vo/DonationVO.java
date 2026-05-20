package kr.or.ddit.donation.vo;

import java.sql.Date;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor // 기본 생성자
@AllArgsConstructor // 매개변수 있는 생성자
public class DonationVO {

    private int donationId;       // 후원 번호 (PK, SEQ)
    private String memberId;         // 회원 ID (FK → MEMBER)
    private long donationAmount;     
    private Date createDate;       
    private String donationMethod;    
    private String donationType;    
    private String donatorName;   
    private String donatorTel;        // 휴대폰 번호
    private String donatorEmail;      // 이메일
    private String receiptYn;          
    private String regNo1;          
    private String regNo2;     
    
    private String donationDateStr;
    
}
