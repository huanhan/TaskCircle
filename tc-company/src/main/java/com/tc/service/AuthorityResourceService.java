package com.tc.service;

import com.tc.db.entity.AuthorityResource;
import com.tc.dto.authority.QueryAR;
import org.springframework.data.domain.Page;

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
     * @param authorityId
     * @return
     */
    boolean deleteByResourceIds(List<Long> ids,Long authorityId);

    /**
     * 根据查询条件，获取权限或者资源信息
     * @param queryAR
     * @return
     */
    Page<AuthorityResource> findByQuery(QueryAR queryAR);

    /**
     * 根据关键字的编号组获取对应的权限与资源关系
     * @param resources
     * @param authorities
     * @return
     */
    List<AuthorityResource> findByKeys(List<Long> resources,List<Long> authorities);

    /**
     * 保存新资源，删除旧资源
     * @param inAdd
     * @param longs
     * @param aid
     */
    void saveNewsAndRemoveOlds(List<AuthorityResource> inAdd, List<Long> longs, Long aid);

    /**
     * 根据权限编号获取
     * @param longs
     * @return
     */
    List<AuthorityResource> findByAuthorityIds(List<Long> longs);
}
