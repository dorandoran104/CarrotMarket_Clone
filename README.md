# 학원 팀 프로젝트_SPRING Project 당근마켓 클론 코딩

## 개발 동기 및 개요

* 곧 수료이면서 팀 프로젝트 겸 스터디를 위해 널리 알려진 중고 거래 플랫폼인 당근마켓을 클론 코딩 해보기로 했습니다. 1주차씩 기능을 공부하면서 적용해보기로 했습니다.

## 사용 기술
* java
* spring
* jquery
* jsp,jstl
* mybatis
* oracle

## 1주차
#### 로그인/회원가입 기능

<details>
    <summary>로그인/로그아웃</summary>


* 로그인 후 원래 있던곳으로 돌아가기 위해 request.getHeaer()을 썼지만, 아이디 혹은 비밀번호가 틀려서 다시 돌아올경우 로그인폼으로 돌아가는 현상 수정

	<details>
	    <summary>Controller</summary>

	<!-- summary 아래 한칸 공백 두고 내용 삽입 -->
	```java
	  //로그인 폼
	@GetMapping("/login")
	public String loginForm(Model model, HttpServletRequest request) {

		String pre_Url = (String)model.getAttribute("url");
		//request.getHeader로 전 url 불러올때
		//로그인이 실패하면 전 url이 로그인폼으로 지정되어서 아래처럼 변경
		if(pre_Url == null) {
			model.addAttribute("url", request.getHeader("referer"));
		}else {
			model.addAttribute("url",pre_Url);
		}
		return "member/loginForm";
	}

	//로그인 액션
	@PostMapping("/login")
	public String loginAction(HttpSession session, RedirectAttributes rttr, String userid, String userpwd, String url) {

		MemberVO memberVO = memberService.getMember(userid);

		if(memberVO == null || !memberVO.getUserpwd().equals(userpwd)) {
			rttr.addFlashAttribute("message", "아이디 혹은 비밀번호가 맞지않습니다.");
			rttr.addFlashAttribute("url",url);
			return "redirect:/member/login";
		}
		session.setAttribute("loginUser", memberVO.getId());

		return "redirect:"+ url;
	}

	//로그아웃 액션
	@GetMapping("/logoutAction")
	public String logoutAction(HttpSession session, HttpServletRequest request) {
		session.invalidate();
		String url = request.getHeader("referer");
		return "redirect:" + url;
	}

	```
	</details>

	<details>
	    <summary>Service</summary>

	<!-- summary 아래 한칸 공백 두고 내용 삽입 -->
	```java
	  //맴버 가져오기
		@Override
		public MemberVO getMember(String userid) {
			return memberMapper.getMember(userid);
		}
	```
	</details>

	<details>
	    <summary>Mapper</summary>

	<!-- summary 아래 한칸 공백 두고 내용 삽입 -->
	```html
	  <select id="getMember" resultType="org.ezen.ex02.domain.MemberVO">
		select * from carrot_member where userid = #{userid}
	</select>
	```
	</details>

</details>

<details>
	<summary>회원가입</summary>

<!-- summary 아래 한칸 공백 두고 내용 삽입 -->
* ajax을 통해 아이디 중복확인 비동기 처리
* jquery를 이용해 유효성 검사

	
	<details>
		<summary>Controller</summary>
		
		
	```java
	//회원가입 폼
	@GetMapping("/join")
	public String joinForm() {
		return "member/joinForm";
	}

	//회원가입 액션
	@PostMapping("/join")
	public String joinAction(MemberVO memberVO) {
		memberService.joinMember(memberVO);
		return "redirect:/";
	}

	//아이디 중복 확인
	@GetMapping(
			value ="/join/{userid}",
			produces ={MediaType.TEXT_PLAIN_VALUE}
			)
	@ResponseBody
	public ResponseEntity<String> idCheck(@PathVariable("userid") String userid){

		MemberVO memberVO = memberService.getMember(userid);

		return memberVO == null ? new ResponseEntity<String>(HttpStatus.OK) : new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	```
		
	</details>
	
	<details>
   		<summary>Service</summary>

	```java
	//맴버 가져오기
	@Override
	public MemberVO getMember(String userid) {
		return memberMapper.getMember(userid);
	}

	//회원가입
	//로그인시에도 사용
	@Override
	public void joinMember(MemberVO memberVO) {
		memberMapper.joinMember(memberVO);
	}
	```
	</details>

	<details>
	<summary>Mapper</summary>
		
		
	```html
	<select id="getMember" resultType="org.ezen.ex02.domain.MemberVO">
		select * from carrot_member where userid = #{userid}
	</select>


	<insert id="joinMember">
		insert into carrot_member(id, userid, userpwd, username, nickname, address)
		values(carr_mem_id_seq.nextval, #{userid},#{userpwd},#{username},#{nickname},#{address})
	</insert>
	```
	</details>
	
	
	<details>
		<summary>js</summary>
		
		
	* 회원가입 유효성, ajax 아이디 중복처리
	```javascript
	$(document).ready(function(){
		$("#join_button").on("click",function(e){
			e.preventDefault();
			location.href = 'join';
		});

		$("#join_submit").css("cursor","pointer");
		$("#join_reset").css("cursor","pointer");

		//아이디 중복 체크 후 아이디 수정하면 중복체크 확인 날리기
		$("#join_form").find("input[name='userid']").on("keyup",function(){
			$("#id_check_result").empty();
			$("#id_check_success").val("0");
		});

		//아이디 중복 체크
		$("#id_check").on("click",function(){
			let userid = $("#join_form").find("input[name='userid']").val();

			if(userid.length == 0){
				$("#id_check_result").css("color","red").text("아이디를 입력해주세요");
				$("#id_check_success").val("0");
				return false;
			}
			if(userid.length < 4){
				$("#id_check_result").css("color","red").text("아이디가 너무 짧습니다.");
				$("#id_check_success").val("0");
				return false;
			}

			$.ajax({
				url : 'join/'+userid,
				type : 'get',
				success : function(data){
					console.log(data);
					$("#id_check_result").css("color","green").text("사용 가능한 아이디 입니다.");
					$("#id_check_success").val("1");
				},
				error : function(){
					$("#id_check_result").css("color","red").text("사용 불가한 아이디 입니다.");
					$("#id_check_success").val("0");
				}
			});
			console.log(userid);
		});


		//비밀번호 같은지 체크
		$("#join_form").on("keyup","input[name='userpwd']",function(){
			userpwd_check();
		});

		//비밀번호 같은지 체크
		$("#join_form").on("keyup","input[name='userpwd_check']",function(){
			userpwd_check();
		});


		//회원가입이 버튼 누를 시 조건 확인
		$("#join_submit").on("click",function(e){

			e.preventDefault();

			//이름 작성 유무
			if( $("#join_form").find("input[name='username']").val().length == 0 ){
				alert("이름을 입력해 주세요");

				let input = $("#join_form").find("input[name='username']");

				focus_scroll(input);

				return false;
			}
			//닉네임 입력 유무
			if( $("#join_form").find("input[name='nickname']").val().length == 0 ){
				alert("닉네임을 입력해 주세요");
				let input = $("#join_form").find("input[name='nickname']");

				focus_scroll(input);

				return false;
			}

			//아이디 중복 체크 유무
			if( $("#id_check_success").val() == 0){
				alert("아이디 중복확인이 필요합니다.");
				let input = $("#id_check");

				focus_scroll(input);

				return false;
			}


			//비밀번호 일치 유무
			if( $("#userpwd_check_success").val() == 0){
				let input = $("#join_form").find("input[name='userpwd']"); 
				alert("비밀번호가 일치하지 않습니다.");

				focus_scroll(input);

				return false;
			}

			//주소 입력 유무
			if( $("#join_form").find("input[name='address']").val().length == 0 ){
				alert("주소를 입력해 주세요");
				let input = $("#join_form").find("input[name='address']");

				focus_scroll(input);

				return false;
			}

			$("#join_form").submit();
		});

	 });


	 //메소드 시작

	 //비밀번호 같은지 체크
	 function userpwd_check(){
		let userpwd = $("#join_form").find("input[name='userpwd']").val();
		let userpwd_check = $("#join_form").find("input[name='userpwd_check']").val();
		let userpwd_check_success = $("#userpwd_check_success");
		let pwd_check = $("#pwd_check");

		if(userpwd.length < 4){
			pwd_check.css("color","red").text("비밀번호가 짧습니다.");
			userpwd_check_success.val("0");
		}else if(userpwd != userpwd_check){
			pwd_check.css("color","red").text("비밀번호가 다릅니다.");
			userpwd_check_success.val("0");
		}else if(userpwd == userpwd_check){
			pwd_check.css("color","green").text("비밀번호가 같습니다.");
			userpwd_check_success.val("1");
		}
	 }

	 //포커스 및 스크롤 처리
	 function focus_scroll(input){

		input.focus();
		let temp = input.offset();
		$("html body").animate({scrollTop : temp.top});
	 }	
	```

</details>
