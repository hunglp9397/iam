package com.hunglp.iambackend.utils;

import java.util.Base64;

public class CommonFunction {
    public static String passwordBase64(String password){
        return Base64.getEncoder().encodeToString(password.getBytes());
    }
}
