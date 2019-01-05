package com.tc.service;

import com.tc.db.entity.Admin;
import com.tc.dto.admin.QueryAdmin;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 管理员服务接口，在BasicService中以有基本的服务，可以在这里额外添加
 * @author Cyg
 */
public interface AdminService extends BasicService<Admin> {

    /**
     * 根据管理员查询条件与权限编号获取管理员信息
     * @param queryAdmin 管理员查询条件
     * @param authority 权限编号
     * @return
     */
    Page<Admin> findByQueryAdminAndAuthority(QueryAdmin queryAdmin,Long authority);

    /**
     * 根据管理员查询条件与管理员是否拥有权限来获取管理员
     * @param queryAdmin
     * @return
     */
    List<Admin> findByQueryAdminAndAuthorityState(QueryAdmin queryAdmin);
}
