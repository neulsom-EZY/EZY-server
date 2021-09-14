package com.server.EZY.notification.config;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class FirebaseMessagingConfigTest {

    @Autowired
    private FirebaseMessagingConfig firebaseMessagingConfig;

    @Test
    @DisplayName("token이 잘 가져와 지나요?")
    void getAccessToken(){

    }
}
