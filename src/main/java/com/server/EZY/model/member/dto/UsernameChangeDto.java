package com.server.EZY.model.member.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter @Setter @Builder
@AllArgsConstructor @NoArgsConstructor
public class UsernameChangeDto {

    @NotBlank(message = "nickname should be valid")
    @Size(min = 1, max = 10)
    private String username;

    @NotBlank(message = "NewNickname should be valid")
    @Size(min = 1, max = 10)
    private String newUsername;
}
