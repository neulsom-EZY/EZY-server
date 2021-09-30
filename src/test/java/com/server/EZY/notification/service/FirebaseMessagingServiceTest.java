package com.server.EZY.notification.service;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.server.EZY.notification.FcmMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
public class FirebaseMessagingServiceTest {

    @Autowired
    private FirebaseMessagingService firebaseMessagingService;

    @Test @Disabled
    public void sendToToken() throws FirebaseMessagingException {
        //Given
        FcmMessage.FcmRequest sayHello = FcmMessage.FcmRequest.builder()
                .title("EZY의 세상에 오신 것을 환영합니다!")
                .body("Hello EZY world!")
                .build();

        // 노연주의 FCM registration token
        String token = "e60Y1Mnh400Zu_YNhYWEMP:APA91bFCwTIOvgM3CSe9ZH_mLPLeJrL5xE_TPrHQF3UqT_qVMQv_Q0ZsqshExasODCXpMeFZJ2al3dLCJv2n3f4fj-HqHZ7_DPnsKA01NZfHAcZFPGCmt7U0XJCR40_wii0gpiBzvI0Q";

        //When
        firebaseMessagingService.sendToToken(sayHello, token);
    }

    @Test @DisplayName("다중 기기에 동시 전달")
    public void sendTokenMultiDevice() throws FirebaseMessagingException {
        FcmMessage.FcmRequest sayHello = FcmMessage.FcmRequest.builder()
                .title("EZY의 세상에 오신 여러분들, 환영합니다!")
                .body("Hello EZY world!")
                .build();

        ArrayList<String> deviceList = new ArrayList<>();
        deviceList.add("e60Y1Mnh400Zu_YNhYWEMP:APA91bFCwTIOvgM3CSe9ZH_mLPLeJrL5xE_TPrHQF3UqT_qVMQv_Q0ZsqshExasODCXpMeFZJ2al3dLCJv2n3f4fj-HqHZ7_DPnsKA01NZfHAcZFPGCmt7U0XJCR40_wii0gpiBzvI0Q");
        deviceList.add("e60Y1Mnh400Zu_YNhYWEMP:APA91bFCwTIOvgM3CSe9ZH_mLPLeJrL5xE_TPrHQF3UqT_qVMQv_Q0ZsqshExasODCXpMeFZJ2al3dLCJv2n3f4fj-HqHZ7_DPnsKA01NZfHAcZFPGCmt7U0XJCR40_wii0gpiBzvI0Q");
        deviceList.add("eQb5CygpsUahmPBRDnTc0N:APA91bFaOlt2nZDJKJpO8dZsjS8vSDCZKxZWYBWtNXYUiIiUxLPiGTLcXuyuVTW1uqOxu55Ay9z_1ss-D2uz2xP-C_R2-5yxyV2pqn88zYts4WSxS4pgWgdvFtBAG6nU__dSYH7WW8Qk");

        firebaseMessagingService.sendMulticast(sayHello, deviceList);
    }
}
