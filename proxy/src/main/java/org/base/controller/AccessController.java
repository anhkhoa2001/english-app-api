package org.base.controller;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.base.config.EnableWrapResponse;
import org.base.config.JwtTokenSetup;
import org.base.exception.UnauthorizationException;
import org.base.repositories.cache.TokenCacheRepository;
import org.base.services.AccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/access")
@Slf4j
@EnableWrapResponse
public class AccessController {

    @Autowired
    private AccessService accessService;

    @Autowired
    private JwtTokenSetup jwtTokenSetup;

    @Autowired
    private TokenCacheRepository tokenCacheRepo;

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

    @GetMapping("/redirect-to-react-app")
    public ResponseEntity<Void> redirectToReactApp(@AuthenticationPrincipal OAuth2User oAuth2User) {
        System.out.println("hello world");
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(accessService.getUrl(oAuth2User)))
                .build();
    }

    @GetMapping("/get-user")
    public ResponseEntity getUser(@AuthenticationPrincipal OAuth2User oAuth2User) {
        return ResponseEntity.ok(oAuth2User);
    }
}
