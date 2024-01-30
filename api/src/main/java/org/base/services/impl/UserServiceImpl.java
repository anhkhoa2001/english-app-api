package org.base.services.impl;

import org.base.dto.UserDTO;
import org.base.model.UserModel;
import org.base.repositories.UserRepository;
import org.base.repositories.cache.TokenCacheRepository;
import org.base.services.UserService;
import org.base.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private TokenCacheRepository  tokenCacheRepo;

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
    public UserModel getUserInfo(String username) {
        return userRepo.getByUsername(username);
    }
}
