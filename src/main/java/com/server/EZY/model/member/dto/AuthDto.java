package com.server.EZY.model.member.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.server.EZY.model.member.enumType.Permission;
import com.server.EZY.model.member.enumType.Role;
import com.server.EZY.model.member.MemberEntity;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Collections;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthDto {

    @NotBlank(message = "nickname should be valid")
    @Size(min = 1, max = 10)
    private String nickname;

    @NotBlank(message = "password should be valid")
    @Size(min = 4, max = 10)
    private String password;

    @JsonIgnore
    private Permission permission;

    public AuthDto(String nickname, String password) {
        this.nickname = nickname;
        this.password = password;
    }

    public MemberEntity toEntity(){
        return MemberEntity.builder()
                .nickname(this.getNickname())
                .password(this.getPassword())
                .permission(Permission.PERMISSION)
                .roles(Collections.singletonList(Role.ROLE_CLIENT))
                .build();
    }
}
