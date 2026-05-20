package kr.or.ddit.report.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import kr.or.ddit.report.vo.AnimalReportVO;
import kr.or.ddit.report.vo.MemberReportVO;

public interface IReportDao {

	public int insertAnimalReport(SqlSession session,AnimalReportVO vo);
	
	public int insertMemberRepor(SqlSession session,MemberReportVO vo);
	
	List<AnimalReportVO> selectAnimalReportList(SqlSession session);
	
	 public int selectAnimalReportCount(SqlSession session);
	 
	 public int selectAnimalPendingCount(SqlSession session);
	 
	 int updateAnimalReportProcess(SqlSession session, int reportId, String processYn);
	 
	 List<MemberReportVO> selectMemberReportList(SqlSession session);
	 
	 int selectMemberReportCount(SqlSession session);
	 
	 int selectMemberPendingCount(SqlSession session);
	 
	 int updateMemberReportStatus(SqlSession session, int reportId, String status);
	
}
