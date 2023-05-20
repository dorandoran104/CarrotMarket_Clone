/**
 * 
 */
 
 $(document).ready(function(){
 	let id = $("#id").val();
 
 	$.ajax({
 		url : '../attach/' + id,
 		success : function(result){
 			showImage(result);
 		}
 	});
 	
 	function showImage(result){
 		
 		$.each(result,function(key,value){
 			let filecallpath = encodeURIComponent(value.filePath + value.fileName);
 			
 			let str = ' <li style="padding: 5px; display:inline-block; width : calc(100%/3); height : 150px">';
 				str += '<div style="width : 100%; cursor:pointer" class="delete_img" data-articleno="'+value.articleNo+'" data-filename="'+ value.fileName +'" data-filepath="'+ value.filePath+'">X</div>';
 				str += '<img style="display : block; width:100%; height: 90%;" src="../attach/get?fileName=' + filecallpath + '"/>';
 				$("#img_area").append(str);
 		});
 	}
 	
 	$("#img_area").on("click","div[class='delete_img']",function(e){ 		
		let fileName = $(this).data("filename");
		let filePath = $(this).data("filepath");
		let articleNo = $(this).data("articleno");
		
 		let str = '<div data-articleno="'+articleNo+'" data-filename="'+ fileName +'" data-filepath="'+ filePath+'"></div>';
 		$(this).closest("li").remove();
 		$("#delete-area").append(str);
 	});
 	
 	$("#img_area").on("click","div[class='delete_img_modify']",function(e){
 		let fno = $(this).closest("ul").index() -2;
	 	console.log(fno);
	 	
		const dataTransfer = new DataTransfer();
		 
		let files = $("#modify_form").find("input[name='files']")[0].files;
		let fileArray = Array.from(files);
		 
		fileArray.splice(fno, 1);
		 
		fileArray.forEach(file => { 
		 	dataTransfer.items.add(file); 
		});
		 
	 	$("#modify_form").find("input[name='files']")[0].files = dataTransfer.files;
	 	$(this).closest("ul").remove();
 	});
 	
 	$("#modify_submit").on("click",function(e){
 		e.preventDefault();
 		let deleteArea = $("#delete-area div").get();
 		
 		if(deleteArea.length == 0){
 			$("#modify_form").submit();
 		}
 		
 		$.each(deleteArea,function(key,value){
 			let fileName = $(this).data("filename");
 			let filePath = $(this).data("filepath");
 			let articleNo = $(this).data("articleno");
 			
 			$.ajax({
 				url : 'modify/file',
 				type : 'post',
 				data : {
 					fileName : fileName,
 					filePath : filePath,
 					articleNo : articleNo
 				},
 				success : function(result){
 					$("#modify_form").submit();
 				}
 			});
 			
 		});
 	});
	 	
	$("#modify_form").on("change","input[name='files']",function(e){
 		console.log("change");
 		
 		let files = e.target.files;
 		
 		let regex = new RegExp("(.*?)\.(jpg|png|jpeg|bmp)$");
 		
 		if(files.length >10 ){
 			alert("최대 10개까지만 등록할 수 있습니다.");
 			$("input[name='files']").val("");
 			return false;
 		}
 		
 		for(let i = 0; i<files.length; i++){
 			if( ! checkFile(regex, files[i].name) ){
 				alert('이미지만 등록 가능합니다.');
 				$("input[name='files']").val("");
 				return false;
 			}
 			let reader = new FileReader();
 			
 			reader.onload = function(e){
 				let str = '<ul style="padding: 5px; display:inline-block; width : calc(100%/3); height : 150px"><li data-modify="1">';
 				str+='<div style="width : 100%; cursor:pointer" class="delete_img_modify" data-fno="'+ i +'">X</div>';
 				str+= '<img style="display : block; width:100%; height: 90%;" src="' + e.target.result + '"/>';
 				str+='<div style="font-size : 1.2rem;height: 10%;overflow:hidden; text-overflow:ellipsis; white-space:nowrap">' + files[i].name + '</div></li></ul>';
 				
 				$("#img_area").append(str);
 			}
 			reader.readAsDataURL(files[i]);
 		}
 		
 	});
 	
 	
 });
 
 function checkFile(regex,name){
 	if( regex.test(name) ){
 		return true;
 	}
 	return false;
 }