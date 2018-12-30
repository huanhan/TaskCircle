package com.tc.db.repository;

import com.tc.db.entity.UserImg;
import com.tc.db.entity.pk.UserImgPK;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 评论仓库
 * @author Cyg
 */
public interface UserImgRepository extends JpaRepository<UserImg,UserImgPK> {
}
