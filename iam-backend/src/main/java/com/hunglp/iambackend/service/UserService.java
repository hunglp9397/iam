package com.hunglp.iambackend.service;


import com.hunglp.iambackend.dto.UserDTO;



public interface UserService {

    void login();

    void createUser(UserDTO userDTO);
}
