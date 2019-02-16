package com.tc.service;

import com.tc.db.entity.UserAuthority;
import com.tc.db.enums.UserCategory;
import com.tc.dto.admin.QueryAdmin;
import com.tc.dto.authority.RemoveUser;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 用户权限关系参考
 * @author Cyg
 */
public interface UserAuthorityService extends BasicService<UserAuthority> {

    /**
     * 根据权限编号获取对应的使用者
     * @param authorityId
     * @return
     */
    List<UserAuthority> findByAuthorityId(Long authorityId);

    /**
     * 删除记录，根据编号组与指定ID
     * @param ids
     * @return
     */
    boolean deleteByIds(RemoveUser ids);

    /**
     * 根据用户分类查询
     * @param userCategory
     * @return
     */
    List<UserAuthority> findByUserCategory(UserCategory userCategory);

    /**
     * 根据权限编号删除用户与权限之间的关系
     * @param ids
     * @param userCategory
     * @return
     */
    boolean deleteByAuthorityIds(List<Long> ids,UserCategory userCategory);

    /**
     * 根据权限编号与用户使用者分类查询
     * @param id
     * @param ids
     * @return
     */
    List<UserAuthority> findBy(Long id, List<UserCategory> ids);
}
