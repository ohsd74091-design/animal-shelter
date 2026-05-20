package kr.or.ddit.admin.notification.dao;

import org.apache.ibatis.session.SqlSession;

public class AdminNotificationDaoImpl implements IAdminNotificationDao {

    private static IAdminNotificationDao dao = new AdminNotificationDaoImpl();

    private AdminNotificationDaoImpl() {}

    public static IAdminNotificationDao getInstance() {
        return dao;
    }

    @Override
    public int selectAnimalPendingCount(SqlSession session) {
        return session.selectOne("adminNotification.selectAnimalPendingCount");
    }

    @Override
    public int selectMemberReportPendingCount(SqlSession session) {
        return session.selectOne("adminNotification.selectMemberReportPendingCount");
    }

    @Override
    public int selectSupportPendingCount(SqlSession session) {
        return session.selectOne("adminNotification.selectSupportPendingCount");
    }
}