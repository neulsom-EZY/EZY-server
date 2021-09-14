package com.server.EZY.notification.config;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class FirebaseMessagingConfigTest {
    @Test
    @DisplayName("token이 잘 가져와 지나요?")
    public void getAccessToken() throws IOException {
        String accessToken = FirebaseMessagingConfig.getAccessToken();
    }
}
