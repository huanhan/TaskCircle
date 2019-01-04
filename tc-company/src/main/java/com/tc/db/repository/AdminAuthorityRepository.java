package com.tc.db.repository;

import com.tc.db.entity.AdminAuthority;
import com.tc.db.entity.pk.AdminAuthorityPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * 管理员与权限关系仓库
 * @author Cyg
 */
public interface AdminAuthorityRepository extends JpaRepository<AdminAuthority,AdminAuthorityPK>,JpaSpecificationExecutor<AdminAuthority> {


    /**
     * 根据权限编号与管理员列表移除使用指定权限的管理员
     * @param ids
     * @param authorityId
     * @return
     */
    int deleteByAdminIsInAndAuthorityIdEquals(List<Long> ids,Long authorityId);

}
