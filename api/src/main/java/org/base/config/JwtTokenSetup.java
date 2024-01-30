
package org.base.config;

import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import io.jsonwebtoken.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.base.model.cache.TokenCache;
import org.base.repositories.cache.TokenCacheRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Slf4j
@Component
@Data
public class JwtTokenSetup {

    @Value("${secret.key}")
    private String SECRET_KEY;

    @Value("${secret.time_live}")
    private long TIMER;

    @Autowired
    private TokenCacheRepository tokenCacheRepo;

    public JwtTokenSetup() {}

    public String generateToken(String code, String username) {
        Date now = new Date();

        return Jwts.builder()
                .setSubject(code)
                .claim("username", username)
                .setIssuedAt(now)
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    public String getCodeFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody().getSubject();
    }

    public boolean validateToken(String authToken) {
        try {
            String code = getCodeFromToken(authToken);
            TokenCache tokenCache = tokenCacheRepo.findById(code).get();

            return new Date().getTime() < tokenCache.getExpiredIn();
        } catch (Exception ex) {
            log.error("Tracking {} {}", ex.getClass(), ex.getMessage());
            log.error("Token đã hết hạn hoặc không đúng!!");
        }

        return false;
    }
}
