package org.ezen.ex02.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.ezen.ex02.domain.ArticleVO;
import org.ezen.ex02.domain.Criteria;
import org.ezen.ex02.domain.ImageVO;

@Mapper
public interface ArticlesMapper {

	int registerArticles(ArticleVO article);

	int getLastId();

	ArticleVO getArticle(int id);

	List<ArticleVO> getArticles(Criteria cri);
}
