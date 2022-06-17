package com.hunglp.iambackend.service.impl;

import com.hunglp.iambackend.dto.LoginDTO;
import com.hunglp.iambackend.dto.UserDTO;
import com.hunglp.iambackend.exception.InternalServerErrorException;
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

        LinkedHashMap<String, String > responseMap = (LinkedHashMap<String, String>) response.getBody();
        String keyRedisLoginFail = CommonFunction.createKeyRedisLoginFail(username, tenant);
        String keyRedisAccessToken = CommonFunction.createKeyRedis(username,tenant,responseMap.get("access_token"));
        String keyRedisRefreshToken = CommonFunction.createKeyRedis(username,tenant,responseMap.get("refresh_token"));



        int countLoginFail = redisService.getValueByKey(keyRedisLoginFail) != null ? Integer.parseInt(redisService.getValueByKey(keyRedisLoginFail)) : 0;
        if(countLoginFail > 5){

            throw new InternalServerErrorException("5 failed login attempts. Yours account will be block if login fail over 10 attempts");
        }
        if (response.getStatusCodeValue() == 200){
            Optional<Users> usersOptional = findUser(username, password, tenant);
            if(usersOptional.isPresent()){
                redisService.saveKeyValue(keyRedisRefreshToken, String.valueOf(1800));
                redisService.saveKeyValue(keyRedisAccessToken, String.valueOf(300));
            }else{
                countLoginFail ++;
                redisService.saveKeyValue(keyRedisLoginFail, String.valueOf(countLoginFail));

                throw new UnauthorizedException("Authorization Fail. Username or password incorrect");
            }
        }

        return response;

    }


    @Override
    public void createUser(UserDTO userDTO) {
        Users user = new Users();
        user.setUsername(user.getUsername());
        user.setPassword(user.getPassword());
        Optional<Tenant> tenant = tenantRepository.findById(1L);
        user.setTenant(tenant.get());

        this.userRepository.save(user);


//        Keycloak keycloak = KeycloakBuilder.builder().serverUrl(env.getProperty("keycloak.auth-server-url"))
//                .grantType(OAuth2Constants.PASSWORD).realm("master").clientId("admin-cli")
//                .username(userDTO.getUsername()).password(CommonFunction.passwordBase64(userDTO.getPassword()))
//                .resteasyClient(new ResteasyClientBuilder().connectionPoolSize(10).build()).build();
//
//        UserRepresentation userRepresentation = new UserRepresentation();
//        userRepresentation.setEnabled(true);
//        userRepresentation.setUsername(userDTO.getUsername());
//        userRepresentation.setFirstName(userDTO.getFirstName());
//        userRepresentation.setLastName(userDTO.getLastName());
//        userRepresentation.setEmail(userDTO.getEmail());
//
//        RealmResource realmResource = keycloak.realm(env.getProperty("keycloak.realm"));
//        UsersResource usersRessource = realmResource.users();
//
//        Response response = null;
//        try {
//            response = usersRessource.create(userRepresentation);
//        } catch (Exception ex) {
//            System.out.println(ex.getMessage());
//        }
//
//
//        userDTO.setStatusCode(response.getStatus());
//        userDTO.setStatus(response.getStatusInfo().toString());
//
//        if (response.getStatus() == 201) {
//
//            String userId = CreatedResponseUtil.getCreatedId(response);
//
////            log.info("Created userId {}", userId);
//
//
//            // create password credential
//            CredentialRepresentation passwordCred = new CredentialRepresentation();
//            passwordCred.setTemporary(false);
//            passwordCred.setType(CredentialRepresentation.PASSWORD);
//            passwordCred.setValue(userDTO.getPassword());
//
//            UserResource userResource = usersRessource.get(userId);
//
//            // Set password credential
//            userResource.resetPassword(passwordCred);
//
//            // Get realm role student
//            RoleRepresentation realmRoleUser = realmResource.roles().get(CommonConstant.roleUser).toRepresentation();
//
//            // Assign realm role student to user
//            userResource.roles().realmLevel().add(Arrays.asList(realmRoleUser));
//        }


    }

    @Override
    public Optional<Users> findUser(String username, String password, String tenant) {
        return userRepository.findAccount(username, password, tenant, false);
    }

}
