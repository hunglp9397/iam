package com.hunglp.iambackend.utils;

import java.util.Base64;

public class CommonFunction {

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
}
