package com.tc.dto.app;

import com.tc.db.enums.UserCategory;
import com.tc.db.enums.UserGender;

import java.sql.Timestamp;

public class UserAppDto {
    /**
     * 用户姓名
     */
    private String name;
    /**
     * 用户账户
     */
    private String username;
    /**
     * 用户性别
     */
    private UserGender gender;
    /**
     * 用户分类
     */
    private UserCategory category;
    /**
     * 账户余额
     */
    private Float money = 0.00f;
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
    /**
     * 用户头像
     */
    private String headImg;
    /**
     * 获取所有评论数
     */
    private Long commentsNum;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserGender getGender() {
        return gender;
    }

    public void setGender(UserGender gender) {
        this.gender = gender;
    }

    public UserCategory getCategory() {
        return category;
    }

    public void setCategory(UserCategory category) {
        this.category = category;
    }

    public Float getMoney() {
        return money;
    }

    public void setMoney(Float money) {
        this.money = money;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Timestamp getBirthday() {
        return birthday;
    }

    public void setBirthday(Timestamp birthday) {
        this.birthday = birthday;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public Long getCommentsNum() {
        return commentsNum;
    }

    public void setCommentsNum(Long commentsNum) {
        this.commentsNum = commentsNum;
    }
}
