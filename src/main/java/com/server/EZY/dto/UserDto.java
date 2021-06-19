package com.server.EZY.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.server.EZY.model.user.Permission;
import com.server.EZY.model.user.Role;
import com.server.EZY.model.user.UserEntity;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Collections;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    @NotBlank(message = "nickname should be valid")
    @Size(min = 1, max = 10)
    private String nickname;

    @NotBlank(message = "password should be valid")
    @Size(min = 4, max = 10)
    private String password;

    @NotBlank(message = "phoneNumber should be valid")
    @Size(min = 11, max = 11)
    private String phoneNumber;

    @JsonIgnore
    private Permission permission;

    public UserDto(String nickname, String password, String phoneNumber) {
        this.nickname = nickname;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }

    public UserEntity toEntity(){
        return UserEntity.builder()
                .nickname(this.getNickname())
                .password(this.getPassword())
                .phoneNumber(this.getPhoneNumber())
                .permission(Permission.PERMISSION)
                .roles(Collections.singletonList(Role.ROLE_CLIENT))
                .build();
    }
}
