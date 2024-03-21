package org.base.services.impl;

import io.jsonwebtoken.Claims;
import org.base.config.JwtTokenSetup;
import org.base.dto.UserDTO;
import org.base.entity.UserModel;
import org.base.repo.UserRepository;
import org.base.repo.cache.TokenCacheRepository;
import org.base.services.UserService;
import org.base.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private TokenCacheRepository tokenCacheRepo;

    @Autowired
    private JwtTokenSetup jwtTokenSetup;

    @Autowired
    private UserRepository userRepo;

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public UserModel saveOrUpdate(UserDTO userDTO) {
        UserModel user = userRepo.getByUsernameAndType(userDTO.getUsername(), userDTO.getType());

        if(user == null) {
            user = new UserModel();
            user.setUserId(UUID.randomUUID().toString());
            user.setCreateAt(new Date());
            user.setAvatar(userDTO.getAvatar());
            user.setFullname(userDTO.getFullname());
            user.setEmail(userDTO.getEmail());
            user.setType(userDTO.getType());
            user.setRoleCode(Constants.ROLE_CODE.STUDENT);
            user.setUsername(userDTO.getUsername());

            userRepo.save(user);
        }

        return user;
    }

    @Override
    public Object getAllUser() {
        return userRepo.findAll();
    }

    @Override
    public UserModel getUserInfo(String token) {
        Claims claims = jwtTokenSetup.getClaimsFromToken(token);
        String userId = claims.get("userId").toString();

        Optional<UserModel> opUser = userRepo.findById(userId);
        return opUser.orElse(null);
    }
}
