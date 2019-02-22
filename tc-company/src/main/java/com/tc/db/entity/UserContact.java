package com.tc.db.entity;

import com.tc.db.entity.pk.UserContactPK;
import com.tc.db.enums.UserContactName;
import com.tc.dto.trans.Trans;
import com.tc.until.ListUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * 用户联系方式
 * @author Cyg
 */
@Entity
@Table(name = "user_contact", schema = "tc-company")
@IdClass(UserContactPK.class)
public class UserContact implements Serializable {

    public static final String CONTACT_NAME = "contactName";

    private Long userId;
    private UserContactName contactName;
    private String contact;
    private User user;

    private Trans trans;

    public UserContact() {
    }

    public UserContact(Long userId, UserContactName contactName, String contact) {
        this.userId = userId;
        this.contactName = contactName;
        this.contact = contact;
    }

    @Id
    @Column(name = "user_id")
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Id
    @Enumerated(EnumType.STRING)
    @Column(name = "contact_name")
    public UserContactName getContactName() {
        return contactName;
    }

    public void setContactName(UserContactName contactName) {
        this.contactName = contactName;
    }


    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false,insertable = false,updatable = false)
    public User getUser() {
        return user;
    }

    public void setUser(User userByUserId) {
        this.user = userByUserId;
    }

    @Basic
    @Column(name = "contact")
    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    @Transient
    public Trans getTrans() {
        return trans;
    }

    public void setTrans(Trans trans) {
        this.trans = trans;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        UserContact that = (UserContact) o;
        return user.getId().equals(that.getUser().getId()) &&
                Objects.equals(contactName, that.getContactName()) &&
                Objects.equals(contact, that.getContact());
    }

    @Override
    public int hashCode() {

        return Objects.hash(userId, contactName, contact);
    }

    public static List<UserContact> toListInIndex(List<UserContact> query) {
        if (!ListUtils.isEmpty(query)){
            query.forEach(userContact -> {
                if (userContact.user != null){
                    User user = userContact.user;
                    userContact.setUser(new User(user.getId(),user.getName(),user.getUsername()));
                }
                userContact.setTrans(new Trans(userContact.contactName.name(),userContact.contactName.getContactName()));
            });
        }
        return query;
    }

    public static UserContact toDetail(UserContact userContact) {
        if (userContact != null){
            if (userContact.user != null){
                User user = userContact.user;
                userContact.setUser(new User(user.getId(),user.getName(),user.getUsername()));
            }
            userContact.setTrans(new Trans(userContact.contactName.name(),userContact.contactName.getContactName()));
        }
        return userContact;
    }
}
