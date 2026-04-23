package koboolean.springproject.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public interface UploadService {
    Map<String, List<String>> fileUpload(List<MultipartFile> files);

    Resource getUploadedFile(Path path);

    Path getPath(String storedFilename);

    String getContentType(Path path);
}
