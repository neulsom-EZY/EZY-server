package com.server.EZY.notification;

import lombok.*;

import java.util.Map;

@Data
public class FcmMessage {
    private String subject;
    private String content;
    private Map<String, String> data;
}
