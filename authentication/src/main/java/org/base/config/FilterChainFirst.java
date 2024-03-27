
package org.base.config;

import lombok.extern.slf4j.Slf4j;
import org.base.exception.ValidationException;
import org.base.utils.Constants;
import org.springframework.http.HttpMethod;
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
        if(request.getMethod().equals(HttpMethod.OPTIONS.name())) {
            return true;
        }
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
            log.error("Tracking {} {} {}", e.getClass(), e.getMessage(), request.getRequestURI());
            //log.error("URL {}", request.getRequestURI());
            /*if(e instanceof NullPointerException) {
                if(request.getMethod().equals(HttpMethod.OPTIONS.name())) {
                    checkCORS++;
                    response.sendError(HttpServletResponse.SC_OK, "Token invalid or expired");
                } else if(token == null && checkCORS == 1) {
                    checkCORS = 0;
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token invalid or expired");
                }
            } else {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token invalid or expired");
            }*/
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token invalid or expired");
            return false;
            //return false;
        }
    }

    private boolean containsPattern(String pattern1) {
        return pattern1.contains("public") || pattern1.contains("up-file") || pattern1.contains("blog/get-by-condition") || pattern1.contains("/favicon.ico");
    }
}