package com.tc.db.repository;

import com.tc.db.entity.UserContact;
import com.tc.db.entity.pk.UserContactPK;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 评论仓库
 * @author Cyg
 */
public interface UserContactRepository extends JpaRepository<UserContact,UserContactPK> {
}
