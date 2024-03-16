
package org.base.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.base.config.JwtTokenSetup;
import org.base.dto.UserDTO;
import org.base.dto.common.MessageResponseDTO;
import org.base.exception.AppException;
import org.base.exception.SystemException;
import org.base.exception.UnauthorizationException;
import org.base.exception.ValidationException;
import org.base.integrate.RestApiCommunication;
import org.base.model.IndexModel;
import org.base.model.UserModel;
import org.base.model.cache.IndexCache;
import org.base.model.cache.TokenCache;
import org.base.repositories.IndexRepository;
import org.base.repositories.UserRepository;
import org.base.repositories.cache.IndexCacheRepository;
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
    private IndexRepository indexRepository;

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
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public IndexModel saveIndex(String code) {
        log.info("Time current: {}", new Date());
        try {
            // Lấy thời gian hiện tại
            LocalTime currentTime = LocalTime.now();

            // Tính thời gian chênh lệch để đến 10:30:05
            int delaySeconds = 60 - currentTime.getSecond();

            // Đợi cho đến khi đến thời gian 10:30:05
            log.info("=================== delay time {}", delaySeconds);
            Thread.sleep(delaySeconds * 1000);

            IndexModel indexModel = indexRepository.getByCode(code);

            if(indexModel == null) {
                indexModel = new IndexModel();
                indexModel.setIndex(0L);
                indexModel.setCode(code);

                indexRepository.save(indexModel);
            } else {
                indexModel.setIndex(indexModel.getIndex() + 1);
                indexRepository.update(indexModel.getIndex(), indexModel.getCode());
            }

            log.error("value index {}", indexModel.getIndex());

            return indexModel;
        } catch (Exception e) {
            log.error("optimistic loi r {}", e.getClass());
            return new IndexModel();
        }
    }

    @Override
    public synchronized IndexCache saveIndexCache(String id) throws InterruptedException {
        Optional<IndexCache> op = indexCacheRepository.findById(id);
        IndexCache cachedIndex = new IndexCache();
        if (op.isEmpty()) {
            // Tạo mới một IndexCache
            IndexCache index = new IndexCache();
            int count = 0;
            index.setId(id);
            index.setCount(1);
            index.setCreateAt(new Date());

            cachedIndex = index;
        } else {
            IndexCache index = op.get();

            int count = index.getCount() + 1;
            index.setCount(count);

            cachedIndex = index;
        }

        log.info("index {}", cachedIndex.getCount());
        //indexCacheRepository.save(cachedIndex);
        //redisTemplate.opsForHash().put("index_cache1", cachedIndex.getId(), cachedIndex);
        indexCacheRepository.save(cachedIndex);

        if(Math.random() % 2 == 0) {
            throw new AppException("loi");
        }
        return cachedIndex;
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