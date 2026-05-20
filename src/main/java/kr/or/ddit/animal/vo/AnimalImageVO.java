package kr.or.ddit.animal.vo;

import lombok.Data;

@Data
public class AnimalImageVO {
	private int imageId; /*  */
	private int animalId; /*  */
	private String isMain; /* y또는n  */
	private String originFileName; /*  */
	private String saveFileName; /*  */
}
