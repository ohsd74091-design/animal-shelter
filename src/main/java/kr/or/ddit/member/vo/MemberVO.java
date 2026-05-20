package kr.or.ddit.member.vo;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberVO {
	private String memberId; /*  */
	private String memberPw; /*  */
	private String nickname; /*  */
	private String email; /*  */
	private String phone; /*  */
	private Date joinDate; /*  */
	private String status; /*  */
	private String role; /*  */
	private String profileImg; /*  */
	private String memberName;
	private String isDonor;
	@Override
	public String toString() {
		return "MemberVO [memberId=" + memberId + ", memberPw=" + memberPw + ", nickname=" + nickname + ", email="
				+ email + ", phone=" + phone + ", joinDate=" + joinDate + ", status=" + status + ", role=" + role
				+ ", profileImg=" + profileImg + ", memberName=" + memberName + "]";
	}
}
