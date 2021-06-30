package com.yosep.user.data.mapper;

import com.yosep.user.data.dto.request.UserDtoForCreation;
import com.yosep.user.data.dto.response.CreatedUserDto;
import com.yosep.user.data.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    
    @Mapping(target = "userId", ignore = false)
    User userDtoForCreationToUser(UserDtoForCreation userDtoForCreation);
    
    @Mapping(target = "userId", ignore = false)
    CreatedUserDto userToCreatedUserDto(User user);
}
