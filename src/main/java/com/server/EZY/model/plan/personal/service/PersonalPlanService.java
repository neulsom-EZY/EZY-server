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
     * @param myPersonalPlan <br>
     * @param personalPlanCategory <br>
     * @return personalPlanName <br>
     * @author 전지환
     */
    @Transactional
    public PlanEntity savePersonalPlan(PersonalPlanDto myPersonalPlan, List<String> personalPlanCategory){
        // loginUserNickname 은 userService 에 Public static 으로 선언 된 method를 사용합니다.
        String loginUserNickname = userService.getCurrentUserNickname();
        // currentUserEntity 를 사용하여 loginUserNickname 으로 찾은 Entity 를 저장합니다.
        UserEntity loginUserEntity = currentUserEntity(loginUserNickname);
        // return 할 savedPlanEntity 필드를 메서드 내에서 전역 선언 합니다.
        PlanEntity savedPlanEntity;

        /**
         * if -> category list 사이즈가 0 일때는 -> category 없는 planEntity 에 set 해줍니다. <br>
         * else -> category list 사이즈가 > 0 일때 -> category 있는 planEntity 에 set 해줍니다. <br>
         */
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

    /**
     * 이 메서드는 임시적으로 선언한 메서드이며, userNickname을 통해 Entity를 찾아 return 해줍니다.
     * @param loginUserNickname
     * @return UserEntity
     */
    public UserEntity currentUserEntity(String loginUserNickname){
        return userRepository.findByNickname(loginUserNickname);
    }
}
