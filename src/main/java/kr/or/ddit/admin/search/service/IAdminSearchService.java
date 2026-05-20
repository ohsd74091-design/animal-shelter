package kr.or.ddit.admin.search.service;

import kr.or.ddit.admin.search.vo.AdminSearchResponseVO;

public interface IAdminSearchService {

    AdminSearchResponseVO getSearchSuggest(String keyword, String contextPath);

    int getAnimalPageByAnimalId(int animalId, int countPerPage);
}