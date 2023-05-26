/**
 * 
 */
 
 $(document).ready(function(){
 	
 	let timeValue = $("#article-category").data('time');
 	let updateTime = displayTime(timeValue);
 	$("#article-category").text(updateTime);
 	
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
	//판매상태 변경
	$(".isSell").on("click",function(e){
		e.preventDefault();
		
		console.log($(this).attr("href"));
		
		if(confirm('정보를 변경하시겠습니까?') ){
			location.replace("sell?" + $(this).attr("href"));
		}else{
			return false;
		}
	});
	
	//채팅버튼 누를시 유효검사
	$("#chat-btn").click(function(e){
		e.preventDefault();
		
		let sell = $(this).data("sell");
		let targetUser = $(this).data("targetuser");
		let articleNo = $(this).data("articleno");
		
		if(sell == 2){
			alert('이미 거래가 완료되었습니다.');
			return false;
		}
		
		location.href = '../chat/new?targetUser=' + targetUser + '&articleNo=' + articleNo;
	});
 });
 
 
function displayTime(timeValue){
	let today = new Date();
	
	let updateDate = new Date(timeValue);
	let updateTime = Math.floor((today.getTime() - updateDate.getTime()) / 1000 / 60);
	console.log(updateTime);
	let str = '';
	
	if(updateTime <1){
		str='방금전';
		return str;
	}
	if(updateTime < 60){
		str = updateTime + '분 전';
		return str;
	}
	
	if(updateTime < 60 * 24){
		str = Math.floor(updateTime/60) + '시간 전';
		return str;
	}
	if(updateTime< 365 * 24 * 60 ){
		str = Math.floor(updateTime/60/24)+ '일 전';
		return str;
	}
	
	
}