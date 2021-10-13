package com.server.EZY.model.plan.errand.repository.errand;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPQLQueryFactory;
import com.server.EZY.model.plan.errand.ErrandEntity;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.server.EZY.model.plan.errand.QErrandEntity.errandEntity;

@RequiredArgsConstructor
public class ErrandCustomRepositoryImpl implements ErrandCustomRepository {

    private final JPQLQueryFactory queryFactory;

    /**
     * errandIdx로 ErrandEntity를 연관되어 있는 ErrandStatusEntity와 함께 가져온다.
     * @param errandIdx 심부름 Idx
     * @return null를 가져올 수 있고, ErrandStatus를 같이 조회한 ErrandEntity
     */
    @Override
    public Optional<ErrandEntity> findWithErrandStatusByErrandIdx(long errandIdx) {
        Tuple errandEntityTuple = queryFactory
                .from(errandEntity)
                .select(errandEntity, errandEntity.errandStatusEntity)
                .where(errandEntity.planIdx.eq(errandIdx))
                .fetchOne();
        return Optional.ofNullable(errandEntityTuple.get(errandEntity));
    }
}
