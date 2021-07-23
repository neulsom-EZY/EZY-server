package com.server.EZY.model.member.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class PasswordChangeDto {

    @NotBlank(message = "nickname should be valid")
    @Size(min = 1, max = 10)
    private String username;

    @NotBlank(message = "newPassword should be valid")
    @Size(min = 4, max = 10)
    private String newPassword;
}
