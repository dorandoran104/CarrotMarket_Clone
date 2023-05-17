package org.ezen.ex02.controller;

import java.net.MalformedURLException;
import java.util.List;

import org.ezen.ex02.domain.ImageVO;
import org.ezen.ex02.service.AttachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.Setter;

@RestController
@RequestMapping("/attach")
public class AttachController {
	
	@Setter(onMethod_=@Autowired)
	private AttachService attachService;
	
	//게시글 상세 조회시 이미지 list 불러오기
	@GetMapping(
			value="/{articleNo}",
			produces = MediaType.APPLICATION_JSON_VALUE
			)
	public ResponseEntity<List<ImageVO>> getimages(@PathVariable("articleNo") int articleNo){
		List<ImageVO> list = attachService.getArticleImage(articleNo);
		return new ResponseEntity<>(list,HttpStatus.OK);
	}
	
	//불러온 이미지 Resource로 뿌려주기
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
	
	//섬네일 뿌리기
	@GetMapping("/thumbnail/{articleNo}")
	public Resource showThumbnail(@PathVariable("articleNo") int articleNo) throws MalformedURLException {
		
		ImageVO imageVO = attachService.getThumbnail(articleNo);
		
		StringBuilder fileFullPath = new StringBuilder("C:\\Users\\82104\\Desktop\\spring_ex\\teamproject\\carrotmarket\\src\\main\\webapp\\resources\\");
		
		if(imageVO == null) {
			fileFullPath.append("images/DaangnMarket_logo.png");
		}else {
			fileFullPath.append(imageVO.getFilePath() + imageVO.getFileName());
		}
		return new UrlResource("file:"+fileFullPath.toString());
	}
		
}
