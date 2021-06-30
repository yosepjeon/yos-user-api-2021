package com.yosep.user.config;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeyCloakConfiguration {
    @Value("${keycloak.serverUrl}")
    private String SERVER_URL;

    @Value("${keycloak.realm}")
    private String REALM;

    @Value("${keycloak.username}")
    private String USERNAME;

    @Value("${keycloak.password}")
    private String PASSWORD;

    @Value("${keycloak.clientId}")
    private String CLIENT_ID;

    @Value("${keycloak.clientSecret}")
    private String CLIENT_SECRET;

    @Bean
    public Keycloak getAdminKeycloak() {
        System.out.println("" +
                "SERVER_URL: " + SERVER_URL + "\n" +
                "REALM: " + REALM + "\n" +
                "USERNAME: " + USERNAME + "\n" +
                "PASSWORD: " + PASSWORD + "\n" +
                "CLIENT_ID: " + CLIENT_ID);

        return KeycloakBuilder
                .builder()
                .serverUrl(SERVER_URL)
                .realm(REALM)
                .username(USERNAME)
                .password(PASSWORD)
                .clientId(CLIENT_ID)
                .clientSecret(CLIENT_SECRET)
                .build();
    }


}
