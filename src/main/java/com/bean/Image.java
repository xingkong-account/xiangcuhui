package com.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Image {
    private Integer id;
    private Integer articleId;
    private String url;
    private String description;
    private LocalDateTime createdAt;
}
