package com.tc.db.repository;

import com.tc.db.entity.Resource;
import com.tc.dto.resource.QueryResource;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author Cyg
 * url资源仓库
 */
public interface ResourceRepository extends JpaRepository<Resource,Long>,JpaSpecificationExecutor<Resource> {

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
     * 根据ID列表获取资源
     * @param ids
     * @return
     */
    List<Resource> findByIdIn(List<Long> ids);

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

    /**
     * 根据查询条件获取资源列表
     * @param queryResource 查询条件
     * @param pageable 分页条件
     * @return 资源列表
     */
    @Query(value = "SELECT * FROM resource r " +
            "WHERE " +
            "CASE WHEN :#{#queryResource.name == null ? null : #queryResource.name.length() <= 0 ? null : #queryResource.name} IS NULL THEN TRUE ELSE r.name = :#{#queryResource.name} END AND " +
            "CASE WHEN :#{#queryResource.info == null ? null : #queryResource.info.length() <= 0 ? null : #queryResource.info} IS NULL THEN TRUE ELSE r.info LIKE :#{ '%' + #queryResource.info + '%'} END AND " +
            "CASE WHEN :#{#queryResource.begin} IS NULL THEN TRUE ELSE r.create_time >= :#{#queryResource.begin} END AND " +
            "CASE WHEN :#{#queryResource.end} IS NULL THEN TRUE ELSE r.create_time <= :#{#queryResource.end} END AND " +
            "CASE WHEN :#{#queryResource.method == null ? null : #queryResource.method.length() <= 0 ? null : #queryResource.method} IS NULL OR :#{#queryResource.method.length() <= 0} THEN TRUE ELSE r.method = :#{#queryResource.method} END AND " +
            "CASE WHEN :#{#queryResource.className == null ? null : #queryResource.className.length() <= 0 ? null : #queryResource.className} IS NULL OR :#{#queryResource.className.length() <= 0} THEN TRUE ELSE r.class_name = :#{#queryResource.className} END AND " +
            "CASE WHEN :#{#queryResource.type == null ? null : #queryResource.type.length() <= 0 ? null : #queryResource.type} IS NULL OR :#{#queryResource.type.length() <= 0} THEN TRUE ELSE r.type = :#{#queryResource.type} END " +
            "ORDER BY :#{#pageable.sort} ",nativeQuery = true)
    @EntityGraph(value = "resource.all")
    Page<Resource> findByQuery(@Param("queryResource") QueryResource queryResource, @Param("pageable") Pageable pageable);

    @Override
    @EntityGraph(value = "resource.all")
    Page<Resource> findAll(Specification<Resource> specification, Pageable pageable);



}
