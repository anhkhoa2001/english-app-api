package org.base.services.impl;

import org.base.exception.UnauthorizationException;
import org.base.model.cache.TokenCache;
import org.base.repositories.cache.TokenCacheRepository;
import org.base.services.AccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AccessServiceImpl implements AccessService {

    @Value(value = "${url.fe}")
    private String urlFe;

    @Autowired
    private TokenCacheRepository tokenCacheRepo;

    @Override
    public String getUrl(OAuth2User oAuth2User) {
        if(oAuth2User == null) {
            throw new UnauthorizationException();
        }

        String type = oAuth2User.getAttribute("login") != null ? "github" : "google";
        String code = UUID.randomUUID().toString();
        String username = type.equals("github") ? oAuth2User.getAttribute("login") : oAuth2User.getAttribute("email");

        TokenCache tokenCache = new TokenCache();
        tokenCache.setCode(code);
        tokenCache.setType(type);
        tokenCache.setUsername(username);

        tokenCacheRepo.save(tokenCache);
        return urlFe + "?type=" + type + "&code=" + code + "&username=" + username;
    }
}
