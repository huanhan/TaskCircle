package com.tc.service;

import com.tc.db.entity.Authority;
import com.tc.dto.authority.QueryAuthority;

import java.util.List;

/**
 * 权限服务接口，在BasicService中以有基本的服务，可以在这里额外添加
 * @author Cyg
 */
public interface AuthorityService extends BasicService<Authority> {
    List<Authority> findByQuery(QueryAuthority queryAuthority);
}
