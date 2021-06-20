package com.yosep.user.common;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public abstract class BaseTest {
    @BeforeEach
    public void printStartBeforeEach() {
        log.info("===================================================== START =====================================================");
    }

    @AfterEach
    public void printStartAfterEach() {
        log.info("===================================================== END =====================================================");
    }
}
