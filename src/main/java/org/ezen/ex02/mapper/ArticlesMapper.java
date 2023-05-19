package org.ezen.ex02.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.ezen.ex02.domain.ArticleVO;
import org.ezen.ex02.domain.Criteria;

@Mapper
public interface ArticlesMapper {

	void registerArticles(ArticleVO article);

	int getLastId();

	ArticleVO getArticle(int id);

	List<ArticleVO> getArticles(Criteria cri);

	List<ArticleVO> getMyArticles(int id);

	void setSell(@Param("id") int id,@Param("sell") int sell);

	void modifyArticle(ArticleVO articleVO);

	void deleteArticle(int id);

	void hitCountModify(int id);
}
