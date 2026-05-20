package kr.or.ddit.donation.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.LineSeparator;

import kr.or.ddit.donation.service.DonationServiceImpl;
import kr.or.ddit.donation.service.IDonationService;
import kr.or.ddit.donation.vo.AESUtil;
import kr.or.ddit.donation.vo.DonationVO;
import kr.or.ddit.member.vo.MemberVO;

@WebServlet("/donation/receipt.do")
public class DonationReceiptDownload extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // ── 1. 인증 확인 ──
        HttpSession session = req.getSession();
        MemberVO loginUser = (MemberVO) session.getAttribute("loginUser");
        if (loginUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login.do");
            return;
        }

        // ── 2. 파라미터 ──
        int donationId;
        try {
            donationId = Integer.parseInt(req.getParameter("donationId"));
        } catch (NumberFormatException e) {
            resp.sendError(400, "잘못된 요청입니다.");
            return;
        }

        // ── 3. DB 조회 (본인 영수증만) ──
        IDonationService service = DonationServiceImpl.getInstance();
        DonationVO dvo = service.selectDonationForReceipt(donationId, loginUser.getMemberId());

        if (dvo == null) {
            resp.sendError(403, "영수증 정보가 없거나 접근 권한이 없습니다.");
            return;
        }

        // ── 4. PDF 생성 ──
        resp.setContentType("application/pdf");
        resp.setHeader("Content-Disposition",
            "attachment; filename=\"donation_receipt_" + donationId + ".pdf\"");

        try {
            // ── 한글 폰트 로드 ──
            BaseFont bf;
            try {
                bf = BaseFont.createFont(
                    getServletContext().getRealPath("/fonts/MALGUN.TTF"),
                    BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            } catch (Exception ex) {
                bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, false);
            }

            // ── 폰트 정의 ──
            Font titleFont  = new Font(bf, 22, Font.BOLD,   new BaseColor(30, 30, 30));
            Font subFont    = new Font(bf,  9, Font.NORMAL, new BaseColor(130, 130, 130));
            Font headerFont = new Font(bf, 10, Font.BOLD,   new BaseColor(100, 100, 100));
            Font valueFont  = new Font(bf, 11, Font.NORMAL, new BaseColor(30,  30,  30));
            Font noticeFont = new Font(bf,  9, Font.NORMAL, new BaseColor(140, 140, 140));
            Font orgBold    = new Font(bf, 10, Font.BOLD,   new BaseColor(50,  50,  50));
            Font orgNormal  = new Font(bf, 10, Font.NORMAL, new BaseColor(80,  80,  80));
            Font sealFont   = new Font(bf, 11, Font.BOLD,   new BaseColor(185, 28,  28));
            Font sealSmall  = new Font(bf,  8, Font.NORMAL, new BaseColor(185, 28,  28));

            // ── 문서 생성 ──
            Document doc = new Document(PageSize.A4, 60, 60, 80, 60);
            PdfWriter writer = PdfWriter.getInstance(doc, resp.getOutputStream());
            doc.open();

            // ━━━━━━━ 제목 영역 ━━━━━━━
            Paragraph title = new Paragraph("기 부 금  영 수 증", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(4);
            doc.add(title);

            Paragraph subtitle = new Paragraph(
                "소득세법 제34조 및 법인세법 제24조에 의한 기부금 영수증", subFont);
            subtitle.setAlignment(Element.ALIGN_CENTER);
            subtitle.setSpacingAfter(18);
            doc.add(subtitle);

            // ── 주황색 구분선 ──
            LineSeparator orangeLine = new LineSeparator();
            orangeLine.setLineColor(new BaseColor(243, 115, 33));
            orangeLine.setLineWidth(1.5f);
            doc.add(new Chunk(orangeLine));
            doc.add(Chunk.NEWLINE);

            // ━━━━━━━ 기부 내역 테이블 ━━━━━━━
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.setSpacingBefore(14);
            table.setSpacingAfter(20);
            table.setWidths(new float[]{35f, 65f});

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일");
            String dateStr = dvo.getCreateDate() != null
                           ? sdf.format(dvo.getCreateDate()) : "—";

            // 영수증 번호
            String rcptNo = "AMB-"
                + new SimpleDateFormat("yyyyMMdd").format(new Date())
                + "-" + String.format("%04d", donationId);

            addRow(table, "영 수 증  번 호", rcptNo,            bf, true);
            addRow(table, "기 부 자  성 명", dvo.getDonatorName(), bf, false);

            // 주민번호 복호화
            String regDisplay = "—";
            try {
                regDisplay = AESUtil.decrypt(dvo.getRegNo1()) + " - *******";
            } catch (Exception ex) { /* 복호화 실패 시 마스킹 유지 */ }
            addRow(table, "주민등록번호",   regDisplay,           bf, false);
            addRow(table, "기 부  금 액",
                String.format("₩ %,d 원", dvo.getDonationAmount()), bf, true);
            addRow(table, "기 부  유 형",   dvo.getDonationType(), bf, false);
            addRow(table, "결 제  수 단",
                "카드".equals(dvo.getDonationMethod()) ? "신용카드" : dvo.getDonationMethod(),
                bf, false);
            addRow(table, "기  부  일  자", dateStr,              bf, false);
            addRow(table, "수  령  단  체", "너와 나의 연결고리 동물보호소", bf, false);
            addRow(table, "사업자등록번호", "123-45-67890",        bf, false);

            doc.add(table);

            // ━━━━━━━ 안내 문구 ━━━━━━━
            Paragraph notice = new Paragraph(
                "위 금액을 기부금으로 영수하였음을 확인합니다.\n" +
                "본 영수증은 연말정산 기부금 소득공제에 사용하실 수 있습니다.",
                noticeFont);
            notice.setAlignment(Element.ALIGN_CENTER);
            notice.setSpacingBefore(10);
            notice.setSpacingAfter(30);
            doc.add(notice);

            // ── 점선 구분선 ──
            LineSeparator dottedLine = new LineSeparator();
            dottedLine.setLineColor(new BaseColor(200, 200, 200));
            dottedLine.setLineWidth(0.5f);
            doc.add(new Chunk(dottedLine));

            // ━━━━━━━ 발급기관 + 직인 영역 ━━━━━━━
            PdfPTable signTable = new PdfPTable(2);
            signTable.setWidthPercentage(100);
            signTable.setSpacingBefore(20);
            signTable.setWidths(new float[]{62f, 38f});
            signTable.getDefaultCell().setBorder(PdfPCell.NO_BORDER);

            // ── 왼쪽: 발급기관 정보 ──
            String issueDateStr = sdf.format(new Date());

            Paragraph orgInfo = new Paragraph();
            orgInfo.setLeading(20f); // 줄 간격
            orgInfo.add(new Chunk("발 급 기 관  :  ", orgBold));
            orgInfo.add(new Chunk("너와 나의 연결고리 동물보호소\n", orgNormal));
            orgInfo.add(new Chunk("사업자번호  :  ", orgBold));
            orgInfo.add(new Chunk("123-45-67890\n", orgNormal));
            orgInfo.add(new Chunk("대  표  자  :  ", orgBold));
            orgInfo.add(new Chunk("관리자\n", orgNormal));
            orgInfo.add(new Chunk("발 급 일 자  :  ", orgBold));
            orgInfo.add(new Chunk(issueDateStr, orgNormal));

            PdfPCell orgCell = new PdfPCell(orgInfo);
            orgCell.setBorder(PdfPCell.NO_BORDER);
            orgCell.setPaddingTop(8);
            orgCell.setPaddingLeft(4);
            signTable.addCell(orgCell);

            // ── 오른쪽: 직인 (PdfTemplate으로 원 + 텍스트 그리기) ──
            PdfContentByte cb = writer.getDirectContent();

            // 직인 크기 및 위치는 테이블 렌더링 후 절대 좌표로 삽입
            // → PdfTemplate 으로 미리 그려두고 Image로 셀에 삽입
            PdfTemplate sealTemplate = cb.createTemplate(80, 80);

            // 외곽 원 (빨간색)
            sealTemplate.setColorStroke(new BaseColor(185, 28, 28));
            sealTemplate.setColorFill(new BaseColor(255, 255, 255));
            sealTemplate.setLineWidth(2f);
            sealTemplate.circle(40, 40, 38); // x중심, y중심, 반지름
            sealTemplate.fillStroke();

            // 내부 원 (얇은 선)
            sealTemplate.setColorStroke(new BaseColor(185, 28, 28));
            sealTemplate.setLineWidth(0.8f);
            sealTemplate.circle(40, 40, 30);
            sealTemplate.stroke();

            // 직인 텍스트: 상단 "동물보호소"
            sealTemplate.beginText();
            sealTemplate.setFontAndSize(bf, 8);
            sealTemplate.setColorFill(new BaseColor(185, 28, 28));
            sealTemplate.showTextAligned(
                Element.ALIGN_CENTER, "너와나의연결고리", 40, 54, 0);
            // 중앙 "직 인"
            sealTemplate.setFontAndSize(bf, 13);
            sealTemplate.showTextAligned(
                Element.ALIGN_CENTER, "직  인", 40, 36, 0);
            // 하단 "동물보호소"
            sealTemplate.setFontAndSize(bf, 8);
            sealTemplate.showTextAligned(
                Element.ALIGN_CENTER, "동물보호소", 40, 22, 0);
            sealTemplate.endText();

            // PdfTemplate → Image 변환하여 셀에 삽입
            Image sealImage = Image.getInstance(sealTemplate);
            sealImage.scaleToFit(80, 80);

            PdfPCell sealCell = new PdfPCell(sealImage);
            sealCell.setBorder(PdfPCell.NO_BORDER);
            sealCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            sealCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            sealCell.setPaddingTop(4);
            sealCell.setPaddingRight(10);
            signTable.addCell(sealCell);

            doc.add(signTable);

            // ━━━━━━━ 최하단 연락처 ━━━━━━━
            doc.add(Chunk.NEWLINE);
            LineSeparator bottomLine = new LineSeparator();
            bottomLine.setLineColor(new BaseColor(243, 115, 33));
            bottomLine.setLineWidth(0.8f);
            doc.add(new Chunk(bottomLine));

            Paragraph footer = new Paragraph(
                "\n너와 나의 연결고리 동물보호소  |  Tel: 02-1234-5678  |  " +
                "Email: contact@amberhearth.org",
                new Font(bf, 8, Font.NORMAL, new BaseColor(160, 160, 160)));
            footer.setAlignment(Element.ALIGN_CENTER);
            doc.add(footer);

            doc.close();

        } catch (Exception ex) {
            ex.printStackTrace();
            resp.sendError(500, "PDF 생성 중 오류가 발생했습니다.");
        }
    }

    /**
     * 테이블 행 추가 헬퍼
     * @param highlight true면 배경색을 진하게 (금액, 번호 등 강조)
     */
    private void addRow(PdfPTable table, String label, String value,
                        BaseFont bf, boolean highlight) {

        Font labelFont = new Font(bf, 10, Font.BOLD,
            new BaseColor(100, 100, 100));
        Font valueFont = highlight
            ? new Font(bf, 11, Font.BOLD,   new BaseColor(243, 115, 33))
            : new Font(bf, 11, Font.NORMAL, new BaseColor(30,  30,  30));

        // 라벨 셀
        PdfPCell labelCell = new PdfPCell(new Phrase(label, labelFont));
        labelCell.setBackgroundColor(new BaseColor(255, 244, 229));
        labelCell.setPadding(10);
        labelCell.setBorderColor(new BaseColor(235, 235, 235));
        labelCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

        // 값 셀
        PdfPCell valueCell = new PdfPCell(
            new Phrase(value != null ? value : "—", valueFont));
        valueCell.setPadding(10);
        valueCell.setBorderColor(new BaseColor(235, 235, 235));
        if (highlight) {
            valueCell.setBackgroundColor(new BaseColor(255, 250, 245));
        }
        valueCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

        table.addCell(labelCell);
        table.addCell(valueCell);
    }
}