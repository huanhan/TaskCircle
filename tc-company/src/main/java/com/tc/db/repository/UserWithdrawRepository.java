package com.tc.db.repository;

import com.tc.db.entity.UserWithdraw;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 评论仓库
 * @author Cyg
 */
public interface UserWithdrawRepository extends JpaRepository<UserWithdraw,String> {
}
