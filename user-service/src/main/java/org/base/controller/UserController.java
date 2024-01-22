package org.base.controller;

import org.base.config.EnableWrapResponse;
import org.base.dto.UserDTO;
import org.base.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/get-all")
    public Object getAllUser() {
        return userService.getAllUser();
    }

    @GetMapping("/user-info")
    public ResponseEntity userInfo(@RequestParam String username) {
        return ResponseEntity.ok(userService.getUserInfo(username));
    }
}
