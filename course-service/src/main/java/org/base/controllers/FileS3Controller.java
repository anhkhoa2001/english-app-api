package org.base.controllers;

import lombok.extern.slf4j.Slf4j;
import org.base.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
public class FileS3Controller {


    @Autowired
    private CourseService courseService;

    @GetMapping("/get-file")
    public ResponseEntity getFile(@RequestParam String filename) {
        return ResponseEntity.ok(courseService.findByName(filename));
    }

    @PostMapping("/save-file")
    public ResponseEntity saveFile(@RequestBody MultipartFile file) {
        return ResponseEntity.ok(courseService.save(file));
    }
}
