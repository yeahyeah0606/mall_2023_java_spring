package com.tina.mall2023.controller;

import com.tina.mall2023.dto.UserLoginRequest;
import com.tina.mall2023.dto.UserRegisterRequest;
import com.tina.mall2023.model.User;
import com.tina.mall2023.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/users/register")
    public ResponseEntity<User>
    register(@RequestBody @Valid UserRegisterRequest userRegisterRequest){
        Integer userID =  userService.register(userRegisterRequest);
        User user = userService.getUserById(userID);

        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PostMapping("/users/login")
    public  ResponseEntity<User> login(@RequestBody @Valid UserLoginRequest userLoginRequest){
        User user = userService.login(userLoginRequest);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

}
