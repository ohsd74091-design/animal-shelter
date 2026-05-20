package kr.or.ddit.admin.search.service;

import org.apache.ibatis.session.SqlSession;

import kr.or.ddit.admin.search.dao.AdminSearchDaoImpl;
import kr.or.ddit.admin.search.dao.IAdminSearchDao;
import kr.or.ddit.admin.search.vo.AdminSearchItemVO;
import kr.or.ddit.admin.search.vo.AdminSearchResponseVO;
import kr.or.ddit.util.MybatisUtil;

public class AdminSearchServiceImpl implements IAdminSearchService {

    private static IAdminSearchService service = new AdminSearchServiceImpl();
    private IAdminSearchDao dao;

    private AdminSearchServiceImpl() {
        dao = AdminSearchDaoImpl.getInstance();
    }

    public static IAdminSearchService getInstance() {
        return service;
    }

    @Override
    public AdminSearchResponseVO getSearchSuggest(String keyword, String contextPath) {
        AdminSearchResponseVO result = new AdminSearchResponseVO();

        try (SqlSession session = MybatisUtil.getsqlsession()) {
            result.setAnimals(dao.selectAnimalSuggest(session, keyword));
            result.setMembers(dao.selectMemberSuggest(session, keyword));
            result.setSupports(dao.selectSupportSuggest(session, keyword));

            for (AdminSearchItemVO item : result.getAnimals()) {
                item.setType("animal");
                item.setMoveUrl(contextPath + "/admin/animal/jump.do?animalId=" + item.getId());
            }

            for (AdminSearchItemVO item : result.getMembers()) {
                item.setType("member");
                item.setMoveUrl(contextPath + "/admin/member/list.do?keyword=" + keyword);
            }

            for (AdminSearchItemVO item : result.getSupports()) {
                item.setType("support");
                item.setMoveUrl(contextPath + "/admin/support/list.do?keyword=" + keyword);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public int getAnimalPageByAnimalId(int animalId, int countPerPage) {
        try (SqlSession session = MybatisUtil.getsqlsession()) {
            Integer page = dao.selectAnimalPageByAnimalId(session, animalId, countPerPage);
            return page == null ? 1 : page;
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
    }
}