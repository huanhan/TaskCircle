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

    /**
     * 修改路径资源
     * @param resource
     * @return
     */
    @Modifying
    @Query(value = "update Resource r set " +
            "r.name = CASE WHEN :#{#resource.name} IS NULL THEN r.name ELSE :#{#resource.name} END ," +
            "r.className = CASE WHEN :#{#resource.className} IS NULL THEN r.className ELSE :#{#resource.className} END ," +
            "r.info = CASE WHEN :#{#resource.info} IS NULL THEN r.info ELSE :#{#resource.info} END ," +
            "r.method = CASE WHEN :#{#resource.method} IS NULL THEN r.method ELSE :#{#resource.method} END ," +
            "r.path = CASE WHEN :#{#resource.path} IS NULL THEN r.path ELSE :#{#resource.path} END ," +
            "r.type = CASE WHEN :#{#resource.type} IS NULL THEN r.type ELSE :#{#resource.type} END " +
            "WHERE r.id = :#{#resource.id}"
    )
    int update(@Param("resource") Resource resource);
}
