package org.base.controller;

import lombok.extern.slf4j.Slf4j;
import org.base.config.EnableWrapResponse;
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


    @GetMapping()
    public ResponseEntity getAll(@RequestParam(required = false) Integer page,
                                 @RequestParam(required = false) Integer pageSize,
                                 @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(examHistoryService.getAll(token, page, pageSize));
    }
}
