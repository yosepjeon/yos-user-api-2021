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

        body = (Map<String, String>) response.getBody();
        accessToken = body.get("access_token");
        log.info("[BeforeEach] 토큰 요청");
        log.info(body.get("access_token"));
        log.info(response.toString());
    }

    @Test
    @DisplayName("[Keycloak] 토큰 요청 성공 테스트")
    public void 토큰_요청_성공_테스트() {
        log.info("[Keycloak] 토큰 요청 성공 테스트");

        Iterator<String> itr = body.keySet().iterator();

        while(itr.hasNext()) {
            String key= itr.next();
            log.info(key + ": " + String.valueOf(body.get(key)));
        }
    }

    @Test
    @DisplayName("[Keycloak] 토큰 요청 실패 테스트")
    public void 토큰_요청_실패_테스트() {

        Assertions.assertThrows(HttpClientErrorException.Unauthorized.class, () ->{
            log.info("[Keycloak] 토큰 요청 실패 테스트");

            MultiValueMap<String, Object> userInfo = new LinkedMultiValueMap<>();

            userInfo.add("client_id","yos");
            userInfo.add("grant_type","password");
            userInfo.add("client_secret","9021d0bb-33e7-4488-9c85-419b2ed4673a");
            userInfo.add("scope","openid");
            userInfo.add("username","empty-user");
            userInfo.add("password","1");

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




//        MultiValueMap<String, Object> userInfo = new LinkedMultiValueMap<>();
//
//        String userName = "test1";
//        String password = "test1";
//
//        userInfo.add("username",userName);
//        userInfo.add("enabled","true");
////        List<CredentialForCreationUser> credentials = new ArrayList<>();
////        CredentialForCreationUser credential = new CredentialForCreationUser("password",password);
//        List<MultiValueMap<String, Object>> credentials = new ArrayList<>();
//        MultiValueMap<String, Object> credential = new LinkedMultiValueMap<>();
//        credential.add("type", "passowrd");
//        credential.add("value", password);
//        credentials.add(credential);
//        userInfo.add("credentials", credentials);
//
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
//        httpHeaders.setBearerAuth(accessToken);
//
//        HttpEntity httpEntity = new HttpEntity<>(userInfo, httpHeaders);

//        log.info(httpEntity.toString());
//        ResponseEntity response = restTemplate.postForEntity("http://localhost:9999/auth/admin/realms/yosep/users", httpEntity, Map.class);
//        log.info(response.toString());

//        MultiValueMap<String, Object> roleMappingInfo = new LinkedMultiValueMap<>();
//
//        String url = "http://localhost:9999/auth/admin/realms/yosep/users/" + userName + "/role-mappings/clients/yos";
//        roleMappingInfo.add("id","");
    }
}
