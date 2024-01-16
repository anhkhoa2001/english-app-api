package org.base.config;

import lombok.extern.slf4j.Slf4j;
import org.base.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class FilterChainFirst extends OncePerRequestFilter {

    private final JwtTokenSetup jwtTokenSetup;

    public FilterChainFirst(JwtTokenSetup jwtTokenSetup) {
        this.jwtTokenSetup = jwtTokenSetup;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        validateHeader(request, response);
        filterChain.doFilter(request, response);
    }

    private void validateHeader(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if(containsPattern(request.getRequestURI(), "/public")) {
            return;
        }

        try {
            String token = request.getHeader("Authorization");

            token = token.replace(Constants.TOKEN_TYPE, "");
            boolean isno = jwtTokenSetup.validateToken(token);

            if(isno) {
                return;
            }

            throw new Exception();
        } catch (Exception e) {
            log.error("Tracking {} {}", e.getClass(), e.getMessage());
            log.error("URL {}", request.getRequestURI());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token invalid or expired");
        }
    }

    private boolean containsPattern(String pattern1, String pattern2) {
        return pattern1.startsWith(pattern2);
    }
}
