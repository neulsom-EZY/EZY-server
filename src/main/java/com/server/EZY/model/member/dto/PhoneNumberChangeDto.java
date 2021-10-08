package com.server.EZY.model.member.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class PhoneNumberChangeDto {

    @NotBlank
    @Pattern(regexp = "^[0-9]{11}$")
    @Size(min = 11, max = 11)
    private String newPhoneNumber;
}
