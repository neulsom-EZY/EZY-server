package com.server.EZY.model.member.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter @Setter @Builder
@AllArgsConstructor @NoArgsConstructor
public class UsernameChangeDto {

    @NotBlank(message = "username should be valid")
    @Pattern(regexp = "^@[a-zA-Z]*$", message = "유효하지 않은 이름 형식입니다.")
    @Size(min = 1, max = 10)
    private String username;

    @NotBlank(message = "NewUsername should be valid")
    @Pattern(regexp = "^@[a-zA-Z]*$", message = "유효하지 않은 이름 형식입니다.")
    @Size(min = 1, max = 10)
    private String newUsername;
}
