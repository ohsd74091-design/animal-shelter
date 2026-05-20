package kr.or.ddit.admin.search.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import kr.or.ddit.admin.search.vo.AdminSearchItemVO;

public class AdminSearchDaoImpl implements IAdminSearchDao {

    private static IAdminSearchDao dao = new AdminSearchDaoImpl();

    private AdminSearchDaoImpl() {}

    public static IAdminSearchDao getInstance() {
        return dao;
    }

    @Override
    public List<AdminSearchItemVO> selectAnimalSuggest(SqlSession session, String keyword) {
        return session.selectList("adminSearch.selectAnimalSuggest", keyword);
    }

    @Override
    public List<AdminSearchItemVO> selectMemberSuggest(SqlSession session, String keyword) {
        return session.selectList("adminSearch.selectMemberSuggest", keyword);
    }

    @Override
    public List<AdminSearchItemVO> selectSupportSuggest(SqlSession session, String keyword) {
        return session.selectList("adminSearch.selectSupportSuggest", keyword);
    }

    @Override
    public int selectAnimalPageByAnimalId(SqlSession session, int animalId, int countPerPage) {
        Map<String, Object> param = new HashMap<>();
        param.put("animalId", animalId);
        param.put("countPerPage", countPerPage);
        return session.selectOne("adminSearch.selectAnimalPageByAnimalId", param);
    }
}