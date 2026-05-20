package kr.or.ddit.member.service;

import java.util.Map;

import kr.or.ddit.member.vo.MemberVO;

public interface IMemberService {

	public int insertMember(MemberVO vo);

	public MemberVO loginMember(MemberVO vo);
	
	public int checkMemberId(String memberId);
	public int checkEmail(String email);
	
	int checkNickname(String nick);
	
	public boolean sendIdByEmail(MemberVO vo);
	
	public boolean resetPasswordAndSendMail(MemberVO vo);
	
	
	
}
