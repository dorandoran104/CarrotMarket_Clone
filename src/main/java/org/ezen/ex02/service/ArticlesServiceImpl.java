package org.ezen.ex02.service;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.ezen.ex02.domain.ArticleVO;
import org.ezen.ex02.domain.Criteria;
import org.ezen.ex02.domain.ImageVO;
import org.ezen.ex02.mapper.ArticlesMapper;
import org.ezen.ex02.mapper.AttachMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.Setter;
import lombok.extern.log4j.Log4j;
import net.coobird.thumbnailator.Thumbnailator;

@Service
@Log4j
public class ArticlesServiceImpl implements ArticlesService{
	
	@Setter(onMethod_=@Autowired)
	private ArticlesMapper articlesMapper;
	
	@Setter(onMethod_=@Autowired)
	private AttachMapper attachMapper;
	
	//게시글+파일 작성하기
	@Override
	@Transactional
	public int registerArticles(MultipartFile[] files,ArticleVO article){
		int result = articlesMapper.registerArticles(article);
		
		int articleId = articlesMapper.getLastId();
		if(files.length == 0) {
			return articleId;
		}
		StringBuilder filePath = new StringBuilder("images");
		
		String fileFullPath = "C:\\Users\\82104\\Desktop\\spring_ex\\teamproject\\carrotmarket\\src\\main\\webapp\\resources\\";
		
		
		File uploadPath = new File(new StringBuilder().append(fileFullPath).append(filePath).toString(),getFolder());
		
		if(!uploadPath.exists()) {
			uploadPath.mkdirs();
		}
		
		//이미지 파일들 저장하기
		for(int a = 0; a<files.length; a++) {
			ImageVO imageVO = new ImageVO();

			log.info(files[a].getOriginalFilename());
			
			StringBuilder sb = new StringBuilder();
			
			UUID uuid = UUID.randomUUID();
			
			sb.append(uuid + "-");
			sb.append(files[a].getOriginalFilename());
			//new file(경로,파일명);
			File saveFile = new File(uploadPath.getPath(), sb.toString());
		
			try {
				files[a].transferTo(saveFile);
				
				if(a == 0) {
					ImageVO thumbnailVO = new ImageVO();
					FileOutputStream thumbnail = new FileOutputStream(new File(uploadPath.getPath(), "s_"+sb.toString()));
					Thumbnailator.createThumbnail(files[a].getInputStream(),thumbnail,250,250);
					thumbnail.close();
					
					thumbnailVO.setArticleNo(articleId);
					thumbnailVO.setFilePath(filePath.toString() + "\\" +  getFolder() + "\\");
					thumbnailVO.setFileName("s_"+sb.toString());
					
					attachMapper.registerThumbnail(thumbnailVO);
				}

				imageVO.setArticleNo(articleId);
				imageVO.setFileName(sb.toString());
				imageVO.setFilePath(filePath.toString() + "\\" +  getFolder() + "\\");
				
				attachMapper.registerImg(imageVO);
				
				
			} catch (Exception e) {
				log.error(e.getMessage());
				new Exception();
			}
	
		}
		return articleId;
	}
	
	
	//게시글 가져오기
	@Override
	public ArticleVO getArticle(int id) {
		ArticleVO articleVO = articlesMapper.getArticle(id);
		return articleVO;
	}
	
	//게시글 리스트 불러오기
	@Override
	public List<ArticleVO> getArticles(Criteria cri) {
		List<ArticleVO> list = articlesMapper.getArticles(cri);
		return list;
	}

	
	//폴더 날짜별로 정리하기
		private String getFolder() {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			
			Date date = new Date();
			String str = sdf.format(date);
			return str.replace("-", File.separator);
		}


	
}
