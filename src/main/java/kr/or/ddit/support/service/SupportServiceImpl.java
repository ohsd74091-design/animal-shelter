package kr.or.ddit.support.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.ibatis.session.SqlSession;

import jakarta.servlet.http.Part;
import kr.or.ddit.common.vo.PageVO;
import kr.or.ddit.support.dao.ISupportDao;
import kr.or.ddit.support.dao.ISupportFileDao;
import kr.or.ddit.support.dao.SupportDaoImpl;
import kr.or.ddit.support.dao.SupportFileDaoImpl;
import kr.or.ddit.support.vo.SupportFileVO;
import kr.or.ddit.support.vo.SupportVO;
import kr.or.ddit.util.MybatisUtil;

public class SupportServiceImpl implements ISupportService{
	
	private static ISupportService supportService = new SupportServiceImpl();
	private ISupportDao supportDao;
	private ISupportFileDao fileDao;
	
	private static final String UPLOAD_DIR = "d:/D_Other/upload_files";
	
	private SupportServiceImpl() {
		supportDao = SupportDaoImpl.getInstance();
		fileDao = SupportFileDaoImpl.getInstance();
		
		File uploadDir = new File(UPLOAD_DIR);
		if(!uploadDir.exists()) uploadDir.mkdir();
	}
	
	public static ISupportService getInstance() {
		return supportService;
		
	}

	@Override
	public int registerSupport(SupportVO svo, List<Part> parts) {
		
		int cnt = 0;
		try(SqlSession session = MybatisUtil.getsqlsession(false)) {
			
			cnt = supportDao.insertSupport(session, svo);
			
			if(cnt > 0) {  // 글 저장이 성공했다면
				// 파일이 있을 때만 파일 등록 로직 실행
				if(parts != null && !parts.isEmpty()) {
					for(Part part : parts) {
					// 파일 이름이 없는 경우(사용자가 파일을 선택 안 함)를 철저히 체크
						String originName = part.getSubmittedFileName();
						
						// 파일 이름이 없거나, 크기가 0이면 이 파트는 건너뜀.
	                    if (originName == null || originName.isEmpty() || part.getSize() <= 0) {
	                        continue; 
	                    }
	                    
	                    // 확장자 추출 전 점(.)이 있는지 확인 (파일명이 이상할 경우 대비)
	                    int dotIndex = originName.lastIndexOf(".");
	                    if (dotIndex == -1) {
	                        continue; // 확장자가 없는 파일은 무시하거나 기본값 설정
	                    }
	                    String ext = originName.substring(dotIndex + 1);
                        String saveName = UUID.randomUUID().toString() + "." + ext;
                        
                        part.write(UPLOAD_DIR + File.separator + saveName); // 하드디스크 저장

                        // DB 저장용 VO 설정
                        SupportFileVO fileVO = new SupportFileVO();
                        fileVO.setSupportId(svo.getSupportId()); // selectKey로 받아온 번호!
                        fileVO.setOriginFileName(originName);
                        fileVO.setSaveFileName(saveName);
                        fileVO.setFilePath(UPLOAD_DIR);
                        fileVO.setFileSize(part.getSize());
                        fileVO.setFileExt(ext);
                        
                        // 파일 DAO 호출 (동일한 session 전달)
                        fileDao.insertSupportFile(session, fileVO);
						
					}
				}
				// 모든 파일 처리가 정상적으로 끝나면 최종 커밋!
	            session.commit();
			}
			
		}catch(Exception ex) {
			ex.printStackTrace();
			// 에러 발생 시 cnt를 0으로 돌려 실패를 알림.
	        cnt = 0;
		}
		
		return cnt;
	}

	@Override
	public List<SupportVO> selectSupportList(String memberId) {
		
		List<SupportVO> sList = new ArrayList<SupportVO>();
		try(SqlSession session = MybatisUtil.getsqlsession()) {
			
			sList = supportDao.selectSupportList(session, memberId);
			
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return sList;
	}

	@Override
	public SupportVO selectSupportDetail(int supportId) {
		SupportVO svo = null;
		
		try(SqlSession session = MybatisUtil.getsqlsession()) {
			// 특정 ID의 상세 데이터를 가져오라고..
			svo = supportDao.selectSupportDetail(session, supportId);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return svo;
	}

	@Override
	public int updateSupport(SupportVO svo, List<Part> parts) {
		int cnt = 0;
		// 세션 열기 (수동 커밋 모드 false)
		try(SqlSession session = MybatisUtil.getsqlsession(false)) {
			
			// 게시글 텍스트(제목, 내용 등) 업데이트
			cnt = supportDao.updateSupport(session, svo);
			
			if(cnt > 0) {
				// 수정 시 이미지를 새로 올렸다면 기존 이미지를 교체하는 것이 일반적
				if(parts != null && !parts.isEmpty()) {
					boolean hasNewFile = false;
					for(Part part : parts) {
						if(part.getSize() > 0) { hasNewFile = true; break; }
					}
					
					if(hasNewFile) {
						// 기존 DB의 파일 정보 삭제 (자식 먼저)
						supportDao.deleteSupportFiles(session, svo.getSupportId());
						
						// 새로운 파일 저장 로직 (registerSupport와 동일)
						for(Part part : parts) {
							String originName = part.getSubmittedFileName();
							if (originName == null || originName.isEmpty() || part.getSize() <= 0) continue;

							String ext = originName.substring(originName.lastIndexOf(".") + 1);
							String saveName = UUID.randomUUID().toString() + "." + ext;
							
							// 서버 하드디스크에 저장
							part.write(UPLOAD_DIR + File.separator + saveName);

							// 새로운 파일 정보 DB 저장
							SupportFileVO fileVO = new SupportFileVO();
							fileVO.setSupportId(svo.getSupportId());
							fileVO.setOriginFileName(originName);
							fileVO.setSaveFileName(saveName);
							fileVO.setFilePath(UPLOAD_DIR);
							fileVO.setFileSize(part.getSize());
							fileVO.setFileExt(ext);
							
							fileDao.insertSupportFile(session, fileVO);
						}
					}
				}
				session.commit(); // 모든 과정 성공 시 확정
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			cnt = 0;
		}
		return cnt;
	}

	@Override
	public int deleteSupport(int sId) {
		int cnt = 0;
		try(SqlSession session = MybatisUtil.getsqlsession(false)) {
			
			SupportVO svo = supportDao.selectSupportDetail(session, sId);
			
			supportDao.deleteSupportFiles(session, sId);
			
			cnt = supportDao.deleteSupport(session, sId);
			
			if(cnt > 0) {
				if(svo != null && svo.getFileList() !=null) {
					for(SupportFileVO fvo : svo.getFileList()) {
						File file = new File(fvo.getFilePath() 
								+ File.separator + fvo.getSaveFileName());
						if(file.exists()) file.delete();
					}
				}
				session.commit();
			}
			
		}catch(Exception ex) {
			ex.printStackTrace();
			cnt = 0;
		}
		return cnt;
	}

	@Override
	public PageVO calculatePageInfo(int currentPage, String memberId, String status) {
		
	    try(SqlSession session = MybatisUtil.getsqlsession()) {
	    	// 전체 게시글 수 조회
		
	    
	    PageVO pageVO = new PageVO();
	    pageVO.setMemberId(memberId);
	    pageVO.setStatus(status);
	    int totalCount = supportDao.getSupportCount(session, pageVO);
	    
	    pageVO.setCurrentPage(currentPage);
	    pageVO.setCountPerPage(5); // 한 페이지당 5개 
	    pageVO.setPageCount(3);    // 하단 번호 3개씩 
	    pageVO.setTotalCount(totalCount);

	    // 수학적 계산 수행 (서비스에서 직접 계산하여 세팅)
	    int totalPage = (int) Math.ceil((double) totalCount / pageVO.getCountPerPage());
	    pageVO.setTotalPage(totalPage);

	    // DB에서 가져올 행 번호 계산
	    int startRow = (currentPage - 1) * pageVO.getCountPerPage() + 1;
	    int endRow = startRow + pageVO.getCountPerPage() - 1;
	    if (endRow > totalCount) endRow = totalCount;

	    pageVO.setStartRow(startRow);
	    pageVO.setEndRow(endRow);

	    // 하단 페이지네이션 번호 계산
	    int startPage = ((currentPage - 1) / pageVO.getPageCount() * pageVO.getPageCount()) + 1;
	    int endPage = startPage + pageVO.getPageCount() - 1;
	    if (endPage > totalPage) endPage = totalPage;

	    pageVO.setStartPage(startPage);
	    pageVO.setEndPage(endPage);
	    pageVO.setTotalCount(totalCount);
	    
	    return pageVO;
	    }catch(Exception ex){
	    	ex.printStackTrace();
	    	throw new RuntimeException("페이지 정보 계산 중 오류 발생", ex);
	    }
	}

	@Override
	public List<SupportVO> selectSupportListWithPaging(PageVO pageVO) {
		
		List<SupportVO> pList = new ArrayList<SupportVO>();
		try(SqlSession session = MybatisUtil.getsqlsession()) {
			
			pList = supportDao.selectSupportListWithPaging(session, pageVO);
			
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return pList;
	}

}
