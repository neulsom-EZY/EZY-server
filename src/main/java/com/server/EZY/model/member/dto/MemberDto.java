package com.server.EZY.model.member.dto;

import com.server.EZY.model.member.MemberEntity;
import com.server.EZY.model.member.enumType.Role;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Collections;

@Getter @Setter @Builder
@NoArgsConstructor
public class MemberDto {
    @NotBlank(message = "username should be valid")
    @Pattern(regexp = "^@[a-zA-Z]*$")
    @Size(min = 1, max = 10)
    private String username;

    @NotBlank(message = "password should be valid")
    @Size(min = 4, max = 10)
    private String password;

    @NotBlank(message = "phoneNumber should be valid")
    @Pattern(regexp = "^[0-9]{11}$")
    @Size(min = 11, max = 11)
    private String phoneNumber;

    public MemberDto(String username, String password, String phoneNumber) {
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }

    public MemberEntity toEntity(){
        return MemberEntity.builder()
                .username(this.getUsername())
                .password(this.getPassword())
                .phoneNumber(this.getPhoneNumber())
                .roles(Collections.singletonList(Role.ROLE_CLIENT))
                .build();
    }
}
