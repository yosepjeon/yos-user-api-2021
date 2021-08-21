package com.yosep.user.service;

import com.yosep.user.data.dto.request.UserDtoForCreation;
import com.yosep.user.data.entity.User;
import com.yosep.user.mq.producer.UserToProductProducer;
import com.yosep.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@SpringBootTest
@Transactional
@Slf4j
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class UserServiceTest {
    private final UserRepository userRepository;
    private final UserService userService;
    private final Keycloak keycloak;
    private final UserToProductProducer userToProductProducer;
    private final RestTemplate restTemplate;

    private String userId = "user-creation-test1";

    @Autowired
    public UserServiceTest(UserRepository userRepository, UserService userService, Keycloak keycloak, UserToProductProducer userToProductProducer, RestTemplate restTemplate) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.keycloak = keycloak;
        this.userToProductProducer = userToProductProducer;
        this.restTemplate = restTemplate;
    }

    @AfterEach
    public void deleteUser() {
        List<UserRepresentation> userRepresentations = keycloak.realm("yosep").users().search("test2");

        if(!userRepresentations.isEmpty()) {
            String id = userRepresentations.get(0).getId();
            Response response = keycloak.realm("yosep").users().delete(id);
            System.out.println(response.getStatus());
        }
    }

    @Test
    @DisplayName("[UserService] 유저 생성 성공 테스트")
    public void 유저_생성_성공_테스트() throws ExecutionException, InterruptedException {
        log.info("[UserService] 유저 생성 성공 테스트");

        UserDtoForCreation userDtoForCreation = UserDtoForCreation.builder()
                .userId(userId)
//                .userId("user-admin-for-test")
                .name("test1")
                .password("test1")
                .email("test1@test.com")
                .phone("010-1234-1234")
                .postCode("test")
                .roadAddr("test")
                .detailAddr("test")
                .extraAddr("test")
                .jibunAddr("test")
                .build();

//        User user = UserMapper.INSTANCE.userDtoForCreationToUser(userDtoForCreation);
        Boolean result = userService.createUser(userDtoForCreation);

        Assertions.assertEquals(true, result);

        Optional<User> optionalUser = userRepository.findById(userDtoForCreation.getUserId());
        Assertions.assertEquals(true, optionalUser.isPresent());

        User selectedUser = optionalUser.get();
        Assertions.assertEquals(userDtoForCreation.getUserId(), selectedUser.getUserId());

        keycloak.realm("yosep").users().delete(keycloak.realm("yosep").users().search(userId).get(0).getId());
        userToProductProducer.produceCartDeletion(userId);
    }

    @Test
    @DisplayName("[UserService] 상품서비스에 장바구니 생성")
    public void produceCartCreationTest() throws ExecutionException, InterruptedException {
        log.info("[UserService] 상품서비스에 장바구니 생성");

        userToProductProducer.produceCartCreation(userId);
        Thread.sleep(500);

        ResponseEntity response = restTemplate.exchange(
                "http://localhost:8001/carts?userId=" + userId,
                HttpMethod.GET, null, Boolean.class
        );

//        log.info(response.getBody() + "");
        Assertions.assertEquals(true, response.getBody());
        userToProductProducer.produceCartDeletion(userId);

        log.info(response.toString());
    }
}
