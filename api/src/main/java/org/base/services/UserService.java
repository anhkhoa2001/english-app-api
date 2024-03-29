package org.base.services;

import org.base.dto.UserDTO;
import org.base.entity.UserModel;

public interface UserService {

    UserModel saveOrUpdate(UserDTO userDTO);

    Object getAllUser();

    UserModel getUserInfo(String token);
}
