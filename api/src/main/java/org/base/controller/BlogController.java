package org.base.controller;

import lombok.extern.slf4j.Slf4j;
import org.base.config.EnableWrapResponse;
import org.base.dto.blog.BlogDTO;
import org.base.dto.blog.BlogRequest;
import org.base.model.blog.BlogModel;
import org.base.services.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/blog")
@Slf4j
@EnableWrapResponse
public class BlogController {

    private final BlogService blogService;

    @Autowired
    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    @GetMapping("/get-all")
    public ResponseEntity getAll() {
        return ResponseEntity.ok(blogService.getAll());
    }

    @PostMapping("/create")
    public ResponseEntity create(@RequestBody BlogDTO request,
                                 @RequestHeader(name = "Authorization") String token) {
        return ResponseEntity.ok(blogService.create(request, token));
    }

    @PostMapping("/update")
    public ResponseEntity update(@RequestBody BlogDTO request) {
        return ResponseEntity.ok(blogService.update(request));
    }

    @PostMapping("/get-by-condition")
    public ResponseEntity getByCondition(@RequestBody BlogRequest request,
                                         @RequestHeader(value = "Authorization", required = false) String token) {
        return ResponseEntity.ok(blogService.getByCondition(request, null));
    }
}
