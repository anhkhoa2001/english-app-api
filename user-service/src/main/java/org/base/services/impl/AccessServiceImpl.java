package org.base.services.impl;

import org.base.exception.UnauthorizationException;
import org.base.model.UserModel;
import org.base.model.cache.TokenCache;
import org.base.repositories.UserRepository;
import org.base.repositories.cache.TokenCacheRepository;
import org.base.services.AccessService;
import org.base.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class AccessServiceImpl implements AccessService {

    @Value(value = "${url.fe}")
    private String urlFe;

    @Autowired
    private TokenCacheRepository tokenCacheRepo;

    @Autowired
    private UserRepository userRepo;

    @Override
    public String getUrl(OAuth2User oAuth2User) {
        if(oAuth2User == null) {
            throw new UnauthorizationException();
        }

        String type = oAuth2User.getAttribute("avatar_url") != null ? Constants.TYPE_LOGIN.GITHUB : Constants.TYPE_LOGIN.GOOGLE;
        String code = UUID.randomUUID().toString();
        String username = type.equals(Constants.TYPE_LOGIN.GITHUB)
                        ? oAuth2User.getAttribute("login") : oAuth2User.getAttribute("email");

        //save to cache
        TokenCache tokenCache = new TokenCache();
        tokenCache.setCode(code);
        tokenCache.setType(type);
        tokenCache.setUsername(username);

        tokenCacheRepo.save(tokenCache);

        //save user if user not exist
        UserModel userModel = userRepo.getByUsernameAndType(username, type);
        if(userModel == null) {
            userModel = new UserModel();
            userModel.setUsername(username);
            userModel.setUserId(code);
            userModel.setType(type);
            userModel.setCreateAt(new Date());
            userModel.setFullname(oAuth2User.getAttribute("name"));

            if(type.equals(Constants.TYPE_LOGIN.GOOGLE)) {
                userModel.setEmail(username);
                userModel.setAvatar(oAuth2User.getAttribute("picture"));
            } else {
                userModel.setAvatar(oAuth2User.getAttribute("avatar_url"));
            }

            userRepo.save(userModel);
        }

        return urlFe + "?code=" + code;
    }
}
