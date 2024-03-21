
package org.base.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.base.config.JwtTokenSetup;
import org.base.exception.AppException;
import org.base.exception.SystemException;
import org.base.exception.UnauthorizationException;
import org.base.exception.ValidationException;
import org.base.entity.UserModel;
import org.base.entity.cache.IndexCache;
import org.base.entity.cache.TokenCache;
import org.base.repo.UserRepository;
import org.base.repo.cache.IndexCacheRepository;
import org.base.repo.cache.TokenCacheRepository;
import org.base.service.AccessService;
import org.base.utils.Constants;
import org.base.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalTime;
import java.util.*;

@Service
@Slf4j
public class AccessServiceImpl implements AccessService {

    @Value(value = "${kong.fe.port}")
    private Long portFE;

    @Value(value = "${kong.fe.host}")
    private String hostFE;

    @Value(value = "${kong.url.create-user}")
    private String urlCreateUser;

    @Autowired
    private TokenCacheRepository tokenCacheRepo;

    @Autowired
    private JwtTokenSetup jwtTokenSetup;

    @Autowired
    private IndexCacheRepository indexCacheRepository;

    @Autowired
    private UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    @Transactional
    public String generateToken(Map<String, Object> bodyParam) {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                        .getRequest();
        try {
            String username = bodyParam.getOrDefault("username", "").toString();
            String type = bodyParam.getOrDefault("type", "").toString().toUpperCase();
            String code = bodyParam.getOrDefault("code", "").toString();

            if(StringUtil.isNullOrEmpty(username) || StringUtil.isNullOrEmpty(type) || StringUtil.isNullOrEmpty(code)) {
                throw new ValidationException("Add info account to body!!");
            }

            Optional<TokenCache> op = tokenCacheRepo.findById(code);
            if(op.isEmpty()) {
                throw new UnauthorizationException();
            }

            UserModel userModel = userRepository.getByUsernameAndType(username, type);

            if(userModel == null) {
                throw new UnauthorizationException();
            }

            TokenCache tokenCache = op.get();
            String token = jwtTokenSetup.generateToken(code, userModel.getUserId());
            Long currentTime = new Date().getTime();
            tokenCache.setToken(token);
            tokenCache.setCreateTime(currentTime);
            tokenCache.setExpiredIn(currentTime + jwtTokenSetup.getTIMER());

            tokenCacheRepo.save(tokenCache);
            return token;
        } catch (Exception e) {
            //ExceptionUtils.printException(e, request.getRequestURI(), bodyParam);

            throw new UnauthorizationException();
        }
    }

    @Override
    public void killToken(String token) {
        String code = jwtTokenSetup.getCodeFromToken(token);
        Optional<TokenCache> op = tokenCacheRepo.findById(code);

        if(op.isEmpty()) {
            throw new ValidationException("Token invalid");
        }

        tokenCacheRepo.delete(op.get());
    }


    @Override
    @Transactional
    public String getUrl(OAuth2User oAuth2User) {
        try {
            if(oAuth2User == null) {
                throw new UnauthorizationException();
            }

            String type = oAuth2User.getAttribute("avatar_url") != null ?
                    Constants.TYPE_LOGIN.GITHUB :
                    (oAuth2User.getAttribute("name_format") != null ? Constants.TYPE_LOGIN.FACEBOOK : Constants.TYPE_LOGIN.GOOGLE);
            String code = UUID.randomUUID().toString();
            String username = type.equals(Constants.TYPE_LOGIN.GITHUB)
                    ? oAuth2User.getAttribute("login") : oAuth2User.getAttribute("email");

            //save to cache
            TokenCache tokenCache = new TokenCache();
            tokenCache.setCode(code);
            tokenCache.setType(type);
            tokenCache.setUsername(username);

            tokenCacheRepo.save(tokenCache);

            UserModel userModel = userRepository.getByUsernameAndType(username, type);

            if(userModel == null) {
                userModel = new UserModel();
                userModel.setUserId(UUID.randomUUID().toString());
                userModel.setCreateAt(new Date());
                userModel.setRoleCode(Constants.ROLE_CODE.STUDENT);
                userModel.setUsername(username);
                userModel.setType(type);
                userModel.setFullname(oAuth2User.getAttribute("name"));
                if(type.equals(Constants.TYPE_LOGIN.GOOGLE)) {
                    userModel.setEmail(username);
                    userModel.setAvatar(oAuth2User.getAttribute("picture"));
                } else if(type.equals(Constants.TYPE_LOGIN.GITHUB)) {
                    userModel.setAvatar(oAuth2User.getAttribute("avatar_url"));
                } else {
                    userModel.setEmail(username);
                }

                userRepository.save(userModel);
            }

            return hostFE + ":" + portFE + "?code=" + code + "&username=" + username + "&type=" + type;
        } catch (Exception e) {
            throw new SystemException("Server failed");
        }
    }
}