package com.server.EZY.notification.config;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.net.HttpURLConnection;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
public class FirebaseMessagingConfigTest {
    @Test
    @DisplayName("token이 잘 가져와 지나요?")
    public void getAccessToken() throws IOException {
        String accessToken = FirebaseMessagingConfig.getAccessToken();
        log.info("=========fcm AccessToken: {}===========", accessToken);
    }

    @Test
    @DisplayName("getConnection method 테스트")
    public void getConnection_fcm() throws IOException {
        HttpURLConnection connection = FirebaseMessagingConfig.getConnection();
        assertTrue(connection!=null);
    }
}
