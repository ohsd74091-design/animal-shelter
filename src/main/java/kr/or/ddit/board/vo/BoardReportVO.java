package kr.or.ddit.board.vo;

public class BoardReportVO {
    private int reportId;
    private int boardId;
    private String memberId;
    private String reason;
    private String reportDate;
    private String status;
    
    public int getReportId() {
    	return reportId;
    }
    public void setReportId(int reportId) {
    	this.reportId = reportId;
    }
    public int getBoardId() {
        return boardId;
    }
    public void setBoardId(int boardId) {
    	this.boardId = boardId;
    }
    public String getMemberId() {
		return memberId;
	}
    public void setMemberId(String memberId) {
    	this.memberId = memberId;
    }
    public String getReason() {
    	return reason;
    }
    public void setReason(String reason) {
    	this.reason = reason;
    }
    public String getReportDate() {
    	return reportDate;
    }
    public void setReportDate(String reportDate) {
    	this.reportDate = reportDate;
    }
    public String getStatus() {
    	return status;
    }
    public void setStatus(String status) {
    	this.status = status;
    }
}
