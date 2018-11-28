package com.tc.db.entity.pk;

import com.tc.db.entity.User;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class UserContactPK implements Serializable {
    private User user;
    private String contactName;

    @Column(name = "user_id")
    @Id
    public User getUser() {
        return user;
    }

    public void setUser(User userByUserId) {
        this.user = userByUserId;
    }

    @Column(name = "contact_name")
    @Id
    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        UserContactPK that = (UserContactPK) o;
        return user.getId().equals(that.getUser().getId()) &&
                Objects.equals(contactName, that.getContactName());
    }

    @Override
    public int hashCode() {

        return Objects.hash(user.getId(), contactName);
    }
}
