package kr.or.ddit.animal.dto;

import java.util.List;

import kr.or.ddit.animal.vo.AnimalImageVO;
import kr.or.ddit.animal.vo.AnimalMedicalVO;
import kr.or.ddit.animal.vo.AnimalRescueVO;
import kr.or.ddit.animal.vo.AnimalVO;
import lombok.Data;

@Data
public class AnimalDetailDto {
private AnimalVO animal;
private AnimalImageVO mainImage;
private List<AnimalImageVO> imageList;
private AnimalMedicalVO medical;
private AnimalRescueVO rescue;
private List<String> tagList;

}
