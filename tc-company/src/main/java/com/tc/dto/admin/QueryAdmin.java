package com.tc.dto.admin;

import com.tc.db.entity.Admin;
import com.tc.db.entity.AdminAuthority;
import com.tc.db.entity.User;
import com.tc.db.enums.AdminState;
import com.tc.db.enums.UserCategory;
import com.tc.db.enums.UserGender;
import com.tc.db.enums.UserState;
import com.tc.dto.enums.AuthorityState;
import com.tc.until.FloatHelper;
import com.tc.until.PageRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Cyg
 */
public class QueryAdmin extends PageRequest {
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
    /**
     * 创建者
     */
    private Long creation;
    /**
     * 是否拥有权限
     */
    private Boolean isAuthority;

    /**
     * 管理员状态，默认(在岗)
     */
    private AdminState adminState = AdminState.ON_GUARD;

    public QueryAdmin() {
        super(0, 10);
    }

    public QueryAdmin(int page, int size) {
        super(page, size);
    }

    public QueryAdmin(int page, int size, Sort.Direction direction, String... properties) {
        super(page, size, direction, properties);
    }

    public QueryAdmin(int page, int size, Sort sort) {
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

    public AdminState getAdminState() {
        return adminState;
    }

    public void setAdminState(AdminState adminState) {
        this.adminState = adminState;
    }

    public void setState(UserState state) {
        this.state = state;
    }

    public Long getCreation() {
        return creation;
    }

    public void setCreation(Long creation) {
        this.creation = creation;
    }

    public Boolean isAuthority() {
        return isAuthority;
    }

    public void setAuthority(Boolean authority) {
        isAuthority = authority;
    }

    public static List<Predicate> initPredicates(QueryAdmin queryAdmin, Root<Admin> root, CriteriaQuery<?> query, CriteriaBuilder cb){
        List<Predicate> predicates = new ArrayList<>();
        if (!StringUtils.isEmpty(queryAdmin.getName())){
            predicates.add(cb.equal(root.get(Admin.USER).get(User.NAME),queryAdmin.getName()));
        }
        if (!StringUtils.isEmpty(queryAdmin.getUsername())){
            predicates.add(cb.equal(root.get(Admin.USER).get(User.USERNAME),queryAdmin.getUsername()));
        }
        if (queryAdmin.getGender() != null){
            predicates.add(cb.equal(root.get(Admin.USER).get(User.GENDER),queryAdmin.getGender()));
        }
        if (queryAdmin.getCategory() != null){
            predicates.add(cb.equal(root.get(Admin.USER).get(User.CATEGORY),queryAdmin.getCategory()));
        }
        if (queryAdmin.getState() != null){
            predicates.add(cb.equal(root.get(Admin.USER).get(User.STATE),queryAdmin.getState()));
        }
        if (queryAdmin.getAdminState() != null){
            predicates.add(cb.equal(root.get(Admin.STATE),queryAdmin.getAdminState()));
        }
        if (FloatHelper.isNotNull(queryAdmin.creation)){
            predicates.add(cb.equal(root.get(Admin.CREATE_ID),queryAdmin.getCreation()));
        }
        if (!StringUtils.isEmpty(queryAdmin.getAddress())){
            predicates.add(cb.like(root.get(Admin.USER).get(User.ADDRESS),"%" + queryAdmin.getAddress() + "%"));
        }
        if (!StringUtils.isEmpty(queryAdmin.getIdCard())){
            predicates.add(cb.like(root.get(Admin.USER).get(User.ID_CARD),"%" + queryAdmin.getIdCard() + "%"));
        }
        if (!StringUtils.isEmpty(queryAdmin.getSchool())){
            predicates.add(cb.like(root.get(Admin.USER).get(User.SCHOOL),"%" + queryAdmin.getSchool() + "%"));
        }
        if (!StringUtils.isEmpty(queryAdmin.getMajor())){
            predicates.add(cb.like(root.get(Admin.USER).get(User.MAJOR),"%" + queryAdmin.getMajor() + "%"));
        }
        if (!StringUtils.isEmpty(queryAdmin.getInterest())){
            predicates.add(cb.like(root.get(Admin.USER).get(User.INTEREST),"%" + queryAdmin.getInterest() + "%"));
        }
        if (!StringUtils.isEmpty(queryAdmin.getInterest())){
            predicates.add(cb.like(root.get(Admin.USER).get(User.INTRO),"%" + queryAdmin.getInterest() + "%"));
        }

        if (queryAdmin.getLastLoginBegin() != null || queryAdmin.getLastLoginEnd() != null){
            if (queryAdmin.getLastLoginBegin() != null && queryAdmin.getLastLoginEnd() != null){
                predicates.add(cb.between(root.get(Admin.USER).get(User.LAST_LOGIN),queryAdmin.getLastLoginBegin(),queryAdmin.getLastLoginEnd()));
            }else if (queryAdmin.getLastLoginBegin() != null){
                predicates.add(cb.greaterThan(root.get(Admin.USER).get(User.LAST_LOGIN),queryAdmin.getLastLoginBegin()));
            }else if (queryAdmin.getLastLoginEnd() != null){
                predicates.add(cb.lessThan(root.get(Admin.USER).get(User.LAST_LOGIN),queryAdmin.getLastLoginEnd()));
            }
        }
        if (queryAdmin.getCreateTimeBegin() != null || queryAdmin.getCreateTimeEnd() != null){
            if (queryAdmin.getCreateTimeBegin()  != null && queryAdmin.getCreateTimeEnd() != null){
                predicates.add(cb.between(root.get(Admin.USER).get(User.CREATE_TIME),queryAdmin.getCreateTimeBegin() ,queryAdmin.getCreateTimeEnd()));
            }else if (queryAdmin.getCreateTimeBegin()  != null){
                predicates.add(cb.greaterThan(root.get(Admin.USER).get(User.CREATE_TIME),queryAdmin.getCreateTimeBegin() ));
            }else if (queryAdmin.getCreateTimeEnd() != null){
                predicates.add(cb.lessThan(root.get(Admin.USER).get(User.CREATE_TIME),queryAdmin.getCreateTimeEnd()));
            }
        }
        if (queryAdmin.getHeightBegin() != null || queryAdmin.getHeightEnd() != null){
            if (queryAdmin.getHeightBegin()  != null && queryAdmin.getHeightEnd() != null && queryAdmin.getHeightBegin() >= 0 && queryAdmin.getHeightEnd() >= 0){
                predicates.add(cb.between(root.get(Admin.USER).get(User.HEIGHT),queryAdmin.getHeightBegin() ,queryAdmin.getHeightEnd()));
            }else if (queryAdmin.getHeightBegin()  != null && queryAdmin.getHeightBegin() >= 0){
                predicates.add(cb.greaterThan(root.get(Admin.USER).get(User.HEIGHT),queryAdmin.getHeightBegin() ));
            }else if (queryAdmin.getHeightEnd() != null && queryAdmin.getHeightEnd() >= 0){
                predicates.add(cb.lessThan(root.get(Admin.USER).get(User.HEIGHT),queryAdmin.getHeightEnd()));
            }
        }
        if (queryAdmin.getWeightBegin() != null || queryAdmin.getWeightEnd() != null){
            if (queryAdmin.getWeightBegin()  != null && queryAdmin.getWeightEnd() != null && queryAdmin.getWeightBegin() >= 0 && queryAdmin.getWeightEnd() >= 0){
                predicates.add(cb.between(root.get(Admin.USER).get(User.WEIGHT),queryAdmin.getWeightBegin() ,queryAdmin.getWeightEnd()));
            }else if (queryAdmin.getWeightBegin()  != null && queryAdmin.getWeightBegin() >= 0){
                predicates.add(cb.greaterThan(root.get(Admin.USER).get(User.WEIGHT),queryAdmin.getWeightBegin() ));
            }else if (queryAdmin.getWeightEnd() != null && queryAdmin.getWeightEnd() >= 0){
                predicates.add(cb.lessThan(root.get(Admin.USER).get(User.WEIGHT),queryAdmin.getWeightEnd()));
            }
        }
        if (queryAdmin.getBirthdayBegin() != null || queryAdmin.getBirthdayEnd() != null){
            if (queryAdmin.getBirthdayBegin()  != null && queryAdmin.getBirthdayEnd() != null){
                predicates.add(cb.between(root.get(Admin.USER).get(User.BIRTHDAY),queryAdmin.getBirthdayBegin() ,queryAdmin.getBirthdayEnd()));
            }else if (queryAdmin.getBirthdayBegin()  != null){
                predicates.add(cb.greaterThan(root.get(Admin.USER).get(User.BIRTHDAY),queryAdmin.getBirthdayBegin() ));
            }else if (queryAdmin.getBirthdayEnd() != null){
                predicates.add(cb.lessThan(root.get(Admin.USER).get(User.BIRTHDAY),queryAdmin.getBirthdayEnd()));
            }
        }
        if (queryAdmin.getMoneyBegin() != null || queryAdmin.getMoneyEnd() != null){
            if (queryAdmin.getMoneyBegin()  != null && queryAdmin.getMoneyEnd() != null && queryAdmin.getMoneyBegin() >= 0 && queryAdmin.getMoneyEnd() >= 0){
                predicates.add(cb.between(root.get(Admin.USER).get(User.MONEY),queryAdmin.getMoneyBegin() ,queryAdmin.getMoneyEnd()));
            }else if (queryAdmin.getMoneyBegin()  != null && queryAdmin.getMoneyBegin() >= 0){
                predicates.add(cb.greaterThan(root.get(Admin.USER).get(User.MONEY),queryAdmin.getMoneyBegin() ));
            }else if (queryAdmin.getMoneyEnd() != null && queryAdmin.getMoneyEnd() >= 0){
                predicates.add(cb.lessThan(root.get(Admin.USER).get(User.MONEY),queryAdmin.getMoneyEnd()));
            }
        }
        return predicates;
    }

    public static List<Predicate> initPredicatesByAdminAuthority(QueryAdmin queryAdmin, Root<AdminAuthority> root, CriteriaQuery<?> query, CriteriaBuilder cb){
        List<Predicate> predicates = new ArrayList<>();
        if (!StringUtils.isEmpty(queryAdmin.getName())){
            predicates.add(cb.equal(root.get(AdminAuthority.ADMIN).get(Admin.USER).get(User.NAME),queryAdmin.getName()));
        }
        if (!StringUtils.isEmpty(queryAdmin.getUsername())){
            predicates.add(cb.equal(root.get(AdminAuthority.ADMIN).get(Admin.USER).get(User.USERNAME),queryAdmin.getUsername()));
        }
        if (queryAdmin.getGender() != null){
            predicates.add(cb.equal(root.get(AdminAuthority.ADMIN).get(Admin.USER).get(User.GENDER),queryAdmin.getGender()));
        }
        if (queryAdmin.getCategory() != null){
            predicates.add(cb.equal(root.get(AdminAuthority.ADMIN).get(Admin.USER).get(User.CATEGORY),queryAdmin.getCategory()));
        }
        if (queryAdmin.getState() != null){
            predicates.add(cb.equal(root.get(AdminAuthority.ADMIN).get(Admin.USER).get(User.STATE),queryAdmin.getState()));
        }
        if (!StringUtils.isEmpty(queryAdmin.getAddress())){
            predicates.add(cb.like(root.get(AdminAuthority.ADMIN).get(Admin.USER).get(User.ADDRESS),"%" + queryAdmin.getAddress() + "%"));
        }
        if (!StringUtils.isEmpty(queryAdmin.getIdCard())){
            predicates.add(cb.like(root.get(AdminAuthority.ADMIN).get(Admin.USER).get(User.ID_CARD),"%" + queryAdmin.getIdCard() + "%"));
        }
        if (!StringUtils.isEmpty(queryAdmin.getSchool())){
            predicates.add(cb.like(root.get(AdminAuthority.ADMIN).get(Admin.USER).get(User.SCHOOL),"%" + queryAdmin.getSchool() + "%"));
        }
        if (!StringUtils.isEmpty(queryAdmin.getMajor())){
            predicates.add(cb.like(root.get(AdminAuthority.ADMIN).get(Admin.USER).get(User.MAJOR),"%" + queryAdmin.getMajor() + "%"));
        }
        if (!StringUtils.isEmpty(queryAdmin.getInterest())){
            predicates.add(cb.like(root.get(AdminAuthority.ADMIN).get(Admin.USER).get(User.INTEREST),"%" + queryAdmin.getInterest() + "%"));
        }
        if (!StringUtils.isEmpty(queryAdmin.getInterest())){
            predicates.add(cb.like(root.get(AdminAuthority.ADMIN).get(Admin.USER).get(User.INTRO),"%" + queryAdmin.getInterest() + "%"));
        }
        if (queryAdmin.getLastLoginBegin() != null || queryAdmin.getLastLoginEnd() != null){
            if (queryAdmin.getLastLoginBegin() != null && queryAdmin.getLastLoginEnd() != null){
                predicates.add(cb.between(root.get(AdminAuthority.ADMIN).get(Admin.USER).get(User.LAST_LOGIN),queryAdmin.getLastLoginBegin(),queryAdmin.getLastLoginEnd()));
            }else if (queryAdmin.getLastLoginBegin() != null){
                predicates.add(cb.greaterThan(root.get(AdminAuthority.ADMIN).get(Admin.USER).get(User.LAST_LOGIN),queryAdmin.getLastLoginBegin()));
            }else if (queryAdmin.getLastLoginEnd() != null){
                predicates.add(cb.lessThan(root.get(AdminAuthority.ADMIN).get(Admin.USER).get(User.LAST_LOGIN),queryAdmin.getLastLoginEnd()));
            }
        }
        if (queryAdmin.getCreateTimeBegin() != null || queryAdmin.getCreateTimeEnd() != null){
            if (queryAdmin.getCreateTimeBegin()  != null && queryAdmin.getCreateTimeEnd() != null){
                predicates.add(cb.between(root.get(AdminAuthority.ADMIN).get(Admin.USER).get(User.CREATE_TIME),queryAdmin.getCreateTimeBegin() ,queryAdmin.getCreateTimeEnd()));
            }else if (queryAdmin.getCreateTimeBegin()  != null){
                predicates.add(cb.greaterThan(root.get(AdminAuthority.ADMIN).get(Admin.USER).get(User.CREATE_TIME),queryAdmin.getCreateTimeBegin() ));
            }else if (queryAdmin.getCreateTimeEnd() != null){
                predicates.add(cb.lessThan(root.get(AdminAuthority.ADMIN).get(Admin.USER).get(User.CREATE_TIME),queryAdmin.getCreateTimeEnd()));
            }
        }
        if (queryAdmin.getHeightBegin() != null || queryAdmin.getHeightEnd() != null){
            if (queryAdmin.getHeightBegin()  != null && queryAdmin.getHeightEnd() != null && queryAdmin.getHeightBegin() >= 0 && queryAdmin.getHeightEnd() >= 0){
                predicates.add(cb.between(root.get(AdminAuthority.ADMIN).get(Admin.USER).get(User.HEIGHT),queryAdmin.getHeightBegin() ,queryAdmin.getHeightEnd()));
            }else if (queryAdmin.getHeightBegin()  != null && queryAdmin.getHeightBegin() >= 0){
                predicates.add(cb.greaterThan(root.get(AdminAuthority.ADMIN).get(Admin.USER).get(User.HEIGHT),queryAdmin.getHeightBegin() ));
            }else if (queryAdmin.getHeightEnd() != null && queryAdmin.getHeightEnd() >= 0){
                predicates.add(cb.lessThan(root.get(AdminAuthority.ADMIN).get(Admin.USER).get(User.HEIGHT),queryAdmin.getHeightEnd()));
            }
        }
        if (queryAdmin.getWeightBegin() != null || queryAdmin.getWeightEnd() != null){
            if (queryAdmin.getWeightBegin()  != null && queryAdmin.getWeightEnd() != null && queryAdmin.getWeightBegin() >= 0 && queryAdmin.getWeightEnd() >= 0){
                predicates.add(cb.between(root.get(AdminAuthority.ADMIN).get(Admin.USER).get(User.WEIGHT),queryAdmin.getWeightBegin() ,queryAdmin.getWeightEnd()));
            }else if (queryAdmin.getWeightBegin()  != null && queryAdmin.getWeightBegin() >= 0){
                predicates.add(cb.greaterThan(root.get(AdminAuthority.ADMIN).get(Admin.USER).get(User.WEIGHT),queryAdmin.getWeightBegin() ));
            }else if (queryAdmin.getWeightEnd() != null && queryAdmin.getWeightEnd() >= 0){
                predicates.add(cb.lessThan(root.get(AdminAuthority.ADMIN).get(Admin.USER).get(User.WEIGHT),queryAdmin.getWeightEnd()));
            }
        }
        if (queryAdmin.getBirthdayBegin() != null || queryAdmin.getBirthdayEnd() != null){
            if (queryAdmin.getBirthdayBegin()  != null && queryAdmin.getBirthdayEnd() != null){
                predicates.add(cb.between(root.get(AdminAuthority.ADMIN).get(Admin.USER).get(User.BIRTHDAY),queryAdmin.getBirthdayBegin() ,queryAdmin.getBirthdayEnd()));
            }else if (queryAdmin.getBirthdayBegin()  != null){
                predicates.add(cb.greaterThan(root.get(AdminAuthority.ADMIN).get(Admin.USER).get(User.BIRTHDAY),queryAdmin.getBirthdayBegin() ));
            }else if (queryAdmin.getBirthdayEnd() != null){
                predicates.add(cb.lessThan(root.get(AdminAuthority.ADMIN).get(Admin.USER).get(User.BIRTHDAY),queryAdmin.getBirthdayEnd()));
            }
        }
        if (queryAdmin.getMoneyBegin() != null || queryAdmin.getMoneyEnd() != null){
            if (queryAdmin.getMoneyBegin()  != null && queryAdmin.getMoneyEnd() != null && queryAdmin.getMoneyBegin() >= 0 && queryAdmin.getMoneyEnd() >= 0){
                predicates.add(cb.between(root.get(AdminAuthority.ADMIN).get(Admin.USER).get(User.BIRTHDAY),queryAdmin.getMoneyBegin() ,queryAdmin.getMoneyEnd()));
            }else if (queryAdmin.getMoneyBegin()  != null && queryAdmin.getMoneyBegin() >= 0){
                predicates.add(cb.greaterThan(root.get(AdminAuthority.ADMIN).get(Admin.USER).get(User.BIRTHDAY),queryAdmin.getMoneyBegin() ));
            }else if (queryAdmin.getMoneyEnd() != null && queryAdmin.getMoneyEnd() >= 0){
                predicates.add(cb.lessThan(root.get(AdminAuthority.ADMIN).get(Admin.USER).get(User.BIRTHDAY),queryAdmin.getMoneyEnd()));
            }
        }
        return predicates;
    }

}
