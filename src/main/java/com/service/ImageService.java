package com.service;

import com.bean.Image;
import com.mapper.ImageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImageService {

    @Autowired
    private ImageMapper imageMapper;

    public List<Image> findByArticleId(Integer articleId) {
        return imageMapper.findByArticleId(articleId);
    }

    public void insert(Image image) {
        imageMapper.insert(image);
    }

    public void deleteByArticleId(Integer articleId) {
        imageMapper.deleteByArticleId(articleId);
    }
}
