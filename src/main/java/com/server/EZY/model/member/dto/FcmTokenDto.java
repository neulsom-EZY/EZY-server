package com.server.EZY.model.member.dto;

import lombok.*;

@Getter @Builder
@AllArgsConstructor @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FcmTokenDto {

    private String fcmToken;
}
