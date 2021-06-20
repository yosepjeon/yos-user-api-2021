package com.yosep.user.keycloak;


import com.yosep.user.common.BaseTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;

@SpringBootTest
@Slf4j
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class KeyCloakTest extends BaseTest {
    @Autowired
    RestTemplate restTemplate;

    @Test
    @DisplayName("[Keycloak] 토큰 요청 성공 테스트")
    public void 토큰_요청_성공_테스트() {
        log.info("[Keycloak] 토큰 요청 성공 테스트");
        MultiValueMap<String, String> userInfo = new LinkedMultiValueMap<>();

        userInfo.add("client_id","yos");
        userInfo.add("grant_type","password");
        userInfo.add("client_secret","9021d0bb-33e7-4488-9c85-419b2ed4673a");
        userInfo.add("scope","openid");
        userInfo.add("username","user1");
        userInfo.add("password","1");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity httpEntity = new HttpEntity<>(userInfo, httpHeaders);

        ResponseEntity response = restTemplate.postForEntity("http://localhost:9999/auth/realms/yosep/protocol/openid-connect/token", httpEntity, Map.class);

        log.info(response.toString());
    }

    @Test
    @DisplayName("[Keycloak] 유저 생성 성공 테스트")
    public void 유저_생성_성공_테스트() {
        log.info("[Keycloak] 유저 생성 성공 테스트");
        MultiValueMap<String, String> userInfo = new LinkedMultiValueMap<>();
    }
}
