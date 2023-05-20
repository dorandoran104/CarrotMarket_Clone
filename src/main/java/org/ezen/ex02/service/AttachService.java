package org.ezen.ex02.service;

import java.util.List;

import org.ezen.ex02.domain.AttachVO;
import org.springframework.web.multipart.MultipartFile;

public interface AttachService {

	List<AttachVO> getArticleImage(int articleNo);

	AttachVO getThumbnail(int articleNo);

	void deleteArticleFile(AttachVO attachVO);

	void deleteArticleAllImage(int id);

	void insertImg(MultipartFile[] files, int articleNo);

	void insertDBImg(AttachVO[] attachVO, int id);

	void deleteArticleImageDB(String fileName);
}
