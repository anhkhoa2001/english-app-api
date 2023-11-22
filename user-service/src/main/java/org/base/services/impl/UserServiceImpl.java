package org.base.services.impl;

import org.base.config.JwtTokenSetup;
import org.base.exception.ValidationException;
import org.base.model.cache.TokenCache;
import org.base.repositories.cache.TokenCacheRepository;
import org.base.services.UserService;
import org.base.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private JwtTokenSetup jwtTokenSetup;

    @Autowired
    private TokenCacheRepository tokenCacheRepo;

    @Override
    public String generateToken(Map<String, Object> bodyParam) {
        String username = bodyParam.getOrDefault("username", "").toString();
        String type = bodyParam.getOrDefault("type", "").toString();
        String code = bodyParam.getOrDefault("code", "").toString();

        if(StringUtil.isNullOrEmpty(username) || StringUtil.isNullOrEmpty(type) || StringUtil.isNullOrEmpty(code)) {
            throw new ValidationException("Add info account to body!!");
        }

        TokenCache tokenCache = tokenCacheRepo.findTokenCacheByUsernameAndType(username, type);

        if(tokenCache != null && tokenCache.getToken() != null) {
            tokenCacheRepo.delete(tokenCache);
        }
        String token = jwtTokenSetup.generateToken(username, code, type);
        Long currentTime = new Date().getTime();
        tokenCache.setToken(token);
        tokenCache.setCreateTime(currentTime);
        tokenCache.setExpiredIn(currentTime + jwtTokenSetup.getTIMER());

        tokenCacheRepo.save(tokenCache);
        return token;
    }
}