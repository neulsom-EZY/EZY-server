package com.server.EZY.notification.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

@Component
public class FirebaseMessagingConfig {
    private final String PROJECT_ID = "ezy-fcm";
    private final String BASE_URL = "https://fcm.googleapis.com";
    private final String FCM_SEND_ENDPOINT = "/v1/projects/" + PROJECT_ID + "/messages:send";

    private final String MESSAGING_SCOPE = "https://www.googleapis.com/auth/firebase.messaging";
    private final String[] SCOPES = { MESSAGING_SCOPE };

    private final String firebaseConfigPath = "firebase-service-account.json";

    /**
     * FCM메시지를 보내기 위해 FirebaseMessaging객체를 설정 후 Bean으로 등록해 IoC가 관리하도록 하는 매서드
     * @return FirebaseMessaging
     * @throws IOException -
     * @author 정시원
     */
    @Bean
    private FirebaseMessaging firebaseMessaging() throws IOException {
        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                .createScoped(Arrays.asList(SCOPES));
        FirebaseOptions firebaseOptions = FirebaseOptions
                .builder()
                .setCredentials(googleCredentials)
                .build();

        FirebaseApp app = FirebaseApp.initializeApp(firebaseOptions, PROJECT_ID);
        return FirebaseMessaging.getInstance(app);
    }

    /**
     * 이 메서드는 google fcm 서버로 부터 Access token 을 발급 받기 위한 과정입니다.
     * @return Access token.
     * @throws IOException
     * @author 전지환
     */
    public final String getAccessToken() throws IOException {
      
        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                .createScoped(Arrays.asList(SCOPES));
        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }

    /**
     * HTTP 요청 헤더에 Access token 을 추가하는 메서드 입니다.
     * FCM에 대한 액세스를 승인하려면 https://www.googleapis.com/auth/firebase.messaging 범위를 요청하면 됩니다.
     * @return httpURLConnection
     * @throws IOException
     * @author 전지환
     */
    public final HttpURLConnection getConnection() throws IOException {
        // [START use_access_token]
        URL url = new URL(BASE_URL + FCM_SEND_ENDPOINT);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestProperty("Authorization", "Bearer " + getAccessToken());
        httpURLConnection.setRequestProperty("Content-Type", "application/json; UTF-8");
        return httpURLConnection;
        // [END use_access_token]
    }
}
