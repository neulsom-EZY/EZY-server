package com.server.EZY.model.plan.errand.repository.errand;

import com.server.EZY.model.plan.errand.ErrandEntity;

import java.util.Optional;

public interface ErrandCustomRepository {

    /**
     * errandIdx로 ErrandEntity를 연관되어 있는 ErrandStatusEntity와 함께 가져온다.
     * @param errandIdx 심부름 Idx
     * @return null를 가져올 수 있고, ErrandStatus를 같이 조회한 ErrandEntity
     */
    Optional<ErrandEntity> findWithErrandStatusByErrandIdx(long errandIdx);
}
