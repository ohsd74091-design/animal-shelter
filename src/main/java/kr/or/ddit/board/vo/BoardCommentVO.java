package kr.or.ddit.board.vo;

public class BoardCommentVO {
  
	private int commentId; // 댓글 번호
	private int boardId; // 게시글 번호
    private String memberId; // 작성자 아이디
    private String content; // 댓글 내용
    private String createDate; // 작성일
    private String role; // 작성자의 권한
    private Integer parentCommentId;
    
    public BoardCommentVO() {
    }
    
    public int getCommentId() {
    	return commentId;
    }
    
    public void setCommentId(int commentId) {
    	this.commentId = commentId;
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
    
    public String getContent() {
    	return content;
    }
    
    public void setContent(String content) {
    	this.content = content;
    }
    
    public String getCreateDate() {
    	return createDate;
    }
    
    public void setCreateDate(String createDate) {
    	this.createDate = createDate;
    }
    
    public Integer getParentCommentId() {
    	return parentCommentId;
    }
    
    public void setParentCommentId(Integer parentCommentId) {
    	this.parentCommentId = parentCommentId;
    }
    
    public String getRole() {
    	return role;
    }
    
    public void setRole(String role) {
    	this.role = role;
    }
}