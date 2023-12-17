package org.base.controllers;

import org.base.dto.common.EFile;
import org.base.service.AzureBlobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/course")
public class CourseController {

    @Autowired
    private AzureBlobService azureBlobService;

    @GetMapping
    public ResponseEntity getAllCourse() {
        return ResponseEntity.ok(azureBlobService.listBlobs());
    }

    @GetMapping("/get-in-file-share")
    public ResponseEntity getInFileShare() {
        return ResponseEntity.ok(azureBlobService.listFileShares());
    }

    @PostMapping("/upload")
    public ResponseEntity uploadFile(@RequestBody EFile file) {
        return ResponseEntity.ok(azureBlobService.upload(file));
    }
}
