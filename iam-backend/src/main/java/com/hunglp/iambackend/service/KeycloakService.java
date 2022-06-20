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

    @Autowired
    private RestTemplate restTemplate;

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

    public ResponseEntity<Object> createKeycloakUser(UserDTO userDTO) {

        RestTemplate restTemplate = new RestTemplate();
        String url = CommonFunction.createKeyCloakUserUrl(environment.getProperty("keycloak.realm"));

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + userDTO.getAuthToken());

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("firstName", userDTO.getFirstName());
        map.add("lastName", userDTO.getLastName());
        map.add("email", userDTO.getEmail());
        map.add("username", "password");
        map.add("enabled", String.valueOf(true));

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        ResponseEntity<Object> response = null;
        try {
            response = restTemplate.postForEntity(url, request, Object.class);
        } catch (Exception e) {
            throw new UnauthorizedException("Authorization  fail");
        }
        return response;
    }


    private static CredentialRepresentation createPasswordCredentials(String password) {
        CredentialRepresentation passwordCredentials = new CredentialRepresentation();
        passwordCredentials.setTemporary(false);
        passwordCredentials.setType(CredentialRepresentation.PASSWORD);
        passwordCredentials.setValue(password);
        return passwordCredentials;
    }

    public UsersResource getInstance() {
        return kcProvider.getInstance().realm(kcProvider.realm).users();
    }


}
