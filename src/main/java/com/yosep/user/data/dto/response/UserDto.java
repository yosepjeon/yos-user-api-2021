package com.yosep.user.data.dto.response;

import com.yosep.user.data.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.hateoas.RepresentationModel;


@Getter
@AllArgsConstructor
@ToString
public abstract class UserDto extends RepresentationModel<UserDto> {
    private String userId;
    private String password;
    private String name;
    private String email;
    private String phone;
    private String postCode;
    private String roadAddr;
    private String jibunAddr;
    private String extraAddr;
    private String detailAddr;

    public UserDto() {
        this.userId = "";
        this.password = "";
        this.name = "";
        this.email = "";
        this.phone = "";
        this.postCode = "";
        this.roadAddr = "";
        this.jibunAddr = "";
        this.extraAddr = "";
        this.detailAddr = "";
    }

    public UserDto(User user) {
        this.userId = user.getUserId();
        this.password = user.getPassword();
        this.name = user.getName();
        this.email = user.getEmail();
        this.phone = user.getPhone();
        this.postCode = user.getPostCode();
        this.roadAddr = user.getRoadAddr();
        this.jibunAddr = user.getJibunAddr();
        this.extraAddr = user.getExtraAddr();
        this.detailAddr = user.getDetailAddr();
    }
}
