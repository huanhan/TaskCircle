package com.tc.dto.user;

import com.tc.db.entity.Comment;
import com.tc.db.entity.CommentUser;
import com.tc.db.entity.Task;
import com.tc.db.entity.User;
import com.tc.db.enums.UserCategory;
import com.tc.db.enums.UserGender;
import com.tc.db.enums.UserState;
import com.tc.until.QueryUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import javax.persistence.criteria.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 用户查询条件
 * @author Cyg
 */
public class QueryUser extends PageRequest {

    /**
     * 用户姓名
     */
    private String name;
    /**
     * 用户账户
     */
    private String username;
    /**
     * 用户分类
     */
    private UserCategory category;
    /**
     * 用户性别
     */
    private UserGender gender;
    /**
     * 最后一次登陆时间开始
     */
    private Timestamp lastLoginBegin;
    /**
     * 最后一次登陆时间开始
     */
    private Timestamp lastLoginEnd;
    /**
     * 创建时间开始
     */
    private Timestamp createTimeBegin;
    /**
     * 创建时间结束
     */
    private Timestamp createTimeEnd;
    /**
     * 账户余额开始
     */
    private Float moneyBegin;
    /**
     * 账户余额结束
     */
    private Float moneyEnd;
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
    private Integer heightBegin;
    /**
     * 身高
     */
    private Integer heightEnd;
    /**
     * 体重开始
     */
    private Integer weightBegin;
    /**
     * 体重开始
     */
    private Integer weightEnd;
    /**
     * 生日开始
     */
    private Timestamp birthdayBegin;
    /**
     * 生日结束
     */
    private Timestamp birthdayEnd;
    /**
     * 用户状态
     */
    private UserState state;

    private Integer taskCountBegin;

    private Integer taskCountEnd;

    private Float commentAvgBegin;

    private Float commentAvgEnd;


    public QueryUser() {
        super(0,10);
    }

    public QueryUser(int page, int size) {
        super(page, size);
    }

    public QueryUser(int page, int size, Sort.Direction direction, String... properties) {
        super(page, size, direction, properties);
    }

    public QueryUser(int page, int size, Sort sort) {
        super(page, size, sort);
    }

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

    public UserCategory getCategory() {
        return category;
    }

    public void setCategory(UserCategory category) {
        this.category = category;
    }

    public UserGender getGender() {
        return gender;
    }

    public void setGender(UserGender gender) {
        this.gender = gender;
    }

    public Timestamp getLastLoginBegin() {
        return lastLoginBegin;
    }

    public void setLastLoginBegin(Timestamp lastLoginBegin) {
        this.lastLoginBegin = lastLoginBegin;
    }

    public Timestamp getLastLoginEnd() {
        return lastLoginEnd;
    }

    public void setLastLoginEnd(Timestamp lastLoginEnd) {
        this.lastLoginEnd = lastLoginEnd;
    }

    public Timestamp getCreateTimeBegin() {
        return createTimeBegin;
    }

    public void setCreateTimeBegin(Timestamp createTimeBegin) {
        this.createTimeBegin = createTimeBegin;
    }

    public Timestamp getCreateTimeEnd() {
        return createTimeEnd;
    }

    public void setCreateTimeEnd(Timestamp createTimeEnd) {
        this.createTimeEnd = createTimeEnd;
    }

    public Float getMoneyBegin() {
        return moneyBegin;
    }

    public void setMoneyBegin(Float moneyBegin) {
        this.moneyBegin = moneyBegin;
    }

    public Float getMoneyEnd() {
        return moneyEnd;
    }

    public void setMoneyEnd(Float moneyEnd) {
        this.moneyEnd = moneyEnd;
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

    public Integer getHeightBegin() {
        return heightBegin;
    }

    public void setHeightBegin(Integer heightBegin) {
        this.heightBegin = heightBegin;
    }

    public Integer getHeightEnd() {
        return heightEnd;
    }

    public void setHeightEnd(Integer heightEnd) {
        this.heightEnd = heightEnd;
    }

    public Integer getWeightBegin() {
        return weightBegin;
    }

    public void setWeightBegin(Integer weightBegin) {
        this.weightBegin = weightBegin;
    }

    public Integer getWeightEnd() {
        return weightEnd;
    }

    public void setWeightEnd(Integer weightEnd) {
        this.weightEnd = weightEnd;
    }

    public Timestamp getBirthdayBegin() {
        return birthdayBegin;
    }

    public void setBirthdayBegin(Timestamp birthdayBegin) {
        this.birthdayBegin = birthdayBegin;
    }

    public Timestamp getBirthdayEnd() {
        return birthdayEnd;
    }

    public void setBirthdayEnd(Timestamp birthdayEnd) {
        this.birthdayEnd = birthdayEnd;
    }

    public UserState getState() {
        return state;
    }

    public void setState(UserState state) {
        this.state = state;
    }

    public Integer getTaskCountBegin() {
        return taskCountBegin;
    }

    public void setTaskCountBegin(Integer taskCountBegin) {
        this.taskCountBegin = taskCountBegin;
    }

    public Integer getTaskCountEnd() {
        return taskCountEnd;
    }

    public void setTaskCountEnd(Integer taskCountEnd) {
        this.taskCountEnd = taskCountEnd;
    }

    public Float getCommentAvgBegin() {
        return commentAvgBegin;
    }

    public void setCommentAvgBegin(Float commentAvgBegin) {
        this.commentAvgBegin = commentAvgBegin;
    }

    public Float getCommentAvgEnd() {
        return commentAvgEnd;
    }

    public void setCommentAvgEnd(Float commentAvgEnd) {
        this.commentAvgEnd = commentAvgEnd;
    }

    public static List<Predicate> initPredicates(QueryUser queryUser, Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb){
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(QueryUtils.equals(root,cb,User.NAME,queryUser.getName()));
        predicates.add(QueryUtils.equals(root,cb,User.USERNAME,queryUser.getUsername()));
        predicates.add(QueryUtils.equals(root,cb,User.GENDER,queryUser.getGender()));
        predicates.add(QueryUtils.equals(root,cb,User.CATEGORY,queryUser.getCategory()));
        predicates.add(QueryUtils.equals(root,cb,User.STATE,queryUser.getState()));

        predicates.add(QueryUtils.like(root,cb,User.ADDRESS,queryUser.getAddress()));
        predicates.add(QueryUtils.like(root,cb,User.ID_CARD,queryUser.getIdCard()));
        predicates.add(QueryUtils.like(root,cb,User.SCHOOL,queryUser.getSchool()));
        predicates.add(QueryUtils.like(root,cb,User.MAJOR,queryUser.getMajor()));
        predicates.add(QueryUtils.like(root,cb,User.INTEREST,queryUser.getInterest()));
        predicates.add(QueryUtils.like(root,cb,User.INTRO,queryUser.getIntro()));

        predicates.add(QueryUtils.between(root,cb,User.LAST_LOGIN,queryUser.getLastLoginBegin(),queryUser.getLastLoginEnd()));
        predicates.add(QueryUtils.between(root,cb,User.CREATE_TIME,queryUser.getCreateTimeBegin(),queryUser.getCreateTimeEnd()));
        predicates.add(QueryUtils.between(root,cb,User.HEIGHT,queryUser.getHeightBegin(),queryUser.getHeightEnd()));
        predicates.add(QueryUtils.between(root,cb,User.WEIGHT,queryUser.getWeightBegin(),queryUser.getWeightEnd()));
        predicates.add(QueryUtils.between(root,cb,User.BIRTHDAY,queryUser.getBirthdayBegin(),queryUser.getBirthdayEnd()));
        predicates.add(QueryUtils.between(root,cb,User.MONEY,queryUser.getMoneyBegin(),queryUser.getMoneyEnd()));

        if (!QueryUtils.hasNull(queryUser.getTaskCountBegin()) || !QueryUtils.hasNull(queryUser.getTaskCountEnd())){
            Subquery<Integer> count = QueryUtils.countQuery(root,query,cb,Task.class,Task.USER_ID,User.ID);
            predicates.add(QueryUtils.between(cb,count,queryUser.getTaskCountBegin(),queryUser.getTaskCountEnd()));
        }

        if (!QueryUtils.hasNull(queryUser.getCommentAvgBegin()) || !QueryUtils.hasNull(queryUser.getCommentAvgEnd())){
            Subquery<Float> avg = QueryUtils.avgQuery(root,query,cb,CommentUser.class,CommentUser.COMMENT,Comment.NUMBER,CommentUser.USER_ID,User.ID);
            predicates.add(QueryUtils.between(cb,avg,queryUser.getCommentAvgBegin(),queryUser.getCommentAvgEnd()));
        }

        predicates.removeIf(Objects::isNull);

        return predicates;
    }


}
