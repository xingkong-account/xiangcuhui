package com.project.controller;

import com.project.bean.Article;
import com.project.bean.PageResult;
import com.project.service.ArticleService;
import com.project.service.ImageService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.IOException;
import java.util.*;

@CrossOrigin
@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ImageService imageService;

    private static final String ROOT_PATH = System.getProperty("user.dir") + File.separator + "files";

    // 获取所有文章
    @GetMapping("/articleList")
    public PageResult<Article> getAllArticles(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        return articleService.getAllArticles(pageNum, pageSize);
    }

    // 根据作者名获取文章
    @GetMapping("/user-articles")
    public PageResult<Article> getAllArticlesByName(
            @RequestParam("author") String author,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        return articleService.getAllArticlesByName(author, pageNum, pageSize);
    }

    // 根据ID获取文章
    @GetMapping("/{id}")
    public ResponseEntity<Article> getArticleById(@PathVariable Integer id) {
        Article article = articleService.findById(id);
        if (article == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(article);
    }

    // 添加新文章
    @PostMapping("/add")
    public ResponseEntity<Article> addArticle(@RequestBody Article article) {
        Article savedArticle = articleService.insert(article);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedArticle);
    }

    // 更新文章
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateArticle(@PathVariable Integer id, @RequestBody Article article) {
        if (articleService.findById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        article.setId(id);
        articleService.update(article);
        return ResponseEntity.noContent().build();
    }

    // 删除文章
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Integer id) {
        if (articleService.findById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        imageService.deleteByArticleId(id); // 删除关联的图片
        articleService.deleteArticle(id);
        return ResponseEntity.noContent().build();
    }

    // 文章浏览次数
    @PostMapping("/{id}/increment-views")
    public ResponseEntity<Void> incrementViews(@PathVariable Integer id) {
        articleService.incrementViews(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<String> approveArticle(@PathVariable("id") int id) {
        try {
            articleService.approveArticle(id);
            return ResponseEntity.ok("审核通过");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("审核失败");
        }
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<String> rejectArticle(@PathVariable("id") int id) {
        try {
            articleService.rejectArticle(id);
            return ResponseEntity.ok("文章已拒绝");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("审核失败");
        }
    }

    @GetMapping
    public List<Article> getArticlesByCategory(@RequestParam(value = "category", defaultValue = "") String category) {
        return articleService.getArticlesByCategory(category);
    }

    // 获取最新文章
    @GetMapping("/latest")
    public ResponseEntity<List<Article>> getLatestArticles() {
        List<Article> latestArticles = articleService.getLatestArticles();
        return ResponseEntity.ok(latestArticles);
    }

    // 获取最热文章
    @GetMapping("/popular")
    public ResponseEntity<List<Article>> getPopularArticles() {
        List<Article> popularArticles = articleService.getPopularArticles();
        return ResponseEntity.ok(popularArticles);
    }

    @Data
    private static class ErrorResponse {
        private String message;

        public ErrorResponse(String message) {
            this.message = message;
        }
    }


    @Value("${ip:localhost}")
    String ip;

    @Value("${server.port}")
    String port;
//    @PostMapping("/uploadImage")
//    public Map<String, Object> uploadImage(@RequestParam("file") MultipartFile file, @RequestParam String type) throws IOException {
//        String originalFilename = file.getOriginalFilename();
//        File uploadDirFile = new File(ROOT_PATH);
//        if (!uploadDirFile.exists()) {
//            uploadDirFile.mkdirs();
//        }
//        String mainName = originalFilename.split("\\.")[0];
//        String extName = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf('.') + 1) : "";
//        File saveFile = new File(ROOT_PATH + File.separator + originalFilename);
//        file.transferTo(saveFile);
//
//        String url = "http://" + ip + ":" + port + "/" + originalFilename;
//        if ("img".equals(type)){
//            Map<String, Object> response = new HashMap<>();
//            response.put("errno", 0);
//            response.put("data", new HashMap<String, String>() {{
//                put("url", url);
//            }});
//            return response;
//        }
//        Map<String, Object> response = new HashMap<>();
//        response.put("errno", 0);
//        response.put("data", new HashMap<String, String>() {{
//            put("url", url);
//        }});
//
//        return response;
//    }

    @PostMapping("/uploadImage")
    public Map<String, Object> uploadImage(@RequestParam("file") MultipartFile file, @RequestParam String type) throws IOException {
        String originalFilename = file.getOriginalFilename();
        File uploadDirFile = new File(ROOT_PATH);
        if (!uploadDirFile.exists()) {
            uploadDirFile.mkdirs();
        }
        String mainName = originalFilename.split("\\.")[0];
        String extName = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf('.') + 1) : "";
        File saveFile = new File(ROOT_PATH + File.separator + originalFilename);
        file.transferTo(saveFile);

       // 访问图片文件的地址
        String url = "http://" + ip + ":" + port + "/" + originalFilename;
        Map<String, Object> response = new HashMap<>();

        if ("img".equals(type)) {
            response.put("errno", 0);
            response.put("data", new HashMap<String, String>() {{
                put("url", url);
            }});
        } else if ("video".equals(type)) {
            response.put("errno", 0);
            response.put("data", new HashMap<String, String>() {{
                put("url", url);
            }});
        } else {
            response.put("errno", 1); // 表示错误
            response.put("message", "不支持的文件类型"); // 不支持的文件类型
        }

        return response;
    }
}
