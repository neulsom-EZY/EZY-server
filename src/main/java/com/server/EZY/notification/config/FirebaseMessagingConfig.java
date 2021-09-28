package com.server.EZY.notification.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Arrays;

@Slf4j
@Configuration
public class FirebaseMessagingConfig {
    public static final String PROJECT_ID = "ezy-fcm";
    private final String BASE_URL = "https://fcm.googleapis.com";
    private final String FCM_SEND_ENDPOINT = "/v1/projects/" + PROJECT_ID + "/messages:send";

    private final String MESSAGING_SCOPE = "https://www.googleapis.com/auth/firebase.messaging";
    private final String[] SCOPES = { MESSAGING_SCOPE };

    private final String firebaseConfigPath = "firebase-service-account.json";

    /**
     * FCM메시지를 보내기 위해 프로젝트의 인증정보를 담고있는 FirebaseApp객체를 Spring이 Bean을 로드하는 시점에 초기화 하는 객체.<br>
     *
     * {@link FirebaseApp#getInstance(String)}는 초기화 된 인스턴스가 없으면 {@link IllegalStateException}를 발생시킵니다.<br>
     * test code에서 {@link IllegalStateException}("FirebaseApp name [앱 이름] already exists!") 이 발생하여 try-catch문을 작성했습니다.
     * @return FirebaseMessaging FCM푸시 알람을 보낼 수 있는 객체
     * @throws IOException Stream을 얻기위해 I/O작업을 하므로 발생할 수 있습니다.
     * @author 정시원
     */
    @PostConstruct
    public void firebaseAppInit() throws IOException {
        log.info("FirebaseAppInit");
        // 프로젝트 인증정보를 담은 객체
        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                .createScoped(Arrays.asList(SCOPES));

        // 프로젝트의 인증정보 그리고 프로젝트 아이디를 가지고 있는 객체
        FirebaseOptions firebaseOptions = FirebaseOptions
                .builder()
                .setCredentials(googleCredentials)
                .setProjectId(PROJECT_ID)
                .build();

        try{
            FirebaseApp.getInstance(PROJECT_ID); // FirebaseApp에 PROJECT_ID에 대한 인스턴스가 없을 때
        }catch(IllegalStateException e){
            FirebaseApp.initializeApp(firebaseOptions, PROJECT_ID);
        }
    }
}
