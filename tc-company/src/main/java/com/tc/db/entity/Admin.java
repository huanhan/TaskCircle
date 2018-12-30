package com.tc.db.entity;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

/**
 * 管理员
 * @author Cyg
 */
@Entity
public class Admin {
    private Long userId;
    private Long createId;
    private User user;
    private Admin admin;
    private Collection<Admin> admins;
    private Collection<AdminAuthority> adminAuthorities;
    private Collection<Audit> audits;
    private Collection<Authority> authorities;
    private Collection<Message> messages;
    private Collection<TaskClassify> taskClassifies;

    public Admin() {
    }

    public Admin(Long userId) {
        this.userId = userId;
        this.setUser(new User(userId));
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

    @OneToOne
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
}
