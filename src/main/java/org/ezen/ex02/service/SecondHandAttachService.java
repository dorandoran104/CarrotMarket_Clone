package org.ezen.ex02.service;

import java.util.List;

import org.ezen.ex02.domain.SecondHandAttachVO;
import org.springframework.web.multipart.MultipartFile;

public interface SecondHandAttachService {

	List<SecondHandAttachVO> getArticleImage(int articleNo);

	SecondHandAttachVO getThumbnail(int articleNo);

	void deleteArticleFile(SecondHandAttachVO attachVO);

	void deleteArticleAllImage(int id);

	void insertImg(MultipartFile[] files, int articleNo);

	void insertDBImg(SecondHandAttachVO[] attachVO, int id);

	void deleteArticleImageDB(String fileName);
}
