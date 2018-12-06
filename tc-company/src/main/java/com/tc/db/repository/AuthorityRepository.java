package com.tc.db.repository;

import com.tc.db.entity.Admin;
import com.tc.db.entity.Authority;
import com.tc.db.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author Cyg
 * 权限资源仓库
 */
public interface AuthorityRepository extends JpaRepository<Authority,Long> {

    List<Authority> findByAdmin(Admin admin);
}
