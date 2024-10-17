package com.project.mapper;

import com.project.bean.Advertisement;
import com.project.bean.Image;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ImageMapper {
    int insertAdvertisement(Advertisement advertisement);
    List<Advertisement> selectAllAdvertisements();
    void deleteAdvertisement(int adId);

}
