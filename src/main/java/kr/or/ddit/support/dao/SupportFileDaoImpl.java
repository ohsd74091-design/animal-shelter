package kr.or.ddit.support.dao;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;

import kr.or.ddit.support.vo.SupportFileVO;
import kr.or.ddit.util.MybatisUtil;

public class SupportFileDaoImpl implements ISupportFileDao{
	
	private static ISupportFileDao fileDao = new SupportFileDaoImpl();
	public SupportFileDaoImpl() {
		
	}
	public static ISupportFileDao getInstance() {
		return fileDao;
		
	}
	

	@Override
	public int insertSupportFile(SqlSession session, SupportFileVO fileVO) {
		
		int cnt = 0;
		try {
			
			cnt = session.insert("support.insertSupportFile", fileVO);
			
		}catch(PersistenceException ex) {
			ex.printStackTrace();
			throw new RuntimeException("첨부파일 등록 중 예외발생", ex);
		}
		
		return cnt;
	}
	

}
