package org.base.oauth2.controller.pub;

import lombok.extern.slf4j.Slf4j;
import org.base.config.EnableWrapResponse;
import org.base.oauth2.dto.BookDTO;
import org.base.oauth2.service.PublicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
@Slf4j
@EnableWrapResponse
public class PublicController {

    private final PublicService publicService;


    @Autowired
    public PublicController(PublicService publicService) {
        this.publicService = publicService;
    }

    @PostMapping
    public ResponseEntity testPost(@RequestParam int input) {
        return ResponseEntity.ok(publicService.handle(input));
    }

    @PostMapping("/add-book")
    public ResponseEntity add(@RequestBody BookDTO dto) {
        log.info(dto.getName());
        return ResponseEntity.ok("DONE");
    }

    @GetMapping
    public ResponseEntity<Object> testGet() {
        return new ResponseEntity<>("hihi", HttpStatus.OK);
    }
}
