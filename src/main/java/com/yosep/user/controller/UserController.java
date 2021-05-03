package com.yosep.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/yos-user-api")
public class UserController {
    @RequestMapping(value = "test")
    public String test() {
        return "test";
    }
}