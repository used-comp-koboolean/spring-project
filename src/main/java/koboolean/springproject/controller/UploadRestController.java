package koboolean.springproject.controller;

import koboolean.springproject.service.UploadService;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class UploadRestController {

    private final UploadService uploadService;

    @PostMapping(value = "/api/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, List<String>>> fileUpload(@RequestParam("file") List<MultipartFile> files) {
        return ResponseEntity.ok(uploadService.fileUpload(files));
    }

    @GetMapping("/api/files/{storedFilename}")
    public ResponseEntity<Resource> getUploadedFile(@PathVariable String storedFilename) {

        Path path = uploadService.getPath(storedFilename);

        String contentType = uploadService.getContentType(path);
        Resource uploadedFile = uploadService.getUploadedFile(path);

        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).body(uploadedFile);
    }
}
