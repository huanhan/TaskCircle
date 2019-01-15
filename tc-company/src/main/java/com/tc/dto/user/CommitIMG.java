package com.tc.dto.user;

import com.tc.db.entity.UserImg;
import com.tc.db.enums.UserIMGName;
import com.tc.until.ListUtils;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 用户提交的图片
 * @author Cyg
 */
public class CommitIMG {
    @NotNull
    private UserIMGName userIMGName;
    @NotEmpty
    @URL
    private String url;

    public static Collection<UserImg> toList(Long id, List<CommitIMG> commitIMGS) {
        List<UserImg> result = new ArrayList<>();

        if (!ListUtils.isEmpty(commitIMGS)){
            commitIMGS.forEach(commitIMG -> {
                result.add(new UserImg(id,commitIMG.getUserIMGName(),commitIMG.getUrl()));
            });
        }

        return result;
    }

    public UserIMGName getUserIMGName() {
        return userIMGName;
    }

    public void setUserIMGName(UserIMGName userIMGName) {
        this.userIMGName = userIMGName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
