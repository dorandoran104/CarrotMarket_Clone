/**
 * 
 */
 $(document).ready(function(){
	
	let id = $("#container").data("id");
	$.ajax({
		url: 'list/'+id,
		success : function(result){
		}
	})
 	let roomId = $("#chatroom").data("roomid");
 	
 	console.log(roomId);
 	console.log(id);
 	
	let webSocket = new WebSocket("ws://" + location.host + "/ex02/chating/" + roomId);
	//웹 소켓 서버 연결되었을때
	webSocket.onopen = function(data){};
	
	//웹 소켓에서 메세지가 날라왔을때 이벤트
	webSocket.onmessage = function(data){
			let messageData = data.data;
			if(messageData != null && messageData.trim() != ""){
				let jsonParse = JSON.parse(messageData);
				/*
				let message = "";
				
				if(jsonParse.roomId == roomId){
					message = jsonParse.message;
					let str = targetMessage(message);
					$("#chat").append(str);
					auto_scroll();
				}
				*/
				
				let message = jsonParse.message;
				let str = '';
				
				if(jsonParse.sender == loginUser){
					str = myMessage(message);
				}else{
					str = targetMessage(message);
				}
				$("#chat").append(str);
				auto_scroll();
				
			}
		};

	//엔터 누르면 보내기
	$("#chatting").on("keyup",function(e){
		console.log("enter");
		if(e.keyCode == 13){
			send();
		}
	});
	
	
	$("#sendBtn").on("click",function(){
		send();
	});
	
	//메세지 보내기
	function send() {
		
		let msg = $("#chatting").val();
		let str = myMessage(msg);
		let sender = id;
		let jsondata = {
			roomId : roomId,
			message : msg,
			sender : sender,
			regDate : new Date().toLocaleDateString()
		};
		webSocket.send(JSON.stringify(jsondata));
		//$("#chat").append(str);
		
		$("#chatting").val("");
		//auto_scroll()
	}
	
	//상대방이 보낸 메세지
	function targetMessage(msg){
		let str = '';
		str+= '<li class="you">';
        str+= '<div class="entete">';
        str+= '<span class="status green"></span>';
        str+= '<h2>Vincent</h2>';
        str+= '<h3>10:12AM, Today</h3></div>';
        str+= '<div class="message">';
        str+= msg;
        str+='</div></li>';
        
        return str;
	}
	
	//내가 보낸 메세지 폼
	function myMessage(msg){
		let str =''; 
		str+= '<li class="me">';
        str+= '<div class="entete">';
        str+= '<h3>10:12AM, Today</h3>';
        str+= '<h2>Vincent</h2>';
        str+= '<span class="status blue"></span></div>';
        str+= '<div class="message">'
        str+= msg;
        str+= '</div></li>';
        
        return str;
	}
	
	//채팅시 맨 밑 포커스
	function auto_scroll(){
		$('#chat').scrollTop($('#chat')[0].scrollHeight);
	}

 })