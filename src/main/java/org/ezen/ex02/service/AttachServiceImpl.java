package org.ezen.ex02.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.ezen.ex02.domain.AttachVO;
import org.ezen.ex02.mapper.AttachMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class AttachServiceImpl implements AttachService{
	
	@Setter(onMethod_=@Autowired)
	private AttachMapper attachMapper;
	
	//게시글 이미지 정보 가져오기
	@Override
	public List<AttachVO> getArticleImage(int id) {
		List<AttachVO> list = attachMapper.getArticleImage(id);
		return list;
	}
	//파일 저장하기, db에 넣기
	@Override
	public void insertImg(MultipartFile[] files,int articleNo) {
		
		StringBuilder filePath = new StringBuilder("images");
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
			AttachVO imageVO = new AttachVO();
			StringBuilder sb = new StringBuilder();
			UUID uuid = UUID.randomUUID();
			
			sb.append(uuid + "-");
			sb.append(files[a].getOriginalFilename());
			//new file(경로,파일명);
			File saveFile = new File(uploadPath.getPath(), sb.toString());
		
			try {
				files[a].transferTo(saveFile);
				//첫번째 파일일 경우 thumbnail파일 생성
//				
//				if(a == 0) {
//					AttachVO thumbnailVO = new AttachVO();
//					FileOutputStream thumbnail = new FileOutputStream(new File(uploadPath.getPath(), "s_"+sb.toString()));
//					Thumbnailator.createThumbnail(files[a].getInputStream(),thumbnail,250,250);
//					thumbnail.close();
//					
//					thumbnailVO.setArticleNo(articleId);
//					thumbnailVO.setFilePath(filePath.toString() + "\\" +  getFolder() + "\\");
//					thumbnailVO.setFileName("s_"+sb.toString());
//					
//					attachMapper.registerThumbnail(thumbnailVO);
//				}
//
				imageVO.setArticleNo(articleNo);
				imageVO.setFileName(sb.toString());
				imageVO.setFilePath(filePath.toString() + "\\" +  getFolder() + "\\");
				
				attachMapper.registerImg(imageVO);
			} catch (Exception e) {
				log.error(e.getMessage());
				new Exception();
			}
		}
	}
	
	//섬네일 이미지 가져오기
	@Override
	public AttachVO getThumbnail(int articleNo) {
		AttachVO imageVO = attachMapper.getThumbnail(articleNo);
		return imageVO;
	}
	
	//실제파일 삭제하기
	@Override
	public void deleteArticleFile(List<AttachVO> list) {
		if(list == null || list.size() == 0) {
			return;
		}
		StringBuilder fileFullPath = new StringBuilder("C:\\Users\\82104\\Desktop\\spring_ex\\teamproject\\carrotmarket\\src\\main\\webapp\\resources\\");
		for(int a = 0; a<list.size(); a++) {
			Path file = Paths.get(fileFullPath.toString() + list.get(a).getFilePath() + list.get(a).getFileName());
			
			try {
				Files.deleteIfExists(file);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	//모든파일 db에서 지우기
	@Override
	public void deleteArticleAllImage(int id) {
		attachMapper.deleteArticleAllImage(id);
	}
	
	//폴더 날짜별로 정리하기
		private String getFolder() {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			
			Date date = new Date();
			String str = sdf.format(date);
			return str.replace("-", File.separator);
		}

}
