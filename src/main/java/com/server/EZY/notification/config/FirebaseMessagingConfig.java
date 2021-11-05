package com.server.EZY.notification.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.server.EZY.notification.FcmMessage;
import com.server.EZY.notification.service.FirebaseMessagingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Arrays;

/**
 * FCM관련 구성요소를 초기화하는 Config클래스
 * @author 정시원
 * @version 1.0.0
 * @since 1.0.0
 */
@Slf4j
@Configuration
public class FirebaseMessagingConfig {
    private final String PROJECT_ID = "ezy-fcm";
    private final String BASE_URL = "https://fcm.googleapis.com";
    private final String FCM_SEND_ENDPOINT = "/v1/projects/" + PROJECT_ID + "/messages:send";

    private final String MESSAGING_SCOPE = "https://www.googleapis.com/auth/firebase.messaging";
    private final String[] SCOPES = { MESSAGING_SCOPE };

    private final String firebaseConfigPath = "firebase-service-account.json";

    /**
     * FCM메시지를 보내기 위해 프로젝트의 인증정보를 담고있는 FirebaseApp객체를 Spring이 Bean을 로드하는 시점에 초기화 한다.<br>
     *
     * {@link FirebaseApp#getInstance(String)}는 초기화 된 인스턴스가 없으면 {@link IllegalStateException}를 발생시킨다.<br>
     * test code에서 {@link IllegalStateException}("FirebaseApp name [앱 이름] already exists!") 이 발생하여 try-catch문을 함
     * @return FirebaseMessaging FCM푸시 알람을 보낼 수 있는 객체
     * @throws IOException Stream을 얻기위해 I/O작업을 하므로 발생할 수 있다.
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

        try{// FirebaseApp에 PROJECT_ID에 대한 인스턴스가 없을 때
            FirebaseApp.getInstance(PROJECT_ID);
        }catch(IllegalStateException e){
            FirebaseApp.initializeApp(firebaseOptions, PROJECT_ID); // PROJECT_ID라는 이름을 가진 FirebaseApp객체를 생성한다.
        }
    }

    /**
     * FCM메시지를 보내기위한 {@link FirebaseMessaging}객체를 만들어 Bean으로 등록함
     * @return 서버에서 한 번 초기화 한 FirebaseApp 객체로 만든 메시징을 보내기 위한 {@link FirebaseMessaging}객체
     * @see #firebaseAppInit()
     * @author 정시원
     */
    @Bean
    public FirebaseMessaging firebaseMessaging(){
        return FirebaseMessaging.getInstance(FirebaseApp.getInstance(PROJECT_ID));
    }

    /**
     * 이 메서드는 google fcm 서버로 부터 Access token 을 발급 받기 위한 과정입니다.
     * @return Access token.
     * @throws IOException
     * @author 전지환
     */
    public final String getAccessToken() throws IOException {

        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                .createScoped(Arrays.asList(SCOPES));
        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }

    /**
     * test_code 프로필로 FCM push 알람을 보낼 때 테스트로 보내기 위한 메서드  <br>
     * 실제로 push 알람을 보내지 않아도 되는 테스트 코드에서 즉, test_code로 실행될 떄 활성화 된다. <br>
     * @see FirebaseMessagingService#sendToToken(FcmMessage.FcmRequest, String)
     * @see #isFcmTestFalse
     * @return true
     * @author 정시원
     */
    @Profile("test_code")
    @Bean("isFcmTest")
    public boolean isFcmTestTrue(){
        return true;
    }

    /**
     * 실제 운영시 FCM push 알람을 보낼 떄 실제로 보내기 위한 메서드 <br>
     * dev, test, default 프로필 즉, 테스트 혹은 실제 운영시 활성화 된다.
     * @see FirebaseMessagingService#sendToToken(FcmMessage.FcmRequest, String)
     * @see #isFcmTestTrue()
     * @return false
     * @author 정시원
     */
    @Profile({"dev", "test", "default"})
    @Bean("isFcmTest")
    public boolean isFcmTestFalse(){
        return false;
    }

}
