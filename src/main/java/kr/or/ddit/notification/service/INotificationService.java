package kr.or.ddit.notification.service;

import java.util.List;

import kr.or.ddit.notification.vo.NotificationVO;

public interface INotificationService {

    int insertNotification(NotificationVO vo);

    List<NotificationVO> getNotificationList(String memberId);

    int getUnreadCount(String memberId);

    int readNotification(int notiId, String memberId);

    void notifyBoardComment(int boardId, int commentId, Integer parentCommentId, String commenterId);

    void notifyVolunteerStatus(int volunteerId, String status);

    void notifyAdoptionStatus(int adoptionId, String status);

    void notifyFavoriteAnimalAdopted(int animalId);
}