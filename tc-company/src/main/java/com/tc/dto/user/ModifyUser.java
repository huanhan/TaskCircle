package com.tc.dto.user;


import com.tc.db.enums.UserGender;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

/**
 * 修改用户
 * @author Cyg
 */
public class ModifyUser {
    /**
     * 用户编号
     */
    @NotNull
    @Min(value = 1)
    private Long id;
    /**
     * 用户姓名
     */
    @NotBlank(message = "不能空")
    @Length(max = 10)
    private String name;
    /**
     * 用户性别
     */
    @NotNull(message = "不能空")
    private UserGender gender;
    /**
     * 身份证号码
     */
    private String idCard;
    /**
     * 家庭住址
     */
    private String address;
    /**
     * 毕业学校
     */
    private String school;
    /**
     * 职业
     */
    private String major;
    /**
     * 兴趣
     */
    private String interest;
    /**
     * 简介
     */
    private String intro;
    /**
     * 身高
     */
    private Integer height;
    /**
     * 体重
     */
    private Integer weight;
    /**
     * 生日
     */
    private Timestamp birthday;
    /**
     * 用户手机号码
     */
    private String phone;

}
