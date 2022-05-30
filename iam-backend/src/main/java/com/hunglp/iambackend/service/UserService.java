package com.hunglp.iambackend.service;


import com.hunglp.iambackend.dto.LoginDTO;
import com.hunglp.iambackend.dto.UserDTO;
import com.hunglp.iambackend.model.Users;
import org.springframework.http.ResponseEntity;

import java.util.Optional;


public interface UserService {

    ResponseEntity<String> login(String username, String password);

    void createUser(UserDTO userDTO);

    Optional<Users> findUser(LoginDTO loginDTO);
}
