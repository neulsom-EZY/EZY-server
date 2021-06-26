package com.server.EZY.model.user.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter @Setter
public class PasswordChangeDto {

    @NotBlank(message = "nickname should be valid")
    @Size(min = 1, max = 10)
    private String nickname;

    @NotBlank(message = "currentPassword should be valid")
    @Size(min = 4, max = 10)
    private String currentPassword;

    @NotBlank(message = "newPassword should be valid")
    @Size(min = 4, max = 10)
    private String newPassword;
}
