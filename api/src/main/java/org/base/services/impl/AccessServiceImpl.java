
package org.base.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jwt.JWT;
import lombok.extern.slf4j.Slf4j;
import org.base.config.JwtTokenSetup;
import org.base.dto.UserDTO;
import org.base.dto.common.MessageResponseDTO;
import org.base.exception.SystemException;
import org.base.exception.UnauthorizationException;
import org.base.exception.ValidationException;
import org.base.integrate.RestApiCommunication;
import org.base.model.IndexModel;
import org.base.model.cache.TokenCache;
import org.base.repositories.IndexRepository;
import org.base.repositories.UserRepository;
import org.base.repositories.cache.TokenCacheRepository;
import org.base.services.AccessService;
import org.base.utils.Constants;
import org.base.utils.ExceptionUtils;
import org.base.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

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
    private RestApiCommunication<MessageResponseDTO> restApiCommunication;

    @Autowired
    private JwtTokenSetup jwtTokenSetup;

    @Autowired
    private IndexRepository indexRepository;

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
            TokenCache tokenCache = op.get();
            String token = jwtTokenSetup.generateToken(code, username);
            Long currentTime = new Date().getTime();
            tokenCache.setToken(token);
            tokenCache.setCreateTime(currentTime);
            tokenCache.setExpiredIn(currentTime + jwtTokenSetup.getTIMER());

            tokenCacheRepo.save(tokenCache);
            return token;
        } catch (Exception e) {
            ExceptionUtils.printException(e, request.getRequestURI(), bodyParam);

            throw new SystemException("Server failed");
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
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void saveIndex(String code) {
        IndexModel indexModel = indexRepository.getByCode(code);

        if(indexModel == null) {
            indexModel = new IndexModel();
            indexModel.setIndex(0L);
            indexModel.setCode(code);
        } else {
            indexModel.setIndex(indexModel.getIndex() + 1);
        }

        indexRepository.save(indexModel);
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

            //save user if user not exist
            UserDTO userDTO = new UserDTO();
            userDTO.setUsername(username);
            userDTO.setType(type);
            userDTO.setFullname(oAuth2User.getAttribute("name"));
            if(type.equals(Constants.TYPE_LOGIN.GOOGLE)) {
                userDTO.setEmail(username);
                userDTO.setAvatar(oAuth2User.getAttribute("picture"));
            } else if(type.equals(Constants.TYPE_LOGIN.GITHUB)) {
                userDTO.setAvatar(oAuth2User.getAttribute("avatar_url"));
            } else {
                userDTO.setEmail(username);
            }

            String body = new ObjectMapper().writeValueAsString(userDTO);
            ResponseEntity<MessageResponseDTO> res = restApiCommunication.exchangeJsonUrl(HttpMethod.POST,
                                    new HttpHeaders(), urlCreateUser, body, MessageResponseDTO.class);

            log.info("{}", res.getStatusCode());
            log.info("{}", userDTO);
            return hostFE + ":" + portFE + "?code=" + code + "&username=" + username + "&type=" + type;
        } catch (Exception e) {
            throw new SystemException("Server failed");
        }
    }
}