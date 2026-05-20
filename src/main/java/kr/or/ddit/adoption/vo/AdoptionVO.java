package kr.or.ddit.adoption.vo;

import java.sql.Date;
import lombok.Data;

@Data
public class AdoptionVO {
    private int    adoptionId;    // 입양신청 번호 (PK)
    private String memberId;      // 회원 ID (FK)
    private int    animalId;      // 동물 번호 (FK)
    private Date   visitDate;     // 방문 희망일
    private Date   applyDate;     // 신청일 (SYSDATE)
    private String status;        // 상태: 심사중 / 승인 / 반려
    private String rejectReason;  // 반려 사유 (nullable)

    // JOIN용 추가 필드 (animal 정보)
    private String animalName;
    private String animalType;
    private String breed;
    private int    age;
    private String mainImage;

    // 상세 정보 (ADOPTION_DETAIL JOIN)
    private AdoptionDetailVO detail;
    
    // ANIMAL.PERSONALITY (JOIN용)
    private String personality;  
    // MEMBER.PROFILE_IMG (JOIN용)
    private String profileImg;   
    
    
    //입양신청 결과조회 join용 
    private String gender;
    private String adoptionStatus;

    private String memberName;
    private String nickname;
    private String email;
    private String phone;
}
