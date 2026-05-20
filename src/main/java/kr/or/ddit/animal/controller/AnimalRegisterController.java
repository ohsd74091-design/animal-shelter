package kr.or.ddit.animal.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
import kr.or.ddit.animal.service.AnimalServiceImpl;
import kr.or.ddit.animal.service.IAnimalService;
import kr.or.ddit.animal.vo.AnimalImageVO;
import kr.or.ddit.animal.vo.AnimalMedicalVO;
import kr.or.ddit.animal.vo.AnimalRescueVO;
import kr.or.ddit.animal.vo.AnimalVO;

@WebServlet("/admin/animal/register.do")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2,   // 2MB
    maxFileSize       = 1024 * 1024 * 10,   // 10MB
    maxRequestSize    = 1024 * 1024 * 50    // 50MB
)
public class AnimalRegisterController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private IAnimalService animalService = AnimalServiceImpl.getInstance();

    // ✅ 내 서버 저장 경로
    private static final String UPLOAD_PATH = "D:/upload/animal";
    // ✅ SVN 공용 폴더 경로 (조원 공유용)
    private static final String SHARED_PATH =
    	    "D:\\A_TeachingMaterial\\01_Java\\workspace\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp0\\wtpwebapps\\Animal_Shelter\\images\\animals";
;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
    	System.out.println("실제 배포경로: " + 
    	        getServletContext().getRealPath("/images/animals"));
    	
        req.getRequestDispatcher("/view/admin/animalRegister.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        JsonObject json = new JsonObject();

        try {
            request.setCharacterEncoding("UTF-8");
            System.out.println("=== 동물 등록 시작 ===");

            // ── 1. ANIMAL 기본 정보 ────────────────────────────────
            AnimalVO animal = new AnimalVO();

            String animalName = request.getParameter("animalName");
            System.out.println("animalName: " + animalName);
            animal.setAnimalName(animalName);

            String animalType = request.getParameter("animalType");
            System.out.println("animalType: " + animalType);
            animal.setAnimalType(animalType);

            String breed = request.getParameter("breed");
            animal.setBreed(breed != null && !breed.isEmpty() ? breed : null);

            String gender = request.getParameter("gender");
            animal.setGender(gender);

            String ageStr = request.getParameter("age");
            animal.setAge(ageStr != null && !ageStr.isEmpty() ? Integer.parseInt(ageStr) : 0);

            String weightStr = request.getParameter("weight");
            animal.setWeight(weightStr != null && !weightStr.isEmpty() ? Double.parseDouble(weightStr) : 0);

            String adoptionStatus = request.getParameter("adoptionStatus");
            animal.setAdoptionStatus(adoptionStatus != null ? adoptionStatus : "입양가능");

            String personality = request.getParameter("personality");
            animal.setPersonality(personality != null && !personality.isEmpty() ? personality : null);

            // ── 2. ANIMAL_MEDICAL 정보 ────────────────────────────
            AnimalMedicalVO medical = new AnimalMedicalVO();
            medical.setVaccination("Y".equals(request.getParameter("vaccination")) ? "Y" : "N");
            medical.setNeutered("Y".equals(request.getParameter("neutered")) ? "Y" : "N");
            medical.setMicrochip("Y".equals(request.getParameter("microchip")) ? "Y" : "N");

            String specialNote = request.getParameter("specialNote");
            medical.setSpecialNote(specialNote != null && !specialNote.isEmpty() ? specialNote : null);

            // ── 3. ANIMAL_RESCUE 정보 ─────────────────────────────
            AnimalRescueVO rescue = new AnimalRescueVO();

            String rescueDateStr = request.getParameter("rescueDate");
            if (rescueDateStr != null && !rescueDateStr.isEmpty()) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date utilDate = sdf.parse(rescueDateStr);
                rescue.setRescueDate(new java.sql.Date(utilDate.getTime()));
            }

            String story = request.getParameter("story");
            rescue.setStory(story != null && !story.isEmpty() ? story : null);

            // ── 4. 이미지 파일 업로드 처리 ───────────────────────
            List<AnimalImageVO> imageList = new ArrayList<>();

            // 저장 디렉토리 생성
            new File(UPLOAD_PATH).mkdirs();
            new File(SHARED_PATH).mkdirs();

            // 메인 이미지
            try {
                Part mainImagePart = request.getPart("mainImage");
                if (mainImagePart != null && mainImagePart.getSize() > 0) {
                    System.out.println("메인 이미지 크기: " + mainImagePart.getSize());
                    AnimalImageVO mainImage = saveImageFile(mainImagePart);
                    mainImage.setIsMain("Y");
                    imageList.add(mainImage);
                }
            } catch (Exception e) {
                System.out.println("메인 이미지 처리 오류: " + e.getMessage());
            }

            // 추가 이미지
            try {
                for (Part part : request.getParts()) {
                    if ("additionalImages".equals(part.getName()) && part.getSize() > 0) {
                        System.out.println("추가 이미지 크기: " + part.getSize()); 
                        AnimalImageVO additionalImage = saveImageFile(part);
                        additionalImage.setIsMain("N");
                        imageList.add(additionalImage);
                    }
                }
            } catch (Exception e) {
                System.out.println("추가 이미지 처리 오류: " + e.getMessage());
            }

            System.out.println("총 이미지 개수: " + imageList.size());

            // ── 5. Service 호출 ───────────────────────────────────
            boolean success = animalService.registerAnimal(animal, medical, rescue, imageList);
            System.out.println("등록 결과: " + success);

            if (success) {
                json.addProperty("success", true);
                json.addProperty("message", "동물 등록이 완료되었습니다.");
                json.addProperty("animalId", animal.getAnimalId());
            } else {
                json.addProperty("success", false);
                json.addProperty("message", "등록에 실패했습니다.");
            }

        } catch (NumberFormatException e) {
            e.printStackTrace();
            json.addProperty("success", false);
            json.addProperty("message", "숫자 형식이 올바르지 않습니다: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            json.addProperty("success", false);
            json.addProperty("message", "오류 발생: " + e.getMessage());
        }

        System.out.println("응답 JSON: " + json.toString());
        out.print(json.toString());
        out.flush();
    }

    /**
     * 이미지 파일을 UPLOAD_PATH와 SHARED_PATH 두 군데 저장
     */
    private AnimalImageVO saveImageFile(Part filePart) throws IOException {
        String originalFileName = getFileName(filePart);
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String saveFileName = UUID.randomUUID().toString() + extension;

        // ✅ 1. 내 서버 경로에 저장
        File uploadFile = new File(UPLOAD_PATH, saveFileName);
        filePart.write(uploadFile.getAbsolutePath());
        System.out.println("원본 파일명: " + originalFileName);
        System.out.println("파일 저장(서버): " + saveFileName);

        // ✅ 2. SVN 공용 폴더에 복사
        File sharedFile = new File(SHARED_PATH, saveFileName);
        try (FileInputStream fis = new FileInputStream(uploadFile);
             FileOutputStream fos = new FileOutputStream(sharedFile)) {
            byte[] buffer = new byte[8192];
            int len;
            while ((len = fis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
            System.out.println("파일 저장(공용): " + sharedFile.getAbsolutePath());
        } catch (Exception e) {
            // 공용 폴더 저장 실패는 등록 전체를 막지 않음
            System.out.println("공용 폴더 저장 실패 (무시): " + e.getMessage());
        }

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
