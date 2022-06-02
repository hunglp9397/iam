package com.hunglp.iambackend.controller;

import com.hunglp.iambackend.dto.LoginDTO;
import com.hunglp.iambackend.dto.UserDTO;
import com.hunglp.iambackend.exception.ResourceNotFoundException;
import com.hunglp.iambackend.intercepter.TenantContext;
import com.hunglp.iambackend.model.Tenant;
import com.hunglp.iambackend.model.Users;
import com.hunglp.iambackend.service.KeycloakService;
import com.hunglp.iambackend.service.TenantService;
import com.hunglp.iambackend.service.UserService;
import com.hunglp.iambackend.utils.CommonConstant;
import com.hunglp.iambackend.utils.CommonFunction;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private KeycloakService keycloakService;

    @Autowired
    private TenantService tenantService;



    @PostMapping("/auth/login")
    public ResponseEntity<String> doLogin(@RequestBody LoginDTO loginDTO) {

        String tenantName = TenantContext.getCurrentTenant();
        Optional<Tenant> tenant = tenantService.findByTenantName(tenantName);



        ResponseEntity<String> authenticateResponse = userService.login(loginDTO.getUsername(), loginDTO.getPassword(), tenant.get().getName());

        if (authenticateResponse.getStatusCodeValue() == 200){
            Optional<Users> usersOptional = userService.findUser(loginDTO.getUsername(), loginDTO.getPassword(), tenant.get().getName());
            if(usersOptional.isPresent()){
                return authenticateResponse;
            }else{
                throw new ResourceNotFoundException("Query fail");
            }
        }

        return authenticateResponse;

    }

    @PostMapping("/users")
    public ResponseEntity<?> createUser(@RequestBody @Valid UserDTO userDTO) {

        String tenant = TenantContext.getCurrentTenant();
        System.out.println(tenant);
        keycloakService.createKeycloakUser(userDTO);
        return ResponseEntity.ok(userDTO);

    }


//    @PostMapping(path = "/signin")
//    public ResponseEntity<?> signin(@RequestBody  UserDTO userDTO) {
//
//        Map<String, Object> clientCredentials = new HashMap<>();
//        clientCredentials.put("secret", clientSecret);
//        clientCredentials.put("grant_type", "password");
//
//        Configuration configuration =
//                new Configuration(authServerUrl, realm, clientId, clientCredentials, null);
//        AuthzClient authzClient = AuthzClient.create(configuration);
//
//        AccessTokenResponse response =
//                authzClient.obtainAccessToken(userDTO.getEmail(), userDTO.getPassword());
//
//        return ResponseEntity.ok(response);
//    }



}
