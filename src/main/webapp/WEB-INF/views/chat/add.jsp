<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<c:set var="pageTitle" value="회원정보 수정"></c:set>

<%@ include file="../include/header.jspf"%>


<div id="container" class="container" style="margin-top : 150px;">
		<h1>채팅</h1>
		<div id="chating" class="chating">
		</div>
		<c:forEach var="list" items="${list}">
			<a href="get?roomId=${list.roomId}">${list.roomId}</a>
		</c:forEach>	
</div>

<!-- Footer-->
<%@ include file="../include/footer.jspf"%>