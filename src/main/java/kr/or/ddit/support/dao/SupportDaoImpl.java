package kr.or.ddit.support.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;

import kr.or.ddit.common.vo.PageVO;
import kr.or.ddit.support.vo.SupportReplyVO;
import kr.or.ddit.support.vo.SupportVO;

public class SupportDaoImpl implements ISupportDao{
	
	// 싱글톤 패턴: 객체를 하나만 생성해서 사용
	private static ISupportDao supportDao = new SupportDaoImpl();
	
	private SupportDaoImpl() {
		
	}
	
	public static ISupportDao getInstance() {
		return supportDao;
		
	}

	@Override
	public int insertSupport(SqlSession session, SupportVO svo) {
		
		int cnt = 0;
		try {
			// [연결] id="insertSupport" 호출
			cnt = session.insert("support.insertSupport", svo);
			
		}catch(PersistenceException ex) {
			ex.printStackTrace();
			throw new RuntimeException("문의 등록 중 예외발생!", ex);
		}
		
		return cnt;

	}

	@Override
	public List<SupportVO> selectSupportList(SqlSession session, String memberId) {

		List<SupportVO> sList = new ArrayList<SupportVO>();
		try {
			sList = session.selectList("support.selectSupportList", memberId);
			
		}catch(PersistenceException ex) {
			ex.printStackTrace();
			throw new RuntimeException("문의 글 목록 조회 중 예외발생!", ex);
		}

		return sList;
	}

	@Override
	public SupportVO selectSupportDetail(SqlSession session, int supportId) {
		SupportVO svo = null;
        try {
            //데이터 1건을 조회할 때 selectOne을 사용
            svo = session.selectOne("support.selectSupportDetail", supportId);
        } catch(PersistenceException ex) {
            ex.printStackTrace();
            throw new RuntimeException("문의 글 상세 조회 중 예외 발생!", ex);
        }
        return svo;
    }

	@Override
	public int updateSupport(SqlSession session, SupportVO svo) {
		
		int cnt = 0;
		try {
			
			cnt = session.update("support.updateSupport", svo);
			
		}catch(PersistenceException ex) {
			ex.printStackTrace();
			throw new RuntimeException("문의 글 수정 중 예외발생!", ex);
		}
		
		return cnt;
	}
	
	@Override
	public int deleteSupportFiles(SqlSession session, int sId) {
		int cnt = 0;
		try {
			
			cnt = session.delete("support.deleteSupportFiles", sId);
			
		}catch(PersistenceException ex) {
			ex.printStackTrace();
			throw new RuntimeException("문의 글(이미지) 삭제 중 예외발생!", ex);
		}
		
		return cnt;
	}

	@Override
	public int deleteSupport(SqlSession session, int sId) {
		int cnt = 0;
		try {
			
			cnt = session.delete("support.deleteSupport", sId);
			
		}catch(PersistenceException ex) {
			ex.printStackTrace();
			throw new RuntimeException("문의 글 삭제 중 예외발생!", ex);
		}
		
		return cnt;
	}
	
	@Override
	public int getSupportCount(SqlSession session, PageVO pageVO) {
		int cnt = 0;
	    try {
	   
	        cnt = session.selectOne("support.getSupportCount", pageVO);
	        
	    } catch (PersistenceException ex) {
	        ex.printStackTrace();
	      
	        throw new RuntimeException("전체 게시글 수 조회 중 예외 발생!", ex);
	    }
	    return cnt;
	}

	@Override
	public List<SupportVO> selectSupportListWithPaging(SqlSession session, PageVO pageVo) {
		
		List<SupportVO> pList = new ArrayList<SupportVO>();
		try {
			pList = session.selectList("support.selectSupportListWithPaging", pageVo);
			
		}catch(PersistenceException ex) {
			ex.printStackTrace();
			throw new RuntimeException("전체 문의 글 페이지 조회 중 예외발생!", ex);
		}
		
		return pList;
	}

	@Override
	public List<SupportVO> selectSupportListForAdmin(SqlSession session) {
		List<SupportVO> sList = new ArrayList<SupportVO>();
		try {
			sList = session.selectList("support.selectSupportListForAdmin");
			
		}catch(PersistenceException ex) {
			ex.printStackTrace();
			throw new RuntimeException("문의 글 목록 조회 중 예외발생!", ex);
		}
		//System.out.println(">>> [DAO] DB에서 가져온 글 개수: " + (sList == null ? 0 : sList.size()));
		return sList;
	}

	@Override
	public int insertSupportReply(SqlSession session, SupportReplyVO replyVO) {
		int cnt = 0;
		try {
			
			cnt = session.insert("support.insertSupportReply", replyVO);
			
		}catch(PersistenceException ex) {
			ex.printStackTrace();
			throw new RuntimeException("문의내역 답변 등록 중 예외발생!", ex);
		}
		return cnt;
	}

	@Override
	public int updateSupportStatus(SqlSession session, int supportId) {
		int cnt = 0;
		try {
			cnt = session.update("support.updateSupportStatus", supportId);
		}catch(PersistenceException ex) {
			ex.printStackTrace();
			throw new RuntimeException("문의내역 답변 수정 중 예외발생!", ex);
		}
		return 0;
	}

	@Override
	public int getAdminSupportCount(SqlSession session, PageVO pageVO) {
		 return session.selectOne("support.getAdminSupportCount");
	}

	@Override
	public List<SupportVO> selectAdminSupportListWithPaging(SqlSession session, PageVO pageVO) {
		return session.selectList("support.selectAdminSupportListWithPaging", pageVO);
	}
}
