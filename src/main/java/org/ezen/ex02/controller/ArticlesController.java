package org.ezen.ex02.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.ezen.ex02.domain.ArticleVO;
import org.ezen.ex02.domain.AttachVO;
import org.ezen.ex02.domain.Criteria;
import org.ezen.ex02.service.ArticlesService;
import org.ezen.ex02.service.AttachService;
import org.ezen.ex02.util.ApiKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import lombok.Setter;

@Controller
@RequestMapping("/articles")
public class ArticlesController {
	
	@Setter(onMethod_=@Autowired)
	private ArticlesService articlesService;
	
	@Setter(onMethod_=@Autowired)
	private AttachService attachService;
	
	@GetMapping("/list")
	public String listPage(Model model) {
		
		//리스트
		
		List<ArticleVO> list = articlesService.getArticles(new Criteria());
		
		model.addAttribute("list",list);
		return "articles/list";
	}
	
	//더보기 누를 시 리스트 뿌리기
	@GetMapping(
			value="/list/{page}",
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<List<ArticleVO>> getArticles(@PathVariable("page") int page) {
		Criteria cri = new Criteria(page);
		
		List<ArticleVO> list = articlesService.getArticles(cri);
		
		return new ResponseEntity<>(list,HttpStatus.OK);
	}
	
	//게시글 등록 폼
	@GetMapping("/new")
	public String registerPage(HttpSession session, Model model) {
		String kakaoApiKey = new ApiKey().getKakaoKey(); 
		model.addAttribute("kakaoKey",kakaoApiKey);
		return "articles/register";
	}
	
	//게시글 등록 액션
		@PostMapping("/new")
		public String registerAction(MultipartFile[] files, ArticleVO article){
			int articleNo = articlesService.registerArticles(article);
			attachService.insertImg(files,articleNo);
			return "redirect:/articles/get?id="+articleNo;
		}
	
	//게시글 상세 조회
	@GetMapping("/get")
	public String getArticle(Model model, int id) {
		ArticleVO articleVO = articlesService.getArticle(id);
		model.addAttribute("article",articleVO);
		String kakaoApiKey = new ApiKey().getKakaoKey();
		model.addAttribute("kakaoKey",kakaoApiKey);
		return "articles/article";
	}
	
	//게시글 수정 폼
	@GetMapping("/modify")
	public String modifyArticle(Model model,int id) {
		System.out.println(id);
		ArticleVO articleVO = articlesService.getArticle(id);
		model.addAttribute("article",articleVO);
		
		String kakaoApiKey = new ApiKey().getKakaoKey();
		model.addAttribute("kakaoKey",kakaoApiKey);
		return "articles/modify";
	}
	
	@PostMapping("/modify")
	public String modifyArticle(ArticleVO articleVO, MultipartFile[] files, AttachVO[] attachVO,
			@RequestParam("removeFile") AttachVO[] removeFile) {
			//먼저 db에 있는 이미지 리스트 다 지우기
			attachService.deleteArticleAllImage(articleVO.getId());
			
			
			

		//articlesService.modifyArticle(articleVO);
		return "redirect:/articles/get?id="+articleVO.getId();
	}
	
	//내 게시글 예약중/거래완료로 바꾸기
	@GetMapping("/sell")
	public String isSell(int id, int sell){
		articlesService.setSell(id,sell);
		return "redirect:/articles/get?id="+id;
	}
	
	//게시글 삭제
	@PostMapping("/delete")
	@ResponseBody
	public ResponseEntity<String> deleteArticle(int id){
		List<AttachVO> list = attachService.getArticleImage(id);
		//파일 db삭제
		attachService.deleteArticleAllImage(id);
		//게시글 db삭제
		articlesService.deleteArticle(id);
		//파일 삭제
		attachService.deleteArticleFile(list);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	//리스트에서 게시글 누를 시 조회수 증가
	@PostMapping(value="/hitcount/{id}")
	@ResponseBody
	public ResponseEntity<String> hitcountModify(@PathVariable("id") int id){
		articlesService.hitCountModify(id);
		return new ResponseEntity<String>(HttpStatus.OK);
	}
}
