package kr.or.ddit.admin.search.vo;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class AdminSearchResponseVO {
    private List<AdminSearchItemVO> animals = new ArrayList<>();
    private List<AdminSearchItemVO> members = new ArrayList<>();
    private List<AdminSearchItemVO> supports = new ArrayList<>();
}