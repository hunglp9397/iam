package com.hunglp.iambackend.config;

import org.keycloak.admin.client.Keycloak;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

public class KeycloakConfig {

    @Autowired
    static Environment env;

    public static  Keycloak getKeycloakInstance() {
        return Keycloak.getInstance(
                env.getProperty("keycloak.auth-server-url"),
                env.getProperty("keycloak.realm"),
                env.getProperty("keycloak-config.username"),
                env.getProperty("keycloak-config.password"),
                env.getProperty("keycloak.resource"),
                env.getProperty("keycloak-config.client-secret"));
    }




}
