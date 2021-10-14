package com.server.EZY.model.plan.errand.service;

import com.server.EZY.exception.response.CustomException;
import com.server.EZY.exception.user.exception.InvalidAccessException;
import com.server.EZY.model.member.MemberEntity;
import com.server.EZY.model.member.repository.MemberRepository;
import com.server.EZY.model.plan.errand.ErrandEntity;
import com.server.EZY.model.plan.errand.ErrandStatusEntity;
import com.server.EZY.model.plan.errand.dto.ErrandSetDto;
import com.server.EZY.model.plan.errand.enum_type.ErrandResponseStatus;
import com.server.EZY.model.plan.errand.repository.errand.ErrandRepository;
import com.server.EZY.model.plan.errand.repository.errand_status.ErrandStatusRepository;
import com.server.EZY.notification.dto.FcmSourceDto;
import com.server.EZY.notification.enum_type.FcmPurposeType;
import com.server.EZY.notification.enum_type.FcmRole;
import com.server.EZY.notification.service.feature.ActiveFcmFilterService;
import com.server.EZY.util.CurrentUserUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Supplier;

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
    private final ErrandStatusRepository errandStatusRepository;
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
        activeFcmFilterService.send(fcmSourceDto);

        return savedErrandEntity;
    }

    /**
     * 심부름을 수락한다. <br>
     * 수신자의 Errand가 DB에 저장되고, 심부름을 수락했다는 push알람을 발신자에게 전송한다.
     * @param errandIdx 수락할 errandIdx(planIdx)
     * @return 수신자의 ErrandEntity
     * @throws InvalidAccessException 해당 심부름에 잘못된 접근을 할 경우
     * @throws CustomException        PlanNotFound 해당 심부름이 존재하지 않을 때
     */
    @Override
    @Transactional
    public ErrandEntity acceptErrand(long errandIdx) {
        ErrandEntity senderErrandEntity = errandRepository.findWithErrandStatusByErrandIdx(errandIdx)
                .orElseThrow(
                        () -> new CustomException("해당 심부름은 존재하지 않습니다.", HttpStatus.NOT_FOUND)
                );
        ErrandStatusEntity senderErrandStatusEntity = senderErrandEntity.getErrandStatusEntity();
        MemberEntity currentMember = currentUserUtil.getCurrentUser();

        senderErrandStatusEntity.updateErrandResponseStatus(ErrandResponseStatus.ACCEPT);
        checkRecipientByErrand(senderErrandStatusEntity, currentMember, InvalidAccessException::new);

        ErrandEntity recipientErrand = errandRepository.save(senderErrandEntity.cloneBySetMember(currentMember));

        //TODO fcm push알람 작성
        return recipientErrand;
    }
    /**
     * 이 심부름의 수신자가 아닌지 확인하고, Supplier로 넘겨준 Exception을 던진다.
     * @param errandStatusEntity - 해당 심부름의 수신자의 정보를 가지고 있는 ErrandStatusEntity
     * @param memberEntity - 해당심부름의 수신자인지 검증할 MemberEntity
     * @param exceptionSupplier 해당 심부름의 수신자가 아닐경우 던질 exception supplier
     * @author 정시원
     */
    private void checkRecipientByErrand(ErrandStatusEntity errandStatusEntity, MemberEntity memberEntity, Supplier<? extends RuntimeException> exceptionSupplier){
        if(!errandStatusEntity.getRecipientIdx().equals(memberEntity.getMemberIdx()))
            throw exceptionSupplier.get();
    }
}
