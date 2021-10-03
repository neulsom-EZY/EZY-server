package com.server.EZY.model.plan.errand.service;

import com.server.EZY.model.member.MemberEntity;
import com.server.EZY.model.member.repository.MemberRepository;
import com.server.EZY.model.plan.errand.ErrandEntity;
import com.server.EZY.model.plan.errand.ErrandStatusEntity;
import com.server.EZY.model.plan.errand.dto.ErrandSetDto;
import com.server.EZY.model.plan.errand.enum_type.ErrandResponseStatus;
import com.server.EZY.model.plan.errand.enum_type.ErrandRole;
import com.server.EZY.model.plan.errand.repository.ErrandRepository;
import com.server.EZY.notification.FcmMessage;
import com.server.EZY.notification.dto.FcmSourceDto;
import com.server.EZY.notification.enum_type.FcmPurposeType;
import com.server.EZY.notification.enum_type.FcmRole;
import com.server.EZY.notification.service.ActiveFcmFilterService;
import com.server.EZY.util.CurrentUserUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author 전지환
 * @version 1.0.0
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ErrandServiceImpl implements ErrandService{
    private final CurrentUserUtil currentUserUtil;
    private final MemberRepository memberRepository;
    private final ErrandRepository errandRepository;
    private final ActiveFcmFilterService activeFcmFilterService;

    /**
     * 이 메서드는 심부름을 전송(저장) 할 때 사용하는 비즈니스 로직입니다.
     * @param errandSetDto
     * @return ErrandEntity
     * @author 전지환
     */
    @Override
    public ErrandEntity sendErrand(ErrandSetDto errandSetDto) throws Exception {
        /**
         * sender: 보내는 사람
         * recipient: 받는 사람
         */
        MemberEntity sender = currentUserUtil.getCurrentUser();
        MemberEntity recipient = memberRepository.findByUsername(errandSetDto.getRecipient());

        ErrandStatusEntity errandStatusEntity = ErrandStatusEntity.builder()
                .senderIdx(sender.getMemberIdx())
                .recipientIdx(recipient.getMemberIdx())
                .errandResponseStatus(ErrandResponseStatus.NOT_READ)
                .build();

        ErrandEntity savedErrandEntity = errandRepository.save(errandSetDto.saveToEntity(sender, errandStatusEntity));

        // 여기서 FCM 스펙을 정의 함.
        FcmSourceDto fcmSourceDto = FcmSourceDto.builder()
                .sender(sender.getUsername())
                .recipient(recipient.getUsername())
                .fcmPurposeType(FcmPurposeType.심부름)
                .fcmRole(FcmRole.보내는사람)
                .build();
        // 여기서 filter 되어 fcm send 까지 완성 함.
        activeFcmFilterService.checkFcmPurpose(fcmSourceDto);

        return savedErrandEntity;
    }

    /**
     * 심부름 관련 FcmMessage를 생성할 때 사용하는 메서드 입니다.
     * @return FcmMessage.FcmRequest
     * @author 전지환
     */
    @Override
    public FcmMessage.FcmRequest createFcmMessageAboutErrand(String sender, String recipient, ErrandRole errandRole, ErrandResponseStatus errandResponseStatus){
        String action="";

        if (errandRole!=null){
            switch (errandRole){
                case SENDER: action = "요청";
                    break;
                case RECIPIENT: action = "전송";
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + errandRole);
            }
        } else if (errandResponseStatus!=null){
            switch (errandResponseStatus){
                case CANCEL: action = "거절";
                    break;
                case ACCEPT: action = "수락";
                    break;
            }
        }

        return FcmMessage.FcmRequest.builder()
                .title("누군가 심부름을 " +action+"했어요")
                .body(sender+" 님이 "+action+"한 심부름을 확인해보세요!")
                .build();
    }
}
