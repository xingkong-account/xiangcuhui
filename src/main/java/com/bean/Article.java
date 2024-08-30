package com.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Article {
    private Integer id;
    private String title;
    private String content;
    private String category;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private List<Image> images;  // 文章关联的图片
    private List<Video> videos;  // 文章关联的视频

    // 新增字段
    private String author;        // 文章作者
    private String source;        // 信息来源
    private Integer views;        // 浏览次数
    private FontSize fontSize;    // 字体大小，使用枚举类型
    private String status;        // 文章审核状态

    public enum FontSize {
        大, 中, 小
    }
}