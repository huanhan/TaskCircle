package com.tc.db.repository;

import com.tc.db.entity.User;
import com.tc.exception.DBException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

/**
 * @author Cyg
 * 用户资源
 */
public interface UserRepository extends JpaRepository<User,Long>,JpaSpecificationExecutor<User> {

    /**
     * 根据用户名查询
     * @param username
     * @return
     */
    User queryFirstByUsername(String username);


    /**
     * 更新用户基本信息
     * @param user
     * @return
     */
    @Modifying
    @Query("update User u set " +
            "u.name = CASE WHEN :#{#user.name} IS NULL THEN u.name ELSE :#{#user.name} END ," +
            "u.category = CASE WHEN :#{#user.category.toString()} IS NULL THEN u.category ELSE :#{#user.category.toString()} END ," +
            "u.name = CASE WHEN :#{#user.name} IS NULL THEN u.name ELSE :#{#user.name} END ," +
            "u.gender = CASE WHEN :#{#user.gender.toString()} IS NULL THEN u.gender ELSE :#{#user.gender.toString()} END ," +
            "u.idCard = CASE WHEN :#{#user.idCard} IS NULL THEN u.idCard ELSE :#{#user.idCard} END ," +
            "u.address = CASE WHEN :#{#user.address} IS NULL THEN u.address ELSE :#{#user.address} END ," +
            "u.school = CASE WHEN :#{#user.school} IS NULL THEN u.school ELSE :#{#user.school} END ," +
            "u.major = CASE WHEN :#{#user.major} IS NULL THEN u.major ELSE :#{#user.major} END ," +
            "u.interest = CASE WHEN :#{#user.interest} IS NULL THEN u.interest ELSE :#{#user.interest} END ," +
            "u.intro = CASE WHEN :#{#user.intro} IS NULL THEN u.intro ELSE :#{#user.intro} END ," +
            "u.height = CASE WHEN :#{#user.height} IS NULL THEN u.height ELSE :#{#user.height} END ," +
            "u.weight = CASE WHEN :#{#user.weight} IS NULL THEN u.weight ELSE :#{#user.weight} END ," +
            "u.birthday = CASE WHEN :#{#user.birthday} IS NULL THEN u.birthday ELSE :#{#user.birthday} END " +
            "WHERE u.id = :#{#user.id}"
    )
    int update(@Param("user") User user);
}
