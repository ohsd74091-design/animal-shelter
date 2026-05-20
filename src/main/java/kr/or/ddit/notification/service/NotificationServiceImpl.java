package kr.or.ddit.notification.service;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import kr.or.ddit.notification.dao.INotificationDao;
import kr.or.ddit.notification.dao.NotificationDaoImpl;
import kr.or.ddit.notification.vo.NotificationVO;
import kr.or.ddit.util.MybatisUtil;

public class NotificationServiceImpl implements INotificationService {

    private static INotificationService service = new NotificationServiceImpl();
    private INotificationDao dao;

    private NotificationServiceImpl() {
        dao = NotificationDaoImpl.getInstance();
    }

    public static INotificationService getInstance() {
        return service;
    }

    @Override
    public int insertNotification(NotificationVO vo) {
        try (SqlSession session = MybatisUtil.getsqlsession(false)) {
            int cnt = dao.insertNotification(session, vo);
            if (cnt > 0) {
                session.commit();
            } else {
                session.rollback();
            }
            return cnt;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public List<NotificationVO> getNotificationList(String memberId) {
        try (SqlSession session = MybatisUtil.getsqlsession()) {
            return dao.selectNotificationList(session, memberId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int getUnreadCount(String memberId) {
        try (SqlSession session = MybatisUtil.getsqlsession()) {
            return dao.selectUnreadCount(session, memberId);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int readNotification(int notiId, String memberId) {
        try (SqlSession session = MybatisUtil.getsqlsession(false)) {
            NotificationVO vo = new NotificationVO();
            vo.setNotiId(notiId);
            vo.setMemberId(memberId);

            int cnt = dao.updateRead(session, vo);
            if (cnt > 0) {
                session.commit();
            } else {
                session.rollback();
            }
            return cnt;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public void notifyBoardComment(int boardId, int commentId, Integer parentCommentId, String commenterId) {
        try (SqlSession session = MybatisUtil.getsqlsession(false)) {

            String receiverId = null;
            String message = null;

            // 대댓글이면 부모 댓글 작성자에게 알림
            if (parentCommentId != null) {
                receiverId = dao.selectCommentWriterId(session, parentCommentId);

                if (receiverId != null && !receiverId.equals(commenterId)) {
                    message = "내 댓글에 답글이 달렸습니다.";
                }
            } else {
                // 일반 댓글이면 게시글 작성자에게 알림
                receiverId = dao.selectBoardWriterId(session, boardId);

                if (receiverId != null && !receiverId.equals(commenterId)) {
                    message = "내 게시글에 댓글이 달렸습니다.";
                }
            }

            if (receiverId == null || receiverId.equals(commenterId) || message == null) {
                return;
            }

            NotificationVO vo = new NotificationVO();
            vo.setMemberId(receiverId);
            vo.setNotiType("COMMENT");
            vo.setNotiMsg(message);
            vo.setLinkUrl("/board/detail.do?boardId=" + boardId + "&focusCommentId=" + commentId);

            int cnt = dao.insertNotification(session, vo);
            if (cnt > 0) {
                session.commit();
            } else {
                session.rollback();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void notifyVolunteerStatus(int volunteerId, String status) {
        try (SqlSession session = MybatisUtil.getsqlsession(false)) {
            String memberId = dao.selectVolunteerApplyMemberId(session, volunteerId);

            if (memberId == null || memberId.trim().isEmpty()) {
                return;
            }

            NotificationVO vo = new NotificationVO();
            vo.setMemberId(memberId);
            vo.setNotiType("VOLUNTEER");
            vo.setNotiMsg("봉사 신청이 [" + status + "] 처리되었습니다.");
            vo.setLinkUrl("/mypage/main.do");

            int cnt = dao.insertNotification(session, vo);
            if (cnt > 0) {
                session.commit();
            } else {
                session.rollback();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void notifyAdoptionStatus(int adoptionId, String status) {
        try (SqlSession session = MybatisUtil.getsqlsession(false)) {
            String memberId = dao.selectAdoptionMemberId(session, adoptionId);

            if (memberId == null || memberId.trim().isEmpty()) {
                return;
            }

            NotificationVO vo = new NotificationVO();
            vo.setMemberId(memberId);
            vo.setNotiType("ADOPTION");
            vo.setNotiMsg("입양 신청이 [" + status + "] 처리되었습니다.");
            vo.setLinkUrl("/mypage/main.do");

            int cnt = dao.insertNotification(session, vo);
            if (cnt > 0) {
                session.commit();
            } else {
                session.rollback();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void notifyFavoriteAnimalAdopted(int animalId) {
        try (SqlSession session = MybatisUtil.getsqlsession(false)) {
            List<String> memberList = dao.selectFavoriteMemberIdsByAnimalId(session, animalId);

            if (memberList == null || memberList.isEmpty()) {
                return;
            }

            for (String memberId : memberList) {
                NotificationVO vo = new NotificationVO();
                vo.setMemberId(memberId);
                vo.setNotiType("FAVORITE");
                vo.setNotiMsg("찜한 동물이 입양 완료되었습니다.");
                vo.setLinkUrl("/animal/animalList.do");
                dao.insertNotification(session, vo);
            }

            session.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}