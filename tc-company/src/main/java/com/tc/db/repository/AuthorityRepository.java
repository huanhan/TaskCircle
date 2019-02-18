package com.tc.db.repository;

import com.tc.db.entity.Admin;
import com.tc.db.entity.Authority;
import com.tc.until.ListUtils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author Cyg
 * 权限资源仓库
 */
public interface AuthorityRepository extends JpaRepository<Authority,Long>,JpaSpecificationExecutor<Authority> {

    List<Authority> findByAdmin(Admin admin);


    /**
     * 根据name查询
     * @param name
     * @return
     */
    Authority queryFirstByName(String name);

    /**
     * 根据ID列表删除权限
     * @param ids
     * @return
     */
    @Modifying
    @Query(value = "delete from Authority e where e.id in (:ids)")
    int deleteByIds(@Param("ids") List<Long> ids);

    /**
     * 修改权限资源
     * @param authority
     * @return
     */
    @Modifying
    @Query(value = "update Authority r set " +
            "r.name = CASE WHEN :#{#authority.name} IS NULL THEN r.name ELSE :#{#authority.name} END ," +
            "r.info = CASE WHEN :#{#authority.info} IS NULL THEN r.info ELSE :#{#authority.info} END " +
            "WHERE r.id = :#{#authority.id}"
    )
    int update(@Param("authority") Authority authority);

    /**
     * 获取不包括ids的权限列表
     * @param ids
     * @return
     */
    List<Authority> findByIdNotIn(List<Long> ids);

    /**
     * 获取包括ids的权限列表
     * @param ids
     * @return
     */
    List<Authority> findByIdIn(List<Long> ids);
}
