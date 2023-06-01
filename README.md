## SPRING PROJECT 당근마켓 클론코딩
-----

#### 개발 동기
* 학원 팀 프로젝트 시간이 생기고 조금 더 공부하자는 팀원들과 함께 중고거래로 널리 알려진 당근마켓의 기능들을 구현해보기로 하였습니다.


#### 사용 기술
* java
* spring
* jsp/jstl
* mybatis
* oracle

#### 개발 파트
* 중고거래 게시판
* 중고거래 채팅

#### 개발 코드

<details>
<summary>중고거래 게시판</summary>


<details>
 <summary>게시글 리스트</summary>

<details>
<summary>Controller</summary>

* 게시글 더보기 누를시 ajax를 통해 비동기 방식 사용
```java
@GetMapping("/list")
	public String listPage(Model model) {
		return "secondhandarticles/list";
	}
	
	//더보기 누를 시 리스트 뿌리기
	@GetMapping(
			value="/list/{page}",
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<List<SecondHandArticleVO>> getArticles(@PathVariable("page") int page) {
		Criteria cri = new Criteria(page);
		
		List<SecondHandArticleVO> list = secondHandArticlesService.getArticles(cri);
		
		return new ResponseEntity<>(list,HttpStatus.OK);
	}
```

* 이미지 파일 뿌리기
```java
	//섬네일 뿌리기(이미지중 한장만)
	@GetMapping(value="/thumbnail/{articleNo}")
	public Resource showThumbnail(@PathVariable("articleNo") int articleNo) throws MalformedURLException {
		List<SecondHandAttachVO> list = secondHandAttachService.getArticleImage(articleNo);
		StringBuilder fileFullPath = new StringBuilder("C:\\Users\\82104\\Desktop\\spring_ex\\teamproject\\carrotmarket\\src\\main\\webapp\\resources\\");
		//만약 이미지가 없으면 고유 사진 한장 띄우기
		if(list == null || list.size() == 0) {
			fileFullPath.append("images/DaangnMarket_logo.png");
		}else {
			fileFullPath.append(list.get(0).getFilePath() + list.get(0).getFileName());
		}
		return new UrlResource("file:"+fileFullPath.toString());
	}
```
</details>

<details>
<summary>Service</summary>

```java
//게시글 리스트 불러오기
	@Override
	public List<SecondHandArticleVO> getArticles(Criteria cri) {
		List<SecondHandArticleVO> list = secondHandArticlesMapper.getArticles(cri);
		return list;
	}
```
</details>

<details>
<summary>Mapper</summary>

```html
<select id="getArticles" resultType="org.ezen.ex02.domain.SecondHandArticleVO">
		<![CDATA[
		select c.*
			,m.usernickname as nickname
			,m.useraddress as address
		from 
			carrot_secondhand_articles c left outer join carrot_member m on(c.memberno = m.id) 
		where rownum <= #{page} *6
		order by updatedate desc
		]]>
	</select>
```
</details>
</details>


***
<details>
<summary>게시글 작성</summary>
* 이미지 작성으로 위해 AttachController,Serivce,Mapper 생성

<details>
<summary>Controller</summary>

```java
//게시글 등록 폼
	@GetMapping("/new")
	public String registerPage(Model model) {
		String kakaoApiKey = new ApiKey().getKakaoKey(); 
		model.addAttribute("kakaoKey",kakaoApiKey);
		return "secondhandarticles/register";
	}
	
	//게시글 등록 액션
	@PostMapping("/new")
	public String registerAction(MultipartFile[] files, SecondHandArticleVO article){
		int articleNo = secondHandArticlesService.registerArticles(article);
		attachService.insertImg(files,articleNo);
		return "redirect:/sharticle/get?id="+articleNo;
	}
```
</details>

<details>
<summary>Service</summary>

```java
//게시글+파일 작성하기
	@Override
	@Transactional
	public int registerArticles(SecondHandArticleVO article){
		secondHandArticlesMapper.registerArticles(article);		
		int articleId = secondHandArticlesMapper.getLastId();
		return articleId;
	}
```

```java
//파일 저장하기, db에 넣기
	@Override
	public void insertImg(MultipartFile[] files,int articleNo) {
		StringBuilder filePath = new StringBuilder("images");
		//System.getProperty("user.dir") 가 이상하게 작동해서 일단 절대경로로 설정
		String fileFullPath = "C:\\Users\\82104\\Desktop\\spring_ex\\teamproject\\carrotmarket\\src\\main\\webapp\\resources\\";
		
		File uploadPath = new File(new StringBuilder().append(fileFullPath).append(filePath).toString(),getFolder());
		
		if(!uploadPath.exists()) {
			uploadPath.mkdirs();
		}
		//이미지 파일들 저장하기
		for(int a = 0; a<files.length; a++) {
			//빈파일 체크 후 빈 파일이면 파일저장없이 return
			if(files[a].isEmpty()) {
				return;
			}
			SecondHandAttachVO imageVO = new SecondHandAttachVO();
			StringBuilder sb = new StringBuilder();
			UUID uuid = UUID.randomUUID();
			
			sb.append(uuid + "-");
			sb.append(files[a].getOriginalFilename());
			//new file(경로,파일명);
			File saveFile = new File(uploadPath.getPath(), sb.toString());
		
			try {
				files[a].transferTo(saveFile);
				imageVO.setArticleNo(articleNo);
				imageVO.setFileName(sb.toString());
				imageVO.setFilePath(filePath.toString() + "\\" +  getFolder() + "\\");
				
				secondHandAttachMapper.registerImg(imageVO);
			} catch (Exception e) {
				log.error(e.getMessage());
				new Exception();
			}
		}
	}
```
</details>

<details>
<summary>Mapper</summary>

```html
<insert id="registerArticles">
		insert into carrot_secondhand_articles(
			id, memberno, title, body
			<if test="cost != null">,cost</if>
			<if test="costOffer != null">,costOffer</if>
			<if test="lng != null">,lng</if>
			<if test="lat != null">,lat</if>
			<if test="locationInfo != null">,locationInfo</if>
			)
		values(
			carr_art_id_seq.nextval, #{memberNo}, #{title}, #{body}
			<if test="cost != null">,#{cost}</if>
			<if test="costOffer != null">,#{costOffer}</if>
			<if test="lng != null">,#{lng}</if>
			<if test="lat != null">,#{lat}</if>
			<if test="locationInfo != null">,#{locationInfo}</if>
			)
</insert>

<select id="getLastId" resultType="int">
		select carr_art_id_seq.currval from dual
</select>
```

```html
<insert id="registerImg">
		insert into carrot_secondhand_img(articleno, filepath, filename)
		values(#{articleNo},#{filePath},#{fileName}) 
</insert>
```
</details>

<details>
<summary>파일js</summary>

* 파일은 form방식으로 처리
```js
//이미지 파일을 올릴 시 미리보기
 	$("#register_form").on("change","input[name='files']",function(e){
 		let img_area = $("#img_area");
 		img_area.empty();
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
 				let str = '<li style="padding: 5px; display:inline-block; width : calc(100%/3); height : 150px">';
 				str+='<div style="width : 100%; cursor:pointer" class="delete_img" data-fno="'+ i +'">X</div>';
 				str+= '<img style="display : block; width:100%; height: 90%;" src="' + e.target.result + '"/>';
 				str+='<div style="font-size : 1.2rem;height: 10%;overflow:hidden; text-overflow:ellipsis; white-space:nowrap">' + files[i].name + '</div></li>';
 				
 				img_area.append(str);
 			}
 			reader.readAsDataURL(files[i]);
 		}
 	});
 })
 
 //메서드 시작
  //이미지 파일만 있는지 확인 메서드
 function checkFile(regex,name){
 	if( regex.test(name) ){
 		return true;
 	}
 	return false;
 }
 
 //선택한 이미지 지우기
 $(document).on("click","div[class='delete_img']",function(e){
	
	 let fno = $(this).closest("li").index();
	 
	 const dataTransfer = new DataTransfer();
	 
	 let files = $("#register_form").find("input[name='files']")[0].files;
	 let fileArray = Array.from(files);
	 
	 fileArray.splice(fno, 1);
	 
	 fileArray.forEach(file => { 
	 	dataTransfer.items.add(file); 
	});
	 
	 $("#register_form").find("input[name='files']")[0].files = dataTransfer.files;
	 $(this).closest("li").remove();
 });
```
</details>

<details>
<summary>카카오맵js</summary>

* 카카오맵 사이트에 나와있는 코드들은 따로 안적었습니다.

```js
//수정시 위치 정보가 있으면 표시해주기
	if( $("#maparea").find("span[id='istrue']").length >= 1){ 
	 		let lng = $("#lng").val();
	 		let lat = $("#lat").val();
	 		makeMapDiv();
	 		writeMap(lng, lat);
	}
	
	//위치 찾기에서 엔터 누를시 submit 막으면서 위치 찾기로
	$("#location_search").keydown(function(e){
		if(e.keyCode == 13){
			e.preventDefault();
			$("#maparea").empty();
			let location_search = $("#location_search").val();
			
			if(location_search.length == 0){
				alert("장소를 입력해 주세요");
				return false;
			}
			makeMapDiv();
			startMap(location_search);
		}
	});

 	//버튼 클릭시 장소 찾기
 	$("#button-addon2").on("click",function(){
 		$("#maparea").empty();
		let location_search = $("#location_search").val();
		
		if(location_search.length == 0){
			alert("장소를 입력해 주세요");
			return false;
		}
		makeMapDiv();
		// 키워드로 장소를 검색합니다
		startMap(location_search);
	});
	
	//초기화 누르면 주소를 없앤다
	$("#maparea").on("click","#location_reset",function(){
		$("#maparea").empty();
		$("#hope_location").val("");
		$("form").find("input[name='lat']").val("");
        $("form").find("input[name='lng']").val("");
	});
```
</details>

</details>

***
<details>
<summary>게시글 수정</summary>

* 수정 시 Map부분은 작성과 공통으로 사용하였습니다.
* 이미지 수정 또한 form 방식으로 넘겨주고, 기존 이미지 중 삭제 처리 이벤트가 일어난 것들은 submit을 누를 시 ajax로 삭제 처리 하였습니다.

<details>
<summary>Controller</summary>

```java
//게시글 수정 폼
	@GetMapping("/modify")
	public String modifyArticle(Model model,int id) {
		SecondHandArticleVO articleVO = secondHandArticlesService.getArticle(id);
		model.addAttribute("article",articleVO);
		
		String kakaoApiKey = new ApiKey().getKakaoKey();
		model.addAttribute("kakaoKey",kakaoApiKey);
		return "secondhandarticles/modify";
	}

	//게시글 수정
	@PostMapping("/modify")
	public String modifyArticle(SecondHandArticleVO articleVO, MultipartFile[] files) {
			//새롭게 올린 이미지는 db,파일 저장
			attachService.insertImg(files,articleVO.getId());
			//마지막 게시글 수정
			secondHandArticlesService.modifyArticle(articleVO);
		return "redirect:/sharticle/get?id="+articleVO.getId();
	}
```

```java
@PostMapping(
			value="/deleteFile",
			produces = MediaType.TEXT_PLAIN_VALUE
			)
	public ResponseEntity<String> deleteFile(SecondHandAttachVO attachVO){
		
		secondHandAttachService.deleteArticleFile(attachVO);
		secondHandAttachService.deleteArticleImageDB(attachVO.getFileName());
		return new ResponseEntity<>(HttpStatus.OK);
	}
```
</details>

<details>
<summary>Service</summary>

*기존 이미지 삭제시 
```java
//게시글 수정
	@Override
	public void modifyArticle(SecondHandArticleVO articleVO) {
		secondHandArticlesMapper.modifyArticle(articleVO);
	}	
```

```java
//실제파일 삭제하기
	@Override
	public void deleteArticleFile(SecondHandAttachVO attachVO) {
		StringBuilder fileFullPath = new StringBuilder("C:\\Users\\82104\\Desktop\\spring_ex\\teamproject\\carrotmarket\\src\\main\\webapp\\resources\\");
	
		Path file = Paths.get(fileFullPath.toString() + attachVO.getFilePath() + attachVO.getFileName());
		log.info(file);
		try {
			Files.deleteIfExists(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

    //삭제파일 db에서 지우기
	public void deleteArticleImageDB(String fileName) {
		secondHandAttachMapper.deleteArticleImageDB(fileName);
	}
```
</details>

<details>
<summary>Mapper</summary>

```html
<update id="modifyArticle">
		update carrot_secondhand_articles
		set title = #{title}
		, body = #{body}
		<if test="cost != null">,cost = #{cost}</if>
		<if test="costOffer != null">,costOffer = #{costOffer}</if>
		<if test="lng != null">,lng = #{lng}</if>
		<if test="lat != null">,lat = #{lat}</if>
		<if test="locationInfo != null">,locationInfo = #{locationInfo}</if>
		,updatedate = sysdate
		where id = #{id}
	</update>
```

```html
<delete id="deleteArticleImageDB">
		delete carrot_secondhand_img
		where filename = #{fileName}
	</delete>
```
</details>

<details>
<summary>JS</summary>

```js
 $(document).ready(function(){
 	let id = $("#id").val();
 	
 	//페이지 로딩하면 게시글 이미지 불러오기
 	$.ajax({
 		url : '../shattach/' + id,
 		success : function(result){
 			showImage(result);
 		}
 	});
 	
 	//불러온 이미지 뿌리기
 	function showImage(result){
 		
 		$.each(result,function(key,value){
 			let filecallpath = encodeURIComponent(value.filePath + value.fileName);
 			
 			let str = ' <li style="padding: 5px; display:inline-block; width : calc(32%); height : 150px">';
 				str += '<div style="width : 100%; cursor:pointer" class="delete_img" data-articleno="'+value.articleNo+'" data-filename="'+ value.fileName +'" data-filepath="'+ value.filePath+'">X</div>';
 				str += '<img style="display : block; width:100%; height: 90%;" src="../shattach/get?fileName=' + filecallpath + '"/>';
 				$("#img_area").append(str);
 		});
 	}
 	
 	//게시글 이미지 삭제시 뿌린 이미지 지우고 지우는 정보 추가
 	$("#img_area").on("click","div[class='delete_img']",function(e){ 		
		let fileName = $(this).data("filename");
		let filePath = $(this).data("filepath");
		let articleNo = $(this).data("articleno");
		
 		let str = '<div data-articleno="'+articleNo+'" data-filename="'+ fileName +'" data-filepath="'+ filePath+'"></div>';
 		$(this).closest("li").remove();
 		$("#delete-area").append(str);
 	});
 	
 	//추가로 올린 이미지 지우기
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
 	
 	//글 수정시 먼저 지우기 정보에 있는거 실행
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
 				url : '../attach/deleteFile',
 				type : 'post',
 				data : {
 					fileName : fileName,
 					filePath : filePath,
 					articleNo : articleNo
 				},
 			});
 		});
 		$("#modify_form").submit();
 	});
	
	//추가로 게시글 이미지 등록할때
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
 				let str = '<ul style="margin:0; padding:0;display:inline-block; width : calc(32%); height : 150px"><li style="padding: 5px;margin:0; height:100%">';
 				str+='<div style="width : 100%; cursor:pointer" class="delete_img_modify" data-fno="'+ i +'">X</div>';
 				str+= '<img style="display : block; width:100%; height: 90%;" src="' + e.target.result + '"/>';
 				
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
```
</details>
</details>

***
<details>
<summary>게시글 삭제</summary>

<details>
<summary>Controller</summary>

```java
//게시글 삭제
	@PostMapping("/delete")
	@ResponseBody
	public ResponseEntity<String> deleteArticle(int id){
		List<SecondHandAttachVO> list = attachService.getArticleImage(id);
		//파일 db삭제
		attachService.deleteArticleAllImage(id);
		//게시글 db삭제
		secondHandArticlesService.deleteArticle(id);
		//파일 삭제
		for(int a = 0; a<list.size(); a++) {
			attachService.deleteArticleFile(list.get(a));
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}
```
</details>

<details>
<summary>Service</summary>

* 이미지 파일 삭제
```java
//실제파일 삭제하기
	@Override
	public void deleteArticleFile(SecondHandAttachVO attachVO) {
		StringBuilder fileFullPath = new StringBuilder("C:\\Users\\82104\\Desktop\\spring_ex\\teamproject\\carrotmarket\\src\\main\\webapp\\resources\\");
	
		Path file = Paths.get(fileFullPath.toString() + attachVO.getFilePath() + attachVO.getFileName());
		log.info(file);
		try {
			Files.deleteIfExists(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//모든파일 db에서 지우기
	@Override
	public void deleteArticleAllImage(int id) {
		secondHandAttachMapper.deleteArticleAllImage(id);
	}
```

* 게시글 삭제
```java
//게시글 삭제
	@Override
	public void deleteArticle(int id) {
		secondHandArticlesMapper.deleteArticle(id);
	}
```
</details>

<details>
<summary>Mapper</summary>

* 파일 Mapper
```html
	<delete id="deleteArticleAllImage">
		delete carrot_secondhand_img
		where articleNo = #{id}
	</delete>
	
	<delete id="deleteArticleImageDB">
		delete carrot_secondhand_img
		where filename = #{fileName}
	</delete>
```

* 게시글 Mapper
```html
<delete id="deleteArticle">
		delete carrot_secondhand_articles
		where id = #{id}
	</delete>
```
</details>

<details>
<summary>JS</summary>

```js
 	//게시글 삭제
	$("#delete-article").on("click",function(e){
		e.preventDefault();
		if(confirm('게시글을 삭제하시겠습니까?') ){
			$.ajax({
				url : 'delete',
				data : {id : articleNo},
				type : 'post',
				success : function(result){
					alert("삭제에 성공했습니다.");
					location.replace('/ex02/sharticle/list');
				},
				error : function(error){
					alert("오류");
					history.back();
				}
			})
		}else{
			return false;
		}
	});
```
</details>
</details>

***

<details>
<summary>게시글 조회</summary>

* 게시글에 위치정보 있을 시 카카오맵으로 뿌려주기
* 이미지 슬라이더는 Slick 사용

<details>
<summary>Controller</summary>

* 이미지 띄우기
```java
@GetMapping(
			value="/get"
			)
	public Resource showImage(String fileName) throws MalformedURLException{
		StringBuilder fileFullPath = new StringBuilder("C:\\Users\\82104\\Desktop\\spring_ex\\teamproject\\carrotmarket\\src\\main\\webapp\\resources\\");
		if(fileName.equals("non")) {
			fileFullPath.append("images/DaangnMarket_logo.png");
		}else {
			fileFullPath.append(fileName);
		}
		
	return new UrlResource("file:"+ fileFullPath);
	}
```

* 게시글 띄우기
```java
    //리스트에서 게시글 누를 시 조회수 증가
	@PostMapping(value="/hitcount/{id}")
	@ResponseBody
	public ResponseEntity<String> hitcountModify(@PathVariable("id") int id){
		secondHandArticlesService.hitCountModify(id);
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	//게시글 상세 조회
	@GetMapping("/get")
	public String getArticle(Model model, int id) {
		SecondHandArticleVO articleVO = secondHandArticlesService.getArticle(id);
		model.addAttribute("article",articleVO);
		String kakaoApiKey = new ApiKey().getKakaoKey();
		model.addAttribute("kakaoKey",kakaoApiKey);
		return "secondhandarticles/article";
	}
```
</details>

<details>
<summary>Service</summary>

* 이미지
```java
//게시글 이미지 정보 가져오기
	@Override
	public List<SecondHandAttachVO> getArticleImage(int id) {
		List<SecondHandAttachVO> list = secondHandAttachMapper.getArticleImage(id);
		return list;
	}
```

* 게시글
```java
	//게시글 가져오기
	@Override
	public SecondHandArticleVO getArticle(int id) {
		SecondHandArticleVO articleVO = secondHandArticlesMapper.getArticle(id);
		return articleVO;
	}
```
</details>

<details>
<summary>Mapper</summary>

* 이미지
```html
	<select id="getArticleImage" resultType="org.ezen.ex02.domain.SecondHandAttachVO">
		select * from carrot_secondhand_img where articleno = #{id}
	</select>
```

* 게시글
```html
	<select id="getArticle" resultType="org.ezen.ex02.domain.SecondHandArticleVO">
		select     
			c.*
			,m.usernickname as nickname
			,m.useraddress as address
		from 
			carrot_secondhand_articles c left outer join carrot_member m on(c.memberno = m.id)  
		where c.id = #{id}
	</select>
```
</details>

<details>
<summary>JS</summary>

```js
 	//이미지 처리하기
 	$.ajax({
 		url: '../shattach/' + articleNo,
 		success : function(result){
 			showImage(result);
 			slickfunction();
 		}
 	});

//게시글 올린 시간을 조회한 시간 기준으로 표시하기
function displayTime(timeValue){
	let today = new Date();
	
	let updateDate = new Date(timeValue);
	let updateTime = Math.floor((today.getTime() - updateDate.getTime()) / 1000 / 60);
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
//이미지 가져오기
function showImage(result){
 	let imgArea = $(".imageArea");
 	
 	if(result.length == 0){
	 	let str = '';
		str+= '<img src="../shattach/get?fileName=non"/>';
		imgArea.append(str);
 	}
 	
 	for(let i = 0; i<result.length; i++){
 		let str = '';
 		let filecallpath = encodeURIComponent(result[i].filePath + result[i].fileName);
 		str+= '<img src="../shattach/get?fileName='+ filecallpath + '"/>';
 		imgArea.append(str);
 	}
 }

//이미지 가져온 후 slick을 사용해 슬라이드 구현
 function slickfunction(){
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
```
</details>
</details>

***
	
<details>
<summary>부가기능</summary>

* 게시글 판매 정보 변경(예약중/거래완료)
* 관심글 설정

<details>
<summary>Controller</summary>

```java
	//내 게시글 예약중/거래완료로 바꾸기
	@GetMapping("/sell")
	public String isSell(int id, int sell){
		secondHandArticlesService.setSell(id,sell);
		return "redirect:/sharticle/get?id="+id;
	}
```

```java
	//게시글 상세 조회 시 관심글인지 확인
	@GetMapping(value="/like/{articleNo}")
	public ResponseEntity<String> findLike(HttpSession session, @PathVariable("articleNo") int articleNo){
		if(session.getAttribute("loginUser") == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		int memberNo = (int)session.getAttribute("loginUser");
		SecondHandLikeVO likeVO = likeService.findLike(memberNo, articleNo);
		String result = likeVO != null ? "success" : "fail";
		return new ResponseEntity<>(result,HttpStatus.OK);
	}
	//관심 게시글 설정
	@PostMapping(value="/like/{articleNo}")
	public ResponseEntity<String> likeArticle(HttpSession session,@PathVariable("articleNo") int articleNo){
		int memberNo = (int)session.getAttribute("loginUser");
		
		SecondHandLikeVO likeVO = new SecondHandLikeVO();
		
		likeVO.setArticleNo(articleNo);
		likeVO.setMemberNo(memberNo);
		
		int result = likeService.likeArticle(likeVO);
		return result > 0 ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	//관심 게시글 삭제
	@DeleteMapping("/unlike/{articleNo}")
	public ResponseEntity<String> unlikeArticle(HttpSession session,@PathVariable("articleNo") int articleNo){
		int memberNo = (int)session.getAttribute("loginUser");
		
		SecondHandLikeVO likeVO = new SecondHandLikeVO();
		likeVO.setArticleNo(articleNo);
		likeVO.setMemberNo(memberNo);
		
		int result = likeService.unlikeArticle(likeVO);
		return result > 0 ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
```
</details>

<details>
<summary>Service</summary>

```java
	//내 게시글 예약중,거래완료 정보 넣기
	@Override
	public void setSell(int id, int sell) {
		secondHandArticlesMapper.setSell(id, sell);
	}
```

```java
	//관심글 설정시 게시글 db도 수정
	@Override
	@Transactional
	public int likeArticle(SecondHandLikeVO likeVO) {
		int result = likeMapper.likeArticle(likeVO);
		articlesMapper.updateLikeCnt(likeVO.getArticleNo(),1);
		return result;
	}

	@Override
	@Transactional
	public int unlikeArticle(SecondHandLikeVO likeVO) {
		int result = likeMapper.unlikeArticle(likeVO);
		articlesMapper.updateLikeCnt(likeVO.getArticleNo(), -1);
		return result;
	}
	//관심글 확인
	@Override
	public SecondHandLikeVO findLike(int memberNo, int articleNo) {
		return likeMapper.findLike(memberNo, articleNo);
	}	
```
</details>

<details>
<summary>Mapper</summary>

```html
	<update id="setSell">
		update carrot_secondhand_articles
		set sell = #{sell}
		where id = #{id}
	</update>
```

* 관심 글
```html
	<insert id="likeArticle">
		insert into carrot_secondhand_article_like(id, articleno, memberno)
		values(carr_like_seq.nextval, #{articleNo}, #{memberNo})
	</insert>

	<delete id="unlikeArticle">
		delete carrot_secondhand_article_like
		where articleNo = #{articleNo} and memberNo = #{memberNo}
	</delete>
	
	<select id="findLike" resultType="org.ezen.ex02.domain.SecondHandLikeVO">
		select *
		from carrot_secondhand_article_like
		where articleno = #{articleNo} and memberno = #{memberNo}
	</select>
```
</details>

<details>
<summary>JS</summary>

```js
 	//관심글인지 유무
 	$.ajax({
 		url : '../shlike/like/'+articleNo,
 		success : function(result){
 			if(result == 'success'){
	 			$(".bi").toggleClass("bi-heart-fill");
				$(".bi").toggleClass("bi-heart");
			}
 		}
 	});

 	//관심 게시글 클릭시
 	$(".bi").on("click",function(){
		
		if( $(this).hasClass("bi-heart")){
			console.log(articleNo);
			$.ajax({
				url : '../shlike/like/'+ articleNo ,
				type : 'post',
				success : function(){
					alert("관심글이 등록되었습니다.");
					$(".bi").toggleClass("bi-heart-fill");
					$(".bi").toggleClass("bi-heart");
					let likeCount = parseInt($("#like").text());
					$("#like").text(likeCount+1);
				},
				error : function(){
					alert('오류');
				}
			});
		}else if( $(this).hasClass("bi-heart-fill")){
			$.ajax({
				url : '../shlike/unlike/'+articleNo,
				type : 'delete',
				success : function(result){
					alert("관심글이 해제되었습니다.");
					$(".bi").toggleClass("bi-heart-fill");
					$(".bi").toggleClass("bi-heart");
					let likeCount = parseInt($("#like").text());
					$("#like").text(likeCount-1);
				},
				error : function(){
					alert('오류');
				}
			});
		}
 	});

	//판매상태 변경
	$(".isSell").on("click",function(e){
		e.preventDefault();
		
		if(confirm('정보를 변경하시겠습니까?') ){
			location.replace("sell?" + $(this).attr("href"));
		}else{
			return false;
		}
	});
```
</details>
	
</details>
</details>
	



<details>
<summary>채팅</summary>

* STOMP, socket.js 등등 채팅방을 구현 해볼수 있는 기능들이 많지만 기본 WebSocket을 사용해서 구현 해봤습니다.

<details>
<summary>Handler, Config</summary>

* config
```java
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer{

	private SocketHandler socketHandler;

	public WebSocketConfig(SocketHandler socketHandler) {
		this.socketHandler = socketHandler;
	}
	
	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(socketHandler, "/chating/{roomId}").setAllowedOrigins("*");
	}	
}
```

* handler
* 채팅방 구분을 위해 ChatSession(소켓세션, 소켓세션 아이디, 채팅방 아이디)객체를 생성하였습니다.
* 추후 웹 소켓 공부시 nosql 기능도 사용 예정입니다.
```java
@Log4j
@Component
public class SocketHandler extends TextWebSocketHandler{
	
	//private HashMap<String,WebSocketSession> sessionMap = new HashMap<>();
	//roomid에따라 채팅방 분리를 위해 ChatSession class를 따로 만듬
	private List<ChatSession> sessionList = new ArrayList<>();
	
	@Setter(onMethod_=@Autowired)
	private ChatService chatService;
	
	@Override
	//websocket 연결 성공 시 실행되는 메서드
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		super.afterConnectionEstablished(session);
		//sessionMap.put(session.getId(), session);
		String roomId = session.getUri().toString().split("chating/")[1];
		String sessionId = session.getId();
		ChatSession chatSession = new ChatSession();
		chatSession.setRoomId(roomId);
		chatSession.setSession(session);
		chatSession.setSessionId(sessionId);
		
		sessionList.add(chatSession);
		
	}
	
	@Override
	//websocket 연걸 종료 시 실행되는 메서드
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		//sessionMap.remove(session.getId());
		if(sessionList.size()>0) {
			for(int a = 0; a<sessionList.size(); a++) {
				if(session.getId().equals(sessionList.get(a).getSessionId())) {
					sessionList.remove(a);
				}
			}
		}
		super.afterConnectionClosed(session, status);
	}
	
	@Override
	//메세지 수신 및 송신
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		String id = session.getId();
		
		ObjectMapper objectMapper = new ObjectMapper();
		ChatVO chatVO = objectMapper.readValue(message.getPayload(), ChatVO.class);
		
		 chatService.insertMessage(chatVO);
		
		if(sessionList.size()>0) {
			for(int a = 0; a<sessionList.size(); a++) {
				if(sessionList.get(a).getRoomId().equals(chatVO.getRoomId())) {
					try {
						sessionList.get(a).getSession().sendMessage(message);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
```
</details>

<details>
<summary>Controller</summary>

```java
@RequestMapping("/chat")
@RestController
public class ChatController {
	@Setter(onMethod_=@Autowired )
	private ChatService chatService;
	
	@Setter(onMethod_=@Autowired )
	private SecondHandArticlesService articlesService;
	
	//채팅방 폼
	@GetMapping("/list")
	public ModelAndView chat(HttpSession session) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("chat/list");
		return mav;
	}
	//내 채팅방 목록 불러오기 가져올때 채팅방 정보 다 가져오게끔 (ex 해당채팅방 게시글 정보, 유저정보)
	@GetMapping(value="/list/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ChatRoomVO>> getChatRoomList(@PathVariable("id") int id){
		List<ChatRoomVO> list = chatService.getMyChatRoomList(id);
		
		return list.size()>0? new ResponseEntity<>(list,HttpStatus.OK):new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	//방에 메세지 이력 가져오기
	@GetMapping(value="/message/{roomId}",produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ChatVO>> getMessage(@PathVariable("roomId") String roomId){
		List<ChatVO> list = chatService.getMessage(roomId);
		return new ResponseEntity<>(list,HttpStatus.OK);
	}
	
	//게시글에서 채팅하기 누룰때
	@GetMapping("/new")
	public ModelAndView createNewChat(HttpSession session, int targetUser, int articleNo, RedirectAttributes rttr) {
		int id = (int)session.getAttribute("loginUser");
		
		ChatRoomVO chatRoom = chatService.findChatRoom(id,articleNo);
		//만약 전에 해당 게시글 물품대상으로 채팅한적이 없으면 채팅방 새로 만들기
		if(chatRoom == null) {
			chatRoom = chatService.createChatRoom(id,targetUser, articleNo);
		}
		ModelAndView mav = new ModelAndView();
		mav.setViewName("redirect:/chat/list");
		rttr.addFlashAttribute("create",chatRoom.getArticleNo());
		return mav;
	}
}
```
</details>

<details>
<summary>Service</summary>

```java
	//내 채팅방 불러오기
	@Override
	public List<ChatRoomVO> getMyChatRoomList(int id) {
		List<ChatRoomVO> list = chatMapper.getMyChatRoomList(id);
		return list;
	}
	//채팅방 들어가면 상세 정보 가져오기
	@Override
	public ChatRoomVO getChatRoomDetail(String roomId) {
		ChatRoomVO chatRoomVO = chatMapper.getChatRoomDetail(roomId);
		return chatRoomVO;
	}
	
	//채팅이력 불러오기
	@Override
	public List<ChatVO> getMessage(String roomId) {
		List<ChatVO> list = chatMapper.getMessage(roomId);
		return list;
	}
	
	//채팅방이 존재하는지
	@Override
	public ChatRoomVO findChatRoom(int id, int articleNo) {
		ChatRoomVO chatRoomVO = chatMapper.findChatRoom(id,articleNo);
		return chatRoomVO;
	}
	
	//채팅방이 없으면 새롭게 만들기
	@Override
	public ChatRoomVO createChatRoom(int id, int targetUser, int articleNo) {
		String uuid = UUID.randomUUID().toString();
		
		ChatRoomVO chatRoomVO = new ChatRoomVO();
		chatRoomVO.setRoomId(uuid);
		chatRoomVO.setChatUser(id);
		chatRoomVO.setTargetUser(targetUser);
		chatRoomVO.setArticleNo(articleNo);
		
		chatMapper.createChatRoom(chatRoomVO);
		articlesMapper.chatCountModify(articleNo);
		
		return chatRoomVO;
		
	}
	
	//채팅메세지 db에 저장하기
	@Override
	public void insertMessage(ChatVO chatVO) {
		chatMapper.insertMessage(chatVO);
	}
	
```
</details>

<details>
<summary>Mapper</summary>

```html
	<select id="getMyChatRoomList" resultType="org.ezen.ex02.domain.ChatRoomVO">
		select 
			room.*
			,art.title ,art.cost ,art.sell 
			,mem1.usernickname as chatUserNickName
			,mem2.usernickname as targetUserNickName
		from carrot_chatroom room 
			left outer join carrot_secondhand_articles art on room.articleno = art.id
			left outer join carrot_member mem1 on room.chatuser = mem1.id
			left outer join carrot_member mem2 on room.targetuser = mem2.id
		where 
			room.chatuser = #{id} or room.targetuser = #{id}
	</select>
	
	<select id="getMessage" resultType="org.ezen.ex02.domain.ChatVO">
		select * from carrot_chat where roomid = #{roomId}
	</select>
	
	<select id="findChatRoom" resultType="org.ezen.ex02.domain.ChatRoomVO">
		select * from carrot_chatroom
		where chatuser = #{id} and articleNo = #{articleNo}
	</select>
	
	<insert id="createChatRoom">
		insert into carrot_chatroom(roomid,chatuser,targetuser, articleNo)
		values(#{roomId},#{chatUser},#{targetUser},#{articleNo})
	</insert>
	
	<select id="getChatRoomByRoomId" resultType="org.ezen.ex02.domain.ChatRoomVO">
		select * from carrot_chatroom
		where roomId = #{roomId}
	</select>
	
	<insert id="insertMessage">
		insert into carrot_chat(roomid, message, sender, regdate)
		values(#{roomId}, #{message}, #{sender}, #{regDate})
	</insert>
	
```
</details>

<details>
<summary>JS</summary>
 
```js
let switchingRoom = false;
//중첩연결 방지용
let socket = null;

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
    let regexp = /\B(?=(\d{3})+(?!\d))/g;
    return num.toString().replace(regexp, ',');
}

$(document).ready(function() {
	let id = $("#container").data("id");

	$.ajax({
		url : 'list/' + id,
		success : function(result) {
			let str = '';
			console.log(result);
			$.each(result, function(key, value) {
				let targetNickName;
				let myNickName;
				
				if(id == value.chatUser){
					myNickName = value.chatUserNickName;
					targetNickName = value.targetUserNickName;	
				}else{
					myNickName = value.targetUserNickName;
					targetNickName = value.chatUserNickName;
				}
			
				str += '<li><div class="chatList"><div class="chatListNickName">';
				str += myNickName;
				str+= '</div><h2>';
				str+= '<a href="';
				str += value.roomId;
				str += '" data-title ="' + value.title;
				str += '" data-cost="' + value.cost;
				str += '" data-sell="' + value.sell;
				str += '" data-articleno="' + value.articleNo;
				str += '" data-mynickname="' + value.chatUserNickName;
				str += '" data-targetnickname="' + value.targetUserNickName;
				str += '">' + value.title;
				str += '</a></h2></div></li>';
			});

			$("#chatList").append(str);

			//만약 게시글을 통해 채팅창에 들어왔을경우 자동으로 해당 채팅방 클릭하기
			if ($("#create").length != 0 || $("#create").val() == ' ') {
				let find = $("[data-articleno='" + $("#create").val() + "']");
				find.trigger("click");
			}
		}
	});

	$("#chatList").on("click", "a", function(e) {
		e.preventDefault();
		let roomId = $(this).attr("href");
		let title = $(this).data("title");
 		let cost = $(this).data("cost");
 		let articleno = $(this).data("articleno");
 		let mynickname = $(this).data("mynickname");
 		let targetnickname = $(this).data("targetnickname");
 		let sell = $(this).data("sell");
 		
		$("#chat").empty();
 		
 		$("#chat").append('<div class="spinner-area"><div class="spinner-border text-warning" role="status"><span class="visually-hidden">Loading...</span></div></div>');
		
		chatroominfo(title, cost, articleno, mynickname, targetnickname, roomId, sell);
		
		switchChatRoom(roomId, id, mynickname, targetnickname);
	});
	
	//엔터누르면 보내기
	$("#chatting").on("keyup",function(e){
		if(e.keyCode == 13){
			send();
		}
	});
	//클릭시에도 보내기
	$("#sendBtn").on("click",function(){
		send();
	});
	
	function send() {
		let msg = $("#chatting").val();
		let sender = id;
		let jsondata = {
			roomId : $("#roomId").val(),
			message : msg,
			sender : sender,
			regDate : getTime()
		};
		socket.send(JSON.stringify(jsondata));
		$("#chatting").val("");
	}
	
	//채팅시 맨 밑 포커스
	function auto_scroll(){
		$('#chat').scrollTop($('#chat')[0].scrollHeight);
	}
	
	//채팅방 클릭시 상세정보 들고오기
	function chatroominfo(title, cost, articleno, mynickname, targetnickname,roomId, sell){
		$("#chatInfo").empty();
		
		let str = '';
		str+= '<div id="chatroom"><img style="height:100px; widht: 120px" src="../shattach/thumbnail/';
		str+= articleno;
		str+= '" style="width : 140px;border-radius: 10px;"/>';
	    str+= '<div>'
	    if(sell == 0){
	    	str+= "<span style='color : #ff6f0f'> 판매중 </span>";
	    }else if(sell == 1){
	    	str+= "<span style='color : green'>예약중 </span>";
	    }else if(sell == 2){
	    	str+= "거래 완료 "
	    }
	    str+= '<h2>';
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
		str+= '<input type="hidden" id="roomId" value="'
	    str+= roomId;
	    str+='"/>'
	       
	    $("#chatInfo").append(str);
	}
	
	//웹 소켓 연결 변경하기
	function switchChatRoom(roomId, id, mynickname, targetnickname) {
	if (switchingRoom) {
		alert('잠시후에 다시 질행해 주세요');
		return;
	}

	switchingRoom = true;

	// 이전 WebSocket 연결 종료
	if (socket !== null) {
		socket.close();
	}

	// 새로운 WebSocket 연결 생성
	socket = new WebSocket("ws://" + location.host + "/ex02/chating/" + roomId);

	socket.onopen = function(event) {
		let str = '';
 		$.ajax({
 			url : 'message/'+roomId,
 			success : function(result){
 				$.each(result,function(key,value){
 					if(value.sender == id){
 						str+= myMessage(value.message, value.regDate, mynickname);
 					}else{
 						str+= targetMessage(value.message,value.regDate,targetnickname);
 					}
 				});
 				$("#chat").empty();
				$("#chat").append(str);
				auto_scroll();
 			}
 		});
		switchingRoom = false;
	};

	socket.onclose = function(event) {
		switchingRoom = false;
	};
	
	// WebSocket 메시지 수신 처리
	socket.onmessage = function(event) {
		let messageDate = event.data;
		console.log(messageDate);
		if(messageDate != null && messageDate.trim() != ""){
			let jsonParse = JSON.parse(messageDate);
			let message = jsonParse.message;
			let regDate = jsonParse.regDate;
			let str = '';
			
			if(jsonParse.sender == id){
				str = myMessage(message,regDate,mynickname);
			}else{
				str = targetMessage(message,regDate,targetnickname);
			}
			$("#chat").append(str);
			//document.querySelector("#chat").append(str);
			auto_scroll();
			}
		};
	
		socket.onerror = function(event) {
			switchingRoom = false;
		};
	}
});
```
</details>
</details>
	

#### 느낀점
구현해야하는 부분들 중 학원에서 배운 기능 이외에도 혼자 공부하며 구현해야 했던 부분들이 있었습니다. 이는 아직 배울것이 많다는 것과 지속적인 학습의 필요성을 느끼게 했습니다. 그러나 이러한 상황들을 마주하며 지속적인 도전을 통해 채팅 기능을 구현하게 되었고, 이 과정에서 자신감과 성취감을 느끼며 개발에 대한 매력을 더욱 깊게 느낄 수 있게 되었습니다.

또한, 구현 과정에서 발생한 다양한 오류들을 스스로 해결해 나가면서 문제 해결에 대한 능력도 크게 향상시켰습니다. 이러한 경험을 통해 앞으로의 프로젝트에서도 문제를 해결하고 성공적으로 완료할 수 있는 자신감을 갖게 되었습니다. 이러한 성장과 도전적인 과정은 개발 직업에 대한 더욱 강한 매력을 느끼게 해주었습니다.