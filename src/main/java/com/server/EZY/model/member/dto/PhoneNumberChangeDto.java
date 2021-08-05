package com.server.EZY.model.member.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class PhoneNumberChangeDto {

    @NotBlank(message = "username should be valid")
    @Pattern(regexp = "^@[a-zA-Z]*$")
    @Size(min = 1, max = 10)
    private String username;

    @NotBlank(message = "phoneNumber should be valid")
    @Pattern(regexp = "^[0-9]{11}$")
    @Size(min = 11, max = 11)
    private String newPhoneNumber;
}
