package com.server.EZY.model.member.dto;

import lombok.*;

@Getter @Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED) @AllArgsConstructor
public class UsernameDto {

    private String username;
}
