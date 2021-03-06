package com.yosep.user.controller;

import com.yosep.user.data.dto.request.UserDtoForCreation;
import com.yosep.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(value = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping(value = "/test")
    public ResponseEntity test() {
        return ResponseEntity.ok("test");
    }

    @GetMapping
    public ResponseEntity checkDupId(@RequestParam("userId") String userId) {
        return ResponseEntity.ok(userService.checkDuplicatedUser(userId));
    }

    @PostMapping
    public ResponseEntity createUser(@RequestBody @Valid UserDtoForCreation userDtoForCreation, Errors errors) {
        if(errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
        }

        return null;
    }
}