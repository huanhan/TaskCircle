package com.tc.dto.authority;

import com.tc.db.entity.Authority;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;

/**
 * @author Cyg
 */
public abstract class BasicAuthority {



    @NotBlank(message = "权限描述不能为空")
    @Size(max = 100,message = "最多100个字符")
    private String info;

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Authority toAuthority(BasicAuthority authority){

        Authority result = new Authority();
        result.setInfo(authority.info);
        return result;

    }

}
