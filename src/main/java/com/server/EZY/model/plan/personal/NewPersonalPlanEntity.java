package com.server.EZY.model.plan.personal;

import com.server.EZY.model.plan.Period;
import com.server.EZY.model.plan.PlanInfo;
import com.server.EZY.model.plan.errand.ErrandStatus;
import com.server.EZY.model.plan.headOfPlan.enumType.PlanDType;
import com.server.EZY.model.user.UserEntity;
import lombok.*;
import javax.persistence.*;
import java.util.Objects;

import static javax.persistence.FetchType.*;

@Entity @Table(name = "new_personal_plan")
@Getter
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
     * 현재 겍체에 저장되어있는 userEntity와 업데이트를 하려는 currentUser를 동등성 비교를 해서 이 일정의 소유자일 경우에 이 객체를 변경할 수 있다.
     * @param updatedPersonalPlanEntity 업데이트 할 PersonalPlan타입의 인자
     * @author 정시원
     */
    public void updatePersonalPlanEntity(UserEntity currentUser, NewPersonalPlanEntity updatedPersonalPlanEntity) throws Exception {
        if(this.userEntity.equals(currentUser)) {
            repetition = updatedPersonalPlanEntity.repetition != null ? updatedPersonalPlanEntity.repetition : this.repetition;

            if (updatedPersonalPlanEntity.planInfo != null)
                this.planInfo.updatePlanInfo(updatedPersonalPlanEntity.planInfo);
            if (updatedPersonalPlanEntity.period != null)
                this.period.updatePeriod(updatedPersonalPlanEntity.period);
        }else{
            throw new Exception("해당 일정에 대한 접근권한이 없습니다."); // Exception 추가 예정
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NewPersonalPlanEntity)) return false;
        NewPersonalPlanEntity that = (NewPersonalPlanEntity) o;
        return Objects.equals(getPersonalPlanIdx(), that.getPersonalPlanIdx()) && Objects.equals(getUserEntity(), that.getUserEntity()) && Objects.equals(getErrandStatus(), that.getErrandStatus()) && Objects.equals(getPlanInfo(), that.getPlanInfo()) && Objects.equals(getPeriod(), that.getPeriod()) && Objects.equals(getRepetition(), that.getRepetition()) && dType == that.dType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPersonalPlanIdx(), getUserEntity(), getErrandStatus(), getPlanInfo(), getPeriod(), getRepetition(), dType);
    }
}
