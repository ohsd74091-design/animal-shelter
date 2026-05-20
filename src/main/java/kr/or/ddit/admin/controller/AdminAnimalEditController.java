package kr.or.ddit.admin.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.google.gson.JsonObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import kr.or.ddit.admin.service.AdminServiceImpl;
import kr.or.ddit.admin.service.IadminService;
import kr.or.ddit.animal.dto.AnimalDetailDto;
import kr.or.ddit.animal.vo.AnimalImageVO;
import kr.or.ddit.animal.vo.AnimalMedicalVO;
import kr.or.ddit.animal.vo.AnimalRescueVO;
import kr.or.ddit.animal.vo.AnimalVO;

@WebServlet("/admin/animal/edit.do")
@MultipartConfig(
	    fileSizeThreshold = 1024 * 1024 * 2,   // 2MB
	    maxFileSize       = 1024 * 1024 * 10,   // 10MB
	    maxRequestSize    = 1024 * 1024 * 50    // 50MB
	)
public class AdminAnimalEditController extends HttpServlet{

	
	//등록컨트롤러와 동일한 저장경로 
	private static final String UPLOAD_PATH = "D:/upload/animal";
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		
		//동물ID 파라미터 수집 
		String animalIdStr =req.getParameter("animalId");
		if(animalIdStr == null) {
			resp.sendRedirect(req.getContextPath() + "/admin/animal/list.do");
            return;
		}
		
		int animalId =Integer.parseInt(animalIdStr);
		IadminService service =AdminServiceImpl.getinsetance();
		
		 // 기존 AnimalServiceImpl.getAnimalDetail() 재사용
        // AnimalDetailDto에 animal, medical, rescue, imageList 모두 담겨있음
		
		AnimalDetailDto dto = service.getAnimalDetailForEdit(animalId);
		
		// JSP로 전달 → 폼에 기존 데이터 채워넣기용
        req.setAttribute("dto", dto);
        req.getRequestDispatcher("/view/admin/animalEdit.jsp")
           .forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json; charset=UTF-8");

        PrintWriter out = resp.getWriter();
        JsonObject json = new JsonObject();
        IadminService service = AdminServiceImpl.getinsetance();

        try {
            // ── 1. animalId 수집 ──────────────────────────────
            int animalId = Integer.parseInt(req.getParameter("animalId"));

            // ── 2. ANIMAL 기본정보 수집 ───────────────────────
            AnimalVO animal = new AnimalVO();
            animal.setAnimalId(animalId); // ✅ 수정이므로 animalId 필수
            animal.setAnimalName(req.getParameter("animalName"));
            animal.setAnimalType(req.getParameter("animalType"));

            String breed = req.getParameter("breed");
            animal.setBreed(breed != null && !breed.isEmpty() ? breed : null);

            animal.setGender(req.getParameter("gender"));

            String ageStr = req.getParameter("age");
            animal.setAge(ageStr != null && !ageStr.isEmpty() ? Integer.parseInt(ageStr) : 0);

            String weightStr = req.getParameter("weight");
            animal.setWeight(weightStr != null && !weightStr.isEmpty() ? Double.parseDouble(weightStr) : 0);

            animal.setAdoptionStatus(req.getParameter("adoptionStatus"));

            String personality = req.getParameter("personality");
            animal.setPersonality(personality != null && !personality.isEmpty() ? personality : null);

            // ── 3. ANIMAL_MEDICAL 의료정보 수집 ──────────────
            AnimalMedicalVO medical = new AnimalMedicalVO();
            medical.setAnimalId(animalId);
            medical.setVaccination("Y".equals(req.getParameter("vaccination")) ? "Y" : "N");
            medical.setNeutered("Y".equals(req.getParameter("neutered")) ? "Y" : "N");
            medical.setMicrochip("Y".equals(req.getParameter("microchip")) ? "Y" : "N");

            String specialNote = req.getParameter("specialNote");
            medical.setSpecialNote(specialNote != null && !specialNote.isEmpty() ? specialNote : null);

            // ── 4. ANIMAL_RESCUE 구조정보 수집 ───────────────
            AnimalRescueVO rescue = new AnimalRescueVO();
            rescue.setAnimalId(animalId);

            String rescueDateStr = req.getParameter("rescueDate");
            if (rescueDateStr != null && !rescueDateStr.isEmpty()) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date utilDate = sdf.parse(rescueDateStr);
                rescue.setRescueDate(new java.sql.Date(utilDate.getTime()));
            }

            String story = req.getParameter("story");
            rescue.setStory(story != null && !story.isEmpty() ? story : null);

            // ── 5. 이미지 처리 ────────────────────────────────
            List<AnimalImageVO> imageList = new ArrayList<>();
            boolean hasNewMainImage = false; // 새 메인 이미지가 있는지 여부

            new File(UPLOAD_PATH).mkdirs();

            // 새 메인 이미지가 있는지 확인
            Part mainImagePart = req.getPart("mainImage");
            if (mainImagePart != null && mainImagePart.getSize() > 0) {
                hasNewMainImage = true;
                AnimalImageVO mainImage = saveImageFile(mainImagePart);
                mainImage.setIsMain("Y");
                imageList.add(mainImage);
                System.out.println("새 메인 이미지: " + mainImage.getSaveFileName());
            }

            // 새 추가 이미지 처리
            for (Part part : req.getParts()) {
                if ("additionalImages".equals(part.getName()) && part.getSize() > 0) {
                    AnimalImageVO additionalImage = saveImageFile(part);
                    additionalImage.setIsMain("N");
                    imageList.add(additionalImage);
                    System.out.println("새 추가 이미지: " + additionalImage.getSaveFileName());
                }
            }

            // ── 6. Service 호출 ───────────────────────────────
            boolean success = service.updateAnimal(
                animal, medical, rescue, imageList, hasNewMainImage
            );

            if (success) {
                json.addProperty("success", true);
                json.addProperty("message", "수정이 완료되었습니다.");
            } else {
                json.addProperty("success", false);
                json.addProperty("message", "수정에 실패했습니다.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            json.addProperty("success", false);
            json.addProperty("message", "오류 발생: " + e.getMessage());
        }

        out.print(json.toString());
        out.flush();
    }

    /**
     * 이미지 파일 저장 (등록 컨트롤러와 동일한 로직)
     */
    private AnimalImageVO saveImageFile(Part filePart) throws IOException {
        String originalFileName = getFileName(filePart);
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String saveFileName = UUID.randomUUID().toString() + extension;

        File uploadFile = new File(UPLOAD_PATH, saveFileName);
        filePart.write(uploadFile.getAbsolutePath());

        AnimalImageVO image = new AnimalImageVO();
        image.setOriginFileName(originalFileName);
        image.setSaveFileName(saveFileName);
        return image;
    }

    /**
     * Part에서 파일명 추출
     */
    private String getFileName(Part part) {
        String contentDisposition = part.getHeader("content-disposition");
        for (String token : contentDisposition.split(";")) {
            if (token.trim().startsWith("filename")) {
                return token.substring(token.indexOf("=") + 2, token.length() - 1);
            }
        }
        return "";
	}
	
}
