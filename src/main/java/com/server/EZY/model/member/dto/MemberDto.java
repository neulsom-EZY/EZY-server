package com.server.EZY.model.member.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.server.EZY.model.member.MemberEntity;
import com.server.EZY.model.member.enumType.Permission;
import com.server.EZY.model.member.enumType.Role;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Collections;

@Getter @Setter @Builder
@AllArgsConstructor @NoArgsConstructor
public class MemberDto {
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

    public MemberDto(String nickname, String password, String phoneNumber) {
        this.nickname = nickname;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }

    public MemberEntity toEntity(){
        return MemberEntity.builder()
                .nickname(this.getNickname())
                .password(this.getPassword())
                .phoneNumber(this.getPhoneNumber())
                .permission(Permission.PERMISSION)
                .roles(Collections.singletonList(Role.ROLE_CLIENT))
                .build();
    }
}
