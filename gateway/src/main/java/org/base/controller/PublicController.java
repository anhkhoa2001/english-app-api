package org.base.controller;

import lombok.extern.slf4j.Slf4j;
import org.base.config.EnableWrapResponse;
import org.base.exception.SystemException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
@Slf4j
@EnableWrapResponse
public class PublicController {

    @PutMapping
    public ResponseEntity testPut(@RequestParam int input) {
        if(input == 303) {
            throw new SystemException("hihi");
        }
        return ResponseEntity.ok("DONE");
    }

    @GetMapping
    public ResponseEntity<Object> testGet() {
        return new ResponseEntity<>("hihi", HttpStatus.OK);
    }
}
