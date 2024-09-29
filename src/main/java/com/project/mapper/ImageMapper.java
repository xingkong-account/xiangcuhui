package com.project.mapper;

import com.project.bean.Image;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface ImageMapper {
    List<Image> findByArticleId(Integer articleId);
    void insert(Image image);
    void deleteByArticleId(Integer articleId);
}
