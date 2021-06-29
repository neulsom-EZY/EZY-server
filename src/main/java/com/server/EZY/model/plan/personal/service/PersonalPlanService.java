package com.server.EZY.model.plan.personal.service;

import com.server.EZY.model.plan.personal.PersonalPlanEntity;
import com.server.EZY.model.plan.personal.dto.PersonalPlanDto;
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
        // 로그인된 userEntity를 불러옵니다.
        UserEntity loginUserEntity = currentUserEntity();
        // return 할 savedPlanEntity 필드를 메서드 내에서 전역 선언 합니다.
        PlanEntity savedPlanEntity;

        /**
         * if -> category list 사이즈가 0 일때는 -> category 없는 planEntity 에 set 해줍니다.
         * else -> category list 사이즈가 > 0 일때 -> category 있는 planEntity 에 set 해줍니다.
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

    /**
     * 이 메서드는 엔티티를 넘겨주면 그 엔티티에 해당하는 모든 개인 일정을 조회합니다.
     * @param userEntity
     * @return PlanEntity
     * @author 전지환
     */
    public List<PlanEntity> getAllMyPersonalPlan(UserEntity userEntity){
        return planRepository.findAllPersonalPlanByUserEntity(userEntity);
    }

    public PlanEntity getThisPersonalPlan(Long planId){

    }

    /**
     * 이 메서드는 임시적으로 선언한 메서드이며, userNickname을 통해 Entity를 찾아 return 해줍니다.
     * @return UserEntity
     */
    public UserEntity currentUserEntity(){
        String loginUserNickname = UserServiceImpl.getCurrentUserNickname();
        return userRepository.findByNickname(loginUserNickname);
    }
}
