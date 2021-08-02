package com.server.EZY.model.plan.errand.service;

import com.server.EZY.model.member.MemberEntity;
import com.server.EZY.model.plan.errand.ErrandEntity;
import com.server.EZY.model.plan.errand.dto.ErrandSetDto;
import com.server.EZY.util.CurrentUserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ErrandServiceImpl implements ErrandService{
    private final CurrentUserUtil currentUserUtil;

    @Override
    public ErrandEntity sendErrand(ErrandSetDto errandSetDto) {
        MemberEntity currentUser = currentUserUtil.getCurrentUser();
        return null;
    }
}
