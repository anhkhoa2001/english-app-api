package org.base.controller;

import org.base.config.EnableWrapResponse;
import org.base.dto.exam.ExamDTO;
import org.base.model.exam.ExamModel;
import org.base.services.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/exam")
@EnableWrapResponse
public class ExamController {

    @Autowired
    private ExamService examService;

    @PostMapping("/create")
    public ResponseEntity create(@RequestBody ExamDTO request) {
        return ResponseEntity.ok(examService.create(request));
    }

    @GetMapping("/get-all")
    public ResponseEntity getAll() {
        return ResponseEntity.ok(examService.getAll());
    }

    @GetMapping("/get-by-code")
    public ResponseEntity getByCode(@RequestParam String code) {
        return ResponseEntity.ok(examService.getByCode(code));
    }


    @PostMapping("/update")
    public ResponseEntity update(@RequestBody ExamDTO request) {
        return ResponseEntity.ok(examService.update(request));
    }

    @GetMapping("/delete")
    public ResponseEntity delete(@RequestParam String examCode) {
        examService.delete(examCode);
        return ResponseEntity.ok("DONE!!!");
    }
}
