package com.server.EZY.model.user.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NicknameDto {

    @NotBlank(message = "nickname should be valid")
    @Size(min = 1, max = 10)
    private String nickname;

    @NotBlank(message = "NewNickname should be valid")
    @Size(min = 1, max = 10)
    private String NewNickname;
}
