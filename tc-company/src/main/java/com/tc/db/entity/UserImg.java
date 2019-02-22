package com.tc.db.entity;

import com.tc.db.entity.pk.UserImgPK;
import com.tc.db.enums.UserIMGName;
import com.tc.dto.trans.Trans;
import com.tc.until.ListUtils;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

/**
 * 用户图片
 * @author Cyg
 */
@Entity
@Table(name = "user_img", schema = "tc-company")
@IdClass(UserImgPK.class)
public class UserImg {

    public static final String IMG_NAME = "imgName";

    private Long userId;
    private UserIMGName imgName;
    private String urlLocation;
    private User user;

    private Trans trans;

    public UserImg() {
    }

    public UserImg(Long userId, UserIMGName imgName, String urlLocation) {
        this.userId = userId;
        this.imgName = imgName;
        this.urlLocation = urlLocation;
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
    @Column(name = "img_name")
    public UserIMGName getImgName() {
        return imgName;
    }

    public void setImgName(UserIMGName imgName) {
        this.imgName = imgName;
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
    @Column(name = "url_location")
    public String getUrlLocation() {
        return urlLocation;
    }

    public void setUrlLocation(String urlLocation) {
        this.urlLocation = urlLocation;
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
        UserImg userImg = (UserImg) o;
        return userId.equals(userImg.getUserId()) &&
                Objects.equals(imgName, userImg.getImgName()) &&
                Objects.equals(urlLocation, userImg.getUrlLocation());
    }

    @Override
    public int hashCode() {

        return Objects.hash(user.getId(), imgName, urlLocation);
    }

    public static List<UserImg> toListInIndex(List<UserImg> userImgs) {
        if (!ListUtils.isEmpty(userImgs)){
            userImgs.forEach(userImg -> {
                if (userImg.getUser() != null){
                    User user = userImg.user;
                    userImg.setUser(new User(user.getId(),user.getName(),user.getUsername()));
                }
                userImg.setTrans(new Trans(userImg.getImgName().name(), userImg.getImgName().getIMGName()));
            });
        }
        return userImgs;
    }

    public static UserImg toDetail(UserImg userImg) {
       if (userImg != null){
           if (userImg.getUser() != null){
               User user = userImg.user;
               userImg.setUser(new User(user.getId(),user.getName(),user.getUsername()));
           }
           userImg.setTrans(new Trans(userImg.getImgName().name(), userImg.getImgName().getIMGName()));
       }
       return userImg;
    }
}
