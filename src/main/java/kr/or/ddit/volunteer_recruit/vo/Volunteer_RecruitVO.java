package kr.or.ddit.volunteer_recruit.vo;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Volunteer_RecruitVO {
	private int recruitId; /*  */
	private String title; /*  */
	private String content; /*  */
	private Date volunteerDate; /*  */
	private String location; /*  */
	private Date applyDeadline; /*  */
	private Date createDate; /*  */
	private String status; /*  */
	private String memberId; /*  */
	private String thumbnailImg; /*  */
	private String recruitSummary;
}
