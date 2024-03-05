package org.base.controller;

import lombok.extern.slf4j.Slf4j;
import org.base.config.EnableWrapResponse;
import org.base.model.course.SectionModel;
import org.base.services.SectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/section")
@Slf4j
@EnableWrapResponse
public class SectionController {

    @Autowired
    private SectionService sectionService;

    @PostMapping("/create")
    public ResponseEntity create(@RequestBody SectionModel sectionModel) {
        return ResponseEntity.ok(sectionService.create(sectionModel));
    }

    @DeleteMapping("/delete-by-id")
    public ResponseEntity delete(@RequestParam Integer sectionId) {
        sectionService.deleteBySectionId(sectionId);
        return ResponseEntity.ok("DONE!!");
    }

}
