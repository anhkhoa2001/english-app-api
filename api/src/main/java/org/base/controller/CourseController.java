package org.base.controller;

import lombok.extern.slf4j.Slf4j;
import org.base.config.EnableWrapResponse;
import org.base.dto.course.CourseItemDTO;
import org.base.dto.course.CourseRequest;
import org.base.exception.SystemException;
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

    @PostMapping("/get-courses-public")
    public ResponseEntity getCoursesPublic(@RequestBody CourseRequest request,
                                           @RequestHeader(name = "Authorization", required = false) String token) {
        return ResponseEntity.ok(courseService.getCoursesPublic(request, token));
    }

    @PostMapping("/get-all")
    public ResponseEntity getAll() {
        return ResponseEntity.ok(courseService.getAll());
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

    @GetMapping("/join-course")
    public ResponseEntity joinCourse(@RequestHeader(name = "Authorization") String token,
                                     @RequestParam String courseCode) {
        try {
            courseService.joinCourse(courseCode, token);
            return ResponseEntity.ok("DONE!!");
        } catch (Exception e) {
            e.printStackTrace();
            throw new SystemException("join course failed!!!");
        }
    }
}
