package com.server.EZY.notification.service;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.server.EZY.notification.FcmMessage;
import org.junit.Test;
import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

@SpringBootTest
@Transactional
public class FirebaseMessagingServiceTest {

    @Autowired
    private FirebaseMessagingService firebaseMessagingService;

    @Test @Disabled
    public void sendToToken() throws FirebaseMessagingException {
        //Given
        FcmMessage.FcmRequest sayHello = FcmMessage.FcmRequest.builder()
                .subject("안녕하세요")
                .content("Hello world!")
                .build();

        String token = "38862C718A0C40968254F8EB0E2E64156E35A8BAB91CE1664D9E19B530DEA8F6";

        //When
        firebaseMessagingService.sendToToken(sayHello, token);
    }
}
