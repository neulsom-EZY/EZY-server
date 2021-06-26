package com.server.EZY.model.user.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter @Setter
public class PhoneNumberDto {

    @NotBlank(message = "phoneNumber should be valid")
    @Size(min = 11, max = 11)
    private String phoneNumber;
}
