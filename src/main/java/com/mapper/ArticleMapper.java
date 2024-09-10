package com.mapper;

import com.bean.Article;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ArticleMapper {
    List<Article> findAll(@Param("offset") int offset, @Param("limit") int limit);
    int countAll();
    List<Article> findAllByName(@Param("author") String author, @Param("offset") int offset, @Param("limit") int limit);
    int countAllByName(@Param("author") String author);
    Article findById(Integer id);
    void insert(Article article);
    void update(Article article);
    void delete(Integer id);
    void incrementViews(Integer id);
    void approveArticle(@Param("id") int id, @Param("status") String status, @Param("updated_at") LocalDateTime updated_at);
    List<Article> findArticlesByCategory(String category);
    // 获取最新文章
    List<Article> selectLatestArticles();

    // 获取最热文章
    List<Article> selectPopularArticles();

    List<Article> getAllArticles();
}
