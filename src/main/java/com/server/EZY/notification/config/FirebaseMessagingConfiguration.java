package com.server.EZY.notification.config;

import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.context.annotation.Bean;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

public class FirebaseMessagingConfiguration {
    private static final String MESSAGING_SCOPE = "https://www.googleapis.com/auth/firebase.messaging";
    private static final String[] SCOPES = { MESSAGING_SCOPE };

    /**
     * 이 메서드는 google fcm 서버로 부터 Access token 을 발급 받기 위한 과정입니다.
     * @return Access token.
     * @throws IOException
     * @author 전지환
     */
    @Bean
    private static String getAccessToken() throws IOException {
        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new FileInputStream("firebase-service-account.json"))
                .createScoped(Arrays.asList(SCOPES));
        googleCredentials.refreshAccessToken();
        return googleCredentials.getAccessToken().getTokenValue();
    }
}
