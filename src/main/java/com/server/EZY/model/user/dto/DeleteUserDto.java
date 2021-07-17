package com.server.EZY.model.user.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class DeleteUserDto {

    @NotBlank(message = "nickname should be valid")
    @Size(min = 1, max = 10)
    private String nickname;

    @NotBlank(message = "password should be valid")
    @Size(min = 4, max = 10)
    private String password;
}
