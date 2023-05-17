/**
 * 
 */
 
 $(document).ready(function(){
	
 	let articleNo = $("#content").data("id");
 	
 	
 	$.ajax({
 		url: '../attach/' + articleNo,
 		success : function(result){
 			console.log(result);
 			showImage(result);
 			slickfunction();
 		}
 	}).then;
 });
 
 function showImage(result){
 	let imgArea = $(".imageArea");
 	
 	if(result.length == 0){
	 	let str = '';
		str+= '<img src="../attach/get?fileName=non"/>';
		imgArea.append(str);
		console.log(str);
 	}
 	
 	for(let i = 0; i<result.length; i++){
 		let str = '';
 		let filecallpath = encodeURIComponent(result[i].filePath + result[i].fileName);
 		str+= '<img src="../attach/get?fileName='+ filecallpath + '"/>';
 		imgArea.append(str);
 		console.log(str);
 	}
 }
 
 function slickfunction(){
 	console.log("slick");
 	$(".imageArea").slick({
 		slide: 'img',
 		infinite : true,
 		dots : true,
 		draggable : true,
 		arrows: true,
 		prevArrow: $('#aro1_prev'),
		nextArrow: $('#aro1_next'),
 	})
 }