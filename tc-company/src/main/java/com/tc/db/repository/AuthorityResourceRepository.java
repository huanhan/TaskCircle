package com.tc.db.repository;

import com.tc.db.entity.Authority;
import com.tc.db.entity.AuthorityResource;
import com.tc.db.entity.Resource;
import com.tc.db.entity.pk.AuthorityResourcePK;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


/**
 * @author Cyg
 *
 * 权限与资源关系仓库
 */
public interface AuthorityResourceRepository extends JpaRepository<AuthorityResource,AuthorityResourcePK>,JpaSpecificationExecutor<AuthorityResource> {

    /**
     * 根据Authority_Id来获取对应的资源
     * @param authority
     * @return
     */
    List<AuthorityResource> findAllByAuthority(Authority authority);

    /**
     * 根据resource_id来获取对应权限
     * @param id
     * @return
     */
    List<AuthorityResource> findAllByResourceId(Long id);

    /**
     * 根据权限编号列表获取
     * @param ids
     * @return
     */
    List<AuthorityResource> findAllByAuthorityIdIn(List<Long> ids);

    /**
     * 根据资源ID组删除权限与资源关系
     * @param ids
     * @param authorityId
     * @return
     */
    @Modifying
    int deleteByResourceIdIsInAndAuthorityIdEquals(List<Long> ids,Long authorityId);

    /**
     * 根据资源编号与权限编号组删除资源与权限的关系
     * @param authorityIds 权限组编号
     * @param resourceId 资源编号
     * @return
     */
    int deleteByAuthorityIdIsInAndResourceIdEquals(List<Long> authorityIds,Long resourceId);
}
