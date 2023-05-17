package org.ezen.ex02.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.ezen.ex02.domain.ImageVO;

@Mapper
public interface AttachMapper {

	void registerImg(ImageVO imageVO);
	
	List<ImageVO> getArticleImage(int id);
	
	void registerThumbnail(ImageVO thumbnail);

	ImageVO getThumbnail(int articleNo);
}
