package com.tc.db.repository;

import com.tc.db.entity.AdminAuthority;
import com.tc.db.entity.pk.AdminAuthorityPK;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 管理员与权限关系仓库
 * @author Cyg
 */
public interface AdminAuthorityRepository extends JpaRepository<AdminAuthority,AdminAuthorityPK> {
}
