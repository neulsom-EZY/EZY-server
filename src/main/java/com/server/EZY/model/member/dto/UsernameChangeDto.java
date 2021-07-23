package com.server.EZY.model.member.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter @Setter @Builder
@AllArgsConstructor @NoArgsConstructor
public class UsernameChangeDto {

    @NotBlank(message = "username should be valid")
    @Size(min = 1, max = 10)
    private String username;

    @NotBlank(message = "NewUsername should be valid")
    @Size(min = 1, max = 10)
    private String newUsername;
}
