package org.base.services;

import org.base.dto.UserDTO;
import org.base.model.UserModel;

import java.util.Map;

public interface UserService {

    UserModel saveOrUpdate(UserDTO userDTO);

    Object getAllUser();

    UserModel getUserInfo(String username);
}
