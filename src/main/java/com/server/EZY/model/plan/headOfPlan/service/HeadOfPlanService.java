package com.server.EZY.model.plan.headOfPlan.service;

import com.server.EZY.model.plan.headOfPlan.HeadOfPlanEntity;
import com.server.EZY.model.plan.headOfPlan.repository.HeadOfPlanRepository;
import com.server.EZY.model.user.UserEntity;
import com.server.EZY.util.CurrentUserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HeadOfPlanService {
    private final CurrentUserUtil currentUserUtil;
    private final HeadOfPlanRepository headOfPlanRepository;

    @Transactional
    public List<HeadOfPlanEntity> getAllPlan() {
        UserEntity getCurrentUser = currentUserUtil.getCurrentUser();
        return headOfPlanRepository.findAllHeadOfPlanByUserEntity(getCurrentUser);
    }
}
