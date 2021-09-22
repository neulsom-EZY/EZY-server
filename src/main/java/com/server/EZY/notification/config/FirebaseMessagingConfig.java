package com.server.EZY.notification.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;

public class FirebaseMessagingConfig {
    private final String PROJECT_ID = "ezy-fcm";
    private final String BASE_URL = "https://fcm.googleapis.com";
    private final String FCM_SEND_ENDPOINT = "/v1/projects/" + PROJECT_ID + "/messages:send";

    private final String MESSAGING_SCOPE = "https://www.googleapis.com/auth/firebase.messaging";
    private final String[] SCOPES = { MESSAGING_SCOPE };

    private final String firebaseConfigPath = "firebase-service-account.json";

    /**
     * FCM메시지를 보내기 위해 프로젝트의 인증정보를 담고 푸시알람을 보낼 수 있는 FirebaseMessaging객체를 Bean으로 등록합니다.
     * @return FirebaseMessaging FCM푸시 알람을 보낼 수 있는 객체
     * @throws IOException -
     * @author 정시원
     */
    @Bean
    private FirebaseMessaging firebaseMessagingInit() throws IOException {
        // 프로젝트 인증정보를 담은 객체
        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                .createScoped(Arrays.asList(SCOPES));

        // 프로젝트의 인증정보 그리고 프로젝트 아이디를 가지고 있는 객체
        FirebaseOptions firebaseOptions = FirebaseOptions
                .builder()
                .setCredentials(googleCredentials)
                .setProjectId(PROJECT_ID)
                .build();

        FirebaseApp app = FirebaseApp.initializeApp(firebaseOptions);
        return FirebaseMessaging.getInstance(app);
    }
}
