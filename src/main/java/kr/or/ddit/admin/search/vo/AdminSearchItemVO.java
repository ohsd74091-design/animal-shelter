package kr.or.ddit.admin.search.vo;

import lombok.Data;

@Data
public class AdminSearchItemVO {
    private String type;       // animal / member / support
    private String id;         // animalId / memberId / supportId
    private String title;      // 동물명 / 닉네임 / 문의제목
    private String subText;    // 부가정보
    private String imageName;  // 동물사진 / 프로필사진
    private String moveUrl;    // 클릭 이동 URL
}