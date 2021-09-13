package com.server.EZY.notification.config;

import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.context.annotation.Bean;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

<<<<<<< HEAD:src/main/java/com/server/EZY/notification/config/FirebaseMessagingConfig.java
public class FirebaseMessagingConfig {
=======
public class FirebaseMessagingConfiguration {
    private static final String PROJECT_ID = "<YOUR-PROJECT-ID>";
    private static final String BASE_URL = "https://fcm.googleapis.com";
    private static final String FCM_SEND_ENDPOINT = "/v1/projects/" + PROJECT_ID + "/messages:send";

>>>>>>> c890763cd9e73e6c6b5906dfee8f8c61bc1e480e:src/main/java/com/server/EZY/notification/config/FirebaseMessagingConfiguration.java
    private static final String MESSAGING_SCOPE = "https://www.googleapis.com/auth/firebase.messaging";
    private static final String[] SCOPES = { MESSAGING_SCOPE };

    /**
     * 이 메서드는 google fcm 서버로 부터 Access token 을 발급 받기 위한 과정입니다.
     * 이 메서드를 통해서 user device 에 대한 token 을 가져옵니다.
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

    /**
     * HTTP 요청 헤더에 Access token 을 추가하는 메서드 입니다.
     * FCM에 대한 액세스를 승인하려면 https://www.googleapis.com/auth/firebase.messaging 범위를 요청하면 됩니다.
     * @return httpURLConnection
     * @throws IOException
     * @author 전지환
     */
    @Bean
    private static HttpURLConnection getConnection() throws IOException {
        // [START use_access_token]
        URL url = new URL(BASE_URL + FCM_SEND_ENDPOINT);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestProperty("Authorization", "Bearer " + getAccessToken());
        httpURLConnection.setRequestProperty("Content-Type", "application/json; UTF-8");
        return httpURLConnection;
        // [END use_access_token]
    }
}
