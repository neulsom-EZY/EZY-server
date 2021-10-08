package com.server.EZY.model.member.dto;

import com.server.EZY.model.member.MemberEntity;
import com.server.EZY.model.member.enum_type.Role;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Collections;

@Getter @Setter @Builder
@NoArgsConstructor
public class MemberDto {
    @NotBlank
    @Pattern(regexp = "^@[a-zA-Z]*$")
    @Size(min = 1, max = 10)
    private String username;

    @NotBlank
    @Size(min = 8)
    private String password;

    @NotBlank
    @Pattern(regexp = "^[0-9]{11}$")
    @Size(min = 11, max = 11)
    private String phoneNumber;

    private String fcmToken;

    public MemberDto(String username, String password, String phoneNumber, String fcmToken) {
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.fcmToken = fcmToken;
    }

    public MemberEntity toEntity(){
        return MemberEntity.builder()
                .username(this.getUsername())
                .password(this.getPassword())
                .phoneNumber(this.getPhoneNumber())
                .fcmToken(this.getFcmToken())
                .roles(Collections.singletonList(Role.ROLE_CLIENT))
                .build();
    }
}
