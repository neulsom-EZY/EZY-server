package com.server.EZY.notification;

import lombok.*;

public class FcmMessage {
    @Getter @Builder
    @AllArgsConstructor
    public static class MessageResponse{
        private String validateOnly; // 실제로 메시지를 전달하지 않고 요청을 테스트하기위한 플래그입니다.
        private String message;
    }

    @Getter @Builder
    @AllArgsConstructor
    public static class FcmRequest{
        private String title;
        private String body;
    }
}
