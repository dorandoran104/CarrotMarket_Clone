package org.ezen.ex02.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.ezen.ex02.domain.AttachVO;

@Mapper
public interface AttachMapper {

	void registerImg(AttachVO imageVO);
	
	List<AttachVO> getArticleImage(int id);
	
	void registerThumbnail(AttachVO thumbnail);

	AttachVO getThumbnail(int articleNo);

	void deleteArticleAllImage(int id);
}
