package com.tc.service;

import com.tc.db.entity.AdminAuthority;
import com.tc.db.entity.AuthorityResource;
import com.tc.dto.admin.QueryAdmin;
import org.springframework.data.domain.Page;

import java.util.List;


/**
 * 管理员与权限关系服务接口，在BasicService中以有基本的服务，可以在这里额外添加
 * @author Cyg
 */
public interface AdminAuthorityService extends BasicService<AdminAuthority> {
    /**
     * 根据管理员信息查询
     * @param queryAdmin
     * @return
     */
    Page<AdminAuthority> findByQueryAdmin(QueryAdmin queryAdmin);

    /**
     * 根据关键字的编号组获取对应的权限与资源关系
     * @param admins
     * @param authorities
     * @return
     */
    List<AdminAuthority> findByKeys(List<Long> admins, List<Long> authorities);

    /**
     * 根据管理员编号获取管理员拥有的权限
     * @param id 管理员编号
     * @return
     */
    List<AdminAuthority> findByAdminId(Long id);

    /**
     * 根据管理员编号与对应的权限编号组，删除两者关系
     * @param authorityIds
     * @param id
     * @return
     */
    boolean deleteByAuthorityIds(List<Long> authorityIds, Long id);
}
