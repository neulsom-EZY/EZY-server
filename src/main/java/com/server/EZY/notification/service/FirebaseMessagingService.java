package com.server.EZY.notification.service;

import com.google.api.core.ApiFuture;
import com.google.firebase.messaging.*;
import com.server.EZY.notification.FcmMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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
                        Notification.builder()
                                .setTitle(fcmMessage.getTitle())
                                .setBody(fcmMessage.getBody())
                                .build()
                )
                .setToken(token)
                .build();

        String response = firebaseMessaging.send(message);
        log.info("Successfully sent message: {}", response);
    }

    /**
     * FCM registration token을 이용하여 해당 device에 알림을 전송한다.
     * @param fcmMessage FCM메시지를 보내기 위한 DTO
     * @param fcmToken FCM registration token 즉 FCM에서 발급한 기기의 토큰이다.
     * @param payload 해당 push 알람에대한 추가적인 페이로드
     * @return 해당 알람을 비동기로 처리하는({@link ApiFuture})
     * @throws FirebaseMessagingException FCM 메시지 전송이 실패했을 경우 throw된다.
     * @author 정시원
     */
    @Async
    public ApiFuture<String> sendToToken (FcmMessage.FcmRequest fcmMessage, String fcmToken, Map<String, String> payload) {
        return sendToToken(fcmMessage, fcmToken, payload, false);
    }

    /**
     * FCM registration token을 이용하여 해당 device에 알림을 전송한다.
     * @param fcmMessage FCM메시지를 보내기 위한 DTO
     * @param fcmToken FCM registration token 즉 FCM에서 발급한 기기의 토큰이다.
     * @param payload 해당 push 알람에대한 추가적인 페이로드
     * @param isTest 해당 FCM push 알람 요청을 테스트로 진행 할 여부
     * @return 해당 알람을 비동기로 처리하는({@link ApiFuture})
     * @throws FirebaseMessagingException FCM 메시지 전송이 실패했을 경우 throw된다.
     * @author 정시원
     */
    @Async
    public ApiFuture<String> sendToToken (FcmMessage.FcmRequest fcmMessage, String fcmToken, Map<String, String> payload, boolean isTest) {
        Message message = Message.builder()
                .setNotification(
                        Notification.builder()
                                .setTitle(fcmMessage.getTitle())
                                .setBody(fcmMessage.getBody())
                                .build()
                )
                .setToken(fcmToken)
                .putAllData(payload)
                .build();

        return firebaseMessaging.sendAsync(message, isTest);
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
                        Notification.builder()
                                .setTitle(fcmMessage.getTitle())
                                .setBody(fcmMessage.getBody())
                                .build()
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
