package com.tc.db.entity.pk;


import com.tc.db.enums.UserIMGName;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

/**
 * 用户图片主键
 * @author Cyg
 */
public class UserImgPK implements Serializable {
    private Long userId;
    private UserIMGName imgName;

    @Column(name = "user_id")
    @Id
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }


    @Column(name = "img_name")
    @Enumerated(EnumType.STRING)
    @Id
    public UserIMGName getImgName() {
        return imgName;
    }

    public void setImgName(UserIMGName imgName) {
        this.imgName = imgName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        UserImgPK userImgPK = (UserImgPK) o;
        return userId.equals(userImgPK.getUserId()) &&
                Objects.equals(imgName, userImgPK.imgName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, imgName);
    }
}
