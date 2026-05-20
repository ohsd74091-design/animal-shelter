package kr.or.ddit.mypage.vo;

import java.util.Date;

import lombok.Data;

/**
 * AdoptionHistoryVO.java
 * 입양 신청 내역 페이지 전용 VO
 *
 * - ADOPTION 테이블 기본 컬럼
 * - ANIMAL, ANIMAL_IMAGE 조인 컬럼
 * - 화면 표시용 가공 컬럼 (statusClass, statusLabel, adoptionNo)
 *
 * ※ REJECT_REASON 컬럼은 ADOPTION 테이블에 추가 예정
 */
@Data
public class AdoptionHistoryVO
{

	// ── ADOPTION 테이블 ──────────────────────────────
    private int    adoptionId;
    private String memberId;
    private int    animalId;
    private Date   visitDate;
    private Date   applyDate;
    private String status;
    private String rejectReason;   // 추가 예정 컬럼
 
    // ── 화면 표시용 가공 컬럼 ─────────────────────────
    private String adoptionNo; //신청번호 (예: AD-20240315-01)
 
    // ── ANIMAL 조인 ───────────────────────────────────
    private String animalName;
    private String breed;
    private String gender;
    private int    age;
 
    // ── ANIMAL_IMAGE 조인 ─────────────────────────────
    private String mainImage;
}
