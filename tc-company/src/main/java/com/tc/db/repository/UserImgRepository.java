package com.tc.db.repository;

import com.tc.db.entity.UserImg;
import com.tc.db.entity.pk.UserImgPK;
import com.tc.db.enums.UserIMGName;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
    Long countByUserId(Long userId, Sort sort);

    /**
     * 获取用户的图片资料
     * @param id
     * @return
     */
    List<UserImg> findByUserId(Long id);

    /**
     * 修改用户图片资料
     * @param source
     * @param names
     * @param id
     * @return
     */
    @Modifying
    @Query(value = "update UserImg u set u.urlLocation = :source where u.imgName = :names and u.userId = :id ")
    int update(@Param("source") String source, @Param("names") UserIMGName names, @Param("id") Long id);
}
