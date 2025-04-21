package ru.kolpakovee.userservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.kolpakovee.userservice.services.S3Service;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/s3")
public class S3Controller {

    private final S3Service s3ObjectService;

    @PostMapping("/file/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        return s3ObjectService.uploadFile(file);
    }

    @GetMapping("/file/get/{key}")
    public String getFile(@PathVariable String key) {
        return s3ObjectService.getBase64File(key);
    }
}
