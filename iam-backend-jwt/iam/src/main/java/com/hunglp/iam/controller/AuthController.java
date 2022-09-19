package com.hunglp.iam.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hunglp.iam.domain.CustomUser;
import com.hunglp.iam.dto.UserDTO;
import com.hunglp.iam.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class AuthController {

    @Autowired
    private JwtProvider jwtProvider;


    @Autowired
    private AuthenticationManager authenticationManager;


    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public void login(HttpServletRequest request, HttpServletResponse response, @RequestBody UserDTO userDTO) throws IOException {

        // Authenticate
        response.setContentType("application/json");
        passwordEncoder.encode(userDTO.getPassword());
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(userDTO.getUsername(), userDTO.getPassword());
        Authentication auth = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        CustomUser customUser = (CustomUser) auth.getPrincipal();

        // return token
        new ObjectMapper().writeValue(response.getOutputStream(), jwtProvider.generateToken(request,customUser));
    }



}
