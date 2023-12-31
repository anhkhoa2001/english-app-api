package org.base.controller;

import org.base.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    public Object generateToken(Map<String, Object> bodyParam){
        return userService.generateToken(bodyParam);
    }

    public boolean checkToken(Map<String, String> headerParam) {
        return userService.checkToken(headerParam);
    }

    public Object getAllUser() {
        return userService.getAllUser();
    }

}
