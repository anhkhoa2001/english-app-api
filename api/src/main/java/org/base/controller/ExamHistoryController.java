package org.base.controller;

import lombok.extern.slf4j.Slf4j;
import org.base.config.EnableWrapResponse;
import org.base.dto.exam.ExamHistoryDTO;
import org.base.services.ExamHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/exam-history")
@EnableWrapResponse
@Slf4j
public class ExamHistoryController {

    private final ExamHistoryService examHistoryService;

    @Autowired
    public ExamHistoryController(ExamHistoryService examHistoryService) {
        this.examHistoryService = examHistoryService;
    }


    @GetMapping
    public ResponseEntity getAll(@RequestParam(required = false) Integer page,
                                 @RequestParam(required = false) Integer pageSize,
                                 @RequestParam String examCode,
                                 @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(examHistoryService.getAll(token, examCode, page, pageSize));
    }

    @PostMapping("/get-by-condition")
    public ResponseEntity getByCondition(@RequestBody ExamHistoryDTO request) {
        return ResponseEntity.ok(examHistoryService.getByCondition(request));
    }

    @GetMapping("/get-by-id")
    public ResponseEntity getById(@RequestParam Integer historyId) {
        return ResponseEntity.ok(examHistoryService.getByHistoryId(historyId));
    }
}
