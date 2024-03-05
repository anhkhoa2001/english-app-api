package org.base.controller;

import lombok.extern.slf4j.Slf4j;
import org.base.config.EnableWrapResponse;
import org.base.config.IgnoreWrapResponse;
import org.base.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/up-file")
@Slf4j
@EnableWrapResponse
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/upload-to-cloud")
    @IgnoreWrapResponse
    public ResponseEntity uploadToCloud(@RequestPart MultipartFile file) throws IOException {
        return ResponseEntity.ok(fileService.saveFileToCloud(file.getOriginalFilename(), file.getInputStream(), file.getSize(), null));
    }

    @PostMapping("/get-all")
    @IgnoreWrapResponse
    public ResponseEntity getAll() throws IOException {
        return ResponseEntity.ok(fileService.getAll());
    }
}
