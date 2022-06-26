package com.hunglp.iambackend.controller;

import com.hunglp.iambackend.dto.LoginDTO;
import com.hunglp.iambackend.dto.TotpDTO;
import com.hunglp.iambackend.dto.UserDTO;
import com.hunglp.iambackend.exception.ResourceNotFoundException;
import com.hunglp.iambackend.intercepter.TenantContext;
import com.hunglp.iambackend.model.Tenant;
import com.hunglp.iambackend.model.Users;
import com.hunglp.iambackend.service.RedisService;
import com.hunglp.iambackend.service.TenantService;
import com.hunglp.iambackend.service.UserService;
import com.hunglp.iambackend.utils.CommonFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/v1")
public class AuthController {

    @Autowired
    private TenantService tenantService;

    @Autowired
    private UserService userService;

    @Autowired
    private RedisService redisService;

    @PostMapping("/auth/login")
    public ResponseEntity<Object> doLogin(@RequestBody LoginDTO loginDTO) {

        String tenantName = TenantContext.getCurrentTenant();
        Optional<Tenant> tenant = tenantService.findByTenantName(tenantName);

        ResponseEntity<Object> authResponse = userService.login(loginDTO.getUsername(), loginDTO.getPassword(), tenant.get().getName());
        return authResponse;
    }

    @PostMapping("/auth/logout")
    public void doLogout(@RequestBody UserDTO userDTO){
        String username = userDTO.getUsername();
        String tenantName = TenantContext.getCurrentTenant();
        Optional<Users> user = userService.findByUsernameAndTenant(username,tenantName);
        if(user.isPresent()){
            String keyTokenPrefix = CommonFunction.createKeyRedisPrefix(username, tenantName);
            List<String> keysToken = redisService.getKeyPrefix(keyTokenPrefix);
            keysToken.forEach(k -> redisService.deleteValueByKey(k));

        }else {
            throw new ResourceNotFoundException("Username not exist in tenant");
        }
    }
}
