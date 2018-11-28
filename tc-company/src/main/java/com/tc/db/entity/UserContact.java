package com.tc.db.entity;

import com.tc.db.entity.pk.UserContactPK;
import com.tc.db.enums.UserContactName;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "user_contact", schema = "tc-company")
@IdClass(UserContactPK.class)
public class UserContact implements Serializable {

    private UserContactName contactName;
    private String contact;
    private Boolean isDefault;
    private User user;

    @Id
    @Enumerated(EnumType.STRING)
    @Column(name = "contact_name")
    public UserContactName getContactName() {
        return contactName;
    }

    public void setContactName(UserContactName contactName) {
        this.contactName = contactName;
    }

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
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

    @Basic
    @Column(name = "is_default")
    public Boolean getDefault() {
        return isDefault;
    }

    public void setDefault(Boolean aDefault) {
        isDefault = aDefault;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        UserContact that = (UserContact) o;
        return user.getId().equals(that.getUser().getId()) &&
                isDefault.equals(that.getDefault()) &&
                Objects.equals(contactName, that.getContactName()) &&
                Objects.equals(contact, that.getContact());
    }

    @Override
    public int hashCode() {

        return Objects.hash(user.getId(), contactName, contact, isDefault);
    }


}
