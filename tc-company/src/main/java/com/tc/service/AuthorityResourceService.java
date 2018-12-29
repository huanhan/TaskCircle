package com.tc.service;

import com.tc.db.entity.AuthorityResource;

import java.util.List;


/**
 * @author Cyg
 *
 * 权限与资源关系服务接口，在BasicService中以有基本的服务，可以在这里额外添加
 *
 */
public interface AuthorityResourceService extends BasicService<AuthorityResource> {


    /**
     * 根据authorityID获取对应资源
     * @param authorityID
     * @return
     */
    List<AuthorityResource> findByAuthorityID(Long authorityID);

    /**
     * 根据resource的ID组来删除权限资源对应关系
     * @param ids
     * @return
     */
    boolean deleteByResourceIds(List<Long> ids);

}
