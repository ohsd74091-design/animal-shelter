package kr.or.ddit.admin.notification.service;

import kr.or.ddit.admin.notification.vo.AdminNotificationResponseVO;

public interface IAdminNotificationService {
    AdminNotificationResponseVO getNotificationList(String contextPath);
}