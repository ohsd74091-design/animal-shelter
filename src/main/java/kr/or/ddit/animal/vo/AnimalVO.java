package kr.or.ddit.animal.vo;

import java.sql.Date;

import lombok.Data;

@Data
public class AnimalVO
{
	private int animalId; 			// 동물 번호
	private String animalName; 		// 이름
	private String animalType; 		// 타입
	private String breed; 			// 품종
	private String gender; 			// 성별
	private int age; 				// 나이
	private double weight; 			// 체중
	private String adoptionStatus; 	// 입앙여부
	private String personality; 	// 성격
	private int viewCount; 			// 조회수
	private Date createDate; 		// 등록일
	
	private String mainImage;		// 메인 이미지
	private int isFavorite;			// 관심 등록 여부
	private int    favoriteCount; // 
}
