package com.server.EZY.model.plan.personal.service;

import com.server.EZY.model.plan.personal.PersonalPlanEntity;
import com.server.EZY.model.plan.personal.dto.PersonalPlanDto;
import com.server.EZY.model.plan.personal.dto.PersonalPlanUpdateDto;
import com.server.EZY.model.plan.personal.repository.PersonalPlanRepository;
import com.server.EZY.model.plan.plan.PlanEntity;
import com.server.EZY.model.plan.plan.repository.PlanRepository;
import com.server.EZY.model.user.UserEntity;
import com.server.EZY.model.user.repository.UserRepository;
import com.server.EZY.model.user.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class PersonalPlanService {
    private final PlanRepository planRepository;
    private final PersonalPlanRepository personalPlanRepository;
    private final UserServiceImpl userService;
    private final UserRepository userRepository;
    private PlanEntity planEntity;

    /**
     * PersonalPlan 을 저장하는 서비스 메서드 입니다 <br>
     * 1. category list 사이즈가 0 일때는 -> category 없는 planEntity 에 set 해줍니다. <br>
     * 2. category list 사이즈가 > 0 일때 -> category는 있는 planEntity 에 set 해줍니다. <br>
     * 3. 메서드 수행 시, 저장되는 savedPlanEntity 을 return 해줍니다.
     * @param myPersonalPlan <br>
     * @param personalPlanCategory <br>
     * @return personalPlanName <br>
     * @author 전지환
     */
    @Transactional
    public PlanEntity savePersonalPlan(PersonalPlanDto myPersonalPlan, List<String> personalPlanCategory){
        String loginUserNickname = userService.getCurrentUserNickname();
        UserEntity loginUserEntity = currentUserEntity(loginUserNickname);
        PlanEntity savedPlanEntity;

        if(personalPlanCategory.size() == 0){
            PlanEntity planEntity = new PlanEntity(
                    myPersonalPlan.toEntity(),
                    loginUserEntity
            );
            // PlanEntity 에는 personalPlan 과의 연관관계가 맺어 있습니다. 그대로 save 요청합니다.
            savedPlanEntity = planRepository.save(planEntity);
        } else {
            PlanEntity planEntityWithCategory = new PlanEntity(
                    myPersonalPlan.toEntity(),
                    loginUserEntity,
                    personalPlanCategory
            );
            // PlanEntity 에는 personalPlan 과의 연관관계가 맺어 있습니다. 그대로 save 요청합니다.
            savedPlanEntity = planRepository.save(planEntityWithCategory);
        }
        // savedPlanEntity return 해줍니다.
        return savedPlanEntity;
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

    public UserEntity currentUserEntity(String loginUserNickname){
        return userRepository.findByNickname(loginUserNickname);
    }
}
