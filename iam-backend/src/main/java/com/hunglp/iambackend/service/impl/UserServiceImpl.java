package com.hunglp.iambackend.service.impl;

import com.hunglp.iambackend.dto.LoginDTO;
import com.hunglp.iambackend.dto.UserDTO;
import com.hunglp.iambackend.exception.InternalServerErrorException;
import com.hunglp.iambackend.exception.ResourceAlreadyExistException;
import com.hunglp.iambackend.exception.UnauthorizedException;
import com.hunglp.iambackend.model.Tenant;
import com.hunglp.iambackend.model.Users;
import com.hunglp.iambackend.repository.TenantRepository;
import com.hunglp.iambackend.repository.UserRepository;
import com.hunglp.iambackend.service.KeycloakService;
import com.hunglp.iambackend.service.RedisService;
import com.hunglp.iambackend.service.UserService;
import com.hunglp.iambackend.utils.CommonConstant;
import com.hunglp.iambackend.utils.CommonFunction;
import com.hunglp.iambackend.utils.IAMResponse;
import org.apache.http.auth.Credentials;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.util.*;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private RedisTemplate template;

    @Autowired
    private Environment env;

    @Autowired
    private KeycloakService keycloakService;

    @Autowired
    private RedisService redisService;


    @Override
    public ResponseEntity<Object> login(String username, String password, String tenant) {
        ResponseEntity<Object> response = keycloakService.authentication(username, password, tenant);

        HashMap<String, String> responseMap = (HashMap<String, String>) response.getBody();

        String keyRedisLoginFail = CommonFunction.createKeyRedisLoginFail(username, tenant);
        String keyRedisAccessToken = CommonFunction.createKeyRedisWithToken(username, tenant, CommonFunction.TokenType.ACCESS_TOKEN, responseMap.get("access_token"));
        String keyRedisRefreshToken = CommonFunction.createKeyRedisWithToken(username, tenant, CommonFunction.TokenType.REFRESH_TOKEN, responseMap.get("refresh_token"));


        int countLoginFail = redisService.getValueByKey(keyRedisLoginFail) != null ? Integer.parseInt(redisService.getValueByKey(keyRedisLoginFail)) : 0;
        if (countLoginFail > 5) {

            throw new InternalServerErrorException("5 failed login attempts. Yours account will be block if login fail over 10 attempts");
        }
        if (response.getStatusCodeValue() == 200) {
            Optional<Users> usersOptional = findUser(username, password, tenant);
            if (usersOptional.isPresent()) {
                Users user = usersOptional.get();
                responseMap.put("authenticationType", user.getAuthenticationType());


                redisService.saveKeyValueWithExpire(keyRedisAccessToken,"1",1800);
                redisService.saveKeyValueWithExpire(keyRedisRefreshToken,"1", 300);
            } else {
                countLoginFail++;
                redisService.saveKeyValue(keyRedisLoginFail, String.valueOf(countLoginFail));

                throw new UnauthorizedException("Authorization Fail. Username or password incorrect");
            }
        }

        HashMap<String,String> responseBody = new HashMap<>(responseMap);
        return ResponseEntity.ok().headers(response.getHeaders()).body(responseBody);
    }


    @Override
    public void createUser(UserDTO userDTO, String tenant) {

        if (findByUsernameAndTenant(userDTO.getUsername(), tenant).isPresent()) {
            throw new ResourceAlreadyExistException("username already exist");
        }
        ResponseEntity<Object> response = keycloakService.createKeycloakUser(userDTO);
        System.out.println(response);


    }

    @Override
    public Optional<Users> findUser(String username, String password, String tenant) {
        return userRepository.findAccount(username, password, tenant, false);
    }

    @Override
    public Optional<Users> findByUsernameAndTenant(String username, String tenant) {
        return userRepository.findByUsernameAndTenant(username, tenant);
    }

}
