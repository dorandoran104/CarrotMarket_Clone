package org.ezen.ex02.service;

import java.util.List;

import org.ezen.ex02.domain.AttachVO;

public interface AttachService {

	List<AttachVO> getArticleImage(int articleNo);

	AttachVO getThumbnail(int articleNo);

	void deleteArticleImage(List<AttachVO> list);

	void deleteArticleAllImage(int id);

}
