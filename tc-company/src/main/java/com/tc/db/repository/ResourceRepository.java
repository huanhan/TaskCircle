package com.tc.db.repository;

import com.tc.db.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Cyg
 * url资源仓库
 */
public interface ResourceRepository extends JpaRepository<Resource,Long> {

    /**
     * 根据name查询
     * @param name
     * @return
     */
    Resource queryFirstByName(String name);

}
