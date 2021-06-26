package com.server.EZY.model.plan.personal.service;

import com.server.EZY.model.plan.personal.PersonalPlanEntity;
import com.server.EZY.model.plan.personal.dto.PersonalPlanDto;
import com.server.EZY.model.plan.personal.dto.PersonalPlanUpdateDto;
import com.server.EZY.model.plan.personal.repository.PersonalPlanRepository;
import com.server.EZY.model.plan.plan.PlanEntity;
import com.server.EZY.model.plan.plan.repository.PlanRepository;
import com.server.EZY.model.user.UserEntity;
import com.server.EZY.model.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class PersonalPlanService {
    private final PlanRepository planRepository;
    private final PersonalPlanRepository personalPlanRepository;

    @Transactional
    public String savePersonalPlan(PersonalPlanDto myPersonalPlan, List<String> personalPlanCategory){
        UserEntity currentUserEntity = UserService.currentUserEnity();
        PlanEntity planEntity = new PlanEntity(
                currentUserEntity,
                myPersonalPlan,
                personalPlanCategory
        );

        PlanEntity savedPlanEntity = planRepository.save(planEntity);

        return savedPlanEntity.getPersonalPlanEntity().getPlanName();
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
