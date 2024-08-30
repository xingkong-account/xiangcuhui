package com.service;

import com.bean.Article;
import com.bean.Image;
import com.bean.PageResult;
import com.mapper.ArticleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ImageService imageService;

    public List<Article> getAllArticles() {
        return articleMapper.getAllArticles();
    }

    public Article findById(Integer id) {
        Article article = articleMapper.findById(id);
        if (article != null) {
            // 获取与文章关联的图片
            List<Image> images = imageService.findByArticleId(id);
            article.setImages(images);
        }
        return article;
    }

    public Article insert(Article article) {
        // 插入文章
        article.setCreated_at(LocalDateTime.now());
        articleMapper.insert(article);
        return article;
    }

    public void update(Article article) {
        article.setUpdated_at(LocalDateTime.now());
        articleMapper.update(article);
    }

    public void deleteArticle(Integer id) {
        articleMapper.delete(id);
    }

    // 文章浏览次数
    public void incrementViews(Integer id) {
        articleMapper.incrementViews(id);
    }

    public void approveArticle(Integer id){
        articleMapper.approveArticle(id, "已审核",LocalDateTime.now());
    }

    public void rejectArticle(Integer id){
        articleMapper.approveArticle(id, "已拒绝",LocalDateTime.now());
    }

    public List<Article> getArticlesByCategory(String category) {
        if (category == null || category.isEmpty()) {
            return articleMapper.getAllArticles();
        }
        return articleMapper.findArticlesByCategory(category);
    }

    public List<Article> getLatestArticles() {
        return articleMapper.selectLatestArticles();
    }

    public List<Article> getPopularArticles() {
        return articleMapper.selectPopularArticles();
    }

    public PageResult<Article> getAllArticles(int pageNum, int pageSize) {
        int offset = (pageNum - 1) * pageSize; // 计算偏移量
        List<Article> articles = articleMapper.findAll(offset, pageSize);
        int total = articleMapper.countAll(); // 获取总记录数
        return new PageResult<>(articles, total, pageNum, pageSize);
    }
}
