package org.ezen.ex02.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.ezen.ex02.domain.ArticleVO;
import org.ezen.ex02.domain.Criteria;
import org.ezen.ex02.service.ArticlesService;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import lombok.Setter;
import oracle.jdbc.proxy.annotation.Post;

@Controller
@RequestMapping("/articles")
public class ArticlesController {
	
	@Setter(onMethod_=@Autowired)
	private ArticlesService articlesService;
	
	@GetMapping("/list")
	public String listPage(Model model) {
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
			int articleId = articlesService.registerArticles(files,article);
			return "redirect:/articles/get?id="+articleId;
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
	public String modifyArticle(ArticleVO articleVO) {
		articlesService.modifyArticle(articleVO);
		return "redirect:/articles/get?id="+articleVO.getId();
	}
	
	//내 게시글 예약중/거래완료로 바꾸기
	@GetMapping("/sell/{sell}/{id}")
	@ResponseBody
	public ResponseEntity<String> isSell(@PathVariable("id") int id, @PathVariable("sell") int sell){
		articlesService.setSell(id,sell);
		return new ResponseEntity<>("success",HttpStatus.OK);
	}
}
