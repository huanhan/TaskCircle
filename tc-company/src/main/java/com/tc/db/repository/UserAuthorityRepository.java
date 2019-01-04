package com.tc.db.repository;

import com.tc.db.entity.UserAuthority;
import com.tc.db.entity.pk.UserAuthorityPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * 评论仓库
 * @author Cyg
 */
public interface UserAuthorityRepository extends JpaRepository<UserAuthority,UserAuthorityPK>,JpaSpecificationExecutor<UserAuthority> {


    /**
     * 根据权限编号查询使用权限的用户分类
     * @param authorityId
     * @return
     */
    List<UserAuthority> findByAuthorityIdEquals(Long authorityId);

    /**
     * 根据权限编号与用户分类列表移除使用指定权限的管理员
     * @param categories
     * @param authorityId
     * @return
     */
    int deleteByCategoryIsInAndAuthorityIdEquals(List<String> categories,Long authorityId);
}
