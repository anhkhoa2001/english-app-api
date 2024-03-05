package org.base.controller;

import lombok.extern.slf4j.Slf4j;
import org.base.config.EnableWrapResponse;
import org.base.dto.course.CourseItemDTO;
import org.base.dto.course.CourseRequest;
import org.base.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/course")
@Slf4j
@EnableWrapResponse
public class CourseController {

    @Autowired
    private CourseService courseService;


    @PostMapping("/get-all")
    public ResponseEntity getAll(@RequestBody CourseRequest request) {
        return ResponseEntity.ok(courseService.getAll(request));
    }

    @PostMapping("/create")
    public ResponseEntity createCourse(@RequestBody CourseItemDTO item) {
        return ResponseEntity.ok(courseService.create(item));
    }

    @PostMapping("/update")
    public ResponseEntity updateCourse(@RequestBody CourseItemDTO item) {
        return ResponseEntity.ok(courseService.update(item));
    }

    @GetMapping("/get-by-code")
    public ResponseEntity getByCode(@RequestParam String code) {
        return ResponseEntity.ok(courseService.getByCode(code));
    }
}
