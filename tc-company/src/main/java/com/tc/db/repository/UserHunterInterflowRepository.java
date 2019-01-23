package com.tc.db.repository;

import com.tc.db.entity.UserHunterInterflow;
import com.tc.db.entity.pk.UserHunterInterflowPK;
import com.tc.until.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 评论仓库
 * @author Cyg
 */
public interface UserHunterInterflowRepository extends JpaRepository<UserHunterInterflow,UserHunterInterflowPK>,JpaSpecificationExecutor<UserHunterInterflow> {


}
