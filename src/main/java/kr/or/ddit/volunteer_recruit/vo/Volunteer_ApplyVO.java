package kr.or.ddit.volunteer_recruit.vo;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Volunteer_ApplyVO {
	private int volunteerId;
	private String memberId;
	private int recruitId;
	private String interestType;
	private String status;
	private String applyReason;
	private Date applyDate;
	private Date volunteerDate;
	private String nickname;
	private String phone;
}
