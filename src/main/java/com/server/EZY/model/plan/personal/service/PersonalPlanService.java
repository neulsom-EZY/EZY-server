package com.server.EZY.model.plan.personal.service;

import com.server.EZY.model.plan.personal.PersonalPlanEntity;
import com.server.EZY.model.plan.personal.dto.PersonalPlanDto;
import com.server.EZY.model.plan.personal.dto.PersonalPlanUpdateDto;
import com.server.EZY.model.plan.personal.repository.PersonalPlanRepository;
import com.server.EZY.model.plan.headOfPlan.HeadOfPlanEntity;
import com.server.EZY.model.plan.headOfPlan.repository.HeadOfPlanRepository;
import com.server.EZY.model.user.UserEntity;
import com.server.EZY.model.user.util.CurrentUserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

 
@Service
@RequiredArgsConstructor
public class PersonalPlanService {
    private final HeadOfPlanRepository headOfPlanRepository;
    private final PersonalPlanRepository personalPlanRepository;
    private final CurrentUserUtil currentUserUtil;

    /**
     * PersonalPlan 을 저장하는 서비스 메서드 입니다 <br>
     * @param myPersonalPlan <br>
     * @param personalPlanCategory <br>
     * @return personalPlanName <br>
     * @author 전지환
     */
    @Transactional
    public HeadOfPlanEntity savePersonalPlan(PersonalPlanDto myPersonalPlan, List<String> personalPlanCategory){
        // 로그인된 userEntity를 불러옵니다.
        UserEntity loginUserEntity = currentUserUtil.getCurrentUser();
        // return 할 savedPlanEntity 필드를 메서드 내에서 전역 선언 합니다.
        HeadOfPlanEntity savedHeadOfPlanEntity;
        /**
         * if -> category list 사이즈가 0 일때는 -> category 없는 planEntity 에 set 해줍니다.
         * else -> category list 사이즈가 > 0 일때 -> category 있는 planEntity 에 set 해줍니다.
         */
        if(personalPlanCategory.size() == 0){
            HeadOfPlanEntity headOfPlanEntity = new HeadOfPlanEntity(
                    loginUserEntity,
                    myPersonalPlan.toEntity()
                        );
            // PlanEntity 에는 personalPlan 과의 연관관계가 맺어 있습니다. 그대로 save 요청합니다.
            savedHeadOfPlanEntity = headOfPlanRepository.save(headOfPlanEntity);
        } else {
            HeadOfPlanEntity headOfPlanEntityWithCategory = new HeadOfPlanEntity(
                    loginUserEntity,
                    myPersonalPlan.toEntity(),
                    personalPlanCategory
            );
            // HeadOfPlanEntity 에는 personalPlan 과의 연관관계가 맺어 있습니다. 그대로 save 요청합니다.
            savedHeadOfPlanEntity = headOfPlanRepository.save(headOfPlanEntityWithCategory);
        }
        // savedPlanEntity return 해줍니다.
        return savedHeadOfPlanEntity;
    }

    /**
     * 이 메서드는 유저 엔티티를 넘겨주면 그 유저에 해당하는 모든 개인 일정을 조회합니다.
     * @return HeadOfPlanEntity
     * @author 전지환
     */
    public List<HeadOfPlanEntity> getAllMyPersonalPlan(){
        UserEntity currentUserEntity = currentUserUtil.getCurrentUser();
        return headOfPlanRepository.findAllPersonalPlanByUserEntity(currentUserEntity);
    }

    /**
     * 이 메서드는 PersonalPlanId 를 넘겨주면 로그인 된 userEntity 와 PlanEntity 를 비교하여 조회합니다.
     * @param personalPlanId
     * @return HeadOfPlanEntity
     * @author 전지환
     */
    public HeadOfPlanEntity getThisPersonalPlan(Long personalPlanId){
        UserEntity currentUserEntity = currentUserUtil.getCurrentUser();
        return headOfPlanRepository.findThisPlanByUserEntityAndHeadOfPlanIdx(currentUserEntity, personalPlanId);
    }

    /**
     * 이 메서드는 personalPlanIdx를 param 으로 받아 일정을 찾고, 그 일정을 원하는 변경사항으로 변경하는 메서드 입니다.
     * @param personalPlanIdx
     * @param personalPlanUpdateDto
     * @throws Exception
     * @author 전지환
     */
    @Transactional
    public void updateThisPersonalPlan(Long personalPlanIdx, PersonalPlanUpdateDto personalPlanUpdateDto) throws Exception {
        // 현재 로그인 된 user 가져오기.
        Long currentUserIdx = currentUserUtil.getCurrentUser().getUserIdx();
        // HeadOfPlanEntity에 이 userIdx와 personalIdx를 and 로 넘겨 존재하는지 확인하기.
        headOfPlanRepository.findPlanEntityByUserEntity_UserIdxAndPersonalPlanEntity_PersonalPlanIdx(currentUserIdx, personalPlanIdx);
        // personalPlanIdx 넣어 조회하기.
        PersonalPlanEntity updatePersonalEntity = personalPlanRepository.findByPersonalPlanIdx(personalPlanIdx);
        if(updatePersonalEntity != null){
            updatePersonalEntity.updatePersonalPlan(personalPlanUpdateDto.toEntity());
        } else {
            throw new Exception("해당 일정이 없습니다. 업데이트 실패");
        }
    }

    /**
     * 이 메서드는 personalPlanIdx를 넘겨 일정을 찾고 삭제하는 메서드 입니다.
     * @param personalPlanIdx
     * @throws Exception
     * @author 전지환
     */
    public void deleteThisPersonalPlan(Long personalPlanIdx) throws Exception {
        // 현재 로그인 된 user 가져오기.
        Long currentUserIdx = currentUserUtil.getCurrentUser().getUserIdx();
        // headOfPlanEntity에 이 userIdx와 personalIdx를 and 로 넘겨 존재하는지 확인하기.
        HeadOfPlanEntity wannaDeletePersonalPlan = headOfPlanRepository.findPlanEntityByUserEntity_UserIdxAndPersonalPlanEntity_PersonalPlanIdx(currentUserIdx, personalPlanIdx);
        if(wannaDeletePersonalPlan != null) {
            // PersonalPlan 삭제하기
            headOfPlanRepository.delete(wannaDeletePersonalPlan);
        } else {
            throw new Exception("삭제하고자 하는 일정이 존재하지 않습니다.");
        }
    }
}
