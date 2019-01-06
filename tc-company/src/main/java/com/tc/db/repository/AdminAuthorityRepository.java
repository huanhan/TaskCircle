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
    int deleteByUserIdIsInAndAuthorityIdEquals(List<Long> ids,Long authorityId);

    /**
     * 根据管理员编号获取对应的权限
     * @param userId
     * @return
     */
    List<AdminAuthority> findByUserIdEquals(Long userId);

    /**
     * 删除权限与管理员关系，根据管理员编号与对应的权限编号组,
     * @param authorityIds
     * @param id
     * @param createID
     * @return
     */
    int deleteByAuthorityIdIsInAndUserIdEquals(List<Long> authorityIds, Long id);
}
