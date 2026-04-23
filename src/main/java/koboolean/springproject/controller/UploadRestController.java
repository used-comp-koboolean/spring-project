package koboolean.springproject.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@Slf4j
public class UploadRestController {

    @Value("${file.upload.path}")
    private String uploadPath;

    @PostMapping(value = "/api/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, List<String>>> fileUpload(@RequestParam("file") List<MultipartFile> files) {
        if (files == null || files.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("urls", List.of()));
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
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            log.error("file upload failed", e);
            return ResponseEntity.internalServerError().body(Map.of("urls", List.of()));
        }
    }

    @GetMapping("/api/files/{storedFilename}")
    public ResponseEntity<Resource> getUploadedFile(@PathVariable String storedFilename) {
        if (storedFilename.contains("..") || storedFilename.contains("/") || storedFilename.contains("\\")) {
            return ResponseEntity.badRequest().build();
        }

        Path targetDirectory = Paths.get(uploadPath).toAbsolutePath().normalize();
        Path filePath = targetDirectory.resolve(storedFilename).normalize();

        if (!filePath.startsWith(targetDirectory) || !Files.exists(filePath) || !Files.isRegularFile(filePath)) {
            return ResponseEntity.notFound().build();
        }

        try {
            Resource resource = new UrlResource(filePath.toUri());
            String contentType = Files.probeContentType(filePath);

            if (contentType == null || contentType.isBlank()) {
                contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(resource);
        } catch (MalformedURLException e) {
            log.error("invalid file url, file={}", storedFilename, e);
            return ResponseEntity.notFound().build();
        } catch (IOException e) {
            log.error("failed to read uploaded file, file={}", storedFilename, e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
