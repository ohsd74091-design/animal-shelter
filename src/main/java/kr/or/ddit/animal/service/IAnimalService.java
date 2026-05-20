package kr.or.ddit.animal.service;

import java.util.List;
import java.util.Map;

import jakarta.websocket.Session;
import kr.or.ddit.animal.dto.AnimalDetailDto;
import kr.or.ddit.animal.vo.AnimalImageVO;
import kr.or.ddit.animal.vo.AnimalMedicalVO;
import kr.or.ddit.animal.vo.AnimalRescueVO;
import kr.or.ddit.animal.vo.AnimalVO;

public interface IAnimalService
{
	public List<AnimalVO> displayAnimalList(Map<String, Object> paramMap);
	
	public int getAnimalCount(Map<String, Object> paramMap);
	
	public int updateFavorite(Map<String, Object> paramMap);
	
	AnimalDetailDto getAnimalDetail(int animalId);
	
	boolean isFavorited(int animalId,String memberId);
	
	 public boolean registerAnimal(AnimalVO animal, 
             AnimalMedicalVO medical, 
             AnimalRescueVO rescue, 
             List<AnimalImageVO> imageList);
	 
	 String getPreviewImage(String keyword);
}
