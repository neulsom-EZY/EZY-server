package com.server.EZY.notification.service;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutureCallback;
import com.google.api.core.ApiFutures;
import com.google.firebase.messaging.*;
import com.server.EZY.exception.fcm_push.FcmPushFailException;
import com.server.EZY.notification.FcmMessage;
import com.server.EZY.notification.config.FirebaseMessagingConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FirebaseMessagingService {

    private final FirebaseMessaging firebaseMessaging;
    /**
     * 해당 필드명은 Bean과 관련이 있으므로 변경하면 안된다!
     * @see FirebaseMessagingConfig#isFcmTestTrue()
     * @see FirebaseMessagingConfig#isFcmTestFalse()
     */
    private final boolean isFcmTest;

    /**
     * FCM registration token을 이용하여 해당 device에 알림을 전송한다.
     * @param fcmMessage FCM메시지를 보내기 위한 DTO
     * @param token FCM registration token 즉 FCM에서 발급한 기기의 토큰이다.
     * @throws FirebaseMessagingException FCM 메시지 전송이 실패했을 경우 throw된다.
     * @author 전지환, 정시원
     */
    public void sendToToken(FcmMessage.FcmRequest fcmMessage, String token) throws FirebaseMessagingException {
        String response = sendToToken(fcmMessage, token, isFcmTest);
        log.info("Successfully sent FCM push notification: {}", response);
    }

    /**
     * FCM registration token을 이용하여 해당 device에 알림을 전송한다.
     * @param fcmMessage FCM메시지를 보내기 위한 DTO
     * @param token FCM registration token 즉 FCM에서 발급한 기기의 토큰이다.
     * @param isFcmTest 실제로 push알람을 보낼지 말지 여부 (해당 토큰의 유효성 검사만 진행)
     * @throws FirebaseMessagingException FCM 메시지 전송이 실패했을 경우 throw된다.
     * @author 전지환, 정시원
     */
    private String sendToToken(FcmMessage.FcmRequest fcmMessage, String token, boolean isFcmTest) throws FirebaseMessagingException {
        // FirebaseMessging으로 푸시알람을 보내기 위한 객체
        Message message = Message.builder()
                .setNotification(
                        Notification.builder()
                                .setTitle(fcmMessage.getTitle())
                                .setBody(fcmMessage.getBody())
                                .build()
                )
                .setApnsConfig(
                        ApnsConfig.builder()
                                .setAps(Aps.builder()
                                        .setSound("default")
                                        .build()
                                ).build()
                )
                .setToken(token)
                .putAllData(fcmMessage.getPayloads())
                .build();

        return firebaseMessaging.send(message, isFcmTest);
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

    // FCM 비동기 처리는 추후 개선할 예정
    /**
     * FCM registration token을 이용하여 해당 device에 알림을 전송한다.
     * @param fcmMessage FCM메시지를 보내기 위한 DTO
     * @param fcmToken FCM registration token 즉 FCM에서 발급한 기기의 토큰이다.
     * @return 해당 알람을 비동기로 처리하는({@link ApiFuture})
     * @throws FirebaseMessagingException FCM 메시지 전송이 실패했을 경우 throw된다.
     * @author 정시원
     */
    @Async
    public ApiFuture<String> sendAsyncToToken(FcmMessage.FcmRequest fcmMessage, String fcmToken) {
        ApiFuture<String> apiFutureOfPushResult = sendAsyncToToken(fcmMessage, fcmToken, isFcmTest);
        return apiFutureOfPushResult;
    }

    /**
     * FCM registration token을 이용하여 해당 device에 알림을 전송한다.
     * @param fcmMessage FCM메시지를 보내기 위한 DTO
     * @param fcmToken FCM registration token 즉 FCM에서 발급한 기기의 토큰이다.
     * @param isTest 해당 FCM push 알람 요청을 테스트로 진행 할 여부
     * @return 해당 알람을 비동기로 처리하는({@link ApiFuture})
     * @throws FirebaseMessagingException FCM 메시지 전송이 실패했을 경우 throw된다.
     * @author 정시원
     */
    @Async
    public ApiFuture<String> sendAsyncToToken(FcmMessage.FcmRequest fcmMessage, String fcmToken, boolean isTest) {
        Message message = Message.builder()
                .setNotification(
                        Notification.builder()
                                .setTitle(fcmMessage.getTitle())
                                .setBody(fcmMessage.getBody())
                                .build()
                )
                .setToken(fcmToken)
                .putAllData(fcmMessage.getPayloads())
                .build();

        return firebaseMessaging.sendAsync(message, isTest);
    }
}
