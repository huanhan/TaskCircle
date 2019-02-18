package com.tc.db.entity;

import com.tc.db.enums.AdminState;
import com.tc.dto.Show;
import com.tc.dto.trans.TransEnum;
import com.tc.dto.trans.Trans;
import com.tc.dto.trans.TransAdmin;
import com.tc.dto.trans.TransOP;
import com.tc.until.ListUtils;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * 管理员
 * @author Cyg
 */
@Entity
public class Admin {

    public static final String USER = "user";
    public static final String ADMIN = "admin";
    public static final String STATE = "adminState";
    public static final String USER_ID = "userId";
    public static final String CREATE_ID = "createId";
    public static final String ENTRY_TIME = "entryTime";
    public static final String ADMIN_AUTHORITIES = "adminAuthorities";


    private Long userId;
    private Long createId;
    private AdminState adminState;
    private User user;
    private Admin admin;
    private Timestamp entryTime;
    private Collection<Admin> admins;
    private Collection<AdminAuthority> adminAuthorities;
    private Collection<Audit> audits;
    private Collection<Authority> authorities;
    private Collection<Message> messages;
    private Collection<TaskClassify> taskClassifies;

    private List<TransEnum> adminStates;
    private boolean isManager = false;
    private Trans trans;

    public Admin() {
    }

    public Admin(Long userId) {
        this.userId = userId;
        this.setUser(new User(userId));
    }

    public Admin(Long userId,String name,String username){
        this.userId = userId;
        this.setUser(new User(userId,name,username));
    }


    public Admin(Long userId, User user) {
        this.userId = userId;
        if (user != null){
            this.user = new User(userId,user.getName(),user.getUsername());
        }
    }




    @Id
    @Column(name = "user_id")
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "create_id")
    public Long getCreateId() {
        return createId;
    }

    public void setCreateId(Long createId) {
        this.createId = createId;
    }

    @Basic
    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    public AdminState getAdminState() {
        return adminState;
    }

    public void setAdminState(AdminState adminState) {
        this.adminState = adminState;
    }

    @Basic
    @Column(name = "entry_time")
    public Timestamp getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(Timestamp entryTime) {
        this.entryTime = entryTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Admin admin = (Admin) o;
        return userId.equals(admin.userId) &&
                createId.equals(admin.createId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(userId, createId);
    }

    @OneToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @ManyToOne
    @JoinColumn(name = "create_id", referencedColumnName = "user_id", nullable = false,insertable = false,updatable = false)
    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    @OneToMany(mappedBy = "admin")
    public Collection<Admin> getAdmins() {
        return admins;
    }

    public void setAdmins(Collection<Admin> admins) {
        this.admins = admins;
    }

    @OneToMany(mappedBy = "admin")
    public Collection<AdminAuthority> getAdminAuthorities() {
        return adminAuthorities;
    }

    public void setAdminAuthorities(Collection<AdminAuthority> adminAuthorities) {
        this.adminAuthorities = adminAuthorities;
    }

    @OneToMany(mappedBy = "admin")
    public Collection<Audit> getAudits() {
        return audits;
    }

    public void setAudits(Collection<Audit> audits) {
        this.audits = audits;
    }

    @OneToMany(mappedBy = "admin")
    public Collection<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Collection<Authority> authorities) {
        this.authorities = authorities;
    }

    @OneToMany(mappedBy = "creation")
    public Collection<Message> getMessages() {
        return messages;
    }

    public void setMessages(Collection<Message> messages) {
        this.messages = messages;
    }

    @OneToMany(mappedBy = "creation")
    public Collection<TaskClassify> getTaskClassifies() {
        return taskClassifies;
    }

    public void setTaskClassifies(Collection<TaskClassify> taskClassifies) {
        this.taskClassifies = taskClassifies;
    }

    @Transient
    public List<TransEnum> getAdminStates() {
        return adminStates;
    }

    public void setAdminStates(List<TransEnum> adminStates) {
        this.adminStates = adminStates;
    }

    @Transient
    public boolean isManager() {
        return isManager;
    }

    public void setManager(boolean manager) {
        isManager = manager;
    }

    @Transient
    public Trans getTrans() {
        return trans;
    }

    public void setTrans(Trans trans) {
        this.trans = trans;
    }

    public static TransAdmin toTrans(List<Admin> content, List<Admin> notAuthAdmins, Authority query, Long me) {
        TransAdmin result = new TransAdmin();
        List<Trans> trans = new ArrayList<>();
        List<TransOP> transAdmins = new ArrayList<>();
        if (ListUtils.isNotEmpty(content)){
            content.forEach(con -> transAdmins.add(new TransOP(con.userId,
                    con.user.getUsername() + "(" + con.user.getName() + ")",
                    con.createId.equals(me))));
        }
        if (ListUtils.isNotEmpty(notAuthAdmins)){
            notAuthAdmins.forEach(con -> trans.add(new TransAdmin(con.userId,con.user.getUsername() + "(" + con.user.getName() + ")")));
        }
        result.setKey(query.getId());
        result.setValue(query.getName());
        result.setAdmins(transAdmins);
        result.setTrans(trans);
        return result;
    }

    public static List<Trans> toTrans(List<Admin> queryAdmins) {
        List<Trans> result = new ArrayList<>();
        if (ListUtils.isNotEmpty(queryAdmins)){
            queryAdmins.forEach(adm -> result.add(new Trans(adm.userId,adm.user.getUsername() + "(" + adm.user.getName() + ")")));
        }
        return result;
    }

    public static List<Show> toShows(List<Admin> admins){
        List<Show> result = new ArrayList<>();
        if (!admins.isEmpty()){
            admins.forEach(admin ->{
                result.add(new Show(admin.getUserId(),admin.getUser().getName()));
            });
        }
        return result;
    }

    public static List<Long> toKeys(List<Admin> list) {
        List<Long> result = new ArrayList<>();
        if (!list.isEmpty()){
            list.forEach(admin -> result.add(admin.getUserId()));
        }
        return result;
    }

    /**
     * 管理员管理列表
     * @param admins
     * @return
     */
    public static List<Admin> toListInIndex(List<Admin> admins) {
        if (!ListUtils.isEmpty(admins)){
            admins.forEach(admin ->{
                User user = new User(admin.getUserId(),admin.getUser().getName());
                user.setUsername(admin.getUser().getUsername());
                user.setPhone(admin.getUser().getPhone());
                user.setAddress(admin.getUser().getAddress());
                admin.setUser(user);
                admin.setAdmin(null);
                admin.setAdmins(null);
                admin.setAdminAuthorities(null);
                admin.setAudits(null);
                admin.setAuthorities(null);
                admin.setMessages(null);
                admin.setTaskClassifies(null);
            });
        }
        return admins;
    }

    public static List<Admin> toListInIndex(List<Admin> admins, Long id) {
        if (!ListUtils.isEmpty(admins)){
            admins.forEach(admin ->{
                User user = new User(admin.getUserId(),admin.getUser().getName());
                user.setUsername(admin.getUser().getUsername());
                user.setPhone(admin.getUser().getPhone());
                user.setAddress(admin.getUser().getAddress());
                admin.setUser(user);
                admin.setAdmin(null);
                admin.setAdmins(null);
                admin.setAdminAuthorities(null);
                admin.setAudits(null);
                admin.setAuthorities(null);
                admin.setMessages(null);
                admin.setTaskClassifies(null);
                admin.setManager(admin.getCreateId().equals(id));
                admin.setTrans(new Trans(admin.getAdminState().name(),admin.getAdminState().getState()));
            });
        }
        return admins;
    }

    public static Admin toDetail(Admin admin){
        if (admin != null){
            admin.setAdminStates(AdminState.toList());
            admin.setUser(User.toDetail(admin.getUser()));
            admin.setAdmin(new Admin(admin.getCreateId(),admin.getUser().getName(),admin.getUser().getUsername()));
            admin.setAdmins(null);
            admin.setAdminAuthorities(null);
            admin.setAudits(null);
            admin.setAuthorities(null);
            admin.setMessages(null);
            admin.setTaskClassifies(null);
            admin.setTrans(new Trans(admin.getAdminState().name(),admin.getAdminState().getState()));
        }
        return admin;
    }
}
