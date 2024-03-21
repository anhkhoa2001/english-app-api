
package org.base.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.base.entity.cache.TokenCache;
import org.base.repo.UserRepository;
import org.base.repo.cache.TokenCacheRepository;
import org.base.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

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

    @Autowired
    private UserRepository userRepository;

    public JwtTokenSetup() {}

    public String generateToken(String code, String userId) {
        Date now = new Date();

        return Jwts.builder()
                .setSubject(code)
                .claim("userId", userId)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + TIMER))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public Claims getClaimsFromToken(String token) {
        token = token.replaceAll(Constants.TOKEN_TYPE, "");
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    public String getUserIdFromToken(String token) {
        token = token.replaceAll(Constants.TOKEN_TYPE, "");
        String userId = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody().get("userId").toString();
        return userRepository.findById(userId).isEmpty() ? null : userId;
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
