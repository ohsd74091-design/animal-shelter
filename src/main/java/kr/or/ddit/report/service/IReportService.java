package kr.or.ddit.report.service;

import java.util.List;
import kr.or.ddit.report.vo.AnimalReportVO;
import kr.or.ddit.report.vo.MemberReportVO;

public interface IReportService {

    // ========================= 유기동물 제보 =========================

    /** 유기동물 제보 등록 */
    int insertAnimalReport(AnimalReportVO vo);

    /** 유기동물 제보 전체 목록 (관리자) */
    List<AnimalReportVO> selectAnimalReportList();

    /** 유기동물 제보 전체 건수 */
    int selectAnimalReportCount();

    /** 유기동물 제보 미처리 건수 */
    int selectAnimalPendingCount();

    /** 유기동물 제보 처리 상태 업데이트 (Y=완료, R=기각) */
    boolean updateAnimalReportProcess(int reportId, String action, String replyEmail, String processContent);

    // ========================= 회원 신고 =========================

    /** 회원 신고 등록 */
    int insertMemberRepor(MemberReportVO vo);

    /** 회원 신고 전체 목록 (관리자) */
    List<MemberReportVO> selectMemberReportList();

    /** 회원 신고 전체 건수 */
    int selectMemberReportCount();

    /** 회원 신고 미처리 건수 */
    int selectMemberPendingCount();

    /** 회원 신고 처리 상태 업데이트 (Y=완료, R=기각) */
    boolean updateMemberReportStatus(int reportId, String action, String replyEmail, String processContent);
}