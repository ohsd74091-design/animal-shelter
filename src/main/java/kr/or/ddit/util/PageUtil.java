package kr.or.ddit.util;

public class PageUtil {
 
    public static String pageList(int sp, int ep, int tp, int currentPage) {
        
        StringBuilder pager = new StringBuilder(); // 문자열 결합 효율을 위해 StringBuilder 사용
        
        // 전체 감싸는 태그
        pager.append("<div class='pagination-container'>"); 
        pager.append("<ul class='pagination'>");

        //  [이전(Previous)] 버튼 처리
        if(sp > 1){
            // 클릭 시 해당 페이지로 이동하도록 <a> 태그에 링크(href) 또는 id를 추가
            pager.append("<li class='page-item'>");
            pager.append("<a href='javascript:void(0)' class='page-link' onclick='changePage(" + (sp - 1) + ")'>&laquo; 이전</a>");
            pager.append("</li>");
        }
        
        // 현재 페이지가 전체 페이지보다 큰 경우 보정
        if(currentPage > tp) currentPage = tp;
        
        //  [숫자 번호] 나열
        for(int i = sp; i <= ep; i++){
            if(i == currentPage){
                // 현재 보고 있는 페이지는 'active' 클래스를 붙여 강조합니다.
                pager.append("<li class='page-item active'>");
                pager.append("<span class='page-link'>" + i + "</span>"); 
                pager.append("</li>");
            } else {
                // 다른 페이지 번호들
                pager.append("<li class='page-item'>");
                pager.append("<a href='javascript:void(0)' class='page-link' onclick='changePage(" + i + ")'>" + i + "</a>");
                pager.append("</li>");
            }
        }
        
        //  [다음(Next)] 버튼 처리
        if(ep < tp){
            pager.append("<li class='page-item'>");
            pager.append("<a href='javascript:void(0)' class='page-link' onclick='changePage(" + (ep + 1) + ")'>다음 &raquo;</a>");
            pager.append("</li>");
        }
        
        pager.append("</ul>");
        pager.append("</div>");
        
        return pager.toString();
    }
}