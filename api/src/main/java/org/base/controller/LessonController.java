package org.base.controller;

import lombok.extern.slf4j.Slf4j;
import org.base.config.EnableWrapResponse;
import org.base.dto.lesson.LessonDTO;
import org.base.model.LessonModel;
import org.base.services.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/lesson")
@Slf4j
@EnableWrapResponse
public class LessonController {

    @Autowired
    private LessonService lessonService;

    @PostMapping("/create")
    public ResponseEntity create(@RequestBody LessonDTO dto) {
        return ResponseEntity.ok(lessonService.create(dto));
    }

    @GetMapping("/get-by-lesson-id")
    public ResponseEntity getByLessonId(@RequestParam Integer lessonId) {
        return ResponseEntity.ok(lessonService.getByLessonId(lessonId));
    }
}
