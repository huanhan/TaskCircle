package com.tc.db.repository;

import com.tc.db.entity.Hunter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * 评论仓库
 * @author Cyg
 */
public interface HunterRepository extends JpaRepository<Hunter,Long>,JpaSpecificationExecutor<Hunter> {

}
