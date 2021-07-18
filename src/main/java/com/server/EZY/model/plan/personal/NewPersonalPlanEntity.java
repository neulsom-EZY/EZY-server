package com.server.EZY.model.plan.personal;

import com.server.EZY.model.plan.Period;
import com.server.EZY.model.plan.PlanInfo;
import com.server.EZY.model.plan.errand.ErrandStatus;
import com.server.EZY.model.plan.headOfPlan.enumType.PlanDType;
import com.server.EZY.model.user.UserEntity;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity @Table(name = "new_personal_plan")
@Builder @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NewPersonalPlanEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "personal_plan_id")
    private Long personalPlanIdx;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "errand_status_id")
    private ErrandStatus errandStatus;

    @Embedded
    private PlanInfo planInfo;

    @Embedded
    private Period period;

    @Column(name = "repetition")
    private Boolean repetition;

    @Column(name = "d_type" )
    @Enumerated(EnumType.STRING)
    private PlanDType dType;

    /**
     * 개인일정을 추가하는 생성자
     * @param userEntity 연관관계를 맻을 유저엔티티
     * @param planInfo 개인일정의 기본적인 정보
     * @param period 개인일정의 기간
     * @param repetition 반복여부
     * @author 정시원
     */
    public NewPersonalPlanEntity(UserEntity userEntity, PlanInfo planInfo, Period period, Boolean repetition){
        this(userEntity, planInfo, period);
        if(repetition != null){
            this.repetition = repetition;
            this.dType = PlanDType.PERSONAL_PLAN;
        }else{
            throw new IllegalArgumentException("null값이 들어갈 수 없습니다.");
        }
    }

    /**
     * 심부름을 추가하는 생성자
     * @param userEntity 연관관계를 맻을 "유저 엔티티"
     * @param planInfo 심부름의 정보를 담고 있는 임베디드 타입의 객체
     * @param period 심부름의 기간 정보를 담고 있는 임베디드 타입의 객체
     * @param errandStatus 심부름의 상태를 나타내는 "심부름상태 엔티티"
     * @author 정시원
     */
    public NewPersonalPlanEntity(UserEntity userEntity ,PlanInfo planInfo, Period period, ErrandStatus errandStatus){
        this(userEntity, planInfo, period);
        if(errandStatus != null){
            this.errandStatus = errandStatus;
            this.dType = PlanDType.ERRAND;
        }else
            throw new IllegalArgumentException("null값이 들어갈 수 없습니다.");
    }

    /**
     * 기본적인 정보를 통해 객체를 생성하는 생성자
     * @param userEntity 연관관계를 맻을 "유저 엔티티"
     * @param planInfo 일정의 기본적인 정보
     * @param period 일정의 기간
     * @author 정시원
     */
    private NewPersonalPlanEntity(UserEntity userEntity, PlanInfo planInfo, Period period){
        if(userEntity != null && planInfo != null && period != null){
            this.userEntity = userEntity;
            this.planInfo = planInfo;
            this.period = period;
        }else
            throw new IllegalArgumentException("null값이 들어갈 수 없습니다.");
    }

    /**
     * personalPlan를 업데이트 하는 매서드
     * @param updatedPersonalPlanEntity 업데이트 할 PersonalPlan타입의 인자
     * @author 정시원
     */
    public void updatePersonalPlanEntity(NewPersonalPlanEntity updatedPersonalPlanEntity){
        repetition = updatedPersonalPlanEntity.repetition != null ? updatedPersonalPlanEntity.repetition : this.repetition;

        if(updatedPersonalPlanEntity.planInfo != null)
            this.planInfo.updatePlanInfo(updatedPersonalPlanEntity.planInfo);
        if(updatedPersonalPlanEntity.period != null)
            this.period.updatePeriod(updatedPersonalPlanEntity.period);
    }
}
