package org.ezen.ex02.service;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.ezen.ex02.domain.ArticleVO;
import org.ezen.ex02.domain.Criteria;
import org.ezen.ex02.domain.AttachVO;
import org.ezen.ex02.mapper.ArticlesMapper;
import org.ezen.ex02.mapper.AttachMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.Setter;
import lombok.extern.log4j.Log4j;
import net.coobird.thumbnailator.Thumbnailator;

@Service
@Log4j
public class ArticlesServiceImpl implements ArticlesService{
	
	@Setter(onMethod_=@Autowired)
	private ArticlesMapper articlesMapper;
	
	@Setter(onMethod_=@Autowired)
	private AttachMapper attachMapper;
	
	//게시글+파일 작성하기
	@Override
	@Transactional
	public int registerArticles(ArticleVO article){
		articlesMapper.registerArticles(article);
		
		int articleId = articlesMapper.getLastId();
		
		
		
		return articleId;
	}
	
	//내정보에서 내 게시글 띄우기
	@Override
	public List<ArticleVO> getMyArticles(int id) {
		List<ArticleVO> list = articlesMapper.getMyArticles(id);
		return list;
	}

	//게시글 가져오기
	@Override
	public ArticleVO getArticle(int id) {
		ArticleVO articleVO = articlesMapper.getArticle(id);
		return articleVO;
	}
	
	//게시글 리스트 불러오기
	@Override
	public List<ArticleVO> getArticles(Criteria cri) {
		List<ArticleVO> list = articlesMapper.getArticles(cri);
		return list;
	}
	
	//내 게시글 예약중,거래완료 정보 넣기
	@Override
	public void setSell(int id, int sell) {
		articlesMapper.setSell(id, sell);
	}

	//게시글 수정
	@Override
	public void modifyArticle(ArticleVO articleVO) {
		articlesMapper.modifyArticle(articleVO);
		
	}	
	//게시글 삭제
	@Override
	public void deleteArticle(int id) {
		articlesMapper.deleteArticle(id);
	}
	
	//게시글 조회 수 수정
	@Override
	public void hitCountModify(int id) {
		articlesMapper.hitCountModify(id);
	}

}
