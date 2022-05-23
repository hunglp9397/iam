package com.hunglp.iambackend.controller;

import com.hunglp.iambackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate template;


    @PostMapping("/auth/login")
    public ResponseEntity<?> doLogin() {
        userService.login();
        template.opsForValue().set("loda","hello world");
        System.out.println("Value of key loda: "+template.opsForValue().get("loda"));
        return null;

    }


}
