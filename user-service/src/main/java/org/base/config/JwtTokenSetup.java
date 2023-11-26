package org.base.config;

import io.jsonwebtoken.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
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

    public JwtTokenSetup() {
    }

    public String generateToken(String code) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + TIMER);

        return Jwts.builder()
                .setSubject(code)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException ex) {
            log.error("Sai token");
        } catch (ExpiredJwtException ex) {
            log.error("Token đã hết hạn");
        }

        return false;
    }
}
