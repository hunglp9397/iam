package com.hunglp.iambackend.service;


import com.hunglp.iambackend.dto.UserDTO;
import org.springframework.http.ResponseEntity;


public interface UserService {

    ResponseEntity<String> login(String username, String password);

    void createUser(UserDTO userDTO);
}
