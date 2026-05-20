package kr.or.ddit.report.service;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import kr.or.ddit.report.dao.IReportDao;
import kr.or.ddit.report.dao.ReportDaoImpl;
import kr.or.ddit.report.vo.AnimalReportVO;
import kr.or.ddit.report.vo.MemberReportVO;
import kr.or.ddit.util.MailUtil;
import kr.or.ddit.util.MybatisUtil;

public class ReportServiceImpl implements IReportService {

    private static IReportService service = new ReportServiceImpl();
    private IReportDao dao;

    private ReportServiceImpl() { dao = ReportDaoImpl.getdao(); }

    public static IReportService getservice() { return service; }

    // ========================= 유기동물 제보 =========================

    @Override
    public int insertAnimalReport(AnimalReportVO vo) {
        int cnt = 0;
        try (SqlSession session = MybatisUtil.getsqlsession(false)) {
            cnt = dao.insertAnimalReport(session, vo);
            if (cnt > 0) session.commit();
            else         session.rollback();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cnt;
    }

    @Override
    public List<AnimalReportVO> selectAnimalReportList() {
        try (SqlSession session = MybatisUtil.getsqlsession()) {
            return dao.selectAnimalReportList(session);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int selectAnimalReportCount() {
        try (SqlSession session = MybatisUtil.getsqlsession()) {
            return dao.selectAnimalReportCount(session);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int selectAnimalPendingCount() {
        try (SqlSession session = MybatisUtil.getsqlsession()) {
            return dao.selectAnimalPendingCount(session);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public boolean updateAnimalReportProcess(int reportId, String action, String replyEmail, String processContent) {
    	SqlSession session = MybatisUtil.getsqlsession(false);

        try {
            int cnt = dao.updateAnimalReportProcess(session, reportId, action);

            if (cnt <= 0) {
                session.rollback();
                return false;
            }

            String resultText = "Y".equals(action) ? "처리완료" : "기각";
            String subject = "[너와 나의 연결고리] 유기동물 제보 처리 결과 안내";
            String content = "안녕하세요.\n\n"
                    + "접수하신 유기동물 제보의 처리 결과를 안내드립니다.\n\n"
                    + "처리 상태 : " + resultText + "\n"
                    + "처리 내용 : " + processContent + "\n\n"
                    + "소중한 제보에 감사드립니다.";

            MailUtil.sendMail(replyEmail, subject, content);

            session.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            session.rollback();
            return false;
        } finally {
            session.close();
        }
    }
    // ========================= 회원 신고 =========================

    @Override
    public int insertMemberRepor(MemberReportVO vo) {
        int cnt = 0;
        try (SqlSession session = MybatisUtil.getsqlsession(false)) {
            cnt = dao.insertMemberRepor(session, vo);
            if (cnt > 0) session.commit();
            else         session.rollback();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cnt;
    }

    @Override
    public List<MemberReportVO> selectMemberReportList() {
        try (SqlSession session = MybatisUtil.getsqlsession()) {
            return dao.selectMemberReportList(session);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int selectMemberReportCount() {
        try (SqlSession session = MybatisUtil.getsqlsession()) {
            return dao.selectMemberReportCount(session);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int selectMemberPendingCount() {
        try (SqlSession session = MybatisUtil.getsqlsession()) {
            return dao.selectMemberPendingCount(session);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public boolean updateMemberReportStatus(int reportId, String action, String replyEmail, String processContent) {
    	SqlSession session = MybatisUtil.getsqlsession(false);

        try {
            int cnt = dao.updateMemberReportStatus(session, reportId, action);

            if (cnt <= 0) {
                session.rollback();
                return false;
            }

            String resultText = "Y".equals(action) ? "처리완료" : "기각";
            String subject = "[너와 나의 연결고리] 회원 신고 처리 결과 안내";
            String content = "안녕하세요.\n\n"
                    + "접수하신 회원 신고의 처리 결과를 안내드립니다.\n\n"
                    + "처리 상태 : " + resultText + "\n"
                    + "처리 내용 : " + processContent + "\n\n"
                    + "소중한 신고에 감사드립니다.";

            MailUtil.sendMail(replyEmail, subject, content);

            session.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            session.rollback();
            return false;
        } finally {
            session.close();
        }
    }
}