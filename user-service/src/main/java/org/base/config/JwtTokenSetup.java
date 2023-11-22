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

    public String generateToken(String username, String code, String type) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + TIMER);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .setId(code)
                .setPayload(type)
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public String getUsernamFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
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
