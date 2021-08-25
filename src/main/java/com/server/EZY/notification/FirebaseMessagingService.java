package com.server.EZY.notification;

import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.List;

public class FirebaseMessagingService {
    private String getAccessToken() throws IOException{
        String firebaseConfigPath = "resources/ezy-fcm-firebase-adminsdk-key.json";

        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }
}
