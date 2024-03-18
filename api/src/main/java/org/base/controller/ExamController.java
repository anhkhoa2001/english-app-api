package org.base.controller;

import lombok.extern.slf4j.Slf4j;
import org.base.config.EnableWrapResponse;
import org.base.dto.exam.ExamDTO;
import org.base.dto.exam.ExamRequest;
import org.base.exception.SystemException;
import org.base.model.exam.ExamModel;
import org.base.services.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/exam")
@EnableWrapResponse
@Slf4j
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

    @GetMapping("/delete-part")
    public ResponseEntity deletePart(@RequestParam String examCode, @RequestParam int partId) {
       try {
           examService.deletePart(examCode, partId);
           return ResponseEntity.ok("DONE!!!");
       } catch (Exception e) {
           log.error("Failed when delete part in exam {} {}", e.getClass(), e.getMessage());
           throw new SystemException(e.getMessage());
       }
    }

    @PostMapping("/get-all-exam")
    public ResponseEntity getAllExam(@RequestBody ExamRequest request) {
        return ResponseEntity.ok(examService.getAllExam(request));
    }
}
