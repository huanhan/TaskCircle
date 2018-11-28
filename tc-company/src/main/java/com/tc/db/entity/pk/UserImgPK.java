package com.tc.db.entity.pk;

import com.tc.db.entity.User;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class UserImgPK implements Serializable {
    private User user;
    private String imgName;

    @Column(name = "user_id")
    @Id
    public User getUser() {
        return user;
    }

    public void setUser(User userByUserId) {
        this.user = userByUserId;
    }

    @Column(name = "img_name")
    @Id
    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        UserImgPK userImgPK = (UserImgPK) o;
        return user.getId().equals(userImgPK.getUser().getId()) &&
                Objects.equals(imgName, userImgPK.imgName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user.getId(), imgName);
    }
}
