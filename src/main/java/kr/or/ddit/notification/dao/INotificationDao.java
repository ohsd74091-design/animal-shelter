package kr.or.ddit.notification.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import kr.or.ddit.notification.vo.NotificationVO;

public interface INotificationDao {
	int insertNotification(SqlSession session, NotificationVO vo);

    List<NotificationVO> selectNotificationList(SqlSession session, String memberId);

    int selectUnreadCount(SqlSession session, String memberId);

    int updateRead(SqlSession session, NotificationVO vo);

    String selectBoardWriterId(SqlSession session, int boardId);

    String selectVolunteerApplyMemberId(SqlSession session, int volunteerId);

    String selectAdoptionMemberId(SqlSession session, int adoptionId);

    List<String> selectFavoriteMemberIdsByAnimalId(SqlSession session, int animalId);
    
    String selectCommentWriterId(SqlSession session, int commentId);
}
