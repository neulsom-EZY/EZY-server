package com.server.EZY.notification.service;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.server.EZY.notification.FcmMessage;
import org.junit.jupiter.api.Test;
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
                .title("안녕하세요")
                .body("Hello world!")
                .build();

        // 김유진의 FCM registration token
        String token = "dBzseFuYD0dCv2-AoLOA_9:APA91bE2q3aMdjvA3CIEKouMujj4E7V_t6aKM6RFxmrCwKCDOXeB39wasAk2uEhcGo3OTU2hr2Ap4NLbKRnsaQfxeRJnF_IZ9ReOUXSCAFIuJB3q1fgfKado3al15yJQkebGU6JSfxSL";

        //When
        firebaseMessagingService.sendToToken(sayHello, token);
    }
}
