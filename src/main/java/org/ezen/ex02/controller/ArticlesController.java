package org.ezen.ex02.controller;

import java.net.MalformedURLException;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.ezen.ex02.domain.ArticleVO;
import org.ezen.ex02.domain.ImageVO;
import org.ezen.ex02.service.ArticlesService;
import org.ezen.ex02.util.ApiKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.UrlResource;
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

import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Controller
@RequestMapping("/articles")
@Log4j
public class ArticlesController {
	
	@Setter(onMethod_=@Autowired)
	private ArticlesService articlesService;
	
	//게시글 등록 폼
	@GetMapping("/new")
	public String registerPage(HttpSession session, Model model) {
		String kakaoApiKey = new ApiKey().getKakaoKey(); 
		model.addAttribute("kakaoKey",kakaoApiKey);
		return "articles/register";
	}
	
	@GetMapping("/list")
	public String listPage() {
		List<ArticleVO> list = articlesService.getArticles();
		return "articles/list";
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
		return "articles/article";
	}
	//게시글 조회시 이미지 list 불러오기
	@GetMapping(
			value="/images/{articleNo}",
			produces = MediaType.APPLICATION_JSON_VALUE
			)
	public ResponseEntity<List<ImageVO>> getimages(@PathVariable("articleNo") int articleNo){
		List<ImageVO> list = articlesService.getArticleImage(articleNo);
		return new ResponseEntity<>(list,HttpStatus.OK);
	}
	
	//불러온 이미지 Resource로 뿌려주기
	@GetMapping(
			value="/image"
			)
	@ResponseBody
	public Resource showImage(String fileName) throws MalformedURLException{
		String fileFullPath = "C:\\Users\\82104\\Desktop\\spring_ex\\teamproject\\carrotmarket\\src\\main\\webapp\\resources\\" + fileName;
		
	return new UrlResource("file:"+ fileFullPath);
	}
}
