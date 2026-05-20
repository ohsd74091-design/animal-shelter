<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<c:set var="ctp" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>회원 상세 관리</title>

    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700;800&family=Manrope:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:wght@100..700" rel="stylesheet">

    <link rel="stylesheet" href="${ctp}/view/admin/css/common/admin-layout.css">
    <link rel="stylesheet" href="${ctp}/view/admin/css/member-detail.css">

    <script defer src="${ctp}/view/admin/js/admin-layout.js"></script>
    <script defer src="${ctp}/view/admin/js/member-detail.js"></script>
</head>
<body>
<div class="admin-layout">

    <jsp:include page="/view/admin/common/admin-sidebar.jsp" />

    <div class="admin-main-wrap">

        <jsp:include page="/view/admin/common/admin-header.jsp" />

        <main class="admin-content" data-ctp="${ctp}" data-memberid="${member.memberId}">
            <div class="member-detail-wrap">

                <section class="member-profile-card">
                    <div class="member-profile-cover"></div>

                    <div class="member-profile-content">
                        <div class="member-profile-image-box">
                            <c:choose>
                                <c:when test="${not empty member.profileImg}">
                                    <img
                                        src="${ctp}/member/image.do?fileName=${member.profileImg}"
                                        alt="회원 프로필 이미지"
                                        class="member-profile-image">
                                </c:when>
                                <c:otherwise>
                                    <img
                                        src="${ctp}/images/default-profile.png"
                                        alt="기본 프로필 이미지"
                                        class="member-profile-image">
                                </c:otherwise>
                            </c:choose>

                            <span class="member-status-dot ${member.status eq 'Y' ? 'is-active' : 'is-inactive'}"></span>
                        </div>

                        <div class="member-profile-info">
                            <div class="member-profile-name-row">
                                <h2 class="member-name">${member.nickname}</h2>

                                <span class="member-badge role-badge">
                                    <c:choose>
                                        <c:when test="${member.role eq 'ADMIN'}">관리자</c:when>
                                        <c:otherwise>일반 회원</c:otherwise>
                                    </c:choose>
                                </span>

                                <span class="member-badge status-badge ${member.status eq 'Y' ? 'active' : 'inactive'}">
                                    <c:choose>
                                        <c:when test="${member.status eq 'Y'}">활동 중</c:when>
                                        <c:otherwise>정지됨</c:otherwise>
                                    </c:choose>
                                </span>
                            </div>

                            <p class="member-sub-info">
                                @${member.memberName} · ${member.email}
                            </p>
                        </div>

                       <a href="${ctp}/admin/member/list.do" class="btn btn-secondary">
    					<span class="material-symbols-outlined">list</span>
   							 목록으로
						</a>
                    </div>
                </section>

                <div class="member-detail-grid">

                    <div class="member-detail-left">
                        <section class="card">
                            <h3 class="card-title">
                                <span class="material-symbols-outlined">info</span>
                                기본 정보
                            </h3>

                            <div class="info-grid">
                                <div class="info-item">
                                    <p class="info-label">회원 아이디</p>
                                    <p class="info-value">${member.memberId}</p>
                                </div>

                                <div class="info-item">
                                    <p class="info-label">이름</p>
                                    <p class="info-value">${member.memberName}</p>
                                </div>

                                <div class="info-item">
                                    <p class="info-label">닉네임</p>
                                    <p class="info-value">${member.nickname}</p>
                                </div>

                                <div class="info-item">
                                    <p class="info-label">이메일 주소</p>
                                    <p class="info-value">${member.email}</p>
                                </div>

                                <div class="info-item">
                                    <p class="info-label">전화번호</p>
                                    <p class="info-value">${member.phone}</p>
                                </div>

                                <div class="info-item">
                                    <p class="info-label">가입일</p>
                                    <p class="info-value">
                                        <c:choose>
                                            <c:when test="${not empty member.joinDate}">
                                                <fmt:formatDate value="${member.joinDate}" pattern="yyyy.MM.dd" />
                                            </c:when>
                                            <c:otherwise>-</c:otherwise>
                                        </c:choose>
                                    </p>
                                </div>
                            </div>
                        </section>

                        <section class="card">
                            <h3 class="card-title">
                                <span class="material-symbols-outlined">security</span>
                                계정 권한 및 상태
                            </h3>

                            <div class="status-grid">
                                <div class="status-box">
                                    <p class="status-box-label">현재 권한</p>
                                    <div class="status-box-row">
                                        <span class="status-box-value">
                                            <c:choose>
                                                <c:when test="${member.role eq 'ADMIN'}">Admin (관리자)</c:when>
                                                <c:otherwise>User (일반 회원)</c:otherwise>
                                            </c:choose>
                                        </span>
                                        <span class="material-symbols-outlined">shield_person</span>
                                    </div>
                                </div>

                                <div class="status-box">
                                    <p class="status-box-label">현재 상태</p>
                                    <div class="status-box-row">
                                        <span class="status-box-value ${member.status eq 'Y' ? 'text-active' : 'text-inactive'}">
                                            <c:choose>
                                                <c:when test="${member.status eq 'Y'}">Active (활동 중)</c:when>
                                                <c:otherwise>Blocked (정지됨)</c:otherwise>
                                            </c:choose>
                                        </span>
                                        <span class="material-symbols-outlined">
                                            <c:choose>
                                                <c:when test="${member.status eq 'Y'}">check_circle</c:when>
                                                <c:otherwise>block</c:otherwise>
                                            </c:choose>
                                        </span>
                                    </div>
                                </div>
                            </div>
                        </section>
                    </div>

                    <div class="member-detail-right">
                       <section class="card">
    <h3 class="card-title">
        <span class="material-symbols-outlined">manage_accounts</span>
        관리 작업
    </h3>

    <div class="action-list">

        <!-- 권한 변경 -->
        <form action="${ctp}/admin/member/roleUpdate.do" method="post" class="action-form">
            <input type="hidden" name="memberId" value="${member.memberId}">

            <c:choose>
                <c:when test="${member.role eq 'ADMIN'}">
                    <input type="hidden" name="role" value="USER">
                    <button type="submit" class="action-btn action-btn-primary">
                        <span class="action-btn-left">
                            <span class="material-symbols-outlined">badge</span>
                            <span>일반회원으로 변경</span>
                        </span>
                        <span class="material-symbols-outlined">chevron_right</span>
                    </button>
                </c:when>
                <c:otherwise>
                    <input type="hidden" name="role" value="ADMIN">
                    <button type="submit" class="action-btn action-btn-primary">
                        <span class="action-btn-left">
                            <span class="material-symbols-outlined">badge</span>
                            <span>관리자로 변경</span>
                        </span>
                        <span class="material-symbols-outlined">chevron_right</span>
                    </button>
                </c:otherwise>
            </c:choose>
        </form>

        <!-- 상태 변경 -->
        <form action="${ctp}/admin/member/statusUpdate.do" method="post" class="action-form" id="statusForm">
            <input type="hidden" name="memberId" value="${member.memberId}">

            <c:choose>
                <c:when test="${member.status eq 'Y'}">
                    <input type="hidden" name="status" value="N">
                    <button type="submit" class="action-btn action-btn-warn" id="btnToggleStatus">
                        <span class="action-btn-left">
                            <span class="material-symbols-outlined">block</span>
                            <span>회원 정지</span>
                        </span>
                        <span class="material-symbols-outlined">chevron_right</span>
                    </button>
                </c:when>
                <c:otherwise>
                    <input type="hidden" name="status" value="Y">
                    <button type="submit" class="action-btn action-btn-warn" id="btnToggleStatus">
                        <span class="action-btn-left">
                            <span class="material-symbols-outlined">check_circle</span>
                            <span>정지 해제</span>
                        </span>
                        <span class="material-symbols-outlined">chevron_right</span>
                    </button>
                </c:otherwise>
            </c:choose>
        </form>

        <div class="divider"></div>

        <!-- 회원 삭제 -->
        <form action="${ctp}/admin/member/delete.do" method="post" class="action-form" id="deleteForm">
            <input type="hidden" name="memberId" value="${member.memberId}">
            <button type="submit" class="action-btn action-btn-danger" id="btnDeleteMember">
                <span class="action-btn-left">
                    <span class="material-symbols-outlined">delete_forever</span>
                    <span>회원 삭제</span>
                </span>
                <span class="material-symbols-outlined">chevron_right</span>
            </button>
        </form>
    </div>

    <p class="action-desc">
        삭제 작업은 신중하게 진행하세요. 실제 삭제 대신 상태 변경 방식이 더 안전할 수 있습니다.
    </p>
</section>
                        </section>

                        <section class="card">
                            <h3 class="stats-title">안내</h3>

                            <div class="stats-list">
                                <div class="stats-item">
                                    <span>회원 ID</span>
                                    <strong>${member.memberId}</strong>
                                </div>
                                <div class="stats-item">
                                    <span>현재 권한</span>
                                    <strong>${member.role}</strong>
                                </div>
                                <div class="stats-item">
                                    <span>현재 상태</span>
                                    <strong class="${member.status eq 'Y' ? 'text-active' : 'text-danger'}">
                                        <c:choose>
                                            <c:when test="${member.status eq 'Y'}">활동 중</c:when>
                                            <c:otherwise>정지됨</c:otherwise>
                                        </c:choose>
                                    </strong>
                                </div>
                            </div>
                        </section>
                    </div>

                </div>
            </div>
        </main>
    </div>
</div>
</body>
</html>