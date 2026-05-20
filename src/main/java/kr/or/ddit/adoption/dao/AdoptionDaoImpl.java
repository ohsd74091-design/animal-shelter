package kr.or.ddit.adoption.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import kr.or.ddit.adoption.vo.AdoptionDetailVO;
import kr.or.ddit.adoption.vo.AdoptionVO;
import kr.or.ddit.util.MybatisUtil;

public class AdoptionDaoImpl implements IAdoptionDao
{

	private static AdoptionDaoImpl instance;

	private AdoptionDaoImpl()
	{
	}

	public static AdoptionDaoImpl getInstance()
	{
		if (instance == null)
		{
			instance = new AdoptionDaoImpl();
		}
		return instance;
	}

	/** 중복 신청 확인 (같은 동물·같은 회원·심사중) */
	public int checkDuplicateAdoption(Map<String, Object> param)
	{
		try (SqlSession session = MybatisUtil.getsqlsession())
		{
			return session.selectOne("adoption.checkDuplicateAdoption", param);
		}
	}

	/** 반려 이력 확인 (같은 동물·같은 회원·반려) */
	public int checkRejectedAdoption(Map<String, Object> param)
	{
		try (SqlSession session = MybatisUtil.getsqlsession())
		{
			return session.selectOne("adoption.checkRejectedAdoption", param);
		}
	}

	/** 반려 사유 조회 (가장 최근) */
	public String getRejectReason(Map<String, Object> param)
	{
		try (SqlSession session = MybatisUtil.getsqlsession())
		{
			return session.selectOne("adoption.getRejectReason", param);
		}
	}

	/**
	 * ADOPTION 기본정보 INSERT ※ selectKey 로 adoptionId 가 vo 에 자동 세팅됨
	 */
	public int insertAdoption(AdoptionVO adoptionVO)
	{
		try (SqlSession session = MybatisUtil.getsqlsession())
		{
			int result = session.insert("adoption.insertAdoption", adoptionVO);
			session.commit();
			return result;
		}
	}

	/** ADOPTION_DETAIL 상세정보 INSERT */
	public int insertAdoptionDetail(AdoptionDetailVO detailVO)
	{
		try (SqlSession session = MybatisUtil.getsqlsession())
		{
			int result = session.insert("adoption.insertAdoptionDetail", detailVO);
			session.commit();
			return result;
		}
	}

	/** 단건 상세 조회 (동물정보 + 상세 JOIN) */
	public AdoptionVO selectAdoption(int adoptionId)
	{
		try (SqlSession session = MybatisUtil.getsqlsession())
		{
			return session.selectOne("adoption.selectAdoption", adoptionId);
		}
	}

	/** 회원 본인 신청 목록 */
	public List<AdoptionVO> selectAdoptionListByMember(String memberId)
	{
		try (SqlSession session = MybatisUtil.getsqlsession())
		{
			return session.selectList("adoption.selectAdoptionListByMember", memberId);
		}
	}
}