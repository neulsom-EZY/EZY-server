package com.server.EZY.model.member.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UsernameResponseDto {
    private String username;

    @QueryProjection
    public UsernameResponseDto(@NotNull String username) {
        this.username = username;
    }
}
