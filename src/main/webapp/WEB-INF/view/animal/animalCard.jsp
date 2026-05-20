<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<div class="animal-card">
    <div class="img-box">
        <%-- 이미지 경로 및 파일명 출력 --%>
        <c:choose>
            <c:when test="${not empty animal.mainImage}">
                <%-- 
                    ANIMAL_IMAGE 테이블의 SAVE_FILE_NAME을 사용.
                    업로드 경로가 /upload/animal/ 이라면 실제 서버 업로드 경로에 맞게 수정하세요.
                --%>
                <img src="${pageContext.request.contextPath}/animal/image?fileName=${animal.mainImage}" 
                	 alt="${animal.animalName}"
                     onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/images/animals/default.jpg'"
                     class="img-box__img ${animal.adoptionStatus eq '입양완료' ? 'grayscale' : ''}"
                     >
            </c:when>
            <c:otherwise>
                <img src="${pageContext.request.contextPath}/images/animals/default.jpg"
                     alt="등록된 이미지 없음">
            </c:otherwise>
        </c:choose>
        
        <%-- 입양 상태 표시 --%>
        <div class="badge badge--${animal.adoptionStatus eq '입양가능' ? 'available' : animal.adoptionStatus eq '입양완료' ? 'done' : 'review'}">
            ${animal.adoptionStatus}
        </div>
        
        <%-- 하트 아이콘 --%>
    	<button type="button" 
    	class="btn-wish ${animal.isFavorite == 1 ? 'active' : ''}" onclick="toggleWish(this, '${animal.animalId}')">
	        <span class="material-symbols-outlined">
	        	${animal.isFavorite == 1 ? 'favorite' : 'favorite_border'}
	        </span>
	    </button>
    </div>
    
    <div class="info-box">
	    <div class="card-header">
	        <%-- 이름 --%>
	        <h3>${animal.animalName}</h3>
	        
	        <%-- 등록일 --%>
	        <span class="reg-date">
	        <fmt:formatDate value="${animal.createDate}" pattern="yy.MM.dd 등록"/>
	        </span>
        </div>
        
        <%-- 품종, 나이, 성별 --%>
        <div class="info-text">
            ${animal.breed} · ${animal.age}살 · ${animal.gender == 'M' ? '남아' : '여아'}
        </div>
        
        <%-- 태그 --%>
        <div class="tag-list">
        	<%-- 성격 --%>
            <c:forTokens var="tag" items="${animal.personality}" delims=",">
                <span class="tag-personality">#${tag}</span>
            </c:forTokens>
            
            <%-- 크기 --%>
            <c:choose>
                <c:when test="${animal.weight < 10}">
                    <span class="tag-size">#소형</span>
                </c:when>
                <c:when test="${animal.weight < 25}">
                    <span class="tag-size">#중형</span>
                </c:when>
                <c:otherwise>
                    <span class="tag-size">#대형</span>
                </c:otherwise>
            </c:choose>
        </div>
        
        <%-- 상세 페이지 이동 버튼 --%>
        
        <%-- 기존 줄에 추가(1줄)
        <a href="${pageContext.request.contextPath}/animal/animalDetail.do?animalId=${animal.animalId}${not empty param.fromGuide ? '&fromGuide='.concat(param.fromGuide) : ''}" 
        class="btn-detail">자세히 보기</a> --%>
        <%-- 동일 기능 코드 분할 --%>
        <c:set var="detailUrl" value="${pageContext.request.contextPath}/animal/animalDetail.do?animalId=${animal.animalId}" />
        <c:if test="${not empty param.fromGuide}">
            <c:set var="detailUrl" value="${detailUrl}&fromGuide=${param.fromGuide}" />
        </c:if>
        <a href="${detailUrl}" class="btn-detail">자세히 보기</a>
    </div>
</div>
