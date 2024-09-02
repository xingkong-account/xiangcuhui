package com.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileService {

    @Value("${server.port}")
    private String serverPort;

    private static final String ROOT_PATH = System.getProperty("user.dir") + File.separator + "files";

    @Value("${ip:localhost}")
    private String ACCESS_HOST;

    private String getAccessUrlPrefix() {
        return "http://" + ACCESS_HOST + ":" + serverPort;
    }

    public String saveFile(MultipartFile file) throws IOException {
        // 创建一个唯一的文件名，避免文件名冲突
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path uploadPath = Paths.get(ROOT_PATH);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath);
        // 返回文件的访问URL
        return getAccessUrlPrefix() + "/" + fileName;
    }
}
