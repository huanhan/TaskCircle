package com.tc.db.repository;

import com.tc.db.entity.Admin;
import com.tc.db.enums.AdminState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 管理员仓库
 * @author Cyg
 */
public interface AdminRepository extends JpaRepository<Admin,Long>,JpaSpecificationExecutor<Admin> {


    /**
     * 修改管理员状态
     * @param adminState
     * @param id
     * @return
     */
    @Modifying
    @Query(value = "update Admin a set " +
            "a.adminState = :adminState " +
            "where a.userId = :id")
    int updateByAdminState(@Param("adminState") AdminState adminState,@Param("id") Long id);

    /**
     * 获取没有指定权限的管理员
     * @param id
     * @return
     */
    @Query(value = "select a from Admin a where a.userId not in (select b.userId from AdminAuthority b where b.authorityId = :authorityId )")
    List<Admin> findByNotAuthority(@Param("authorityId") Long id);

    /**
     * 获取没有指定权限,并且创建者是自己的管理员
     * @param id
     * @param creation
     * @return
     */
    @Query(value = "select a from Admin a where a.userId not in (select b.userId from AdminAuthority b where b.authorityId = :authorityId ) and a.createId = :creation")
    List<Admin> findByNotAuthority(@Param("authorityId") Long id,@Param("creation") Long creation);

    /**
     * 根据用户编号获取
     * @param ids
     * @return
     */
    List<Admin> findByUserIdIn(List<Long> ids);
}
