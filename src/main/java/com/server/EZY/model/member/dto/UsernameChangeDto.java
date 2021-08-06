package com.server.EZY.model.member.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter @Setter @Builder
@AllArgsConstructor @NoArgsConstructor
public class UsernameChangeDto {

    @NotBlank
    @Pattern(regexp = "^@[a-zA-Z]*$")
    @Size(min = 1, max = 10)
    private String username;

    @NotBlank
    @Pattern(regexp = "^@[a-zA-Z]*$")
    @Size(min = 1, max = 10)
    private String newUsername;
}
