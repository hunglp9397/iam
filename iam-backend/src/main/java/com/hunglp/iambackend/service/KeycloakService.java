package com.hunglp.iambackend.service;

import com.hunglp.iambackend.utils.CommonFunction;
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

@Service
public class KeycloakService {

    @Autowired
    private Environment environment;

    public ResponseEntity<String> authentication(String clientId, String username, String password){

        RestTemplate restTemplate = new RestTemplate();
        String url = CommonFunction.getAuthenUrl(environment.getProperty("keycloak.realm"));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("client_id", environment.getProperty("login-app"));
        map.add("username", username);
        map.add("password", password);
        map.add("grant_type", "password");


        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        ResponseEntity<String> response = null;
        try {
            response = restTemplate.postForEntity(url, request, String.class);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        return response;


    }

}
