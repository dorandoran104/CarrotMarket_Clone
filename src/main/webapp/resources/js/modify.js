/**
 * 
 */
 
 $(document).ready(function(){
 	let id = $("#id").val();
 	let count = 0;
 	$.ajax({
 		url : '../attach/' + id,
 		success : function(result){
 			console.log(result);
 			showImage(result);
 		}
 	});
 	
 	function showImage(result){
 		
 		$.each(result,function(key,value){
 			let filecallpath = encodeURIComponent(value.filePath + value.fileName);
 			
 			let str = '<li style="padding: 5px; display:inline-block; width : calc(100%/3); height : 150px">';
 				str += '<div style="width : 100%; cursor:pointer" class="delete_img" data-filename="'+ value.fileName +'" data-filepath="'+ value.filePath+'">X</div>';
 				str += '<img style="display : block; width:100%; height: 90%;" src="../attach/get?fileName=' + filecallpath + '"/>';
 				str += '<input type="text" name="attachVO['+key+'].filePath" value="' + value.filePath + '"/>';
 				str += '<input type="text" name="attachVO['+key+'].fileName" value="' + value.fileName + '"/>';
 				str += '<input type="text" name="attachVO['+key+'].articleNo" value="' + id + '"/>';
 				$("#img_area").append(str);
 		});
 		clone = $("#img_area").html();
 	}
 	
 	$("#img_area").on("click","div[class='delete_img']",function(e){
 		console.log($(this));
 		
		
		let fileName = $(this).data("filename");
		let filePath = $(this).data("filepath");
		
		console.log(fileName);
		console.log(filePath);
		 		
 		let str = '<input type="text" name="removeFile['+count+'].fileName" value="'+ fileName+'"/>'
 		str += '<input type="text" name="removeFile['+count+'].filePath" value="'+ filePath+'"/>'
 		
 		$(this).closest("li").remove();
 		
 		$("#modify_form").append(str);
 		count = count+1;
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