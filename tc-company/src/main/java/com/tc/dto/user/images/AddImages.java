package com.tc.dto.user.images;

import com.tc.db.entity.UserImg;
import com.tc.db.enums.UserIMGName;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 添加用户图片
 * @author Cyg
 */
public class AddImages {

    @NotNull
    @Min(1)
    private Long userId;
    @NotNull
    private UserIMGName imgName;
    @NotEmpty
    @Length(max = 200)
    @URL
    private String urlLocation;



    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public UserIMGName getImgName() {
        return imgName;
    }

    public void setImgName(UserIMGName imgName) {
        this.imgName = imgName;
    }

    public String getUrlLocation() {
        return urlLocation;
    }

    public void setUrlLocation(String urlLocation) {
        this.urlLocation = urlLocation;
    }

    public static UserImg toImages(AddImages addImages){
        UserImg result = new UserImg();
        result.setUserId(addImages.userId);
        result.setUrlLocation(addImages.urlLocation);
        result.setImgName(addImages.imgName);
        return result;
    }

    public static boolean toContact(UserImg query, AddImages addImages) {
        boolean isUpdate = false;
        if (!query.getUrlLocation().equals(addImages.urlLocation)){
            query.setUrlLocation(addImages.urlLocation);
            return true;
        }
        return isUpdate;
    }
}
