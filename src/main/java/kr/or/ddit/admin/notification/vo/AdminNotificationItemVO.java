package kr.or.ddit.admin.notification.vo;

import lombok.Data;

@Data
public class AdminNotificationItemVO {
    private String type;
    private String label;
    private int count;
    private String url;
}