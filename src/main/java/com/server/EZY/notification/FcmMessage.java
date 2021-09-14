package com.server.EZY.notification;

import lombok.*;

public class FcmMessage {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MessageResponse{
        private String message;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FcmRequest{
        private String subject;
        private String content;

        public void setRequest(String subject, String content){
            this.subject = subject;
            this.content = content;
        }
    }
}
