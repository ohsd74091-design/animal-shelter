package kr.or.ddit.member.dao;

import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;

import kr.or.ddit.member.vo.MemberVO;

public class MemberDaoImpl implements IMemberDao {
	private static IMemberDao Memberdao = new MemberDaoImpl();
	
	private MemberDaoImpl() {
	}
	
	public static IMemberDao getInsetance() {
		return Memberdao;
	}
	
	
	@Override
	public int insertMember(SqlSession session, MemberVO vo) {
		int cnt =0;
		
		try {
			cnt =session.insert("member.insertMember",vo);
			
		}catch (PersistenceException e) {
			e.printStackTrace();
			throw new RuntimeException("회원 등록중 오류가 발생하였습니다..",e); 
			
		}
		return cnt;
	}

	@Override
	public MemberVO loginMember(SqlSession session, MemberVO vo) {
		
		return session.selectOne("member.loginMember", vo);
	}

	@Override
	public int checkMemberId(SqlSession session, String memberId) {
		return session.selectOne("member.checkMemberId", memberId);
	}

	@Override
	public int checkEmail(SqlSession session, String email) {
		return session.selectOne("member.checkEmail", email);
	}

	@Override
	public int checkNickname(SqlSession session, String nick) {
	
		return session.selectOne("member.checkNickname", nick);
	}

	@Override
	public String findIdByNameEmail(SqlSession session, MemberVO vo) {
		String res=null;
		
		try {
			res = session.selectOne("member.findIdByNameEmail",vo);
			
		}catch(PersistenceException e ){
			e.printStackTrace();
			throw new RuntimeException("아이디찾기중 오류발생!..",e); 
		}
		
		
		
		return res;
	}

	@Override
	public MemberVO findMemberForPw(SqlSession session, MemberVO vo) {
		
		return session.selectOne("member.findMemberForPw",vo);
	}

	@Override
	public int updateTempPassword(SqlSession session, MemberVO vo) {
		int cnt=0;
		
		try {
		cnt =session.update("member.updateTempPassword",vo);	
		}catch (PersistenceException e) {
			e.printStackTrace();
			throw new RuntimeException("비밀번호 찾기중 오류발생!..",e); 
		}
		return cnt;
	}


}
