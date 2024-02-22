
package org.base.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.base.config.EnableWrapResponse;
import org.base.config.JwtTokenSetup;
import org.base.exception.UnauthorizationException;
import org.base.model.CountRequestModel;
import org.base.model.IndexModel;
import org.base.model.cache.IndexCache;
import org.base.model.cache.TokenCache;
import org.base.repositories.cache.TokenCacheRepository;
import org.base.services.AccessService;
import org.base.thread.CountRequestManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalTime;
import java.util.Date;
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

    @Autowired
    private TokenCacheRepository tokenCacheRepository;

    @Autowired
    private CountRequestManager countRequestManager;


    private final int countRetry = 3;

    @GetMapping("/test")
    public ResponseEntity test(@RequestParam String code) throws JsonProcessingException {
        IndexModel indexModel = new IndexModel();
        int count = 0;
        try {
            indexModel = accessService.saveIndex(code);
        } catch (Exception e) {
            log.info("loi oi controller");
        }

        CountRequestModel req = new CountRequestModel();

        req.setCount(1);
        req.setRequest(code);
        req.setResponse(new ObjectMapper().writeValueAsString(indexModel));
        req.setCreateTime(new Date());

        countRequestManager.submit(req);
        return ResponseEntity.ok(indexModel);
    }

    @GetMapping("/test-cache")
    public ResponseEntity testCache(@RequestParam String code) throws JsonProcessingException {
        IndexCache obj = new IndexCache();
        int count = 0;
        try {
            obj = accessService.saveIndexCache(code);
        } catch (Exception e) {
            log.info("loi oi controller");
            e.printStackTrace();
        }

        CountRequestModel req = new CountRequestModel();

        req.setCount(1);
        req.setRequest(code);
        req.setResponse(new ObjectMapper().writeValueAsString(obj));
        req.setCreateTime(new Date());

        countRequestManager.submit(req);
        return ResponseEntity.ok(obj);
    }
}
