package com.tc.db.repository;

import com.tc.db.entity.UserAuthority;
import com.tc.db.entity.pk.UserAuthorityPK;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 评论仓库
 * @author Cyg
 */
public interface UserAuthorityRepository extends JpaRepository<UserAuthority,UserAuthorityPK> {
}
