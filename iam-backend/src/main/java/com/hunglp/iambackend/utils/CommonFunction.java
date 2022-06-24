package com.hunglp.iambackend.utils;

import java.util.Base64;

public class CommonFunction {

    public static final String COUNT_LOGIN_FAIL="LoginFail";
    public static final String ACCESS_TOKEN = "AccessToken";
    public static final String REFRESH_TOKEN = "RefreshToken";

    public enum TokenType {
        COUNT_LOGIN_FAIL("LoginFail"),
        ACCESS_TOKEN("AccessToken"),
        REFRESH_TOKEN("RefreshToken");

        private String name;

        private TokenType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public static String passwordBase64(String password){
        return Base64.getEncoder().encodeToString(password.getBytes());
    }

    public static String passwordDecoded(String encodedString){
        byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
        return  new String(decodedBytes);
    }


    public static String getAuthenUrl(String realm) {
        return "http://localhost:8080/auth/realms/" + realm + "/protocol/openid-connect/token";
    }

    public static String createKeyCloakUserUrl(String realm){
        return "http://localhost:8080/auth/admin/realms/" + realm + "/users";
    }

    public static String createKeyRedisWithToken(String username, String tenantName, TokenType tokenType, String token){
        return username + "::" + tenantName + "::" + tokenType.getName() + "::" + token;
    }

    public static String createKeyRedisLoginFail(String username, String tenantName){
        return username + "::" + tenantName + "::" + COUNT_LOGIN_FAIL;
    }

    public static String createKeyRedisPrefix(String username, String tenantName ){
        return username + "::" + tenantName +"::*";
    }
}
