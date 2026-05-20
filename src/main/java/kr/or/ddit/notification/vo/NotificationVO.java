package kr.or.ddit.notification.vo;

import java.util.Date;

import lombok.Data;

@Data
public class NotificationVO {

 private int notiId;
 private String memberId;
 private String notiType;
 private String notiMsg;
 private String linkUrl;
 private String isRead;
 private Date createDate;

}
