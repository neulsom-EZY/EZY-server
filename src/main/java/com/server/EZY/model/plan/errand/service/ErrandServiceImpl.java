package com.server.EZY.model.plan.errand.service;

import com.server.EZY.model.member.MemberEntity;
import com.server.EZY.model.member.repository.MemberRepository;
import com.server.EZY.model.plan.errand.ErrandEntity;
import com.server.EZY.model.plan.errand.ErrandStatusEntity;
import com.server.EZY.model.plan.errand.dto.ErrandSetDto;
import com.server.EZY.model.plan.errand.enum_type.ErrandResponseStatus;
import com.server.EZY.model.plan.errand.repository.ErrandRepository;
import com.server.EZY.util.CurrentUserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ErrandServiceImpl implements ErrandService{
    private final CurrentUserUtil currentUserUtil;
    private final MemberRepository memberRepository;
    private final ErrandRepository errandRepository;

    /**
     * 이 메서드는 심부름을 전송(저장) 할 때 사용하는 비즈니스 로직입니다.
     * @param errandSetDto
     * @return ErrandEntity
     * @author 전지환
     */
    @Override
    public ErrandEntity sendErrand(ErrandSetDto errandSetDto) {
        /**
         * currentUser: 보내는 사람
         * recipientIdx: 받는 사람
         */
        MemberEntity currentUser = currentUserUtil.getCurrentUser();
        Long recipientIdx = memberRepository.findByUsername(errandSetDto.getRecipient()).getMemberIdx();

        ErrandStatusEntity errandStatusEntity = ErrandStatusEntity.builder()
                .senderIdx(currentUser.getMemberIdx())
                .recipientIdx(recipientIdx)
                .errandResponseStatus(ErrandResponseStatus.NOT_READ)
                .build();

        return errandRepository.save(errandSetDto.saveToEntity(currentUser, errandStatusEntity));
    }
}
