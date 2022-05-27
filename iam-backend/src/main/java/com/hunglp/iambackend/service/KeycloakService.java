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
    static Environment environment;

    public  void authentication(String clientId, String username, String password){

        RestTemplate restTemplate = new RestTemplate();
        String url = CommonFunction.authenUrl(environment.getProperty("keycloak.auth-server-url"));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("", "first.last@example.com");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        ResponseEntity<String> response = restTemplate.postForEntity( url, request , String.class );


    }

}
