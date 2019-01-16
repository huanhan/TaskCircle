package com.tc.db.repository;

import com.tc.db.entity.CommentHunter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 评论猎刃仓库
 * @author Cyg
 */
public interface CommentHunterRepository extends JpaRepository<CommentHunter,Long>,JpaSpecificationExecutor<CommentHunter> {
}
