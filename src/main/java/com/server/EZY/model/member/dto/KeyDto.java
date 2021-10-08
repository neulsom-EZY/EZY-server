package com.server.EZY.model.member.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter @Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED) @AllArgsConstructor
public class KeyDto {

    @NotBlank
    @Pattern(regexp = "^[0-9]{4}$")
    @Size(min = 4, max = 4)
    private String key;
}
