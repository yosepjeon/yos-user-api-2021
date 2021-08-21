package com.yosep.user.keycloak;


import com.yosep.user.common.BaseTest;
import com.yosep.user.common.KeyCloakUtil;
import com.yosep.user.data.vo.CredentialForCreationUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.ws.rs.core.Response;
import java.util.*;

@SpringBootTest
@Slf4j
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class KeyCloakTest extends BaseTest {
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    Keycloak keycloak;

    String accessToken;
    Map<String, String> body = new HashMap<>();

    @BeforeEach
    public void getTokenBeforeTest() {
        MultiValueMap<String, Object> userInfo = new LinkedMultiValueMap<>();

        userInfo.add("client_id", "yos");
        userInfo.add("grant_type", "password");
        userInfo.add("client_secret", "9021d0bb-33e7-4488-9c85-419b2ed4673a");
        userInfo.add("scope", "openid");
        userInfo.add("username", "admin");
        userInfo.add("password", "admin");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity httpEntity = new HttpEntity<>(userInfo, httpHeaders);

        ResponseEntity response = restTemplate.postForEntity("http://localhost:9999/auth/realms/yosep/protocol/openid-connect/token", httpEntity, Map.class);

        body = (Map<String, String>) response.getBody();
        accessToken = body.get("access_token");
        log.info("[BeforeEach] 토큰 요청");
        log.info(body.get("access_token"));
        log.info(response.toString());
    }

    @AfterEach
    public void deleteUser() {
        List<UserRepresentation> userRepresentations = keycloak.realm("yosep").users().search("test1");

        if (!userRepresentations.isEmpty()) {
            String id = userRepresentations.get(0).getId();
            keycloak.realm("yosep").users().delete(id);
        }
    }

    @Test
    @DisplayName("[Keycloak] 토큰 요청 성공 테스트")
    public void 토큰_요청_성공_테스트() {
        log.info("[Keycloak] 토큰 요청 성공 테스트");

        Iterator<String> itr = body.keySet().iterator();

        while (itr.hasNext()) {
            String key = itr.next();
            log.info(key + ": " + String.valueOf(body.get(key)));
        }
    }

    @Test
    @DisplayName("[Keycloak] 토큰 요청 실패 테스트")
    public void 토큰_요청_실패_테스트() {

        Assertions.assertThrows(HttpClientErrorException.Unauthorized.class, () -> {
            log.info("[Keycloak] 토큰 요청 실패 테스트");

            MultiValueMap<String, Object> userInfo = new LinkedMultiValueMap<>();

            userInfo.add("client_id", "yos");
            userInfo.add("grant_type", "password");
            userInfo.add("client_secret", "9021d0bb-33e7-4488-9c85-419b2ed4673a");
            userInfo.add("scope", "openid");
            userInfo.add("username", "empty-user");
            userInfo.add("password", "1");

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            HttpEntity httpEntity = new HttpEntity<>(userInfo, httpHeaders);

            ResponseEntity response = restTemplate.postForEntity("http://localhost:9999/auth/realms/yosep/protocol/openid-connect/token", httpEntity, Map.class);

            log.info("[연산 완료]");
            log.info(response.toString());
        });
    }

    @Test
    @DisplayName("[Keycloak] 유저 생성 성공 테스트")
    public void 유저_생성_성공_테스트() {
        log.info("[Keycloak] 유저 생성 성공 테스트");
        String username = "test1";
        String password = "test1";
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        UserRepresentation user = new UserRepresentation();
        user.setUsername(username);
//        user.setFirstName("First Name");
//        user.setLastName("Last Name");
//        user.singleAttribute("customAttribute", "customAttribute");
        user.setCredentials(Arrays.asList(credential));
        javax.ws.rs.core.Response response = keycloak.realm("yosep").users().create(user);
        final int status = response.getStatus();

//        if (status != HttpStatus.CREATED.value()) {
//            return status;
//        }
        final String createdId = KeyCloakUtil.getCreatedId(response);
        // Reset password
        CredentialRepresentation newCredential = new CredentialRepresentation();
        UserResource userResource = keycloak.realm("yosep").users().get(createdId);
        newCredential.setType(CredentialRepresentation.PASSWORD);
        newCredential.setValue(password);
        newCredential.setTemporary(false);
        userResource.resetPassword(newCredential);

    }

    @Test
    @DisplayName("[Keycloak] 유저 찾기 성공 테스트")
    public void 유저_찾기_성공_테스트() {
        log.info("[Keycloak] 유저 찾기 성공 테스트");
//        List<UserRepresentation> users = keycloak.realm("yosep").users().search("user-creation-test1");
//        log.info("userId: " + users.get(0).getUsername());
    }

    @Test
    @DisplayName("[Keycloak] 유저 삭제 성공 테스트")
    public void 유저_삭제_성공_테스트() {
        log.info("[Keycloak] 유저 삭제 성공 테스트");

//        Response response = keycloak.realm("yosep").users().delete(keycloak.realm("yosep").users().search("user-creation-test1").get(0).getId());
//        System.out.println(response.getStatus());
    }
}
