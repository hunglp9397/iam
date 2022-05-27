package com.hunglp.iambackend.service;


import com.hunglp.iambackend.dto.UserDTO;



public interface UserService {

    void login(String username, String password);

    void createUser(UserDTO userDTO);
}
