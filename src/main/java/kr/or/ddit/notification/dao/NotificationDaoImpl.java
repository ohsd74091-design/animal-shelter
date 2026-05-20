package kr.or.ddit.notification.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import kr.or.ddit.notification.vo.NotificationVO;

public class NotificationDaoImpl implements INotificationDao {

	private static INotificationDao dao = new NotificationDaoImpl();

    private NotificationDaoImpl() {}

    public static INotificationDao getInstance() {
        return dao;
    }

    @Override
    public int insertNotification(SqlSession session, NotificationVO vo) {
        return session.insert("notification.insertNotification", vo);
    }

    @Override
    public List<NotificationVO> selectNotificationList(SqlSession session, String memberId) {
        return session.selectList("notification.selectNotificationList", memberId);
    }

    @Override
    public int selectUnreadCount(SqlSession session, String memberId) {
        return session.selectOne("notification.selectUnreadCount", memberId);
    }

    @Override
    public int updateRead(SqlSession session, NotificationVO vo) {
        return session.update("notification.updateRead", vo);
    }

    @Override
    public String selectBoardWriterId(SqlSession session, int boardId) {
        return session.selectOne("notification.selectBoardWriterId", boardId);
    }

    @Override
    public String selectVolunteerApplyMemberId(SqlSession session, int volunteerId) {
        return session.selectOne("notification.selectVolunteerApplyMemberId", volunteerId);
    }

    @Override
    public String selectAdoptionMemberId(SqlSession session, int adoptionId) {
        return session.selectOne("notification.selectAdoptionMemberId", adoptionId);
    }

    @Override
    public List<String> selectFavoriteMemberIdsByAnimalId(SqlSession session, int animalId) {
        return session.selectList("notification.selectFavoriteMemberIdsByAnimalId", animalId);
    }

	@Override
	public String selectCommentWriterId(SqlSession session, int commentId) {
		
		return session.selectOne("notification.selectCommentWriterId", commentId);
	}
}
