package com.server.EZY.model.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "User")
@Builder
@NoArgsConstructor @AllArgsConstructor
@Getter
public class UserEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UserId")
    private Long userIdx;

    @Column(name = "NickName")
    private String nickname;

    @Column(name = "Password")
    private String password;

    @Column(name = "PhoneNumber")
    private String phoneNumber;

    @Column(name = "Permission")
    @Enumerated(value = EnumType.STRING)
    private Permission permission;

}
