package kr.or.ddit.admin.notification.service;

import org.apache.ibatis.session.SqlSession;

import kr.or.ddit.admin.notification.dao.AdminNotificationDaoImpl;
import kr.or.ddit.admin.notification.dao.IAdminNotificationDao;
import kr.or.ddit.admin.notification.vo.AdminNotificationItemVO;
import kr.or.ddit.admin.notification.vo.AdminNotificationResponseVO;
import kr.or.ddit.util.MybatisUtil;

public class AdminNotificationServiceImpl implements IAdminNotificationService {

    private static IAdminNotificationService service = new AdminNotificationServiceImpl();
    private IAdminNotificationDao dao;

    private AdminNotificationServiceImpl() {
        dao = AdminNotificationDaoImpl.getInstance();
    }

    public static IAdminNotificationService getInstance() {
        return service;
    }

    @Override
    public AdminNotificationResponseVO getNotificationList(String contextPath) {
        AdminNotificationResponseVO result = new AdminNotificationResponseVO();

        try (SqlSession session = MybatisUtil.getsqlsession()) {
            int animalCnt = dao.selectAnimalPendingCount(session);
            int memberReportCnt = dao.selectMemberReportPendingCount(session);
            int supportCnt = dao.selectSupportPendingCount(session);

            if (animalCnt > 0) {
                AdminNotificationItemVO item = new AdminNotificationItemVO();
                item.setType("animal");
                item.setLabel("유기동물 제보");
                item.setCount(animalCnt);
                item.setUrl(contextPath + "/admin/report/list.do?tab=animal");
                result.getItems().add(item);
            }

            if (memberReportCnt > 0) {
                AdminNotificationItemVO item = new AdminNotificationItemVO();
                item.setType("member");
                item.setLabel("회원 신고");
                item.setCount(memberReportCnt);
                item.setUrl(contextPath + "/admin/report/list.do?tab=member");
                result.getItems().add(item);
            }

            if (supportCnt > 0) {
                AdminNotificationItemVO item = new AdminNotificationItemVO();
                item.setType("support");
                item.setLabel("문의");
                item.setCount(supportCnt);
                item.setUrl(contextPath + "/admin/support/list.do");
                result.getItems().add(item);
            }

            result.setHasUnread(!result.getItems().isEmpty());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}