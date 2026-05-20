package kr.or.ddit.mypage.vo;

import java.sql.Date;

/**
 * VolunteerHistoryVO.java
 * 봉사 신청 내역 페이지 전용 VO
 *
 * - VOLUNTEER_APPLY + VOLUNTEER_RECRUIT 조인
 * - 화면 표시용 가공 컬럼 포함
 */
public class VolunteerHistoryVO {

    // ── VOLUNTEER_APPLY 컬럼 ──────────────────────────
    private int    volunteerId;
    private String memberId;
    private int    recruitId;
    private String interestType;
    private String status;
    private String applyReason;
    private Date   applyDate;
    private Date   volunteerDate;
    private String rejectReason;   // VOLUNTEER_APPLY.REJECT_REASON

    // ── VOLUNTEER_RECRUIT 조인 ────────────────────────
    private String title;
    private String location;

    // ── 화면 표시용 가공 컬럼 ─────────────────────────
    /** 신청일 표시용 (YYYY.MM.DD) */
    private String applyDateStr;

    // ── Getter / Setter ───────────────────────────────
    public int    getVolunteerId()              { return volunteerId; }
    public void   setVolunteerId(int v)         { this.volunteerId = v; }

    public String getMemberId()                 { return memberId; }
    public void   setMemberId(String v)         { this.memberId = v; }

    public int    getRecruitId()                { return recruitId; }
    public void   setRecruitId(int v)           { this.recruitId = v; }

    public String getInterestType()             { return interestType; }
    public void   setInterestType(String v)     { this.interestType = v; }

    public String getStatus()                   { return status; }
    public void   setStatus(String v)           { this.status = v; }

    public String getApplyReason()              { return applyReason; }
    public void   setApplyReason(String v)      { this.applyReason = v; }

    public Date   getApplyDate()                { return applyDate; }
    public void   setApplyDate(Date v)          { this.applyDate = v; }

    public Date   getVolunteerDate()            { return volunteerDate; }
    public void   setVolunteerDate(Date v)      { this.volunteerDate = v; }

    public String getRejectReason()             { return rejectReason; }
    public void   setRejectReason(String v)     { this.rejectReason = v; }

    public String getTitle()                    { return title; }
    public void   setTitle(String v)            { this.title = v; }

    public String getLocation()                 { return location; }
    public void   setLocation(String v)         { this.location = v; }

    public String getApplyDateStr()             { return applyDateStr; }
    public void   setApplyDateStr(String v)     { this.applyDateStr = v; }
}
