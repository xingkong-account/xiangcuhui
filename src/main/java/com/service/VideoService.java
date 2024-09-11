package com.service;

import com.bean.Video;
import com.mapper.VideoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
public class VideoService {

    @Autowired
    private VideoMapper videoMapper;

    public void uploadVideo(Video video) {
        videoMapper.insertVideo(video);
    }

    public void reviewVideo(Integer id, String status, String reviewer) {
        Video video = videoMapper.getVideoById(id);
        if (video != null) {
            video.setStatus(status);
            video.setReviewer(reviewer);
            video.setReviewed_at(java.time.LocalDateTime.now());
            videoMapper.updateVideo(video);
        }
    }

    public Video getVideoById(Integer id) {
        return videoMapper.getVideoById(id);
    }

    public List<Video> getAllVideos() {
        return videoMapper.getAllVideos();
    }

    public List<Video> getAllCheckedVideos() {
        return videoMapper.getAllCheckedVideos();
    }

    public List<Video> getPendingVideos() {
        return videoMapper.getVideosByStatus("待审核");
    }

    public void deleteVideo(Integer id) {
        videoMapper.deleteVideo(id);
    }

    // 待审核的视频列表
    public List<Video> getUnReviewedVideos(){
        return videoMapper.getAllUnReviewVideos();
    }

    // 插入视频信息
    public Video insert(Video video){
        video.setCreated_at(LocalDateTime.now());
        videoMapper.insertVideo(video);
        return video;
    }

    public void approveVideo(Integer id, String reviewer) {
        videoMapper.reviewVideo(id, "已审核", LocalDateTime.now(), LocalDateTime.now(),reviewer);
    }

    public void rejectVideo(Integer id, String reviewer) {
        videoMapper.reviewVideo(id, "已拒绝", LocalDateTime.now(), LocalDateTime.now(),reviewer);
    }


    private String getCurrentUsername() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            return (String) attributes.getRequest().getSession().getAttribute("username");
        }
        return "admin";
   }

    public List<Video> searchVideos(String query) {
        if (query == null || query.trim().isEmpty()) {
            return Collections.emptyList();
        }
        if (query.length() > 255) {
            throw new IllegalArgumentException("查询内容过长");
        }
        try {
            return videoMapper.searchVideos(query.trim());
        } catch (Exception e) {
            throw new RuntimeException("搜索视频失败，请稍后重试", e);
        }
    }

    public List<Video> getVideosByUsername(String uploader) {
        return videoMapper.findVideosByUsername(uploader);
    }

    public void updateVideo(Video video) {
        videoMapper.updateVideo(video);
    }
}
