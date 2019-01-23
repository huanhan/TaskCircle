package com.tc.db.repository;

import com.tc.db.entity.UserContact;
import com.tc.db.entity.pk.UserContactPK;
import com.tc.db.enums.UserContactName;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.method.P;

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
    List<UserContact> findByUserId(Long id, Sort sort);

    /**
     * 修改数据
     * @param id
     * @param name
     * @param contact
     * @return
     */
    @Modifying
    @Query(value = "update UserContact u set u.contact = :contact where u.userId = :id and u.contactName = :name")
    int update(@Param("id") Long id, @Param("name")UserContactName name, @Param("contact") String contact);
}
