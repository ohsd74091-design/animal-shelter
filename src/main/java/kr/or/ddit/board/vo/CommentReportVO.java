package kr.or.ddit.board.vo;

import jakarta.servlet.annotation.WebServlet;
import lombok.Data;

@Data
public class CommentReportVO {
	private int reportId;
    private int commentId;
    private String memberId;
    private String reason;
}
