package org.ezen.ex02.service;

import java.util.List;

import org.ezen.ex02.domain.ImageVO;
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
	public List<ImageVO> getArticleImage(int id) {
		List<ImageVO> list = attachMapper.getArticleImage(id);
		return list;
	}
	
	//섬네일 이미지 가져오기
	@Override
	public ImageVO getThumbnail(int articleNo) {
		ImageVO imageVO = attachMapper.getThumbnail(articleNo);
		return imageVO;
	}
	

}
