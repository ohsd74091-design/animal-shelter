package kr.or.ddit.board.vo;

public class BoardFileVO {
  
	private int fileId; // 파일 번호
	private int boardId; // 게시글 번호
	private String originFileName; // 원본 파일명
	private String saveFileName; // 저장 파일명
	private String filePath; // 저장 경로
	private long fileSize; // 파일 크기
	private String fileExt; // 확장자
	private String createDate; // 등록일
	private String isImage; // 이미지 여부
	
	public BoardFileVO() {		
	}
	
	public int getFileId() {
		return fileId;
	}
	
	public void setFileId(int fileId) {
		this.fileId = fileId;
	}
	
	public int getBoardId() {
		return boardId;
	}
	
	public void setBoardId(int boardId) {
		this.boardId = boardId;
	}
	
	public String getOriginFileName() {
		return originFileName;
	}
	
    public void setOriginFileName(String originFileName) {
    	this.originFileName = originFileName;
    }
    
    public String getSaveFileName() {
    	return saveFileName;
    }
    
    public void setSaveFileName(String saveFileName) {
    	this.saveFileName = saveFileName;
    }
    
    public String getFilePath() {
    	return filePath;
    }
    
    public void setFilePath(String filePath) {
    	this.filePath = filePath;
    }
    
    public long getFileSize() {
    	return fileSize;
    }
    
    public void setFileSize(long fileSize) {
    	this.fileSize = fileSize;
    }
    
    public String getFileExt() {
    	return fileExt;   
    }
    
    public void setFileExt(String fileExt) {
    	this.fileExt = fileExt;
    }
    
    public String getCreateDate() {
    	return createDate;
    }
    
    public void setCreateDate(String createDate) {
    	this.createDate = createDate;
    }
    
    public String getIsImage() {
    	return isImage;
    }
    
    public void setIsImage(String isImage) {
    	this.isImage = isImage;
    }
}
