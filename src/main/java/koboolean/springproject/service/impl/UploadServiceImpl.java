package koboolean.springproject.service.impl;

import koboolean.springproject.service.UploadService;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Service
@Slf4j
public class UploadServiceImpl implements UploadService {

    @Value("${file.upload.path}")
    private String uploadPath;


    @Override
    public Map<String, List<String>> fileUpload(List<MultipartFile> files) {
        if (files == null || files.isEmpty()) {
            // return ResponseEntity.badRequest().body(Map.of("urls", List.of()));
            throw new RuntimeException();
        }

        Path targetDirectory = Paths.get(uploadPath).toAbsolutePath().normalize();
        List<String> urls = new ArrayList<>();

        try {
            Files.createDirectories(targetDirectory);

            for (MultipartFile file : files) {
                if (file == null || file.isEmpty()) {
                    continue;
                }

                String originalFilename = file.getOriginalFilename();
                String extension = "";
                if (originalFilename != null && originalFilename.contains(".")) {
                    extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
                }

                String storedFilename = UUID.randomUUID() + extension;
                Path storedPath = targetDirectory.resolve(storedFilename);

                Files.copy(file.getInputStream(), storedPath, StandardCopyOption.REPLACE_EXISTING);
                String imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/api/files/")
                        .path(storedFilename)
                        .toUriString();
                urls.add(imageUrl);
            }

            log.info("uploaded file urls={}", urls);
            Map<String, List<String>> response = new HashMap<>();
            response.put("urls", urls);
            return response;
        } catch (IOException e) {
            log.error("file upload failed", e);
            // return ResponseEntity.internalServerError().body(Map.of("urls", List.of()));
            throw new RuntimeException();
        }
    }

    @Override
    public Resource getUploadedFile(Path filePath) {

        try {
            Resource resource = new UrlResource(filePath.toUri());

            return resource;
        } catch (MalformedURLException e) {
            log.error("invalid file url, file={}", filePath, e);
            //return ResponseEntity.notFound().build();
            throw new RuntimeException();
        } catch (IOException e) {
            log.error("failed to read uploaded file, file={}", filePath, e);
            //return ResponseEntity.internalServerError().build();
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getContentType(Path filePath) {
        String contentType = null;
        try {
            contentType = Files.probeContentType(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (contentType == null || contentType.isBlank()) {
            contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }

        return contentType;
    }

    @Override
    public Path getPath(String storedFilename) {
        if (storedFilename.contains("..") || storedFilename.contains("/") || storedFilename.contains("\\")) {
            //return ResponseEntity.badRequest().build();
            throw new RuntimeException();
        }

        Path targetDirectory = Paths.get(uploadPath).toAbsolutePath().normalize();
        Path filePath = targetDirectory.resolve(storedFilename).normalize();

        if (!filePath.startsWith(targetDirectory) || !Files.exists(filePath) || !Files.isRegularFile(filePath)) {
            //return ResponseEntity.notFound().build();
            throw new RuntimeException();
        }
        return filePath;
    }
}
