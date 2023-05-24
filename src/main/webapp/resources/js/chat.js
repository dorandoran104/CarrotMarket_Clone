/**
 * 
 */
 $(document).ready(function(){
	
	let id = $("#container").data("id");
	
	//채팅방 리스트 불러오기
	$.ajax({
		url: 'list/'+id,
		success : function(result){
			console.log(result);
			let str = '';
			$.each(result,function(key,value){
				str+= '<li><div><h2><a href="';
				str+= value.roomId;
				str+= '" data-title ="' + value.title;
				str+= '" data-cost="' + value.cost;
				str+= '" data-articleno="'+ value.articleNo;
				if(id == value.chatUser){
					str+= '" data-mynickname="'+ value.chatUserNickName;
					str+= '" data-targetnickname="'+ value.targetUserNickName;
				}else{
					str+= '" data-mynickname="'+ value.targetUserNickName;
					str+= '" data-targetnickname="'+ value.chatUserNickName;
				}
				str+= '">'+value.title;
				str+='</a></h2></div></li>';
			});
			
			$("#chatList").append(str);
		}
	});
 	
 	//채팅방 클릭하면
 	$("#chatList").on("click","a",function(e){
 		e.preventDefault();
 		
 		let title = $(this).data("title");
 		let cost = $(this).data("cost");
 		let articleno = $(this).data("articleno");
 		let mynickname = $(this).data("mynickname");
 		let targetnickname = $(this).data("targetnickname");
 		
 		
 		chatroominfo(title, cost, articleno, mynickname, targetnickname);
 		
 		$("#chat").empty();
 		
 		let roomId = $(this).attr("href");
 		
 		$.ajax({
 			url : 'message/'+roomId,
 			success : function(result){
 				console.log(result);
 				$.each(result,function(key,value){
 					let str = '';
 					if(value.sender == id){
 						str+= myMessage(value.message, value.regDate, mynickname);
 					}else{
 						str+= targetMessage(value.message,value.regDate,targetnickname);
 					}
 					$("#chat").append(str);
 					auto_scroll();
 				});
 			}
 		});
		webSocketCon(roomId,id);
 	});
 	
 	function webSocketCon(roomId,loginUser){
 		console.log("socket");
		let webSocket = new WebSocket("ws://" + location.host + "/ex02/chating/" + roomId);
		//웹 소켓 서버 연결되었을때
		webSocket.onopen = function(data){};
		
		//웹 소켓에서 메세지가 날라왔을때 이벤트
		webSocket.onmessage = function(data){
			let messageData = data.data;
			let mynickname = $("#mynickname").val();
			let targetnickname = $("#targetnickname").val();
			
			console.log(mynickname);
			console.log(targetnickname);
			
			if(messageData != null && messageData.trim() != ""){
				let jsonParse = JSON.parse(messageData);
				let message = jsonParse.message;
				let regDate = jsonParse.regDate;
				let str = '';
				
				if(jsonParse.sender == loginUser){
					str = myMessage(message,regDate,mynickname);
				}else{
					str = targetMessage(message,regDate,targetnickname);
				}
				$("#chat").append(str);
				auto_scroll();
			}
		};
		
		//메세지 보내기
		function send() {
			let msg = $("#chatting").val();
			let sender = id;
			
			let jsondata = {
				roomId : roomId,
				message : msg,
				sender : sender,
				regDate : getTime()
			};
			webSocket.send(JSON.stringify(jsondata));
			$("#chatting").val("");
		}
		
		//엔터누르면 보내기
		$("#chatting").on("keyup",function(e){
			console.log("enter");
			if(e.keyCode == 13){
				send();
			}
		});
		
		$("#sendBtn").on("click",function(){
			send();
		});
	
 	}
 	
	
	
	
	//상대방이 보낸 메세지
	function targetMessage(msg, regDate,targetnickname){
		let str = '';
		str+= '<li class="you">';
        str+= '<div class="entete">';
        str+= '<span class="status green"></span>';
        str+= '<h2>'+ targetnickname+'</h2>';
        str+= '<h3>'+regDate+'</h3></div>';
        str+= '<div class="message">';
        str+= msg;
        str+='</div></li>';
        
        return str;
	}
	
	//내가 보낸 메세지 폼
	function myMessage(msg,regDate,mynickname){
		let str =''; 
		str+= '<li class="me">';
        str+= '<div class="entete">';
        str+= '<h3>'+ regDate +'</h3>';
        str+= '<h2>'+mynickname+'</h2>';
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
	
	//채팅방 클릭시 상세정보 들고오기
	function chatroominfo(title, cost, articleno, mynickname, targetnickname){
		$("#chatInfo").empty();
		let str = '';
		str+= '<div id="chatroom"><img src="../attach/thumbnail/';
		str+= articleno;
		str+= '" style="width : 140px;border-radius: 10px;"/>';
        str+= '<div><h2>';
        str+= title;
        str+= '</h2><h3>';
        str+= AddComma(cost);
        str+= '원</h3><div><button id="rebtn">예약하기</button></div></div></div>';
        str+= '<input type="hidden" id="mynickname" value="'
        str+= mynickname;
        str+='"/>'
        str+= '<input type="hidden" id="targetnickname" value="'
        str+= targetnickname;
        str+='"/>'
        $("#chatInfo").append(str);
	}
	
	//메세지 전송시 현재시각 구하기
	function getTime(){
		let now = new Date();
		
		let year = now.getFullYear();
		let month = ('0' + (now.getMonth() + 1)).slice(-2);
		let day = ('0' + now.getDate()).slice(-2);
		let hour = ('0' + now.getHours()).slice(-2);
		let minutes = ('0' + now.getMinutes()).slice(-2);
		let seconds  = ('0' + now.getSeconds()).slice(-2);
		
		let dateString = year + '-' + month  + '-' + day + ' ' + hour + ':' + minutes  + ':' + seconds;
		return dateString;
	}
	//숫자 자릿수 표시
	function AddComma(num) {
	    var regexp = /\B(?=(\d{3})+(?!\d))/g;
	    return num.toString().replace(regexp, ',');
	}
 })