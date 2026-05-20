package kr.or.ddit.support.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import kr.or.ddit.common.vo.PageVO;
import kr.or.ddit.support.dao.ISupportDao;
import kr.or.ddit.support.dao.SupportDaoImpl;
import kr.or.ddit.support.vo.SupportReplyVO;
import kr.or.ddit.support.vo.SupportVO;
import kr.or.ddit.util.MybatisUtil;

public class AdminSupportServiceImpl implements IAdminSupportService{
	
	private static IAdminSupportService adminService = new AdminSupportServiceImpl();
	private ISupportDao supportDao;
	
	private AdminSupportServiceImpl() {
		supportDao = SupportDaoImpl.getInstance();
	}
	
	public static IAdminSupportService getInstance() {
		return adminService;
		
	}

	@Override
	public List<SupportVO> getAllSupportList() {
		
		List<SupportVO> list = null;
		try(SqlSession session = MybatisUtil.getsqlsession()) {
			
			list = supportDao.selectSupportListForAdmin(session);
			
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return list;
	}

	@Override
	public int registerReply(SupportReplyVO replyVO) {
		int cnt = 0;
		try(SqlSession session = MybatisUtil.getsqlsession()) {
			// 답변 등록
			cnt = supportDao.insertSupportReply(session, replyVO);
			if(cnt >0) {
				// 등록 성공 시 문의글 상태를 'Y'로 변경
				supportDao.updateSupportStatus(session, replyVO.getSupportId());
	            session.commit(); // DB에 반영
			}
			
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return cnt;
	}

	@Override
	public SupportVO selectSupportDetail(int supportId) {
		SupportVO svo = null; 
	    
	    try(SqlSession session = MybatisUtil.getsqlsession()) {
	        // DAO에서도 selectOne을 호출하여 객체 하나만 가져옴
	        svo = supportDao.selectSupportDetail(session, supportId);
	    } catch(Exception ex) {
	        ex.printStackTrace();
	    }
	    return svo;
	}

	@Override
	public PageVO calculateAdminPageInfo(PageVO pageVO, int currentPage) {
		try(SqlSession session = MybatisUtil.getsqlsession()) {

	        int totalCount = supportDao.getAdminSupportCount(session, pageVO); // DAO도 추가 필요

	        
	        pageVO.setCurrentPage(currentPage);
	        pageVO.setCountPerPage(5); // 한 페이지당 5개
	        pageVO.setPageCount(3);    // 하단 번호 3개씩
	        pageVO.setTotalCount(totalCount);

	        int totalPage = (int) Math.ceil((double) totalCount / pageVO.getCountPerPage());
	        if(totalPage == 0) totalPage = 1;
	        pageVO.setTotalPage(totalPage);

	        int startRow = (currentPage - 1) * pageVO.getCountPerPage() + 1;
	        int endRow   = startRow + pageVO.getCountPerPage() - 1;
	        if(endRow > totalCount) endRow = totalCount;
	        pageVO.setStartRow(startRow);
	        pageVO.setEndRow(endRow);

	        int startPage = ((currentPage - 1) / pageVO.getPageCount() * pageVO.getPageCount()) + 1;
	        int endPage   = startPage + pageVO.getPageCount() - 1;
	        if(endPage > totalPage) endPage = totalPage;
	        pageVO.setStartPage(startPage);
	        pageVO.setEndPage(endPage);

	        return pageVO;
	    } catch(Exception ex) {
	        ex.printStackTrace();
	        throw new RuntimeException("관리자 페이지 계산 오류", ex);
	    }
	}

	@Override
	public List<SupportVO> selectAdminSupportListWithPaging(PageVO pageVO) {
		 List<SupportVO> list = new ArrayList<SupportVO>();
		    try(SqlSession session = MybatisUtil.getsqlsession()) {
		        list = supportDao.selectAdminSupportListWithPaging(session, pageVO);
		    } catch(Exception ex) {
		        ex.printStackTrace();
		    }
		    return list;
	}

}
