package org.ezen.ex02.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.ezen.ex02.domain.AttachVO;
import org.ezen.ex02.mapper.AttachMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.Setter;

@Service
public class AttachServiceImpl implements AttachService{
	
	@Setter(onMethod_=@Autowired)
	private AttachMapper attachMapper;
	
	//게시글 이미지 정보 가져오기
	@Override
	public List<AttachVO> getArticleImage(int id) {
		List<AttachVO> list = attachMapper.getArticleImage(id);
		return list;
	}
	
	//섬네일 이미지 가져오기
	@Override
	public AttachVO getThumbnail(int articleNo) {
		AttachVO imageVO = attachMapper.getThumbnail(articleNo);
		return imageVO;
	}
	
	//실제파일 삭제하기
	@Override
	public void deleteArticleImage(List<AttachVO> list) {
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
	//파일 db에서 지우기
	@Override
	public void deleteArticleAllImage(int id) {
		attachMapper.deleteArticleAllImage(id);
	}
}
