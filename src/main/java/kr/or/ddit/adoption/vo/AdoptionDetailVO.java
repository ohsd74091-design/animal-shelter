package kr.or.ddit.adoption.vo;

import lombok.Data;

@Data
public class AdoptionDetailVO
{
	private int adoptionId; // FK (PK)
	private String job; // 직업
	private String housingType; // 주거형태: 아파트/연립주택/단독주택/기타
	private String petExperience; // 반려동물 경험: 없음/현재 키움/과거에 키움
	private String address; // 주소
	private String adoptionReason; // 입양 동기 (CLOB)
}
