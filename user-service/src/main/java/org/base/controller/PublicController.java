package org.base.controller;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.base.config.EnableWrapResponse;
import org.base.config.JwtTokenSetup;
import org.base.exception.UnauthorizationException;
import org.base.model.cache.TokenCache;
import org.base.repositories.cache.TokenCacheRepository;
import org.base.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/public")
@Slf4j
@EnableWrapResponse
public class PublicController {

    @Autowired
    private JwtTokenSetup jwtTokenSetup;

    @Autowired
    private TokenCacheRepository tokenCacheRepo;

    @Autowired
    private UserService userService;

    @GetMapping("/check-token")
    public ResponseEntity checkLogin(@RequestHeader("Authorization") String token) {
        try {
            token = token.replaceAll("Bearer ", "");
            Claims claims = jwtTokenSetup.getClaimsFromToken(token);

            return ResponseEntity.ok(tokenCacheRepo.findById(claims.getSubject()).isPresent());
        } catch (Exception e) {
            throw new UnauthorizationException();
        }
    }

    @GetMapping("/kill-token")
    public ResponseEntity killToken(@RequestHeader("Authorization") String token) {
        token = token.replaceAll("Bearer ", "");
        Claims claims = jwtTokenSetup.getClaimsFromToken(token);

        System.out.println(claims.getSubject());

        Optional<TokenCache> tokenCache = tokenCacheRepo.findById(claims.getSubject());
        if(tokenCache.isEmpty()) {
            throw new UnauthorizationException();
        }
        tokenCacheRepo.delete(tokenCache.get());

        return ResponseEntity.ok("DONE!");
    }

    @PostMapping("/generate-token")
    public ResponseEntity generateToken(@RequestBody Map<String, Object> bodyParam){
        return ResponseEntity.ok(userService.generateToken(bodyParam));
    }

}
