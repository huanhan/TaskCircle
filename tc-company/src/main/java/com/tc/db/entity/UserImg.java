package com.tc.db.entity;

import com.tc.db.entity.pk.UserImgPK;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "user_img", schema = "tc-company")
@IdClass(UserImgPK.class)
public class UserImg {
    private String imgName;
    private String urlLocation;
    private User user;

    @Id
    @Column(name = "img_name")
    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
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
    @Column(name = "url_location")
    public String getUrlLocation() {
        return urlLocation;
    }

    public void setUrlLocation(String urlLocation) {
        this.urlLocation = urlLocation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        UserImg userImg = (UserImg) o;
        return user.getId().equals(userImg.getUser().getId()) &&
                Objects.equals(imgName, userImg.getImgName()) &&
                Objects.equals(urlLocation, userImg.getUrlLocation());
    }

    @Override
    public int hashCode() {

        return Objects.hash(user.getId(), imgName, urlLocation);
    }


}
