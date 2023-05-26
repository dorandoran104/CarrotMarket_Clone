## SPRING PROJECT 당근마켓 클론코딩
-----

#### 개발 동기
* 학원 팀 프로젝트 시간이 생기고 조금 더 공부하자는 팀원들과 함께 중고거래로 널리 알려진 당근마켓을 클론코딩 해보기로 하였습니다.


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
 <summary>게시글 리스트</summary>

<details>
<summary>Controller</summary>

* 게시글 더보기 누를시 ajax를 통해 비동기 방식 사용
```java
@GetMapping("/list")
	public String listPage(Model model) {
		
		//리스트
		List<SecondHandArticleVO> list = secondHandArticlesService.getArticles(new Criteria());
		model.addAttribute("list",list);
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
</details>

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
</details>
</details>

***