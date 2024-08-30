package com.controller;

import com.bean.Article;
import com.bean.Video;
import com.service.FileService;
import com.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/videos")
public class VideoController {

    @Autowired
    private VideoService videoService;

    @Autowired
    private FileService fileService;

    // 上传视频
    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadVideo(@RequestParam("file") MultipartFile file) {
        try {
            String fileUrl = fileService.saveFile(file);
            // 构建响应数据
            Map<String, String> response = new HashMap<>();
            response.put("url", fileUrl);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<Video> addArticle(@RequestBody Video video) {
        Video savedVideo = videoService.insert(video);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedVideo);
    }

    // 审核视频（通过审核）
    @PostMapping("/{id}/approve")
    public ResponseEntity<String> approveVideo(@PathVariable("id") int id) {
        try {
            videoService.approveVideo(id);
            return ResponseEntity.ok("审核通过");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("审核失败");
        }
    }

    // 拒绝
    @PostMapping("/{id}/reject")
    public ResponseEntity<String> rejectVideo(@PathVariable("id") int id) {
        try {
            videoService.rejectVideo(id);
            return ResponseEntity.ok("视频审核未通过");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("审核失败");
        }
    }

    // 获取视频详情
    @GetMapping("/{id}")
    public Video getVideoById(@PathVariable Integer id) {
        return videoService.getVideoById(id);
    }

    // 获取所有视频（已审核的视频）
    @GetMapping("/all")
    public List<Video> getAllVideos() {
        return videoService.getAllVideos();
    }

    // 获取待审核的视频
    @GetMapping("/review-video")
    public List<Video> getAllUnreviewVideos() {
        return videoService.getUnReviewedVideos();
    }

    // 删除视频
    @DeleteMapping("/{id}")
    public void deleteVideo(@PathVariable Integer id) {
        videoService.deleteVideo(id);
    }
}
