package com.tc.db.entity;

import com.fasterxml.jackson.annotation.JsonView;
import com.tc.db.enums.UserCategory;
import com.tc.db.enums.UserGender;
import com.tc.db.enums.UserState;
import com.tc.dto.trans.Trans;
import com.tc.dto.trans.TransEnum;
import com.tc.until.ListUtils;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * 用户实体
 * @author Cyg
 */
@Entity
public class User implements Serializable {

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String USERNAME = "username";
    public static final String CATEGORY = "category";
    public static final String GENDER = "gender";
    public static final String LAST_LOGIN = "lastLogin";
    public static final String CREATE_TIME = "createTime";
    public static final String ID_CARD = "idCard";
    public static final String ADDRESS = "address";
    public static final String SCHOOL = "school";
    public static final String MAJOR = "major";
    public static final String INTEREST = "interest";
    public static final String INTRO = "intro";
    public static final String HEIGHT = "height";
    public static final String WEIGHT = "weight";
    public static final String BIRTHDAY = "birthday";
    public static final String STATE = "state";
    public static final String MONEY = "money";
    public static final String TASKS = "tasks";
    public static final String ADMIN_AUDIT_TIME = "adminAuditTime";
    public static final String AUDIT_TIME = "auditTime";


    /**
     * 用户编号
     */
    private Long id;
    /**
     * 用户姓名
     */
    @NotBlank(message = "用户名字不能为空")
    @Size(max = 16,message = "名字最大值不能超过10")
    private String name;
    /**
     * 用户账户
     */
    private String username;
    /**
     * 用户密码
     */
    private String password;
    /**
     * 用户分类
     */
    @NotNull(message = "不能空")
    private UserCategory category;
    /**
     * 用户性别
     */
    @NotNull(message = "不能空")
    private UserGender gender;
    /**
     * 最后一次登陆时间
     */
    private Timestamp lastLogin;
    /**
     * 创建时间
     */
    @CreationTimestamp
    private Timestamp createTime;

    /**
     * 管理员审核开始时间
     */
    private Timestamp adminAuditTime;

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
     * 对应的管理员
     */
    private Admin admin;
    /**
     * 用户状态
     */
    private UserState state;
    /**
     * 用户手机号码
     */
    private String phone;
    /**
     * 用户头像
     */
    private String headImg;
    /**
     * 用户提交审核时间
     */
    private Timestamp auditTime;
    /**
     * 猎刃审核记录
     */
    private Collection<AuditHunter> auditHunters;
    /**
     * 用户评论记录
     */
    private Collection<Comment> comments;
    /**
     * 用户的被评论记录
     */
    private Collection<CommentUser> commentUsers;
    /**
     * 对应的猎刃
     */
    private Hunter hunter;
    /**
     * 开发者创建的资源
     */
    private Collection<Resource> resources;
    /**
     * 用户发布的任务
     */
    private Collection<Task> tasks;
    /**
     * 用户联系方式列
     */
    private Collection<UserContact> userContacts;
    /**
     * 用户与猎刃的交流记录
     */
    private Collection<UserHunterInterflow> userHunterInterflows;
    /**
     * 用户的支出
     */
    private Collection<UserIeRecord> userIncome;
    /**
     * 用户的收入
     */
    private Collection<UserIeRecord> userExpense;
    /**
     * 用户的图片信息
     */
    private Collection<UserImg> userImgs;
    /**
     * 用户的异常操作日志
     */
    private Collection<UserOperationLog> userOperationLogs;
    /**
     * 用户的提现与充值记录
     */
    private Collection<UserWithdraw> userWithdraws;

    /**
     * 所有的用户类别
     */
    private List<TransEnum> categories;

    /**
     * 用户性别
     */
    private List<TransEnum> genders;

    /**
     * 用户状态
     */
    private List<TransEnum> states;

    /**
     * 用户权限
     */
    private List<UserAuthority> userAuthorities;
    private Long adminId;
    private Admin auditAdmin;



    private Trans transCategory;
    private Trans transGender;
    private Trans transState;

    private Boolean isAudit = false;

    public User() {
    }

    public User(Long id) {
        this.id = id;
    }

    public User(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public User(Long id, String name, String username) {
        this.id = id;
        this.name = name;
        this.username = username;
    }

    public User(Long id, String name, String username,String headImg) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.headImg = headImg;
    }

    public User(Long id, String name, String username,UserCategory category) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.transCategory = new Trans(category.name(),category.getCategory());
    }

    public User(Long id, String name, String username,String headImg,UserCategory category) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.headImg = headImg;
        this.transCategory = new Trans(category.name(),category.getCategory());
    }

    public static User toDetail(User user) {
        if (user.getAuditAdmin() != null){
            user.setAuditAdmin(new Admin(user.getAdminId(),user.getAuditAdmin().getUser()));
        }
        user.setGenders(UserGender.toList());
        user.setCategories(UserCategory.toList());
        user.setStates(UserState.toList());
        user.setPassword(null);
        user.setAdmin(null);
        user.setAuditHunters(null);
        user.setComments(null);
        user.setCommentUsers(null);
        user.setResources(null);
        user.setUserContacts(null);
        user.setTasks(null);
        user.setUserExpense(null);
        user.setUserHunterInterflows(null);
        if (user.getUserImgs() != null){
            Collection<UserImg> userImgs = new ArrayList<>();
            user.getUserImgs().forEach(userImg ->
                    userImgs.add(new UserImg(userImg.getUserId(),userImg.getImgName(),userImg.getUrlLocation())));
            user.setUserImgs(userImgs);
        }
        user.setUserIncome(null);
        user.setUserOperationLogs(null);
        user.setUserWithdraws(null);
        user.setHunter(null);
        user.setTransCategory(new Trans(user.getCategory().name(),user.getCategory().getCategory()));
        user.setTransGender(new Trans(user.getGender().name(),user.getGender().getGender()));
        user.setTransState(new Trans(user.getState().name(),user.getState().getState()));
        return user;
    }

    public static User toDetailByNotEnums(User user) {
        if (user.getAuditAdmin() != null){
            user.setAuditAdmin(new Admin(user.getAdminId(),user.getAuditAdmin().getUser()));
        }
        user.setPassword(null);
        user.setAdmin(null);
        user.setAuditHunters(null);
        user.setComments(null);
        user.setCommentUsers(null);
        user.setResources(null);
        user.setUserContacts(null);
        user.setTasks(null);
        user.setUserExpense(null);
        user.setUserHunterInterflows(null);
        if (user.getUserImgs() != null){
            Collection<UserImg> userImgs = new ArrayList<>();
            user.getUserImgs().forEach(userImg ->
                    userImgs.add(new UserImg(userImg.getUserId(),userImg.getImgName(),userImg.getUrlLocation())));
            user.setUserImgs(userImgs);
        }
        user.setUserIncome(null);
        user.setUserOperationLogs(null);
        user.setUserWithdraws(null);
        user.setHunter(null);
        user.setTransCategory(new Trans(user.getCategory().name(),user.getCategory().getCategory()));
        user.setTransGender(new Trans(user.getGender().name(),user.getGender().getGender()));
        user.setTransState(new Trans(user.getState().name(),user.getState().getState()));
        return user;
    }

    public static List<User> toIndexAsList(List<User> content) {
        if (!ListUtils.isEmpty(content)){
            content.forEach(User::toDetailByNotEnums);
        }
        return content;
    }

    public static List<User> toIndexAsList(List<User> content, Long me) {
        if (!ListUtils.isEmpty(content)){
            content.forEach(user -> {
                if (user.getAuditAdmin() != null){
                    if (user.getAdminId().equals(me)){
                        user.setAudit(true);
                    }
                    user.setAuditAdmin(new Admin(user.getAdminId(),user.getAuditAdmin().getUser()));
                }
                user.setPassword(null);
                user.setAdmin(null);
                user.setAuditHunters(null);
                user.setComments(null);
                user.setCommentUsers(null);
                user.setResources(null);
                user.setUserContacts(null);
                user.setTasks(null);
                user.setUserExpense(null);
                user.setUserHunterInterflows(null);
                user.setUserImgs(null);
                user.setUserIncome(null);
                user.setUserOperationLogs(null);
                user.setUserWithdraws(null);
                user.setHunter(null);
                user.setTransCategory(new Trans(user.getCategory().name(),user.getCategory().getCategory()));
                user.setTransGender(new Trans(user.getGender().name(),user.getGender().getGender()));
                user.setTransState(new Trans(user.getState().name(),user.getState().getState()));
            });
        }
        return content;
    }

    public static List<Long> toIds(List<User> users) {
        List<Long> result = new ArrayList<>();
        if (!ListUtils.isEmpty(users)){
            users.forEach(user -> result.add(user.id));
        }
        return result;
    }



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @JsonView(UserBasicView.class)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name")
    @JsonView(UserBasicView.class)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "username")
    @JsonView(UserBasicView.class)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Basic
    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    private void setPassword(String password) {
        this.password = password;
    }

    public void setEncoderPassword(String password) {
        this.password = new BCryptPasswordEncoder().encode(password);
    }

    @Basic
    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    @JsonView(UserBasicDetailView.class)
    public UserCategory getCategory() {
        return category;
    }

    public void setCategory(UserCategory category) {
        this.category = category;
    }

    @Basic
    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    @JsonView(UserBasicDetailView.class)
    public UserGender getGender() {
        return gender;
    }

    public void setGender(UserGender gender) {
        this.gender = gender;
    }

    @Basic
    @Column(name = "last_login")
    @JsonView(UserBasicDetailView.class)
    public Timestamp getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Timestamp lastLogin) {
        this.lastLogin = lastLogin;
    }

    @Basic
    @Column(name = "create_time")
    @JsonView(UserBasicDetailView.class)
    public Timestamp getCreateTime() {
        return createTime;
    }

    private void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Basic
    @Column(name = "admin_audit_time")
    public Timestamp getAdminAuditTime() {
        return adminAuditTime;
    }

    public void setAdminAuditTime(Timestamp adminAuditTime) {
        this.adminAuditTime = adminAuditTime;
    }

    @Basic
    @Column(name = "money")
    public Float getMoney() {
        return money;
    }

    public void setMoney(Float money) {
        this.money = money;
    }

    @Basic
    @Column(name = "id_card")
    @JsonView(UserBasicDetailView.class)
    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    @Basic
    @Column(name = "address")
    @JsonView(UserBasicDetailView.class)
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Basic
    @Column(name = "school")
    @JsonView(UserBasicDetailView.class)
    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    @Basic
    @Column(name = "major")
    @JsonView(UserBasicDetailView.class)
    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    @Basic
    @Column(name = "interest")
    @JsonView(UserBasicDetailView.class)
    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    @Basic
    @Column(name = "intro")
    @JsonView(UserBasicDetailView.class)
    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    @Basic
    @Column(name = "height")
    @JsonView(UserBasicDetailView.class)
    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    @Basic
    @Column(name = "weight")
    @JsonView(UserBasicDetailView.class)
    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    @Basic
    @Column(name = "birthday")
    @JsonView(UserBasicDetailView.class)
    public Timestamp getBirthday() {
        return birthday;
    }

    public void setBirthday(Timestamp birthday) {
        this.birthday = birthday;
    }

    @Basic
    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    @JsonView(UserBasicDetailView.class)
    public UserState getState() {
        return state;
    }

    public void setState(UserState state) {
        this.state = state;
    }

    @Basic
    @Column(name = "phone")
    @JsonView(UserBasicDetailView.class)
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Basic
    @Column(name = "head_img")
    @JsonView(UserBasicDetailView.class)
    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    @Basic
    @Column(name = "audit_time")
    @JsonView(UserBasicDetailView.class)
    public Timestamp getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Timestamp auditTime) {
        this.auditTime = auditTime;
    }

    @Transient
    public Trans getTransCategory() {
        return transCategory;
    }

    public void setTransCategory(Trans transCategory) {
        this.transCategory = transCategory;
    }

    @Transient
    public Trans getTransGender() {
        return transGender;
    }

    public void setTransGender(Trans transGender) {
        this.transGender = transGender;
    }

    @Transient
    public Trans getTransState() {
        return transState;
    }

    public void setTransState(Trans transState) {
        this.transState = transState;
    }

    @Transient
    public Boolean getAudit() {
        return isAudit;
    }

    public void setAudit(Boolean audit) {
        isAudit = audit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        User user = (User) o;
        return id.equals(user.id)  &&
                Double.compare(user.money, money) == 0 &&
                Objects.equals(name, user.name) &&
                Objects.equals(username, user.username) &&
                Objects.equals(password, user.password) &&
                Objects.equals(category, user.category) &&
                Objects.equals(gender, user.gender) &&
                Objects.equals(lastLogin, user.lastLogin) &&
                Objects.equals(createTime, user.createTime) &&
                Objects.equals(idCard, user.idCard) &&
                Objects.equals(address, user.address) &&
                Objects.equals(school, user.school) &&
                Objects.equals(major, user.major) &&
                Objects.equals(interest, user.interest) &&
                Objects.equals(intro, user.intro) &&
                Objects.equals(height, user.height) &&
                Objects.equals(weight, user.weight) &&
                Objects.equals(birthday, user.birthday) &&
                Objects.equals(phone, user.phone) &&
                Objects.equals(headImg, user.headImg) &&
                Objects.equals(auditTime,user.auditTime);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, name, username, password, category, gender, lastLogin, createTime, money, idCard, address, school, major, interest, intro, height, weight, birthday, phone, headImg, auditTime);
    }

    @OneToOne(mappedBy = "user")
    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin adminById) {
        this.admin = adminById;
    }

    @OneToMany(mappedBy = "user")
    public Collection<AuditHunter> getAuditHunters() {
        return auditHunters;
    }

    public void setAuditHunters(Collection<AuditHunter> auditHuntersById) {
        this.auditHunters = auditHuntersById;
    }

    @OneToMany(mappedBy = "creation")
    public Collection<Comment> getComments() {
        return comments;
    }

    public void setComments(Collection<Comment> commentsById) {
        this.comments = commentsById;
    }

    @OneToMany(mappedBy = "user")
    public Collection<CommentUser> getCommentUsers() {
        return commentUsers;
    }

    public void setCommentUsers(Collection<CommentUser> commentUsersById) {
        this.commentUsers = commentUsersById;
    }

    @OneToOne(mappedBy = "user")
    public Hunter getHunter() {
        return hunter;
    }

    public void setHunter(Hunter hunterById) {
        this.hunter = hunterById;
    }

    @OneToMany(mappedBy = "creation")
    public Collection<Resource> getResources() {
        return resources;
    }

    public void setResources(Collection<Resource> resourcesById) {
        this.resources = resourcesById;
    }

    @OneToMany(mappedBy = "user")
    public Collection<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Collection<Task> tasksById) {
        this.tasks = tasksById;
    }

    @OneToMany(mappedBy = "user",cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    public Collection<UserContact> getUserContacts() {
        return userContacts;
    }

    public void setUserContacts(Collection<UserContact> userContactsById) {
        this.userContacts = userContactsById;
    }

    @OneToMany(mappedBy = "user")
    public Collection<UserHunterInterflow> getUserHunterInterflows() {
        return userHunterInterflows;
    }

    public void setUserHunterInterflows(Collection<UserHunterInterflow> userHunterInterflowsById) {
        this.userHunterInterflows = userHunterInterflowsById;
    }

    @OneToMany(mappedBy = "userByMe")
    public Collection<UserIeRecord> getUserIncome() {
        return userIncome;
    }

    public void setUserIncome(Collection<UserIeRecord> income) {
        this.userIncome = income;
    }

    @OneToMany(mappedBy = "userByTo")
    public Collection<UserIeRecord> getUserExpense() {
        return userExpense;
    }

    public void setUserExpense(Collection<UserIeRecord> expense) {
        this.userExpense = expense;
    }

    @OneToMany(mappedBy = "user",cascade = {CascadeType.MERGE})
    @JsonView(UserBasicView.class)
    public Collection<UserImg> getUserImgs() {
        return userImgs;
    }

    public void setUserImgs(Collection<UserImg> userImgsById) {
        this.userImgs = userImgsById;
    }

    @OneToMany(mappedBy = "user")
    public Collection<UserOperationLog> getUserOperationLogs() {
        return userOperationLogs;
    }

    public void setUserOperationLogs(Collection<UserOperationLog> userOperationLogsById) {
        this.userOperationLogs = userOperationLogsById;
    }

    @OneToMany(mappedBy = "user")
    public Collection<UserWithdraw> getUserWithdraws() {
        return userWithdraws;
    }

    public void setUserWithdraws(Collection<UserWithdraw> userWithdrawsById) {
        this.userWithdraws = userWithdrawsById;
    }

    @Transient
    public List<TransEnum> getCategories() {
        return categories;
    }

    public void setCategories(List<TransEnum> categories) {
        this.categories = categories;
    }

    @Transient
    public List<TransEnum> getGenders() {
        return genders;
    }

    public void setGenders(List<TransEnum> genders) {
        this.genders = genders;
    }

    @Transient
    public List<TransEnum> getStates() {
        return states;
    }

    public void setStates(List<TransEnum> states) {
        this.states = states;
    }

    @Transient
    public List<UserAuthority> getUserAuthorities() {
        return userAuthorities;
    }

    public void setUserAuthorities(List<UserAuthority> userAuthorities) {
        this.userAuthorities = userAuthorities;
    }

    public String toTitle(){
        return this.username + "(" + this.name + ")";
    }

    @Basic
    @Column(name = "admin_id")
    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    @ManyToOne
    @JoinColumn(name = "admin_id", referencedColumnName = "user_id",insertable = false,updatable = false)
    public Admin getAuditAdmin() {
        return auditAdmin;
    }

    public void setAuditAdmin(Admin auditAdmin) {
        this.auditAdmin = auditAdmin;
    }

    public interface UserBasicView {}

    public interface UserBasicDetailView extends UserBasicView{}
}
