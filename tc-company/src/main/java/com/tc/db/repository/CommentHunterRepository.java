package com.tc.db.repository;

import com.tc.db.entity.CommentHunter;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 评论猎刃仓库
 * @author Cyg
 */
public interface CommentHunterRepository extends JpaRepository<CommentHunter,Long> {
}
