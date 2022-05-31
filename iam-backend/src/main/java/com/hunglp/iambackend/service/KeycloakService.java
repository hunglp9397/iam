package com.hunglp.iambackend.service;

import com.hunglp.iambackend.config.KeycloakProvider;
import com.hunglp.iambackend.dto.UserDTO;
import com.hunglp.iambackend.exception.ResourceNotFoundException;
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

    public ResponseEntity<String> authentication(String username, String password) {

        RestTemplate restTemplate = new RestTemplate();
        String url = CommonFunction.getAuthenUrl(environment.getProperty("keycloak.realm"));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", environment.getProperty("keycloak.resource"));
        map.add("username", username);
        map.add("password", password);
        map.add("grant_type", "password");


        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        ResponseEntity<String> response = null;
        try {
            response = restTemplate.postForEntity(url, request, String.class);
        } catch (Exception e) {
            throw new UnauthorizedException("Authorization  fail");
        }
        return response;

    }

    public Response createKeycloakUser(UserDTO userDTO) {
        UsersResource usersResource = kcProvider.getInstance().realm(realm).users();
        CredentialRepresentation credentialRepresentation = createPasswordCredentials(userDTO.getPassword());

        UserRepresentation kcUser = new UserRepresentation();
        kcUser.setUsername(userDTO.getEmail());
        kcUser.setCredentials(Collections.singletonList(credentialRepresentation));
        kcUser.setFirstName(userDTO.getFirstName());
        kcUser.setLastName(userDTO.getLastName());
        kcUser.setEmail(userDTO.getEmail());
        kcUser.setEnabled(true);
        kcUser.setEmailVerified(false);

        Response response = null;
        try {
             response = usersResource.create(kcUser);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }


        if (response.getStatus() == 201) {
            System.out.println("Create user successfully");
            //If you want to save the user to your other database, do it here, for example:
//            User localUser = new User();
//            localUser.setFirstName(kcUser.getFirstName());
//            localUser.setLastName(kcUser.getLastName());
//            localUser.setEmail(user.getEmail());
//            localUser.setCreatedDate(Timestamp.from(Instant.now()));
//            String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
//            usersResource.get(userId).sendVerifyEmail();
//            userRepository.save(localUser);
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

}
