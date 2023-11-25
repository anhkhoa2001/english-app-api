package org.base.controller;

import org.base.exception.ValidationException;
import org.base.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    public Object generateToken(Map<String, Object> bodyParam){
        return ResponseEntity.ok(userService.generateToken(bodyParam));
    }

    public boolean checkToken(Map<String, String> headerParam) {
        return false;
    }

    public Object getAllUser() {
        return userService.getAllUser();
    }

}
