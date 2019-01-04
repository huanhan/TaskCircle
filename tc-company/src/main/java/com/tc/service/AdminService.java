package com.tc.service;

import com.tc.db.entity.Admin;
import com.tc.dto.admin.QueryAdmin;
import org.springframework.data.domain.Page;

/**
 * 管理员服务接口，在BasicService中以有基本的服务，可以在这里额外添加
 * @author Cyg
 */
public interface AdminService extends BasicService<Admin> {
    Page<Admin> findByQueryAdminAndAuthority(QueryAdmin queryAdmin,Long authority);
}
