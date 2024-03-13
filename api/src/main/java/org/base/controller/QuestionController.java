package org.base.controller;

import lombok.extern.slf4j.Slf4j;
import org.base.config.EnableWrapResponse;
import org.base.dto.exam.QuestionDTO;
import org.base.exception.SystemException;
import org.base.model.exam.QuestionModel;
import org.base.services.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/question")
@RestController
@Slf4j
@EnableWrapResponse
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @PostMapping("/create")
    public ResponseEntity create(@RequestBody QuestionDTO request) {
        try {
            QuestionModel questionModel = questionService.create(request);
            return ResponseEntity.ok(questionModel);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SystemException(e.getMessage());
        }
    }

    @GetMapping("/get-all")
    public ResponseEntity getAll() {
        return ResponseEntity.ok(questionService.getAll());
    }
}
