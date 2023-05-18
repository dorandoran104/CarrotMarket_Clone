/**
 * 
 */
 
 $(document).ready(function(){
 
 	//게시글 삭제
	$("#delete-article").on("click",function(e){
		e.preventDefault();
		
		if(confirm('게시글을 삭제하시겠습니까?') ){
			console.log("실행");
			let id = $(this).data("id");
			console.log(id);
			
			$.ajax({
				url : 'delete',
				data : {id : id},
				type : 'post',
				success : function(result){
					alert("삭제에 성공했습니다.");
					location.replace('/ex02/articles/list');
				},
				error : function(error){
					alert("오류");
					history.back();
				}
			})
		}else{
			console.log("취소");
			return false;
		}
	});
	
	$(".isSell").on("click",function(e){
		e.preventDefault();
		
		console.log($(this).attr("href"));
		
		if(confirm('정보를 변경하시겠습니까?') ){
			location.replace("sell?" + $(this).attr("href"));
		}else{
			return false;
		}
	});
 });