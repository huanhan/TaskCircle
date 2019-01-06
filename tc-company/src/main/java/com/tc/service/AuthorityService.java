package com.tc.service;

import com.tc.db.entity.Authority;
import com.tc.dto.StringIds;
import com.tc.dto.authority.QueryAuthority;
import com.tc.dto.authority.RemoveUser;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 权限服务接口，在BasicService中以有基本的服务，可以在这里额外添加
 * @author Cyg
 */
public interface AuthorityService extends BasicService<Authority> {

    /**
     * 根据权限查询条件获取权限列表
     * @param queryAuthority
     * @return
     */
    List<Authority> findByQueryAuthority(QueryAuthority queryAuthority);

    /**
     * 查询管理员权限
     * @param id
     * @param queryAuthority
     * @return
     */
    Page<Authority> findByAdmin(Long id, QueryAuthority queryAuthority);
}
