package com.server.EZY.notification.service.feature;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.server.EZY.model.member.repository.MemberRepository;
import com.server.EZY.notification.FcmMessage;
import com.server.EZY.notification.dto.FcmSourceDto;
import com.server.EZY.notification.enum_type.FcmActionSelector;
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
     * 심부름 수락 정보를 발신자에게 push알람을 보낸다.
     *
     * @param fcmSourceDto push알람의 생성에 기본적인 정보를 가지고 있는 DTO
     * @throws FirebaseMessagingException 해당 push알람이 실패할 때
     * @author 정시원
     */
    public void sendAcceptErrandFcmToSender(FcmSourceDto fcmSourceDto) throws FirebaseMessagingException {
        FcmMessage.FcmRequest request = makeErrandFcmMessage(fcmSourceDto, fcmSourceDto.getRecipient(), FcmActionSelector.ErrandAction.승인);

        firebaseMessagingService.sendToToken(request, findRecipientFcmToken(fcmSourceDto.getSender()));
    }

    /**
     * 심부름 거절 정보를 발신자에게 push알람을 보낸다.
     *
     * @param fcmSourceDto push알람의 생성에 기본적인 정보를 가지고 있는 DTO
     * @throws FirebaseMessagingException 해당 push알람이 실패할 때
     * @author 정시원
     */
    public void sendRefuseErrandFcmToSender(FcmSourceDto fcmSourceDto) throws FirebaseMessagingException {
        FcmMessage.FcmRequest request = makeErrandFcmMessage(fcmSourceDto, fcmSourceDto.getRecipient(), FcmActionSelector.ErrandAction.거절);

        firebaseMessagingService.sendToToken(request, findRecipientFcmToken(fcmSourceDto.getSender()));
    }

    /**
     * 심부름관련 push알람을 보내기 위헤 사용되는 {@link FcmMessage.FcmRequest}객체를 만든다.
     *
     * @param fcmSourceDto push알람의 생성에 기본적인 정보를 가지고 있는 DTO
     * @param senderOfPush 해당 push알람을 보내는 회원의 username
     * @param errandAction 심부름에서 하려는 기능
     * @return fcmPush알람의 메시지를 담고 있는 {@link FcmMessage.FcmRequest}객체
     * @author 정시원
     */
    private FcmMessage.FcmRequest makeErrandFcmMessage(FcmSourceDto fcmSourceDto, String senderOfPush, FcmActionSelector.ErrandAction errandAction){
        return FcmMessage.FcmRequest.builder()
                .title("누군가 " + fcmSourceDto.getFcmPurposeType() + "을(를) " + errandAction + " 했어요!")
                .body(senderOfPush + "님이 " + errandAction+"한 "+fcmSourceDto.getFcmPurposeType()+"을 확인해보세요!")
                .build();
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
