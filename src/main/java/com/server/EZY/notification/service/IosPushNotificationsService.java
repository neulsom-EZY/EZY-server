package com.server.EZY.notification.service;

import com.server.EZY.notification.config.FirebaseMessagingConfig;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class IosPushNotificationsService {

    private final FirebaseMessagingConfig firebaseMessagingConfig;

    private static final String firebase_api_url = "https://fcm.googleapis.com/fcm/send";

    /**
     * push 전송에서 성능을 위해 비동기로 요청합니다.
     * @param entity
     * @return CompletableFuture
     * @author 전지환
     */
    @Async
    public CompletableFuture<String> send(HttpEntity<String> entity) throws IOException {
        RestTemplate restTemplate = new RestTemplate();

        ArrayList<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        final String accessToken = firebaseMessagingConfig.getAccessToken();
        interceptors.add(new HeaderRequestInterceptor(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken));
        interceptors.add(new HeaderRequestInterceptor(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8"));
        restTemplate.setInterceptors(interceptors);

        String firebaseResponse = restTemplate.postForObject(firebase_api_url, entity, String.class);

        return CompletableFuture.completedFuture(firebaseResponse);
    }
}
