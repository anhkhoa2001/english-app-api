package org.base.controller;

import lombok.extern.slf4j.Slf4j;
import org.base.config.EnableWrapResponse;
import org.base.services.AccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/access")
@Slf4j
@EnableWrapResponse
public class AccessController {

    @Autowired
    private AccessService accessService;


    @GetMapping("/check-login")
    public ResponseEntity checkLogin(@AuthenticationPrincipal OAuth2User oAuth2User) {
        if (oAuth2User != null) {
            return ResponseEntity.ok(oAuth2User);
        } else {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/redirect-to-react-app")
    public ResponseEntity<Void> redirectToReactApp(@AuthenticationPrincipal OAuth2User oAuth2User) {
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(accessService.getUrl(oAuth2User)))
                .build();
    }

    @GetMapping("/get-user")
    public ResponseEntity getUser(@AuthenticationPrincipal OAuth2User oAuth2User) {
        return ResponseEntity.ok(oAuth2User);
    }
}
