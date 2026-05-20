package kr.or.ddit.animal.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.ibatis.session.SqlSession;

import jakarta.websocket.Session;
import kr.or.ddit.animal.dao.AnimalDaoImpl;
import kr.or.ddit.animal.dao.IAnimalDao;
import kr.or.ddit.animal.dto.AnimalDetailDto;
import kr.or.ddit.animal.vo.AnimalImageVO;
import kr.or.ddit.animal.vo.AnimalMedicalVO;
import kr.or.ddit.animal.vo.AnimalRescueVO;
import kr.or.ddit.animal.vo.AnimalVO;
import kr.or.ddit.member.vo.MemberVO;
import kr.or.ddit.util.MybatisUtil;

public class AnimalServiceImpl implements IAnimalService
{
	private IAnimalDao<SqlSession> animalDao;
	private static IAnimalService animalSvc;

	private AnimalServiceImpl()
	{
		animalDao = AnimalDaoImpl.getInstance();
	}

	public static IAnimalService getInstance()
	{
		if (animalSvc == null)
			animalSvc = new AnimalServiceImpl();
		return animalSvc;
	}

	@Override
	public List<AnimalVO> displayAnimalList(Map<String, Object> paramMap)
	{
		List<AnimalVO> animalList = new ArrayList<AnimalVO>();

		try (SqlSession session = MybatisUtil.getsqlsession())
		{
			animalList = animalDao.getAnimalList(session, paramMap);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return animalList;
	}

	@Override
	public int getAnimalCount(Map<String, Object> paramMap)
	{
		int count = 0;

		// 세션 오픈 로직에 맞춰 수정
		try (SqlSession session = MybatisUtil.getsqlsession())
		{
			count = animalDao.getAnimalCount(session, paramMap);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return count;
	}

	@Override
	public int updateFavorite(Map<String, Object> paramMap)
	{
		int result = 0;
		
		// auto-commit을 false(DML 작업)
		try (SqlSession session = MybatisUtil.getsqlsession(false))
		{
			result = animalDao.toggleFavorite(session, paramMap);
			
			if (result > 0)
				session.commit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public AnimalDetailDto getAnimalDetail(int animalId) {
		SqlSession session=MybatisUtil.getsqlsession();
		 AnimalDetailDto dto =new AnimalDetailDto();
		
		try {
			//1. 조회수증가
			animalDao.incrementViewCount(session, animalId);
			
			//2. 정보
			AnimalVO animal =animalDao.selectAnimal(session, animalId);
			
			//3.이미지 목록
			List<AnimalImageVO> images=animalDao.selectImagesByAnimalId(session, animalId);
			
			//대표이미지
				AnimalImageVO mainImage = null;
					if (images != null) {
						for (AnimalImageVO img : images) {
							if ("Y".equalsIgnoreCase(img.getIsMain())) { // 필드명에 맞게 getIsMain() 사용
								mainImage = img;
								break;
								}
							}
						if (mainImage == null && !images.isEmpty()) {
							mainImage = images.get(0); // 안전장치: 대표이미지 없으면 첫번째 사용
							}
						}
			
			//4.의료/구조정보
			AnimalMedicalVO medical= animalDao.selectMedical(session, animalId);
			AnimalRescueVO rescue= animalDao.selectRescue(session, animalId);
			
			//5.태그파싱
			List<String> tagList = Collections.emptyList();
            if (animal != null && animal.getPersonality() != null) {
                tagList = Arrays.stream(animal.getPersonality().split(","))
                                .map(String::trim).filter(s -> !s.isEmpty())
                                .collect(Collectors.toList());
            }
            //6.DTO조립 
            dto.setAnimal(animal);
            dto.setImageList(images);
            dto.setMainImage(mainImage);
            dto.setMedical(medical);
            dto.setRescue(rescue);
            dto.setTagList(tagList);
            session.commit();
           
            
          
            
            
		}catch (Exception e) {
			session.rollback();
			e.printStackTrace();
			
		}finally {
			session.close();
		}
		 return dto;
	}

	@Override
	public boolean isFavorited(int animalId, String memberId) {
		 int cnt = 0;
	        try (SqlSession session = MybatisUtil.getsqlsession()) {
	            cnt = animalDao.countFavorite(session, animalId, memberId);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return cnt > 0;
	    }

	@Override
	public boolean registerAnimal(AnimalVO animal, AnimalMedicalVO medical, AnimalRescueVO rescue,
			List<AnimalImageVO> imageList) {
		SqlSession session = MybatisUtil.getsqlsession();
		int cnt=0;
		boolean isSuccess=false;
		try {
			// 1.동물테이블 insert(animal_id ...)
			cnt = animalDao.insertAnimal(session, animal);
			
			if(cnt ==0) {
				session.rollback();
				return false;
			}
			
			// 2. 생성된 animal_id 가져옴 
			int animalId =animal.getAnimalId();
			
			//2-1 의료정보 insert
			
			if(medical !=null) {
				medical.setAnimalId(animalId);
				animalDao.insertAnimalMedical(session, medical);
			}
			
			// 2-3.동물 구조정보 
			
			if(rescue !=null) {
				rescue.setAnimalId(animalId);;
				animalDao.insertAnimalRescue(session, rescue);
				
			}
			
			//2-4.동물 사진 
			if(imageList != null&&!imageList.isEmpty()) {
				for(AnimalImageVO image : imageList) {
					image.setAnimalId(animalId);
					animalDao.insertAnimalImage(session, image);
				}
			}
			session.commit();
			isSuccess= true;
			
		}catch (Exception e) {
			session.rollback();
			e.printStackTrace();
		}
		return  isSuccess;
		
	}

	@Override
	public String getPreviewImage(String keyword) {
		
		SqlSession session = MybatisUtil.getsqlsession();
		String fileName = null;
		
		try {
			fileName = animalDao.SelectPreviewImage(session, keyword);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return fileName;
	}
}
