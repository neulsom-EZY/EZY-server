package com.server.EZY.model.member.dto;

import com.server.EZY.model.member.enumType.Role;
import com.server.EZY.model.member.MemberEntity;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Collections;

@Getter @Setter
@NoArgsConstructor
@Builder
public class AuthDto {

    @NotBlank(message = "username should be valid")
    @Size(min = 1, max = 10)
    private String username;

    @NotBlank(message = "password should be valid")
    @Size(min = 4, max = 10)
    private String password;

    public AuthDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public MemberEntity toEntity(){
        return MemberEntity.builder()
                .username(this.getUsername())
                .password(this.getPassword())
                .roles(Collections.singletonList(Role.ROLE_CLIENT))
                .build();
    }
}
