package kr.or.ddit.adoption.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import kr.or.ddit.adoption.vo.AdoptionDetailVO;
import kr.or.ddit.adoption.vo.AdoptionVO;
import kr.or.ddit.util.MybatisUtil;

public class AdoptionServiceImpl implements IAdoptionService
{

	private static AdoptionServiceImpl instance;

	private AdoptionServiceImpl()
	{
	}

	public static AdoptionServiceImpl getInstance()
	{
		if (instance == null)
		{
			instance = new AdoptionServiceImpl();
		}
		return instance;
	}

	@Override
	public int checkDuplicate(Map<String, Object> param)
	{
		try (SqlSession session = MybatisUtil.getsqlsession())
		{
			return session.selectOne("adoption.checkDuplicateAdoption", param);
		}
	}

	@Override
	public int checkRejected(Map<String, Object> param)
	{
		try (SqlSession session = MybatisUtil.getsqlsession())
		{
			return session.selectOne("adoption.checkRejectedAdoption", param);
		}
	}

	@Override
	public String getRejectReason(Map<String, Object> param)
	{
		try (SqlSession session = MybatisUtil.getsqlsession())
		{
			return session.selectOne("adoption.getRejectReason", param);
		}
	}

	/**
	 * 입양 신청 처리 ADOPTION + ADOPTION_DETAIL 을 같은 세션(트랜잭션)으로 묶어 처리
	 * 
	 * @return 1: 성공 / -1: 중복 신청 / -2: DB 오류
	 */
	@Override
	public int applyAdoption(AdoptionVO adoptionVO)
	{

		// 1. 중복 신청 체크 (별도 세션으로 읽기만)
		Map<String, Object> dupParam = new HashMap<>();
		dupParam.put("animalId", adoptionVO.getAnimalId());
		dupParam.put("memberId", adoptionVO.getMemberId());

		try (SqlSession readSession = MybatisUtil.getsqlsession())
		{
			int dupCount = readSession.selectOne("adoption.checkDuplicateAdoption", dupParam);
			if (dupCount > 0)
			{
				return -1; // 심사중 또는 승인된 신청 이력 존재 → 재신청 불가
			}
		}

		// 2. ADOPTION + ADOPTION_DETAIL 을 하나의 세션(트랜잭션)으로 INSERT
		try (SqlSession session = MybatisUtil.getsqlsession(false))
		{
			try
			{
				// 2-1. ADOPTION 기본 INSERT (selectKey → adoptionId 자동 세팅)
				int result = session.insert("adoption.insertAdoption", adoptionVO);
				if (result < 1)
				{
					session.rollback();
					return -2;
				}

				// 2-2. ADOPTION_DETAIL INSERT (adoptionId 공유)
				AdoptionDetailVO detail = adoptionVO.getDetail();
				if (detail != null)
				{
					detail.setAdoptionId(adoptionVO.getAdoptionId()); // selectKey 로 채워진 값
					session.insert("adoption.insertAdoptionDetail", detail);
				}

				session.commit();
				return 1; // 성공

			}
			catch (Exception e)
			{
				session.rollback();
				e.printStackTrace();
				return -2;
			}
		}
	}

	@Override
	public AdoptionVO getAdoption(int adoptionId)
	{
		try (SqlSession session = MybatisUtil.getsqlsession())
		{
			return session.selectOne("adoption.selectAdoption", adoptionId);
		}
	}

	@Override
	public List<AdoptionVO> getAdoptionListByMember(String memberId)
	{
		try (SqlSession session = MybatisUtil.getsqlsession())
		{
			return session.selectList("adoption.selectAdoptionListByMember", memberId);
		}
	}
}