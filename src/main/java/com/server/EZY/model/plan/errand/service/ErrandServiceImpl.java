package com.server.EZY.model.plan.errand.service;

import com.server.EZY.model.member.MemberEntity;
import com.server.EZY.model.member.repository.MemberRepository;
import com.server.EZY.model.plan.errand.ErrandEntity;
import com.server.EZY.model.plan.errand.ErrandStatusEntity;
import com.server.EZY.model.plan.errand.dto.ErrandSetDto;
import com.server.EZY.model.plan.errand.enumType.ResponseStatus;
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

    @Override
    public ErrandEntity sendErrand(ErrandSetDto errandSetDto) {
        MemberEntity currentUser = currentUserUtil.getCurrentUser();

        ErrandStatusEntity errandStatusEntity = ErrandStatusEntity.builder()
                .senderIdx(currentUser.getMemberIdx())
                .recipientIdx(memberRepository.findByUsername(errandSetDto.getRecipient()).getMemberIdx())
                .responseStatus(ResponseStatus.NOT_READ)
                .build();

        return errandRepository.save(errandSetDto.saveToEntity(currentUser, errandStatusEntity));
    }
}
