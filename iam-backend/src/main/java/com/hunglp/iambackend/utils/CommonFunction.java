package com.hunglp.iambackend.utils;

import java.util.Base64;

public class CommonFunction {

    public static final String COUNT_LOGIN_FAIL="count_login_fail";

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

    public static String createKeyRedis(String username, String tenantName, String token){
        return username + "-" + tenantName + "-" + token;
    }

    public static String createKeyRedisLoginFail(String username, String tenantName){
        return username + "-" + tenantName + "-" + COUNT_LOGIN_FAIL;
    }
}
