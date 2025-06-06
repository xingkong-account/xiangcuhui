package com.project.service;

import com.project.bean.Advertisement;
import com.project.bean.Image;
import com.project.mapper.ImageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImageService {

    @Autowired
    private ImageMapper imageMapper;

    public void insertAdvertisement(Advertisement advertisement) {
        imageMapper.insertAdvertisement(advertisement);
    }

    public List<Advertisement> getAllAdvertisements(){
        return imageMapper.selectAllAdvertisements();
    }
    public void deleteAdvertisement(int adId) {
        imageMapper.deleteAdvertisement(adId);
    }
}
