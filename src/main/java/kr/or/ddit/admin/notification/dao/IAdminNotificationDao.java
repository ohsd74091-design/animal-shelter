package kr.or.ddit.admin.notification.dao;

import org.apache.ibatis.session.SqlSession;

public interface IAdminNotificationDao {
    int selectAnimalPendingCount(SqlSession session);
    int selectMemberReportPendingCount(SqlSession session);
    int selectSupportPendingCount(SqlSession session);
}