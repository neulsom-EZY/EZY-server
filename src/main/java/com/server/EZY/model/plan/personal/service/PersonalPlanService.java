package com.server.EZY.model.plan.personal.service;

import com.server.EZY.model.plan.personal.PersonalPlanEntity;
import com.server.EZY.model.plan.personal.dto.PersonalPlanDto;
import com.server.EZY.model.plan.personal.dto.PersonalPlanUpdateDto;
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

    @Transactional
    public String updatePersonalPlan(PersonalPlanUpdateDto myPersonalPlanUpdate, Long personalPlanIdx) throws Exception {
        PersonalPlanEntity updateTarget = personalPlanRepository.findByPersonalPlanIdx(personalPlanIdx);
        if (updateTarget != null) {
            updateTarget.updatePersonalPlan(myPersonalPlanUpdate.toEntity());
            return myPersonalPlanUpdate.getPlanName() + "의 제목의 일정으로 변경되었습니다.";
        } else {
            throw new Exception("변경하고자 하는 일정이 존재하지 않습니다.");
        }
    }
}
