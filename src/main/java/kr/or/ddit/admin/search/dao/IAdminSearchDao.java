package kr.or.ddit.admin.search.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import kr.or.ddit.admin.search.vo.AdminSearchItemVO;

public interface IAdminSearchDao {

    List<AdminSearchItemVO> selectAnimalSuggest(SqlSession session, String keyword);

    List<AdminSearchItemVO> selectMemberSuggest(SqlSession session, String keyword);

    List<AdminSearchItemVO> selectSupportSuggest(SqlSession session, String keyword);

    int selectAnimalPageByAnimalId(SqlSession session, int animalId, int countPerPage);
}