package kr.or.ddit.admin.notification.vo;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class AdminNotificationResponseVO {
    private boolean hasUnread;
    private List<AdminNotificationItemVO> items = new ArrayList<>();
}