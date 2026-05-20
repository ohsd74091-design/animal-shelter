package kr.or.ddit.certificate.vo;

import lombok.Data;

@Data
public class AdoptionCertificateVO {
	private int adoptionId;
    private String approvalNo;
    private String approvalDate;

    private String memberId;
    private String memberName;
    private String phone;
    private String job;
    private String address;

    private int animalId;
    private String animalName;
    private String breed;
    private String gender;
    private Integer age;
    private String neutered;
    private String rescueDate;

    private String visitDate;
    private String applyDate;
    private String status;
}
