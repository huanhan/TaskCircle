package com.tc.db.repository;

import com.tc.db.entity.UserContact;
import com.tc.db.entity.pk.UserContactPK;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 用户联系方式仓库
 * @author Cyg
 */
public interface UserContactRepository extends JpaRepository<UserContact,UserContactPK> {
    /**
     * 获取用户的联系方式
     * @param id
     * @return
     */
    List<UserContact> findByUserId(Long id);
}
