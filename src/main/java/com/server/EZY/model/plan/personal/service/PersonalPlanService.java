package com.server.EZY.model.plan.personal.service;

import com.server.EZY.model.plan.personal.dto.PersonalPlanDto;
import com.server.EZY.model.plan.personal.repository.PersonalPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PersonalPlanService {
    private final PersonalPlanRepository personalPlanRepository;

    @Transactional
    public String savePersonalPlan(PersonalPlanDto myPersonalPlan){
        personalPlanRepository.save(myPersonalPlan.toEntity());
        return myPersonalPlan.getPlanName() + "의 제목의 일정이 추가 되었습니다.";
    }
}
