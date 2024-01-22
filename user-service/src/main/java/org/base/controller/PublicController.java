package org.base.controller;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.base.config.EnableWrapResponse;
import org.base.exception.UnauthorizationException;
import org.base.model.cache.TokenCache;
import org.base.repositories.cache.TokenCacheRepository;
import org.base.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/public")
@Slf4j
@EnableWrapResponse
public class PublicController {

    @Autowired
    private TokenCacheRepository tokenCacheRepo;

    @Autowired
    private UserService userService;


    @GetMapping("/test-gateway")
    public ResponseEntity testGateway(){
        int[] numbers = {1, 2, 3, 4, 5, 6, 7, 8};
        return ResponseEntity.ok(numbers);
    }

}
