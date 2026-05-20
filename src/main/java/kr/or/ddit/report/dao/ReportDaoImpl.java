package kr.or.ddit.report.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import kr.or.ddit.report.vo.AnimalReportVO;
import kr.or.ddit.report.vo.MemberReportVO;

public class ReportDaoImpl implements IReportDao {

    private static IReportDao dao = new ReportDaoImpl();
    private ReportDaoImpl() {}

    public static IReportDao getdao() { return dao; }

    // ========================= 유기동물 제보 =========================

    @Override
    public int insertAnimalReport(SqlSession session, AnimalReportVO vo) {
        return session.insert("report.insertAnimalReport", vo);
    }

    @Override
    public List<AnimalReportVO> selectAnimalReportList(SqlSession session) {
        return session.selectList("report.selectAnimalReportList");
    }

    @Override
    public int selectAnimalReportCount(SqlSession session) {
        return session.selectOne("report.selectAnimalReportCount");
    }

    @Override
    public int selectAnimalPendingCount(SqlSession session) {
        return session.selectOne("report.selectAnimalPendingCount");
    }

    @Override
    public int updateAnimalReportProcess(SqlSession session, int reportId, String processYn) {
        Map<String, Object> param = new HashMap<>();
        param.put("reportId",  reportId);
        param.put("processYn", processYn);
        return session.update("report.updateAnimalReportProcess", param);
    }

    // ========================= 회원 신고 =========================

    @Override
    public int insertMemberRepor(SqlSession session, MemberReportVO vo) {
        return session.insert("report.insertMemberReport", vo);
    }

    @Override
    public List<MemberReportVO> selectMemberReportList(SqlSession session) {
        return session.selectList("report.selectMemberReportList");
    }

    @Override
    public int selectMemberReportCount(SqlSession session) {
        return session.selectOne("report.selectMemberReportCount");
    }

    @Override
    public int selectMemberPendingCount(SqlSession session) {
        return session.selectOne("report.selectMemberPendingCount");
    }

    @Override
    public int updateMemberReportStatus(SqlSession session, int reportId, String status) {
        Map<String, Object> param = new HashMap<>();
        param.put("reportId", reportId);
        param.put("status",   status);
        return session.update("report.updateMemberReportStatus", param);
    }
}