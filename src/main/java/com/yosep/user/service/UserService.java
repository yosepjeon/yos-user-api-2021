package com.yosep.user.service;

import com.yosep.user.data.dto.request.UserDtoForCreation;
import com.yosep.user.data.dto.response.CreatedUserDto;
import com.yosep.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public boolean checkDuplicatedUser(String userId) {
        return userRepository.findById(userId).isPresent() ? true : false;
    }

    @Transactional(readOnly = false)
    public CreatedUserDto createUser(UserDtoForCreation userDtoForCreation) {
        CreatedUserDto createdUserDto;

        if(userRepository.findById(userDtoForCreation.getUserId()).isPresent()) {
            createdUserDto = new CreatedUserDto();

            return createdUserDto;
        }

        return null;
    }
}
