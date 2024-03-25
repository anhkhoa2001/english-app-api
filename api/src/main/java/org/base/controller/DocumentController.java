package org.base.controller;

import lombok.extern.slf4j.Slf4j;
import org.base.config.EnableWrapResponse;
import org.base.dto.docs.DocumentDTO;
import org.base.dto.docs.DocumentRequest;
import org.base.services.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/document")
@Slf4j
@EnableWrapResponse
public class DocumentController {

    private final DocumentService documentService;

    @Autowired
    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping("/create")
    public ResponseEntity create(@RequestBody DocumentDTO request,
                                 @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(documentService.create(request, token));
    }

    @PostMapping("/update")
    public ResponseEntity update(@RequestBody DocumentDTO request) {
        return ResponseEntity.ok(documentService.update(request));
    }

    @GetMapping("/get-all")
    public ResponseEntity getAll(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(documentService.getAll());
    }

    @PostMapping("/get-all-public")
    public ResponseEntity getAllPublic(@RequestBody DocumentRequest request) {
        return ResponseEntity.ok(documentService.getAllPublic(request));
    }
}
