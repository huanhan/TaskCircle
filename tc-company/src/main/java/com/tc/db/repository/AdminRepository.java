package com.tc.db.repository;

import com.tc.db.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 管理员仓库
 * @author Cyg
 */
public interface AdminRepository extends JpaRepository<Admin,Long> {
}
