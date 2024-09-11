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

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
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

    // 根据视频上传者的名字获取视频信息
    @GetMapping("/my-videos")
    public ResponseEntity<List<Video>> getMyVideos(@RequestParam("uploader") String uploader) {
        List<Video> videos = videoService.getVideosByUsername(uploader);
        return ResponseEntity.ok(videos);
    }


    @PostMapping("/update")
    public ResponseEntity<String> updateVideo(@RequestBody Video video) {
        try {
            video.setUpdated_at(LocalDateTime.now());
            videoService.updateVideo(video);
            return ResponseEntity.ok("视频更新成功！");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("视频更新失败！");
        }
    }

    @PostMapping("/add")
    public ResponseEntity<Video> addArticle(@RequestBody Video video) {
        Video savedVideo = videoService.insert(video);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedVideo);
    }

    // 审核视频（通过审核）
    @PostMapping("/{id}/approve")
    public ResponseEntity<String> approveVideo(
            @PathVariable("id") int id,
            @RequestBody Map<String, Object> requestBody) {
        try {
            String reviewer = (String) requestBody.get("reviewer");
            videoService.approveVideo(id, reviewer);
            return ResponseEntity.ok("审核通过");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("审核失败");
        }
    }

    // 拒绝
    @PostMapping("/{id}/reject")
    public ResponseEntity<String> rejectVideo(
            @PathVariable("id") int id,
            @RequestBody Map<String, Object> requestBody) {
        try {
            String reviewer = (String) requestBody.get("reviewer");
            videoService.rejectVideo(id, reviewer);
            return ResponseEntity.ok("视频已拒绝");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("审核失败");
        }
    }

    // 搜索
    @GetMapping("/search")
    public ResponseEntity<List<Video>> searchVideos(@RequestParam String query) {
        try {
            List<Video> videos = videoService.searchVideos(query);
            return new ResponseEntity<>(videos, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 获取视频详情
    @GetMapping("/{id}")
    public Video getVideoById(@PathVariable Integer id) {
        return videoService.getVideoById(id);
    }

    // 获取所有视频
    @GetMapping("/all")
    public List<Video> getAllVideos() {
        return videoService.getAllVideos();
    }

    @GetMapping("/all-checked")
    public List<Video> getAllCheckedVideos() {
        return videoService.getAllCheckedVideos();
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
