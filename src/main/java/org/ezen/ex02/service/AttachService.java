package org.ezen.ex02.service;

import java.util.List;

import org.ezen.ex02.domain.AttachVO;
import org.springframework.web.multipart.MultipartFile;

public interface AttachService {

	List<AttachVO> getArticleImage(int articleNo);

	AttachVO getThumbnail(int articleNo);

	void deleteArticleFile(List<AttachVO> list);

	void deleteArticleAllImage(int id);

	void insertImg(MultipartFile[] files, int articleNo);

}
