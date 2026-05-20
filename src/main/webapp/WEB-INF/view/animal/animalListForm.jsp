<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>너와 나의 연결고리 - 메인</title>

<%-- header/footer 참조 --%>
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined" rel="stylesheet">
<link href="https://cdn.jsdelivr.net/gh/orioncactus/pretendard/dist/web/static/pretendard.css" rel="stylesheet">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common/common.css">

<%-- animalListForm 참조 --%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/animalList.css">
<script src="${pageContext.request.contextPath}/js/animalList.js" defer></script>

</head>
<body>
	<%-- header --%>
	<jsp:include page="/view/common/header.jsp" />
	<%-- 검색 결과(필터링)에 따른 페이지 출력 --%>
	<div class="animal-container">
		<div class="header-section">
			<h2>당신의 가족이 될 소중한 친구들</h2>
			<p>당신의 소중한 인연, 여기서 시작될 수 있어요.</p>
		</div>
		<form id="filterForm" action="${pageContext.request.contextPath}/animal/animalList.do" method="get">
			<input type="hidden" name="page" id="pageNumber" value="${currentPage}">
		
			<%-- 필터 --%>
	        <div class="filter-container">
	        	<%-- 개/고양이 필터 --%>
	            <div class="type-filters">
		            <%-- button 은 클릭요소일 뿐 값을 서버에 전송하지 못함 --%>
		            <%-- 그래서 filterType() 함수로 value 를 변경하여 submit --%>
	                <input type="hidden" name="animalType" id="animalType" value="${param.animalType}">
	                
	                <button type="button" class="btn-filter ${empty param.animalType ? 'active' : ''}" 
	                onclick="filterType('')">전체</button>
	                <button type="button" class="btn-filter ${param.animalType == 'DOG' ? 'active' : ''}" 
	                onclick="filterType('DOG')">강아지</button>
	                <button type="button" class="btn-filter ${param.animalType == 'CAT' ? 'active' : ''}" 
	                onclick="filterType('CAT')">고양이</button>
	            </div>
	
				<%-- 성격/크기/성별 필터 --%>
				<div class="detailed-filter-bar">
					<%-- 성격 --%>
		            <div class="filter-group dropdown">
					    <button type="button" class="btn-filter btn-dropdown" onclick="toggleDropdown('tagBox', event)">성격 <span class="material-symbols-outlined">expand_more</span></button>
					
					    <div id="tagBox" class="dropdown-menu hidden">
					    	<%-- 선택된 성격 리스트를 콤마(,)로 합친 문자열 생성 --%>
    						<c:set var="selectedPersonalities" value="${fn:join(paramValues.personalityList, ',')}" />
    						
							<label><input type="checkbox" name="personalityList" 
			                value="온순함" ${fn:contains(selectedPersonalities, '온순함') ? 'checked' : ''}>온순함</label>
			                <label><input type="checkbox" name="personalityList" 
			                value="활발함" ${fn:contains(selectedPersonalities, '활발함') ? 'checked' : ''}>활발함</label>
			                <label><input type="checkbox" name="personalityList" 
			                value="똑똑함" ${fn:contains(selectedPersonalities, '똑똑함') ? 'checked' : ''}>똑똑함</label>
					    </div>
					</div>
		            
		            <%-- 크기 --%>
		            <div class="filter-group dropdown">
					    <button type="button" class="btn-filter btn-dropdown" onclick="toggleDropdown('sizeBox', event)">크기 <span class="material-symbols-outlined">expand_more</span></button>
					
					    <div id="sizeBox" class="dropdown-menu hidden">
					    	<%-- 선택된 크기 리스트를 콤마(,)로 합친 문자열 생성 --%>
    						<c:set var="selectedSizes" value="${fn:join(paramValues.sizeList, ',')}" />
    						
							<label><input type="checkbox" name="sizeList" 
			                value="SMALL" ${fn:contains(selectedSizes, 'SMALL') ? 'checked' : ''}>소형</label>
			                <label><input type="checkbox" name="sizeList" 
			                value="MEDIUM" ${fn:contains(selectedSizes, 'MEDIUM') ? 'checked' : ''}>중형</label>
			                <label><input type="checkbox" name="sizeList" 
			                value="LARGE" ${fn:contains(selectedSizes, 'LARGE') ? 'checked' : ''}>대형</label>
					    </div>
					</div>
		            
		            <%-- 성별 --%>
		            <div class="filter-group dropdown">
					    <button type="button" class="btn-filter btn-dropdown" onclick="toggleDropdown('genderBox', event)">성별 <span class="material-symbols-outlined">expand_more</span></button>
					
					    <div id="genderBox" class="dropdown-menu hidden">
			                <label><input type="radio" name="gender" value="" ${empty param.gender ? 'checked' : ''}> 전체</label>
			                <label><input type="radio" name="gender" value="M" ${param.gender == 'M' ? 'checked' : ''}> 남아</label>
			                <label><input type="radio" name="gender" value="F" ${param.gender == 'F' ? 'checked' : ''}> 여아</label>
		           		</div>
		            </div>
		            
		        </div>
					
				<%-- 최신/조회 순 정렬 --%>
	            <select name="sort" class="btn-filter" onchange="submitFilterForm()">
	                <option value="latest" ${param.sort == 'latest' ? 'selected' : ''}>최신순</option>
	                <option value="views" ${param.sort == 'views' ? 'selected' : ''}>조회순</option>
	            </select>
	        </div>
	        
	        <%-- 선택된 필터 태그 표시 영역 --%>
	        <div class="active-filter-tags" id="activeFilterTags"></div>
	        
	        <div class="animal-grid">
	            <c:choose>
	            	<%-- 검색 결과가 있는 경우 --%>
	                <c:when test="${not empty animalList}">
	                    <c:forEach var="animal" items="${animalList}"> 
			                <%-- 동물카드 jsp 호출 --%>
			                <c:set var="animal" value="${animal}" scope="request" />
			                <jsp:include page="animalCard.jsp" />
		            	</c:forEach>
	                </c:when>
	                <%-- 검색 결과가 없는 경우 --%>
	                <c:otherwise>
	                    <div class="no-data">
	                        <p>현재 조건에 맞는 보호 동물이 없습니다.</p>
	                    </div>
	                </c:otherwise>
	            </c:choose>
	        </div>
	    </form>
	</div>
	
	<div class="pagination-wrapper">
	    <%-- 이전 페이지 버튼 --%>
	    <c:if test="${currentPage > 1}">
	        <button type="button" class="btn-page prev" onclick="changePage(${currentPage - 1})">
	            <span class="material-symbols-outlined">chevron_left</span>
	        </button>
	    </c:if>
	
	    <%-- 페이지 번호 버튼들 --%>
	    <div class="page-numbers">
	        <c:forEach var="i" begin="1" end="${totalPage}">
	            <button type="button" 
	                    class="btn-page ${i == currentPage ? 'active' : ''}" 
	                    onclick="changePage(${i})">${i}</button>
	        </c:forEach>
	    </div>
	
	    <%-- 다음 페이지 버튼 --%>
	    <c:if test="${currentPage < totalPage}">
	        <button type="button" class="btn-page next" onclick="changePage(${currentPage + 1})">
	            <span class="material-symbols-outlined">chevron_right</span>
	        </button>
	    </c:if>
	</div>
	
	<div class="promotion-banner">
	
	    <div class="banner-content">
	        <div class="text-group">
	            <h3>아직 가족을 찾지 못한 친구들이 아주 많아요.</h3>
	            <p>보호소의 아이들이 따뜻한 가족의 품으로 돌아갈 수 있도록<br>관심과 사랑을 나누어 주세요.</p>
	            <a href="${pageContext.request.contextPath}/animal/guide.do" class="btn-consult">
	                입양 상담 신청하기
	                <%--<span class="material-symbols-outlined">arrow_forward</span> --%>
	            </a>
	            <a href="${pageContext.request.contextPath}/consult/search.do" class="btn-search">
	                임시보호소 알아보기
	            </a>
	        </div>
	    </div>
	</div>
	
	<%-- footer --%>
	<jsp:include page="/view/common/footer.jsp" />

	
</body>
</html>