package dev.FCAI.LMS_Spring.service;

import org.springframework.core.io.UrlResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;

@Service
public class FileStorageService {

    private final Path rootLocation = Paths.get("uploads");

    public FileStorageService() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload dir!", e);
        }
    }

    public String storeFile(MultipartFile file, String subdirectory) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if (filename.contains("..")) {
                throw new RuntimeException("Invalid file path");
            }
            Path dirPath = this.rootLocation.resolve(subdirectory);
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }
            Path targetLocation = dirPath.resolve(filename);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return targetLocation.toString();
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }

    public Resource loadFileAsResource(String filePath) {
        try {
            Path file = Paths.get(filePath);
            Resource resource = new UrlResource(file.toUri());
            if(resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read file: " + filePath);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Could not load file: " + filePath, e);
        }
    }
}