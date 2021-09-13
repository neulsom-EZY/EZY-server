package com.server.EZY.notification.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

@Service
public class IosPushNotificationsService {
    private static final String firebase_server_key = "AAAA833iS8A:APA91bH8ncfGkXkv0ks00KAwm8voLS8Q1idk5altySnNHy3BWBCAlS0PDjXVUHr2e_aD6jKSY7qW8uApeD3rJEMKsFsucfeatwSBuMfXixGgpnRHLc6fXpCAlbkx8DgnKYTuwl9c_gbd";
    private static final String firebase_api_url = "https://fcm.googleapis.com/fcm/send";

    /**
     * push 전송에서 성능을 위해 비동기로 요청합니다.
     * @param entity
     * @return CompletableFuture
     * @author 전지환
     */
    @Async
    public CompletableFuture<String> send(HttpEntity<String> entity){
        RestTemplate restTemplate = new RestTemplate();

        ArrayList<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();

        interceptors.add(new HeaderRequestInterceptor("Authorization", "key=" + firebase_server_key));
        interceptors.add(new HeaderRequestInterceptor("Content-Type", "application/json; UTF-8 "));
        restTemplate.setInterceptors(interceptors);

        String firebaseResponse = restTemplate.postForObject(firebase_api_url, entity, String.class);

        return CompletableFuture.completedFuture(firebaseResponse);
    }
}
