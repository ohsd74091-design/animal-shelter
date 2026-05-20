package kr.or.ddit.board.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.or.ddit.board.service.BoardService;
import kr.or.ddit.board.service.BoardServiceImpl;
import kr.or.ddit.board.vo.BoardFileVO;

@WebServlet("/board/fileDownload.do")
public class BoardFileDownloadController extends HttpServlet {
  private static final long serialVersionUID = 1L;
  
  private BoardService boardService = BoardServiceImpl.getInstance();

@Override
protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	
	int fileId = Integer.parseInt(req.getParameter("fileId"));
	
	// 파일 정보 가져오기
	BoardFileVO file = boardService.getFile(fileId);
	
	if  (file == null) {
		resp.sendError(HttpServletResponse.SC_NOT_FOUND, "첨부파일 정보를 찾을 수 없습니다.");
		return;
	}
	String filePath = file.getFilePath();
	String saveName = file.getSaveFileName();
	String originName = file.getOriginFileName();
	
	File downloadFile = new File(filePath, saveName);

	resp.setContentType("application/octet-stream");
	resp.setHeader("Content-Disposition",
			"attachment; filename=\"" + new String(originName.getBytes("UTF-8"), "ISO-8859-1") + "\"");

	FileInputStream fis = new FileInputStream(downloadFile);
	OutputStream os = resp.getOutputStream();

	byte[] buffer = new byte[1024];
	int len;

	while ((len = fis.read(buffer)) != -1) {
		os.write(buffer, 0, len);
	}

	os.flush();
	fis.close();
	os.close();
}
  
  
}
