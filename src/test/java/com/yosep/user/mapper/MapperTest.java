package com.yosep.user.mapper;

import com.yosep.user.data.dto.request.UserDtoForCreation;
import com.yosep.user.data.dto.response.CreatedUserDto;
import com.yosep.user.data.entity.User;
import com.yosep.user.data.mapper.UserMapper;
import com.yosep.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Slf4j
@Transactional
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class MapperTest {
    private UserRepository userRepository;
    private String savedUserId;

    @Autowired
    public MapperTest(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @BeforeEach
    public void processBeforeTest() {
        log.info("===================================================== START =====================================================");
        User user = User.builder()
                .userId("test1")
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

        savedUserId = userRepository.save(user).getUserId();
    }

    @AfterEach
    public void processAfterTest() {
        userRepository.deleteById(savedUserId);
        log.info("===================================================== END =====================================================");
    }

    @Test
    @DisplayName("[MapStruct] User에서 CreatedUserDto로 변환 테스트")
    public void user에서_CreatedUserDto로_변환_테스트() {
        log.info("[MapStruct] user에서 CreatedUserDto로 변환 테스트");

        User user = User.builder()
                .userId("test1")
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

        log.info(user.toString());

        CreatedUserDto createdUserDto = UserMapper.INSTANCE.userToCreatedUserDto(user);

        log.info(createdUserDto.getUserId() + ", " + user.getUserId());
        Assertions.assertEquals(true, createdUserDto.getUserId().equals(user.getUserId()));
        Assertions.assertEquals(true, createdUserDto.getName().equals(user.getName()));
        log.info(createdUserDto.toString());
    }

    @Test
    @DisplayName("[MapStruct] UserDtoForCreation에서 User로 변환 테스트")
    public void userDtoForCreation에서_user로_변환_테스트() {
        log.info("[MapStruct] UserDtoForCreation에서 User로 변환 테스트");
        UserDtoForCreation userDtoForCreation = UserDtoForCreation.builder()
                .userId("test2")
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

        User user = UserMapper.INSTANCE.userDtoForCreationToUser(userDtoForCreation);

        User createdUser = userRepository.save(user);

        CreatedUserDto createdUserDto = UserMapper.INSTANCE.userToCreatedUserDto(createdUser);
        Assertions.assertEquals(true, createdUser.getUserId().equals(createdUserDto.getUserId()));
        Assertions.assertEquals(true, createdUser.getName().equals(createdUserDto.getName()));

        log.info(createdUserDto.toString());
    }
}
