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

    private final FirebaseMessaging firebaseMessaging;

    /**
     * FCM registration token을 이용하여 해당 device에 알림을 전송한다.
     * @param fcmMessage FCM메시지를 보내기 위한 DTO
     * @param token FCM registration token 즉 FCM에서 발급한 기기의 토큰이다.
     * @throws FirebaseMessagingException FCM 메시지 전송이 실패했을 경우 throw된다.
     * @author 전지환, 정시원
     */
    public void sendToToken (FcmMessage.FcmRequest fcmMessage, String token) throws FirebaseMessagingException {

        // FirebaseMessging으로 푸시알람을 보내기 위한 객체
        Message message = Message.builder()
                .setNotification(
                        new Notification(
                                fcmMessage.getTitle(),
                                fcmMessage.getBody())
                )
//                .putData("title", fcmMessage.getTitle()) // putData는 추가적인 데이터를 보내고 싶을 때 사용한다.
//                .putData("body", fcmMessage.getBody())
                .setToken(token)
                .build();

        String response = firebaseMessaging.send(message);
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
                .setNotification(
                        new Notification(
                                fcmMessage.getTitle(),
                                fcmMessage.getBody())
                )
                .addAllTokens(tokens)
                .build();

        BatchResponse batchResponse = firebaseMessaging.sendMulticast(message);
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
                                        .setTitle(fcmMessage.getTitle())
                                        .setBody(fcmMessage.getBody())
                                        .build())
                                .setBadge(42)
                                .build())
                        .build())
                .build();
        // [END apns_message]
        return message;
    }
}
