package com.server.EZY.notification.service.feature;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.server.EZY.model.member.repository.MemberRepository;
import com.server.EZY.notification.FcmMessage;
import com.server.EZY.notification.dto.FcmSourceDto;
import com.server.EZY.notification.enum_type.FcmActionSelector;
import com.server.EZY.notification.enum_type.FcmPurposeType;
import com.server.EZY.notification.service.FirebaseMessagingService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author 전지환
 * @version 1.0.0
 */
@Slf4j
@Service
@AllArgsConstructor
public class FcmMakerService {
    private final FirebaseMessagingService firebaseMessagingService;
    private final MemberRepository memberRepository;
    /**
     * [목적: 심부름, 역할: 보내는사람] 에 만족하는 메소드
     * @param fcmSourceDto
     * @throws FirebaseMessagingException
     */
    public void sendErrandFcm(FcmSourceDto fcmSourceDto) throws FirebaseMessagingException {
        FcmMessage.FcmRequest request = FcmMessage.FcmRequest.builder()
                .title("누군가 " + fcmSourceDto.getFcmPurposeType() + "을 " + FcmActionSelector.ErrandAction.요청 + " 했어요!")
                .body(fcmSourceDto.getSender() + "님이 " + FcmActionSelector.ErrandAction.요청+"한 "+fcmSourceDto.getFcmPurposeType()+"을 확인해보세요!")
                .build();

        firebaseMessagingService.sendToToken(request, findRecipientFcmToken(fcmSourceDto.getRecipient()));
    }

    /**
     * 받는사람, fcmToken을 찾아주는 메소드
     * @param recipient
     * @return recipientFcmToken
     */
    private String findRecipientFcmToken(String recipient){
        return memberRepository.findByUsername(recipient).getFcmToken();
    }
}
