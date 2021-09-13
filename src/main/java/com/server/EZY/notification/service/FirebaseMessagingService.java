package com.server.EZY.notification.service;

import com.google.firebase.messaging.*;
import com.server.EZY.notification.FcmMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FirebaseMessagingService {
    /**
     * Client device-token 을 이용하여 해당 device 에 알림을 전송합니다.
     * @param fcmMessage
     * @param token
     * @throws FirebaseMessagingException
     * @author 전지환
     */
    public void sendToToken (FcmMessage.FcmRequest fcmMessage, String token) throws FirebaseMessagingException{
        // [START send_to_token]
        Message message = Message.builder()
                .putData("subject", fcmMessage.getSubject())
                .putData("content", fcmMessage.getContent())
                .setToken(token)
                .build();

        String response = FirebaseMessaging.getInstance().send(message);
        log.info("Successfully sent message: {}", response);
    }

    /**
     * 여러 기기에 메시지를 전송하는 method
     * @param fcmMessage
     * @param tokens
     * @throws FirebaseMessagingException
     * @author 전지환
     */
    public void sendMulticast(FcmMessage.FcmRequest fcmMessage, List<String> tokens) throws FirebaseMessagingException {
        // [START send_multicast]
        MulticastMessage message = MulticastMessage.builder()
                .putData("subject", fcmMessage.getSubject())
                .putData("content", fcmMessage.getContent())
                .addAllTokens(tokens)
                .build();

        BatchResponse batchResponse = FirebaseMessaging.getInstance().sendMulticast(message);
        log.info("{}  messages were sent successfully", batchResponse);
        // [END send_multicast]
    }

    /**
     * 색상 및 아이콘 옵션이 있는 알림 메시지
     * @param fcmMessage
     * @return message
     * @author 전지환
     */
    public Message apnsMessage(FcmMessage.FcmRequest fcmMessage){
        // [START apns_message]
        Message message = Message.builder()
                .setApnsConfig(ApnsConfig.builder()
                        .putHeader("apns-priority", "10")
                        .setAps(Aps.builder()
                                .setAlert(ApsAlert.builder()
                                        .setTitle(fcmMessage.getSubject())
                                        .setBody(fcmMessage.getContent())
                                        .build())
                                .setBadge(42)
                                .build())
                        .build())
                .build();
        // [END apns_message]
        return message;
    }
}
