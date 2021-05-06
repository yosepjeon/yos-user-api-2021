package com.yosep.user.data.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table
@Data
public class UserEntity {
    @Id
    @Column(length = 30)
    private String userName;

    @Column(length = 100, nullable = false)
    private String password;

    @Column(length=50, nullable=false)
    private String name;

    @Column(length=100, nullable=false)
    private String email;

    @Column(length=50, nullable=false)
    private String phone;

    @Column(length=50, nullable=false)
    private String postCode;

    @Column(length=50, nullable=false)
    private String roadAddr;

    @Column(length=50, nullable=false)
    private String jibunAddr;

    @Column(length=50, nullable=false)
    private String extraAddr;

    @Column(length=50, nullable=false)
    private String detailAddr;

    @Column
    private LocalDateTime userRdate;

    @Column
    private LocalDateTime userUdate;

//    @ElementCollection(fetch=FetchType.EAGER)
//    @Enumerated(value=EnumType.STRING)
//    private Set<YoggaebiUserRole> roles;
}
