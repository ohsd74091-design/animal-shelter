package kr.or.ddit.member.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import kr.or.ddit.member.dao.IMemberDao;
import kr.or.ddit.member.dao.MemberDaoImpl;
import kr.or.ddit.member.vo.MemberVO;
import kr.or.ddit.util.MailUtil;
import kr.or.ddit.util.MybatisUtil;

public class MemberServiceImpl implements IMemberService
{
	private static IMemberService service = new MemberServiceImpl();
	private IMemberDao dao;

	private MemberServiceImpl()
	{
		dao = MemberDaoImpl.getInsetance();
	}

	private String creatTempPassword()
	{

		String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < 8; i++)
		{
			int idx = (int) (Math.random() * chars.length());
			sb.append(chars.charAt(idx));
		}

		return sb.toString();
		// 랜덤 비밀번호(임시비밀번호) 생성 메서드...

	}

	public static IMemberService getinstance()
	{
		return service;

	}

	@Override
	public int insertMember(MemberVO vo)
	{
		SqlSession session = MybatisUtil.getsqlsession();
		int cnt = 0;

		try
		{
			cnt = dao.insertMember(session, vo);

			if (cnt > 0)
			{
				session.commit();
			}
			else
			{
				session.rollback();
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
			session.rollback();
		}
		finally
		{
			session.close();
		}

		return cnt;
	}

	@Override
	public MemberVO loginMember(MemberVO vo)
	{
		SqlSession session = MybatisUtil.getsqlsession();
		MemberVO loginMemberVO = null;

		try
		{
			loginMemberVO = dao.loginMember(session, vo);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			session.close();
		}
		return loginMemberVO;
	}

	@Override
	public int checkMemberId(String memberId)
	{
		SqlSession session = MybatisUtil.getsqlsession();
		int count = 0;
		try
		{
			count = dao.checkMemberId(session, memberId);
		}
		finally
		{
			session.close();
		}
		return count;
	}

	@Override
	public int checkEmail(String email)
	{
		SqlSession session = MybatisUtil.getsqlsession();
		int count = 0;
		try
		{
			count = dao.checkEmail(session, email);
		}
		finally
		{
			session.close();
		}
		return count;
	}

	@Override
	public int checkNickname(String nick)
	{
		SqlSession session = MybatisUtil.getsqlsession();
		int count = 0;
		try
		{
			count = dao.checkNickname(session, nick);
		}
		finally
		{
			session.close();
		}
		return count;
	}

	@Override
	public boolean sendIdByEmail(MemberVO vo)
	{
		SqlSession session = MybatisUtil.getsqlsession();
		String memid = null;
		try
		{
			memid = dao.findIdByNameEmail(session, vo);

			if (memid == null)
			{
				return false;
			}
			String to = vo.getEmail();
			String subject = "아이디 찾기 결과";
			String content = "회원님의 아이디는 [" + memid + "] 입니다...";
			MailUtil.sendMail(to, subject, content);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			session.rollback();
		}
		finally
		{
			session.close();

		}

		return true;
	}

	@Override
	public boolean resetPasswordAndSendMail(MemberVO vo)
	{
		SqlSession session = MybatisUtil.getsqlsession(false);
		MemberVO member = null;

		try
		{
			member = dao.findMemberForPw(session, vo);

			if (member == null)
			{
				return false;

			}
			String tempPw = creatTempPassword();
			// 랜덤 난수 비밀번호생성 ...

			MemberVO updateVO = new MemberVO();
			updateVO.setMemberId(member.getMemberId());
			updateVO.setMemberPw(tempPw);

			// 새 비밀번호를 가진 vo 생성후 dao에 넘김
			System.out.println("member.getMemberId() = " + member.getMemberId());
			System.out.println("updateVO.getMemberId() = " + updateVO.getMemberId());
			System.out.println("updateVO.getMemberPw() = " + updateVO.getMemberPw());
			int cnt = dao.updateTempPassword(session, updateVO);

			if (cnt <= 0)
			{
				return false;
			}
			String to = member.getEmail();
			String subject = "임시 비밀번호 안내";
			String content = "회원님의 임시 비밀번호는 [" + tempPw + "] 입니다.\n로그인 후 비밀번호를 변경해주세요.";

			MailUtil.sendMail(to, subject, content);
			session.commit();
			return true;
			// 메일로 쏴줌
		}
		catch (Exception e)
		{
			e.printStackTrace();
			session.rollback();
			return false;
		}
		finally
		{
			session.close();
		}
	}

}
