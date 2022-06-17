package com.hunglp.iambackend.service;

import com.hunglp.iambackend.config.KeycloakConfig;
import com.hunglp.iambackend.config.KeycloakProvider;
import com.hunglp.iambackend.dto.UserDTO;
import com.hunglp.iambackend.exception.UnauthorizedException;
import com.hunglp.iambackend.utils.CommonFunction;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.keycloak.representations.idm.UserRepresentation;


import javax.ws.rs.core.Response;
import java.util.Collections;

@Service
public class KeycloakService {

    @Value("${keycloak.realm}")
    public String realm;

    @Autowired
    private Environment environment;

    private final KeycloakProvider kcProvider;


    public KeycloakService(KeycloakProvider keycloakProvider) {
        this.kcProvider = keycloakProvider;
    }

    public ResponseEntity<Object> authentication(String username, String password, String clientId) {

        RestTemplate restTemplate = new RestTemplate();
        String url = CommonFunction.getAuthenUrl(environment.getProperty("keycloak.realm"));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", clientId);
        map.add("username", username);
        map.add("password", password);
        map.add("grant_type", "password");


        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        ResponseEntity<Object> response = null;
        try {
            response = restTemplate.postForEntity(url, request, Object.class);
        } catch (Exception e) {
            throw new UnauthorizedException("Authorization  fail");
        }
        return response;

    }

    public void createKeycloakUser(UserDTO userDTO) {
//        UsersResource usersResource = kcProvider.getInstance().realm(realm).users();
//        CredentialRepresentation credentialRepresentation = createPasswordCredentials(userDTO.getPassword());
//
//        UserRepresentation kcUser = new UserRepresentation();
//        kcUser.setUsername(userDTO.getEmail());
//        kcUser.setCredentials(Collections.singletonList(credentialRepresentation));
//        kcUser.setFirstName(userDTO.getFirstName());
//        kcUser.setLastName(userDTO.getLastName());
//        kcUser.setEmail(userDTO.getEmail());
//        kcUser.setEnabled(true);
//        kcUser.setEmailVerified(false);
//
//        Response response = null;
//        try {
//             response = usersResource.create(kcUser);
//        }catch (Exception e){
//            System.out.println(e.getMessage());
//        }
//
//
//        if (response.getStatus() == 201) {
//            System.out.println("Create user successfully");
//        }
//
//        return response;

        CredentialRepresentation credential = KeycloakProvider
                .createPasswordCredentials(userDTO.getPassword());
        UserRepresentation user = new UserRepresentation();
        user.setUsername(userDTO.getUsername());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setCredentials(Collections.singletonList(credential));
        user.setEnabled(true);

        UsersResource instance = getInstance();
        try {
            instance.create(user);
        }catch (Exception e){
            System.out.println(e);
        }

    }

    private static CredentialRepresentation createPasswordCredentials(String password) {
        CredentialRepresentation passwordCredentials = new CredentialRepresentation();
        passwordCredentials.setTemporary(false);
        passwordCredentials.setType(CredentialRepresentation.PASSWORD);
        passwordCredentials.setValue(password);
        return passwordCredentials;
    }

    public UsersResource getInstance(){
        return kcProvider.getInstance().realm(kcProvider.realm).users();
    }


}
