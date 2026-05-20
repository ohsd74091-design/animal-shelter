package kr.or.ddit.animal.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.SqlSession;

import kr.or.ddit.animal.vo.AnimalImageVO;
import kr.or.ddit.animal.vo.AnimalMedicalVO;
import kr.or.ddit.animal.vo.AnimalRescueVO;
import kr.or.ddit.animal.vo.AnimalVO;

public interface IAnimalDao<T>
{
	/**
	 * 동물 목록 조회
	 * 
	 * @param transactinoal 서비스에서 넘겨준 SqlSession
	 * @param paramMap      필터(animalType, gender, characterList, sizeList) 및 정렬(sort) 데이터
	 * @return				필터 조건들이 담긴 Map을 받아 리스트를 반환
	 */
	public List<AnimalVO> getAnimalList(T transactinoal, Map<String, Object> paramMap);
	
	/**
	 * 
	 * @param transactinoal
	 * @param paramMap
	 * @return
	 */
	public int getAnimalCount(T transactinoal, Map<String, Object> paramMap);
	
	/**
	 * 
	 * @param transactinoal
	 * @param paramMap
	 * @return
	 */
	public int toggleFavorite(T transactinoal, Map<String, Object> paramMap);
	
	/**
	 * 
	 * @param 세션
	 * @param animalId => 선택동물 id
	 * @return 선택동물 필요정보
	 */
	public AnimalVO selectAnimal(SqlSession session, int animalId);
	
	/**
	 * 
	 * @param 세션
	 * @param animalId => 동일
	 * @return 선택동물 사진들..
	 */
	public List<AnimalImageVO> selectImagesByAnimalId(SqlSession session, int animalId);
	
	/**
	 * 
	 * @param 세션
	 * @param animalId 
	 * @return 건강정보
	 */
	public AnimalMedicalVO selectMedical(SqlSession session, int animalId);
	
	/**
	 * 
	 * @param 세션
	 * @param animalId
	 * @return 해당동물 구조정보
	 */
	 public AnimalRescueVO selectRescue(SqlSession session, int animalId);
	 
	 /**
	  * 
	  * @param ss
	  * @param animalId
	  * @return 해당동물 조회수 증가
	  */
	 public int incrementViewCount(SqlSession session, int animalId);
	 
	 /**
	  * 
	  * @param ss
	  * @param paramMap
	  * @return 해당 로그인session 유저가 찝한 동물인지 아닌지? 
	  */
	 public int checkFavorite(SqlSession session, Map<String, Object> paramMap);
	 
	 public int countFavorite(SqlSession session, @Param("animalId") int animalId, @Param("memberId") String memberId);
	 
	 
	 /**
	     * 동물 기본 정보 등록
	     */
	    int insertAnimal(SqlSession session, AnimalVO animal);
	    
	    /**
	     * 동물 의료 정보 등록
	     */
	    int insertAnimalMedical(SqlSession session,AnimalMedicalVO medical);
	    
	    /**
	     * 동물 구조 정보 등록
	     */
	    int insertAnimalRescue(SqlSession session,AnimalRescueVO rescue);
	    
	    /**
	     * 동물 이미지 등록
	     */
	    int insertAnimalImage(SqlSession session,AnimalImageVO image);
	    
	    public String SelectPreviewImage(SqlSession session, String keyword);
}
