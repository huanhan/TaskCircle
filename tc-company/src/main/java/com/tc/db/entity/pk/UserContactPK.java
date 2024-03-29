package com.tc.db.entity.pk;

import com.tc.db.entity.User;
import com.tc.db.enums.UserContactName;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

/**
 * 用户联系方式主键
 * @author Cyg
 */
public class UserContactPK implements Serializable {
    private Long userId;
    private UserContactName contactName;

    public UserContactPK() {
    }

    public UserContactPK(Long userId, UserContactName contactName) {
        this.userId = userId;
        this.contactName = contactName;
    }

    @Column(name = "user_id")
    @Id
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Column(name = "contact_name")
    @Enumerated(EnumType.STRING)
    @Id
    public UserContactName getContactName() {
        return contactName;
    }

    public void setContactName(UserContactName contactName) {
        this.contactName = contactName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        UserContactPK that = (UserContactPK) o;
        return userId.equals(that.getUserId()) &&
                Objects.equals(contactName, that.getContactName());
    }

    @Override
    public int hashCode() {

        return Objects.hash(userId, contactName);
    }
}
