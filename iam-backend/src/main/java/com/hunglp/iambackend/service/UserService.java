package com.hunglp.iambackend.service;


import com.hunglp.iambackend.dto.UserDTO;
import com.hunglp.iambackend.model.Users;
import org.springframework.http.ResponseEntity;

import java.util.Optional;


public interface UserService {

    ResponseEntity<Object> login(String username, String password, String tenant);

    void createUser(UserDTO userDTO);

    Optional<Users> findUser(String username, String password, String tenant);
}
