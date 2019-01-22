package com.tc.db.repository;

import com.tc.db.entity.UserImg;
import com.tc.db.entity.pk.UserImgPK;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 用户图片资料仓库
 * @author Cyg
 */
public interface UserImgRepository extends JpaRepository<UserImg,UserImgPK> {
    /**
     * 获取用户图片数量
     * @param userId
     * @return
     */
    Long countByUserId(Long userId);

    /**
     * 获取用户的图片资料
     * @param id
     * @return
     */
    List<UserImg> findByUserId(Long id);
}
