package com.project.controller;

import com.project.bean.Advertisement;
import com.project.mapper.ImageMapper;
import com.project.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/advertisement")
// 广告服务
public class ImageController {
    @Autowired
    private ImageService imageService;

    // 插入广告信息
    @PostMapping("/insert")
    public ResponseEntity<?> insertAdvertisement(@RequestBody Map<String, String> request) {
        String imageUrl = request.get("image_url");
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Advertisement advertisement = new Advertisement();
            advertisement.setImageUrl(imageUrl);
            imageService.insertAdvertisement(advertisement);
            return ResponseEntity.ok(Collections.singletonMap("success", true));
        }

        return ResponseEntity.badRequest().body(Collections.singletonMap("success", false));
    }

    // 获取所有广告
    @GetMapping("/all")
    public ResponseEntity<?> getAllAdvertisements() {
        List<Advertisement> advertisements = imageService.getAllAdvertisements();
        return ResponseEntity.ok(advertisements);
    }

    // 删除广告
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAdvertisement(@PathVariable("id") int id) {
        try {
            imageService.deleteAdvertisement(id);
            return ResponseEntity.ok(Collections.singletonMap("success", true));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Collections.singletonMap("success", false));
        }
    }
}
