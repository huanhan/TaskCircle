package com.tc.db.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;


/**
 * @author Cyg
 * 管理员实体
 */
@Entity
public class Admin implements Serializable {

    private User user;
    private Admin creation;
    private Collection<Admin> admins;
    private Collection<Audit> audits;
    private Collection<TaskClassify> taskClassities;
    private Collection<Message> messages;
    private Collection<AdminAuthority> adminAuthorities;

    public Admin() {
    }



    public Admin(Long uid){
        this.user = new User(uid);
    }

    @Id
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @ManyToOne
    @JoinColumn(name = "create_id", referencedColumnName = "user_id", nullable = false)
    public Admin getCreation() {
        return creation;
    }

    public void setCreation(Admin adminByCreateId) {
        this.creation = adminByCreateId;
    }

    @OneToMany(mappedBy = "creation")
    public Collection<Admin> getAdmins() {
        return admins;
    }

    public void setAdmins(Collection<Admin> adminsByUserId) {
        this.admins = adminsByUserId;
    }

    @OneToMany(mappedBy = "creation")
    public Collection<Audit> getAudits() {
        return audits;
    }

    public void setAudits(Collection<Audit> auditsByUserId) {
        this.audits = auditsByUserId;
    }

    @OneToMany(mappedBy = "creation")
    public Collection<TaskClassify> getTaskClassities() {
        return taskClassities;
    }

    public void setTaskClassities(Collection<TaskClassify> taskClassities) {
        this.taskClassities = taskClassities;
    }

    @OneToMany(mappedBy = "creation")
    public Collection<Message> getMessages() {
        return messages;
    }

    public void setMessages(Collection<Message> messages) {
        this.messages = messages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        Admin admin = (Admin) o;
        return user.getId().equals(admin.getUser().getId()) &&
                creation.getUser().getId().equals(admin.getUser().getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(user.getId(),creation.getUser().getId());
    }

    @OneToMany(mappedBy = "admin")
    public Collection<AdminAuthority> getAdminAuthorities() {
        return adminAuthorities;
    }

    public void setAdminAuthorities(Collection<AdminAuthority> adminAuthorities) {
        this.adminAuthorities = adminAuthorities;
    }
}
