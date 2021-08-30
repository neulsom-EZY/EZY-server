package com.server.EZY.notification.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.server.EZY.notification.FcmMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FirebaseMessagingService {
    private final FirebaseMessaging firebaseMessaging;

    public void sendToToken (FcmMessage fcmMessage, String token) throws FirebaseMessagingException{
        // [START send_to_token]
        Message message = Message.builder()
                .putData("subject", fcmMessage.getSubject())
                .putData("content", fcmMessage.getContent())
                .setToken(token)
                .build();

        String response = FirebaseMessaging.getInstance().send(message);
        log.info("Successfully sent message: {}", response);
    }
}
