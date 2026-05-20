package kr.or.ddit.support.dao;

import org.apache.ibatis.session.SqlSession;

import kr.or.ddit.support.vo.SupportFileVO;

public interface ISupportFileDao {
	// 파일 정보를 DB에 저장하는 메서드
	public int insertSupportFile(SqlSession session, SupportFileVO fileVO);
	
	

}
