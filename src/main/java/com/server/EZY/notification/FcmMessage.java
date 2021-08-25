package com.server.EZY.notification;

import lombok.*;

@Getter @Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FcmMessage {
    private boolean validate_only;
    private Message message;

    /**
     * notification: 모든 mobile os를 아우를 수 있는 noti를 제공
     * token: 특정 device에 알림을 보내기 위해 사용
     */
    @Getter @Builder
    @AllArgsConstructor
    public static class Message {
        private Notification notification;
        private String token;
    }

    @Getter @Builder
    @AllArgsConstructor
    public static class Notification {
        private String title;
        private String body;
        private String image;
    }
}
