package com.tc.db.repository;

import com.tc.db.entity.AuditHunter;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 猎刃审核仓库
 * @author Cyg
 */
public interface AuditHunterRepository extends JpaRepository<AuditHunter,String> {
}
