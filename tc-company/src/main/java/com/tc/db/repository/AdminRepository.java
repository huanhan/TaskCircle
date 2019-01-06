package com.tc.db.repository;

import com.tc.db.entity.Admin;
import com.tc.db.enums.AdminState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

}
