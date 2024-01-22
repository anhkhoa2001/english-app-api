package org.base.controller;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.base.config.EnableWrapResponse;
import org.base.config.JwtTokenSetup;
import org.base.exception.UnauthorizationException;
import org.base.model.cache.TokenCache;
import org.base.repositories.cache.TokenCacheRepository;
import org.base.services.AccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashMap;
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
    private AccessService accessService;

    @GetMapping("/check-token")
    public ResponseEntity checkLogin(@RequestHeader("Authorization") String token) {
        token = token.replaceAll("Bearer ", "");
        boolean isno = jwtTokenSetup.validateToken(token);

        if(!isno) {
            throw new UnauthorizationException();
        }

        return ResponseEntity.ok(token);
    }

    @GetMapping("/kill-token")
    public ResponseEntity killToken(@RequestHeader("Authorization") String token) {
        token = token.replaceAll("Bearer ", "");
        accessService.killToken(token);
        return ResponseEntity.ok("DONE!");
    }

    @PostMapping("/generate-token")
    public ResponseEntity generateToken(@RequestBody Map<String, Object> bodyParam){
        return ResponseEntity.ok(accessService.generateToken(bodyParam));
    }

    @GetMapping("/redirect-to-react-app")
    public ResponseEntity<Void> redirectToReactApp(@AuthenticationPrincipal OAuth2User oAuth2User) {
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(accessService.getUrl(oAuth2User)))
                .build();
    }

    @GetMapping("/test")
    public ResponseEntity test(){
        return ResponseEntity.ok("done test");
    }
}
