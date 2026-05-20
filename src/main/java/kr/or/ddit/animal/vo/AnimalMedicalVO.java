package kr.or.ddit.animal.vo;

import lombok.Data;

@Data
public class AnimalMedicalVO {
	private int animalId; /*  */
	private String vaccination; /*  */
	private String microchip; /*  */
	private String neutered; /*  */
	private String specialNote;   // 특이사항(텍스트)
}
