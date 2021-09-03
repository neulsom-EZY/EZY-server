package com.server.EZY.model.plan.tag.repository;

import com.server.EZY.model.plan.tag.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<TagEntity, Long>{
    TagEntity findByTagIdx(Long tagIdx);
}
