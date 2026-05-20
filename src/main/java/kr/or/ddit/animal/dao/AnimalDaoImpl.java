package kr.or.ddit.animal.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;

import kr.or.ddit.animal.vo.AnimalImageVO;
import kr.or.ddit.animal.vo.AnimalMedicalVO;
import kr.or.ddit.animal.vo.AnimalRescueVO;
import kr.or.ddit.animal.vo.AnimalVO;

public class AnimalDaoImpl implements IAnimalDao<SqlSession>
{
	private static IAnimalDao<SqlSession> animalDao;

	private AnimalDaoImpl()
	{
	}

	public static IAnimalDao<SqlSession> getInstance()
	{
		if (animalDao == null)
			animalDao = new AnimalDaoImpl();
		return animalDao;
	}

	@Override
	public List<AnimalVO> getAnimalList(SqlSession session, Map<String, Object> paramMap)
	{
		List<AnimalVO> animalList = new ArrayList<AnimalVO>();

		try
		{
			animalList = session.selectList("animal.getAnimalList", paramMap);
		}
		catch (PersistenceException e)
		{
			e.printStackTrace();
			throw new RuntimeException("동물 목록 출력 작업 중 예외발생 - AnimalDao.getAllMemberList", e);
		}

		return animalList;
	}

	@Override
	public int getAnimalCount(SqlSession session, Map<String, Object> paramMap)
	{
		int count = 0;

		try
		{
			// animal.xml에 정의한 id="getAnimalCount" 호출
			count = session.selectOne("animal.getAnimalCount", paramMap);
		}
		catch (PersistenceException e)
		{
			e.printStackTrace();
			throw new RuntimeException("동물 목록 개수 조회 작업 중 예외발생 - AnimalDao.getAnimalCount", e);
		}

		return count;
	}

	@Override
	public int toggleFavorite(SqlSession session, Map<String, Object> paramMap)
	{
		try
		{
			// 기존에 등록되어 있는지 확인
			int count = session.selectOne("animal.checkFavorite", paramMap);

			// 이미 있다면 삭제 (좋아요 취소)
			if (count > 0)
				return session.delete("animal.deleteFavorite", paramMap);
			// 없다면 삽입 (좋아요 추가)
			else
				return session.insert("animal.insertFavorite", paramMap);
		}
		catch (PersistenceException e)
		{
			e.printStackTrace();
			throw new RuntimeException("관심 동물 작업 중 예외발생 - AnimalDao.toggleFavorite", e);
		}
		
	}

	@Override
	public AnimalVO selectAnimal(SqlSession session, int animalId) {
		
		return session.selectOne("animal.selectAnimal",animalId);
	}

	@Override
	public List<AnimalImageVO> selectImagesByAnimalId(SqlSession session, int animalId) {
		return session.selectList("animal.selectImagesByAnimalId",animalId);
	}

	@Override
	public AnimalMedicalVO selectMedical(SqlSession session, int animalId) {
		
		return session.selectOne("animal.selectMedical",animalId);
	}

	@Override
	public AnimalRescueVO selectRescue(SqlSession session, int animalId) {
	
		return session.selectOne("animal.selectRescue",animalId);
	}

	@Override
	public int incrementViewCount(SqlSession session, int animalId) {
		
		return session.update("animal.incrementViewCount",animalId);
	}

	@Override
	public int checkFavorite(SqlSession session, Map<String, Object> paramMap) {
		
		return session.selectOne("animal.checkFavorite_detail",paramMap);
	}

	@Override
	public int countFavorite(SqlSession session, int animalId, String memberId) {
		Map<String, Object> params = new HashMap<>();
        params.put("animalId", animalId);
        params.put("memberId", memberId);
        return session.selectOne("animal.checkFavorite_detail", params);
    }

	@Override
	public int insertAnimal(SqlSession session, AnimalVO animal) {
		int cnt =0;
		
		cnt=session.insert("animal.insertAnimal",animal);
		
		return cnt;
	}

	@Override
	public int insertAnimalMedical(SqlSession session, AnimalMedicalVO medical) {
		int cnt =0;
		
		cnt=session.insert("animal.insertAnimalMedical",medical);
		
		return cnt;
	}

	@Override
	public int insertAnimalRescue(SqlSession session, AnimalRescueVO rescue) {
		int cnt =0;
		
		cnt=session.insert("animal.insertAnimalRescue",rescue);
		
		return cnt;
	}

	@Override
	public int insertAnimalImage(SqlSession session, AnimalImageVO image) {
		int cnt =0;
		
		cnt=session.insert("animal.insertAnimalImage",image);
		
		return cnt;
	}

	@Override
	public String SelectPreviewImage(SqlSession session, String keyword) {
		String fileName = null;
		try {
			fileName = session.selectOne("animal.selectPreviewImage", keyword);
		} catch (PersistenceException e) {
			e.printStackTrace();
			throw new RuntimeException("실시간 미리보기 이미지 조회중 예외 발생", e);
		}
		return fileName;
	}

}
