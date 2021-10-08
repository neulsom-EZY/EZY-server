package com.server.EZY.model.plan.tag.repository;

import com.server.EZY.model.plan.tag.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<TagEntity, Long>, TagRepositoryCustom {
    TagEntity findByTagIdx(Long tagIdx);
}
