
package org.base.config;

import lombok.extern.slf4j.Slf4j;
import org.base.utils.Constants;
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
        if(!validateHeader(request, response, request.getHeader("authorization"))) {
            return;
        }
        filterChain.doFilter(request, response);
    }

    private boolean validateHeader(HttpServletRequest request, HttpServletResponse response, String token) throws IOException {
        if(containsPattern(request.getRequestURI())) {
            return true;
        }


        try {
            token = token.replace(Constants.TOKEN_TYPE, "");
            boolean isno = jwtTokenSetup.validateToken(token);

            if(isno) {
                return true;
            }

            throw new Exception();
        } catch (Exception e) {
            log.error("Tracking {} {}", e.getClass(), e.getMessage());
            log.error("URL {}", request.getRequestURI());
            response.sendError(HttpServletResponse.SC_OK, "Token invalid or expired");

            return false;
        }
    }

    private boolean containsPattern(String pattern1) {
        return pattern1.startsWith("/api/public") || pattern1.startsWith("/api/up-file");
    }
}