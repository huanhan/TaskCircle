package com.tc.db.repository;

import com.tc.db.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

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


    /**
     * 根据ID列表删除资源
     * @param ids
     * @return
     */
    @Modifying
    @Query(value = "delete from Resource e where e.id in (:ids)")
    int deleteByIds(@Param("ids") List<Long> ids);
}
