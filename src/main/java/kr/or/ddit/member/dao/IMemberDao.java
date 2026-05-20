package kr.or.ddit.member.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import kr.or.ddit.member.vo.MemberVO;
//1123
public interface IMemberDao {
	public int insertMember(SqlSession session, MemberVO vo);
	
	public MemberVO loginMember(SqlSession session, MemberVO vo);
	
	public int checkMemberId(SqlSession session, String memberId);
	
	public int checkEmail(SqlSession session, String email);
	
	public int checkNickname(SqlSession session, String nick);
	
	public String findIdByNameEmail(SqlSession session,MemberVO vo);
	
	public MemberVO findMemberForPw(SqlSession session, MemberVO vo);
	
	public int updateTempPassword(SqlSession session, MemberVO vo);
	

}
