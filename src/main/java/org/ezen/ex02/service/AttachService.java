package org.ezen.ex02.service;

import java.util.List;

import org.ezen.ex02.domain.ImageVO;

public interface AttachService {

	List<ImageVO> getArticleImage(int articleNo);

	ImageVO getThumbnail(int articleNo);

}
