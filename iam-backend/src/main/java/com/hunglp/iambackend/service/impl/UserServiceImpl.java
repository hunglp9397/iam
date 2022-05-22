package com.hunglp.iambackend.service.impl;

import com.hunglp.iambackend.repository.UserRepository;
import com.hunglp.iambackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl extends UserService {

    @Autowired
    private UserRepository userRepository;


    @Override
    public void login() {
        System.out.println(this.userRepository.findAll());
    }
}
