package org.base.controller;

import org.base.config.EnableWrapResponse;
import org.base.dto.UserDTO;
import org.base.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/user")
@EnableWrapResponse
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/save-or-update")
    public ResponseEntity saveOrUpdate(@RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.saveOrUpdate(userDTO));
    }

    public Object getAllUser() {
        return userService.getAllUser();
    }

}
