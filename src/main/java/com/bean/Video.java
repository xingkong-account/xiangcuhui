package com.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Video {
    private Integer id;
    private String title;
    private String url;
    private String description;
    private String thumbnail;         // 视频缩略图的URL地址
    private String status;            // 视频的审核状态（待审核、已审核、已拒绝）
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private LocalDateTime reviewed_at; // 视频被审核的时间
    private String reviewer;          // 审核人的名字或ID
    private String uploader;
}
