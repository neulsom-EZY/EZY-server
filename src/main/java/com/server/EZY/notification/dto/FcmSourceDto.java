package com.server.EZY.notification.dto;

import com.server.EZY.notification.enum_type.FcmPurposeType;
import lombok.*;
import org.jetbrains.annotations.NotNull;

@Builder
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FcmSourceDto {
    @NotNull
    private String sender;
    @NotNull
    private String recipient;
    @NotNull
    private FcmPurposeType fcmPurposeType;
}
