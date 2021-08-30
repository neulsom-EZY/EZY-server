package com.server.EZY.notification;

import lombok.*;

import java.util.Map;

@Getter @Setter @Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FcmMessage {
    private String subject;
    private String content;
    private Map<String, String> data;
    private String image;
}
