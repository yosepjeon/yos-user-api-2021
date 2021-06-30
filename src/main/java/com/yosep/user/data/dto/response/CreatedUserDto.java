package com.yosep.user.data.dto.response;

import com.yosep.user.data.entity.User;
import lombok.Builder;

public class CreatedUserDto extends UserDto {
    public CreatedUserDto() {
        super();
    }

    @Builder
    public CreatedUserDto(
            String userId,
            String password,
            String name,
            String email,
            String phone,
            String postCode,
            String roadAddr,
            String jibunAddr,
            String extraAddr,
            String detailAddr
    ) {
        super(
                userId,
                password,
                name,
                email,
                phone,
                postCode,
                roadAddr,
                jibunAddr,
                extraAddr,
                detailAddr
        );
    }

    public CreatedUserDto(User user) {
        super(user);
    }
}
