package org.base.services.impl;

import io.jsonwebtoken.Claims;
import org.base.config.JwtTokenSetup;
import org.base.exception.UnauthorizationException;
import org.base.exception.ValidationException;
import org.base.model.UserModel;
import org.base.model.cache.TokenCache;
import org.base.repositories.UserRepository;
import org.base.repositories.cache.TokenCacheRepository;
import org.base.services.UserService;
import org.base.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private JwtTokenSetup jwtTokenSetup;

    @Autowired
    private TokenCacheRepository tokenCacheRepo;

    @Autowired
    private UserRepository userRepo;

    @Override
    @Transactional
    public String generateToken(Map<String, Object> bodyParam) {
        String username = bodyParam.getOrDefault("username", "").toString();
        String type = bodyParam.getOrDefault("type", "").toString().toUpperCase();
        String code = bodyParam.getOrDefault("code", "").toString();

        if(StringUtil.isNullOrEmpty(username) || StringUtil.isNullOrEmpty(type) || StringUtil.isNullOrEmpty(code)) {
            throw new ValidationException("Add info account to body!!");
        }

        UserModel user = userRepo.getByUsernameAndType(username, type);
        if (user == null) {
            throw new UnauthorizationException();
        }
        Optional<TokenCache> op = tokenCacheRepo.findById(code);
        if(op.isEmpty()) {
            throw new UnauthorizationException();
        }
        TokenCache tokenCache = op.get();
        String token = jwtTokenSetup.generateToken(code);
        Long currentTime = new Date().getTime();
        tokenCache.setToken(token);
        tokenCache.setCreateTime(currentTime);
        tokenCache.setExpiredIn(currentTime + jwtTokenSetup.getTIMER());

        tokenCacheRepo.save(tokenCache);
        return token;
    }

    @Override
    public boolean checkToken(Map<String, String> headerParam) {
        String token = headerParam.getOrDefault("Authorization", "Bearer ");
        try {
            token = token.replaceAll("Bearer ", "");
            Claims claims = jwtTokenSetup.getClaimsFromToken(token);

            return tokenCacheRepo.findById(claims.getSubject()).isPresent();
        } catch (Exception e) {
            throw new UnauthorizationException();
        }
    }

    @Override
    public Object getAllUser() {
        return userRepo.findAll();
    }

    @Override
    public Object getUserInfo(Map<String, String> headerParam) {
        String token = headerParam.getOrDefault("Authorization", "Bearer ");
        try {
            token = token.replaceAll("Bearer ", "");
            Claims claims = jwtTokenSetup.getClaimsFromToken(token);

            return userRepo.findById(claims.getSubject()).isPresent();
        } catch (Exception e) {
            throw new UnauthorizationException();
        }
    }
}
