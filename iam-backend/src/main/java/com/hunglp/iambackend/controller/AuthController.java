package com.hunglp.iambackend.controller;

import com.hunglp.iambackend.dto.LoginDTO;
import com.hunglp.iambackend.dto.UserDTO;
import com.hunglp.iambackend.intercepter.TenantContext;
import com.hunglp.iambackend.model.Tenant;
import com.hunglp.iambackend.service.TenantService;
import com.hunglp.iambackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;


@RestController
@RequestMapping("/api/v1")
public class AuthController {

    @Autowired
    private TenantService tenantService;

    @Autowired
    private UserService userService;

    @PostMapping("/auth/login")
    public ResponseEntity<Object> doLogin(@RequestBody LoginDTO loginDTO) {

        String tenantName = TenantContext.getCurrentTenant();
        Optional<Tenant> tenant = tenantService.findByTenantName(tenantName);

        ResponseEntity<Object> authenticateResponse = userService.login(loginDTO.getUsername(), loginDTO.getPassword(), tenant.get().getName());

        return authenticateResponse;

    }

    @PostMapping("/auth/logout")
    public ResponseEntity<?> doLogout(@RequestBody UserDTO userDTO){
        String username = userDTO.getUsername();
        String tenantName = TenantContext.getCurrentTenant();
        String keyPrefixAccessToken = username + "::"

    }
}
