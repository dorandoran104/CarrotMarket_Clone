/**
 * 
 */
 
 $(document).ready(function(){
 	let imgArea = $("#article-img-area");
 	let articleNo = $("#content").data("id");
 	console.log(articleNo);
 	
 	$.ajax({
 		url:'images/'+articleNo,
 		success : function(result){
 			console.log(result);
 			showImage(result);
 		}
 	});
 })
 
 function showImage(result){
 	let imgArea = $(".imageArea");
 	for(let i = 0; i<result.length; i++){
 		let str = '';
 		let filecallpath = encodeURIComponent(result[i].filePath + result[i].fileName);
 		
 		str+= '<img src="image?fileName='+ filecallpath + '" style="width:100%"/>';
 		console.log(str);
 		imgArea.append(str);
 	}
 }