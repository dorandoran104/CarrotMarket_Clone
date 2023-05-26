<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>


<div>
	<button>상세보기</button>
	<button class=""    onclick="location.href='get?cno=<c:out">수정</button>
	
		
		<button class="delete">삭제</button>
		<form action="">
		</form>
</div>
<script>
	$(document).ready(function(){
		
		$(".delete").on("click",function(){
			$.ajax({
				url : 'delete',
				type : 'post',
				success : function(){
					
				}
			})	
			
		})
		
	});
</script>
</body>
</html>