/**
 * 
 */
 $(document).ready(function(){
 	console.log("start");
	let webSocket;
	
	webSocketOpen();
	
	function webSocketOpen(){
		console.log("openSocket");
		webSocket = new WebSocket("ws://" + location.host + "/ex02/chating");
		webSocketEvt();
	}
	
	function webSocketEvt(){
		webSocket.onopen = function(data){};
		
		webSocket.onmessage = function(data){
			let msg = data.data;
			if(msg != null && msg.trim() != ""){
				$("#chating").append("<p>" + msg + "</p>");
			}
			
			$("#chatting").on("keyup",function(e){
				console.log("enter");
				if(e.keyCode == 13){
					send();
				}
			});
		}
	}
	$("#startBtn").on("click",function(){
		console.log("click");
		let userName = $("#userName").val();
		
		if(userName == null || userName.trim() == ""){
			alert('사용자 이름을 입력해 주세요');
			$("#userName").focus();
		}else{
			webSocketOpen();
			$("#yourName").hide();
			$("#yourMsg").show();
		}
	});

	$("#sendBtn").on("click",function(){
		send();
	});
	
	function send() {
		let userName = $("#userName").val();
		let msg = $("#chatting").val();
		webSocket.send(userName + " : " + msg);
		$("#chatting").val("");
	}
 });